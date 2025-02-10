package yqloss.yqlossclientmixinkt.module.miningprediction

import net.minecraft.client.renderer.DestroyBlockProgress
import yqloss.yqlossclientmixinkt.YC
import yqloss.yqlossclientmixinkt.api.YCTemplate
import yqloss.yqlossclientmixinkt.event.RegistryEventDispatcher
import yqloss.yqlossclientmixinkt.event.minecraft.YCMinecraftEvent
import yqloss.yqlossclientmixinkt.event.minecraft.YCRenderEvent
import yqloss.yqlossclientmixinkt.event.register
import yqloss.yqlossclientmixinkt.module.SKYBLOCK_MINING_ISLANDS
import yqloss.yqlossclientmixinkt.module.YCModuleBase
import yqloss.yqlossclientmixinkt.module.ensureEnabled
import yqloss.yqlossclientmixinkt.module.ensureInWorld
import yqloss.yqlossclientmixinkt.module.ensureSkyBlockModes
import yqloss.yqlossclientmixinkt.module.moduleInfo
import yqloss.yqlossclientmixinkt.module.option.blockState
import yqloss.yqlossclientmixinkt.module.option.invoke
import yqloss.yqlossclientmixinkt.util.MC
import yqloss.yqlossclientmixinkt.util.SKYBLOCK_MINING_TOOLS
import yqloss.yqlossclientmixinkt.util.asBlockPos
import yqloss.yqlossclientmixinkt.util.asVec3I
import yqloss.yqlossclientmixinkt.util.math.Fraction
import yqloss.yqlossclientmixinkt.util.math.Vec3I
import yqloss.yqlossclientmixinkt.util.math.asFraction
import yqloss.yqlossclientmixinkt.util.math.asInt
import yqloss.yqlossclientmixinkt.util.math.over
import yqloss.yqlossclientmixinkt.util.printChat
import yqloss.yqlossclientmixinkt.util.scope.longreturn
import yqloss.yqlossclientmixinkt.util.scope.longrun
import yqloss.yqlossclientmixinkt.util.scope.noexcept
import yqloss.yqlossclientmixinkt.util.trimStyle
import yqloss.yqlossclientmixinkt.util.updateWorldRenderBlock
import kotlin.math.max
import kotlin.math.min

val INFO_MINING_PREDICTION = moduleInfo<MiningPredictionOptions>("mining_prediction", "Mining Prediction")

object MiningPrediction : YCModuleBase<MiningPredictionOptions>(INFO_MINING_PREDICTION) {
    private data class BlockInfo(
        val block: Ore,
        val blockPos: Vec3I,
        val time: Long,
    )

    private val destroyedBlocks = mutableMapOf<Vec3I, BlockInfo>()
    private var breakingPos = Vec3I(0, 0, 0)
    private var breakingTime = 0

    var breakingBlock: Ore? = null
        private set
    var breakingProgress = Fraction.ZERO
        private set
    var miningSpeed = 0
        private set
    var isAvailable = false
        private set

    fun getTicks(): Int {
        if (!isAvailable) return 0
        val block = breakingBlock ?: return 0
        val modifiedMiningSpeed = block.type.modifyMiningSpeed(miningSpeed)
        return max(0, block.getTicksActual(modifiedMiningSpeed) + options.durationOffset)
    }

    fun getProgressPerTick(): Fraction {
        val ticks = getTicks()
        return if (ticks == 0) Fraction.ZERO else 1 over ticks
    }

    fun setPlaceholders(template: YCTemplate) {
        if (!isAvailable) return
        val block = breakingBlock ?: return
        val modifiedMiningSpeed = block.type.modifyMiningSpeed(miningSpeed)
        val ticks = max(0, block.getTicksActual(modifiedMiningSpeed) + options.durationOffset)
        template["pos"] = breakingPos
        template["ore"] = block
        template["progress"] = breakingProgress
        template["normalizedProgress"] = if (ticks == 0) 0 else (breakingProgress * ticks.asFraction).asBigInteger.asInt
        template["percentage"] = if (ticks == 0) 0.0 else breakingProgress.asDouble * 100.0
        template["duration"] = breakingTime
        template["miningSpeed"] = modifiedMiningSpeed
        template["originalMiningSpeed"] = miningSpeed
        template["ticks"] = ticks
        template["unmodifiedTicks"] = block.getTicksActual(modifiedMiningSpeed)
        template["originalTicks"] = block.getTicksOriginal(modifiedMiningSpeed)
        template["instaMine"] = block.canInstaMine(modifiedMiningSpeed)
    }

    private fun resetBreaking() {
        breakingBlock = null
        breakingProgress = Fraction.ZERO
        breakingTime = 0
    }

