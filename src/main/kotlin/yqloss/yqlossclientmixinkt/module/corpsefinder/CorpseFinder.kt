/*
 * Copyright (C) 2025 Yqloss
 *
 * This file is part of Yqloss Client (Mixin).
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2 (GPLv2)
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Yqloss Client (Mixin). If not, see <https://www.gnu.org/licenses/old-licenses/gpl-2.0.html>.
 */

package yqloss.yqlossclientmixinkt.module.corpsefinder

import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.entity.item.EntityArmorStand
import org.lwjgl.opengl.GL11.*
import yqloss.yqlossclientmixinkt.api.setDefault
import yqloss.yqlossclientmixinkt.event.RegistryEventDispatcher
import yqloss.yqlossclientmixinkt.event.minecraft.YCMinecraftEvent
import yqloss.yqlossclientmixinkt.event.minecraft.YCRenderEvent
import yqloss.yqlossclientmixinkt.event.register
import yqloss.yqlossclientmixinkt.module.*
import yqloss.yqlossclientmixinkt.module.option.YCColor
import yqloss.yqlossclientmixinkt.module.option.invoke
import yqloss.yqlossclientmixinkt.util.*
import yqloss.yqlossclientmixinkt.util.general.intervalAction
import yqloss.yqlossclientmixinkt.util.math.Vec3D
import yqloss.yqlossclientmixinkt.util.math.Vec3I
import yqloss.yqlossclientmixinkt.util.math.asFloorVec3I
import yqloss.yqlossclientmixinkt.util.scope.longrun

val INFO_CORPSE_FINDER = moduleInfo<CorpseFinderOptions>("corpse_finder", "Corpse Finder")

object CorpseFinder : YCModuleBase<CorpseFinderOptions>(INFO_CORPSE_FINDER) {
    private val corpses = mutableMapOf<Vec3I, Pair<Vec3D, CorpseType>>()
    private var exit: Vec3D? = null

    private val scanCorpses =
        intervalAction(1000000000) {
            longrun {
                ensureEnabled()
                ensureInWorld()

                if (!options.forceEnabled) {
                    ensureSkyBlockMode("mineshaft")
                }

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
        color.glColor()

        mcRenderScope(GL_QUAD_STRIP, DefaultVertexFormats.POSITION) {
            pos(Vec3D(-0.5, 0.0, -0.5) + pos).endVertex()
            pos(Vec3D(-0.5, 1.0, -0.5) + pos).endVertex()
            pos(Vec3D(0.5, 0.0, -0.5) + pos).endVertex()
            pos(Vec3D(0.5, 1.0, -0.5) + pos).endVertex()
            pos(Vec3D(0.5, 0.0, 0.5) + pos).endVertex()
            pos(Vec3D(0.5, 1.0, 0.5) + pos).endVertex()
            pos(Vec3D(-0.5, 0.0, 0.5) + pos).endVertex()
            pos(Vec3D(-0.5, 1.0, 0.5) + pos).endVertex()
        }

        mcRenderScope(GL_QUAD_STRIP, DefaultVertexFormats.POSITION) {
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

    override fun registerEvents(registry: RegistryEventDispatcher) {
        registry.run {
            register<YCMinecraftEvent.Tick.Pre> {
                scanCorpses()
            }

            register<YCMinecraftEvent.LoadWorld.Pre> {
                exit = null
                corpses.clear()
            }

            register<YCRenderEvent.Entity.Post> {
                longrun {
                    ensureEnabled()
                    ensureInWorld()

                    if (!options.forceEnabled) {
                        ensureSkyBlockMode("mineshaft")
                    }

                    glStateScope {
                        glDisable(GL_TEXTURE_2D)
                        glEnable(GL_BLEND)
                        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
                        glDisable(GL_DEPTH_TEST)
                        glDisable(GL_ALPHA_TEST)
                        glEnable(GL_CULL_FACE)
                        glDisable(GL_LIGHTING)
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
}
