package yqloss.yqlossclientmixinkt.module.corpsefinder

import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.entity.item.EntityArmorStand
import org.lwjgl.opengl.GL11
import yqloss.yqlossclientmixinkt.api.setDefault
import yqloss.yqlossclientmixinkt.event.RegistrationEventDispatcher
import yqloss.yqlossclientmixinkt.event.minecraft.YCMinecraftEvent
import yqloss.yqlossclientmixinkt.event.minecraft.YCRenderEvent
import yqloss.yqlossclientmixinkt.event.register
import yqloss.yqlossclientmixinkt.module.YCModuleBase
import yqloss.yqlossclientmixinkt.module.ensureEnabled
import yqloss.yqlossclientmixinkt.module.ensureInWorld
import yqloss.yqlossclientmixinkt.module.ensureSkyBlockMode
import yqloss.yqlossclientmixinkt.module.moduleInfo
import yqloss.yqlossclientmixinkt.module.option.YCColor
import yqloss.yqlossclientmixinkt.module.option.invoke
import yqloss.yqlossclientmixinkt.util.MC
import yqloss.yqlossclientmixinkt.util.currPos
import yqloss.yqlossclientmixinkt.util.general.intervalAction
import yqloss.yqlossclientmixinkt.util.glColor
import yqloss.yqlossclientmixinkt.util.glStateScope
import yqloss.yqlossclientmixinkt.util.glTranslate
import yqloss.yqlossclientmixinkt.util.math.Vec3D
import yqloss.yqlossclientmixinkt.util.math.Vec3I
import yqloss.yqlossclientmixinkt.util.mcRenderScope
import yqloss.yqlossclientmixinkt.util.pos
import yqloss.yqlossclientmixinkt.util.renderPos
import yqloss.yqlossclientmixinkt.util.scope.longrun
import yqloss.yqlossclientmixinkt.util.scope.noexcept
import yqloss.yqlossclientmixinkt.util.skyBlockID
import yqloss.yqlossclientmixinkt.util.trimStyle

val INFO_CORPSE_FINDER = moduleInfo<CorpseFinderOptions>("corpse_finder", "Corpse Finder")

object CorpseFinder : YCModuleBase<CorpseFinderOptions>(INFO_CORPSE_FINDER) {
    private val corpses = mutableMapOf<Vec3I, Pair<Vec3D, CorpseType>>()
    private var exit: Vec3D? = null

    private val scanCorpses =
        intervalAction(1000000000) {
            longrun {
                ensureEnabled()
                ensureInWorld()
                ensureSkyBlockMode("mineshaft")

                MC.theWorld.loadedEntityList
                    .filterIsInstance<EntityArmorStand>()
                    .apply {
                        exit ?: onEach {
                            if (it.displayName.unformattedText.trimStyle == "Exit the Glacite Mineshaft") {
                                exit = it.currPos
                            }
                        }
                    }.filter { !it.isInvisible && it.showArms }
                    .filter { (0..3).all { i -> it.getCurrentArmor(i) !== null } }
                    .filter { it.currPos.asFloorVec3I !in corpses }
                    .forEach { entity ->
                        entity.getCurrentArmor(0).skyBlockID?.let { CorpseType.getByArmor(it) }?.let { corpse ->
                            corpses[entity.currPos.asFloorVec3I] = entity.currPos to corpse
                            corpse.option.notification(logger) {
                                setDefault()
                                this["pos"] = entity.currPos
                            }
                        }
                    }
            }
        }

    private fun renderBox(
        pos: Vec3D,
        color: YCColor,
    ) {
        noexcept(logger::catching) {
            color.glColor()

            mcRenderScope(GL11.GL_QUAD_STRIP, DefaultVertexFormats.POSITION) {
                pos(Vec3D(-0.5, 0.0, -0.5) + pos).endVertex()
                pos(Vec3D(-0.5, 1.0, -0.5) + pos).endVertex()
                pos(Vec3D(0.5, 0.0, -0.5) + pos).endVertex()
                pos(Vec3D(0.5, 1.0, -0.5) + pos).endVertex()
                pos(Vec3D(0.5, 0.0, 0.5) + pos).endVertex()
                pos(Vec3D(0.5, 1.0, 0.5) + pos).endVertex()
                pos(Vec3D(-0.5, 0.0, 0.5) + pos).endVertex()
                pos(Vec3D(-0.5, 1.0, 0.5) + pos).endVertex()
            }

            mcRenderScope(GL11.GL_QUAD_STRIP, DefaultVertexFormats.POSITION) {
                pos(Vec3D(0.5, 0.0, -0.5) + pos).endVertex()
                pos(Vec3D(0.5, 0.0, 0.5) + pos).endVertex()
                pos(Vec3D(-0.5, 0.0, -0.5) + pos).endVertex()
                pos(Vec3D(-0.5, 0.0, 0.5) + pos).endVertex()
                pos(Vec3D(-0.5, 1.0, -0.5) + pos).endVertex()
                pos(Vec3D(-0.5, 1.0, 0.5) + pos).endVertex()
                pos(Vec3D(0.5, 1.0, -0.5) + pos).endVertex()
                pos(Vec3D(0.5, 1.0, 0.5) + pos).endVertex()
            }
        }
    }

    override fun RegistrationEventDispatcher.registerEvents() {
        register<YCMinecraftEvent.Tick.Pre> {
            scanCorpses()
        }

        register<YCMinecraftEvent.LoadWorld.Pre> {
            corpses.clear()
        }

        register<YCRenderEvent.Entity.Post> {
            longrun {
                ensureEnabled()
                ensureInWorld()
                ensureSkyBlockMode("mineshaft")

                glStateScope {
                    GL11.glDisable(GL11.GL_TEXTURE_2D)
                    GL11.glEnable(GL11.GL_BLEND)
                    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
                    GL11.glDisable(GL11.GL_DEPTH_TEST)
                    GL11.glDisable(GL11.GL_ALPHA_TEST)
                    GL11.glEnable(GL11.GL_CULL_FACE)
                    GL11.glDisable(GL11.GL_LIGHTING)
                    (-MC.renderViewEntity.renderPos).glTranslate()

                    if (options.showExit) {
                        exit?.let { renderBox(it, options.exitColor) }
                    }

                    corpses.values.forEach { (pos, corpse) ->
                        renderBox(pos + Vec3D(0.0, 0.5, 0.0), corpse.option.color)
                    }
                }
            }
        }
    }
}