    private fun reset() {
        destroyedBlocks.clear()
        isAvailable = false
        resetBreaking()
    }

    private fun getOre(pos: Vec3I): Ore? {
        val blockPos = pos.asBlockPos
        return Ore.getByBlockState(
            MC.theWorld.getBlockState(blockPos).block,
            MC.theWorld.getChunkFromBlockCoords(blockPos).getBlockMetadata(blockPos),
        )
    }

    private fun breakBlock() {
        val block = breakingBlock ?: return
        options.onBreakBlock(logger, ::setPlaceholders)
        YC.eventDispatcher(MiningPredictionEvent.BreakBlock)
        destroyedBlocks[breakingPos] = BlockInfo(block, breakingPos, System.nanoTime())
        updateWorldRenderBlock(breakingPos)
        resetBreaking()
    }

    private fun ensureAvailable() {
        if (!isAvailable) {
            longreturn {}
        }
    }

    private fun check(accumulateProgress: Boolean) {
        val block = breakingBlock ?: return
        val modifiedMiningSpeed = block.type.modifyMiningSpeed(miningSpeed)
        if (modifiedMiningSpeed > 0) {
            val ticks = max(0, block.getTicksActual(modifiedMiningSpeed) + options.durationOffset)
            if (ticks == 0) {
                breakingProgress = Fraction.ONE
                breakBlock()
            } else if (accumulateProgress) {
                breakingProgress += 1 over ticks
            }
        }
        if (breakingProgress >= Fraction.ONE) breakBlock()
    }

    private fun removeOutdatedBlocks() {
        val time = System.nanoTime()
        destroyedBlocks.entries.removeIf { (_, info) ->
            (time - info.time >= 1000000000 || info.block !== getOre(info.blockPos)).also {
                if (it) updateWorldRenderBlock(info.blockPos)
            }
        }
    }

    fun printDebugInfo() {
        printChat("Available: $isAvailable")
        printChat("Tab Mining Speed: $miningSpeed")
    }

    override fun registerEvents(registry: RegistryEventDispatcher) {
        registry.run {
            register<YCMinecraftEvent.LoadWorld.Pre> {
                reset()
            }

            register<YCMinecraftEvent.Tick.Pre> {
                longrun {
                    isAvailable = false
                    miningSpeed = 0

                    ensureEnabled()
                    ensureInWorld()

                    if (!options.forceEnabled) {
                        ensureSkyBlockModes(SKYBLOCK_MINING_ISLANDS)
                    }

                    isAvailable = true

                    MC.thePlayer.sendQueue.playerInfoMap.firstOrNull {
                        noexcept(logger::catching) {
                            val rawName = MC.ingameGUI.tabList.getPlayerName(it)
                            if ("Mining Speed" in rawName) {
                                miningSpeed =
                                    rawName.trimStyle
                                        .filter { it.isDigit() }
                                        .asInt
                                return@firstOrNull true
                            }
                        }
                        false
                    }

                    check(true)
                    removeOutdatedBlocks()
                }
            }

            register<MiningPredictionEvent.Mining> { event ->
                longrun {
                    ensureEnabled()
                    ensureAvailable()

                    if (MC.thePlayer.heldItem?.item !in SKYBLOCK_MINING_TOOLS) {
                        resetBreaking()
                    } else {
                        val ore = getOre(event.pos)
                        if (ore !== breakingBlock) {
                            resetBreaking()
                            breakingPos = event.pos
                            if (event.pos !in destroyedBlocks) {
                                breakingBlock = ore
                                check(false)
                                removeOutdatedBlocks()
                            }
                        }
                    }
                }
            }

            register<MiningPredictionEvent.NotMining> {
                resetBreaking()
            }

            register<MiningPredictionEvent.RenderBlockDamage> { event ->
                longrun {
                    ensureEnabled()
                    ensureAvailable()

                    removeOutdatedBlocks()

                    breakingBlock?.let {
                        event.mutableDamages.entries.removeIf { (_, damage) ->
                            damage.position.asVec3I == breakingPos
                        }

                        event.mutableDamages[MC.thePlayer.entityId] =
                            DestroyBlockProgress(
                                0,
                                breakingPos.asBlockPos,
                            ).apply { partialBlockDamage = max(0, min(10, (10.0 * breakingProgress.asDouble).asInt)) }
                    }
                }
            }

            register<YCRenderEvent.Block.ProcessBlockState> { event ->
                longrun {
                    ensureEnabled()
                    ensureAvailable()

                    destroyedBlocks[event.blockPos]?.let { info ->
                        if (getOre(info.blockPos) === info.block) {
                            event.mutableBlockState = options.destroyedBlock.blockState
                        }
                    }
                }
            }
        }
    }
}
