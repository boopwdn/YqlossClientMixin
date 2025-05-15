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

package yqloss.yqlossclientmixinkt.impl.module.cursor

import yqloss.yqlossclientmixinkt.event.YCEventRegistry
import yqloss.yqlossclientmixinkt.event.register
import yqloss.yqlossclientmixinkt.impl.module.YCModuleImplBase
import yqloss.yqlossclientmixinkt.impl.nanovgui.GUIEvent
import yqloss.yqlossclientmixinkt.impl.option.module.CursorOptionsImpl
import yqloss.yqlossclientmixinkt.module.cursor.Cursor
import yqloss.yqlossclientmixinkt.module.ensureEnabled
import yqloss.yqlossclientmixinkt.util.extension.long
import yqloss.yqlossclientmixinkt.util.math.Vec2D
import yqloss.yqlossclientmixinkt.util.mousePosition
import yqloss.yqlossclientmixinkt.util.scope.longRun

object CursorOverlay : YCModuleImplBase<CursorOptionsImpl, Cursor>(Cursor) {
    data class SamplePoint(
        val position: Vec2D,
        val time: Long,
        val rendered: Boolean,
    )

    private val samplePoints = ArrayDeque<SamplePoint>()

    private val renderedPoints = ArrayDeque<SamplePoint>()

    private fun ArrayDeque<SamplePoint>.removeDead(time: Long) {
        val alive = ((options.duration + options.fade) * 1e9).long
        while (isNotEmpty()) {
            if (time - last().time >= alive) {
                removeLast()
            } else {
                break
            }
        }
    }

    override fun registerEvents(registry: YCEventRegistry) {
        registry.run {
            register<GUIEvent.Screen>(Int.MAX_VALUE - 1000) { event ->
                longRun {
                    ensureEnabled()

                    val mouse = mousePosition
                    val time = System.nanoTime()

                    samplePoints.removeDead(time)
                    renderedPoints.removeDead(time)

                    if (renderedPoints.isEmpty()) {
                        val sample = SamplePoint(mouse, time, true)
                        samplePoints += sample
                        renderedPoints += sample
                    } else {
                        val firstRendered = renderedPoints.first()
                        val direction = firstRendered.position - mouse
                        val unit = direction / direction.length
                        var renderLastPoint = false
                        samplePoints.firstOrNull {
                            if (it === firstRendered) {
                                true
                            } else {
                                val diff = it.position - mouse
                                val length = diff.length
                                val dot = diff * unit
                                val distanceSquared = length * length - dot * dot
                                if (dot < 0 || distanceSquared > options.optimization) {
                                    renderLastPoint = true
                                    true
                                } else {
                                    false
                                }
                            }
                        }
                        if (renderLastPoint) {
                            samplePoints[0] = samplePoints[0].copy(rendered = true)
                            renderedPoints.addFirst(samplePoints[0])
                        }
                        if (mouse != samplePoints.firstOrNull()?.position) {
                            samplePoints.addFirst(SamplePoint(mouse, time, false))
                        } else {
                            samplePoints[0] = samplePoints[0].copy(time = time)
                        }
                    }

                    if (samplePoints.isEmpty()) return@longRun

                    event.widgets +=
                        CursorTrailWidget(
                            samplePoints.filterIndexed { i, it -> i == 0 || it.rendered },
                            options.radius,
                            options.bloom,
                            options.duration,
                            options.fade,
                            options.color.rgb,
                            options.color.alpha / 255.0,
                            options.timeSamples,
                            options.radiusSamples,
                        )
                }
            }
        }
    }
}
