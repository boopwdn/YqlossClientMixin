package yqloss.yqlossclientmixinkt.module.tweaks

import net.minecraft.client.entity.EntityPlayerSP
import net.minecraft.init.Items
import yqloss.yqlossclientmixinkt.event.RegistryEventDispatcher
import yqloss.yqlossclientmixinkt.event.register
import yqloss.yqlossclientmixinkt.module.SKYBLOCK_MINING_ISLANDS
import yqloss.yqlossclientmixinkt.module.YCModuleBase
import yqloss.yqlossclientmixinkt.module.ensureEnabled
import yqloss.yqlossclientmixinkt.module.ensureInWorld
import yqloss.yqlossclientmixinkt.module.ensureNotCanceled
import yqloss.yqlossclientmixinkt.module.ensureSkyBlockModes
import yqloss.yqlossclientmixinkt.module.moduleInfo
import yqloss.yqlossclientmixinkt.util.MC
import yqloss.yqlossclientmixinkt.util.SKYBLOCK_MINING_TOOLS
import yqloss.yqlossclientmixinkt.util.scope.longrun
import yqloss.yqlossclientmixinkt.util.skyBlockUUID

val INFO_TWEAKS = moduleInfo<TweaksOptions>("tweaks", "Tweaks")

object Tweaks : YCModuleBase<TweaksOptions>(INFO_TWEAKS) {
    override fun registerEvents(registry: RegistryEventDispatcher) {
        registry.run {
            register<TweaksEvent.SetAnglesPost> { event ->
                longrun {
                    ensureEnabled { enableInstantAim }

                    if (event.entity !is EntityPlayerSP) return@longrun

                    event.entity.prevRotationYawHead = event.entity.prevRotationYaw
                    event.entity.rotationYawHead = event.entity.rotationYaw
                }
            }

            register<TweaksEvent.RightClickBlockPre> { event ->
                longrun {
                    ensureNotCanceled(event)
                    ensureEnabled { disablePearlClickBlock }
                    ensureInWorld()

                    if (MC.thePlayer.inventory
                            .getCurrentItem()
                            ?.item === Items.ender_pearl
                    ) {
                        event.canceled = true
                    }
                }
            }

            register<TweaksEvent.IsHittingPositionCheck> { event ->
                longrun {
                    ensureNotCanceled(event)
                    ensureEnabled { disableSkyBlockToolsNBTUpdateResetDigging }
                    ensureSkyBlockModes(SKYBLOCK_MINING_ISLANDS)

                    val heldItemStack = MC.thePlayer.heldItem
                    if (event.currentItemHittingBlock !== null &&
                        heldItemStack !== null &&
                        heldItemStack.item in SKYBLOCK_MINING_TOOLS &&
                        heldItemStack.item === event.currentItemHittingBlock.item &&
                        heldItemStack.skyBlockUUID !== null &&
                        heldItemStack.skyBlockUUID == event.currentItemHittingBlock.skyBlockUUID
                    ) {
                        event.canceled = true
                        event.returnValue = event.pos !== null && event.pos == event.currentBlock
                    }
                }
            }
        }
    }
}
