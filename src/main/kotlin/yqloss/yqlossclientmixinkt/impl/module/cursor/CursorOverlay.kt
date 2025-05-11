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
import yqloss.yqlossclientmixinkt.impl.nanovgui.widget.CursorTrailWidget
import yqloss.yqlossclientmixinkt.impl.option.module.CursorOptionsImpl
import yqloss.yqlossclientmixinkt.module.cursor.Cursor
import yqloss.yqlossclientmixinkt.module.ensureEnabled
import yqloss.yqlossclientmixinkt.util.math.Vec2D
import yqloss.yqlossclientmixinkt.util.mousePosition
import yqloss.yqlossclientmixinkt.util.scope.longRun

object CursorOverlay : YCModuleImplBase<CursorOptionsImpl, Cursor>(Cursor) {
    data class SamplePoint(
        val position: Vec2D,
        var alpha: Double,
    )

    private val samplePoints = ArrayDeque<SamplePoint>()

    private var lastUpdateNanos = System.nanoTime()

    private val trailTime = 5e9

    override fun registerEvents(registry: YCEventRegistry) {
        registry.run {
            register<GUIEvent.Screen>(Int.MAX_VALUE - 1000) { event ->
                longRun {
                    ensureEnabled()

//                    samplePoints.clear()
//                    samplePoints.addLast(SamplePoint(Vec2D(200.0, 200.0), 1.0))
//                    samplePoints.addLast(SamplePoint(Vec2D(500.0, 500.0), 1.0))
//                    samplePoints.addLast(SamplePoint(mousePosition, 1.0))
//
//                    event.widgets += CursorTrailWidget(samplePoints)
//
//                    return@longRun
                    val radiusInner = 0.0 // TODO
                    val radiusOuter = 20.0 // TODO
                    val color = -1 // TODO

                    val mouse = mousePosition
                    val time = System.nanoTime()
                    val diff = time - lastUpdateNanos
                    val alphaDiff = diff / trailTime

                    samplePoints.forEach { it.alpha -= alphaDiff }
                    samplePoints.removeAll { it.alpha <= 0 }

                    lastUpdateNanos = time

                    run {
                        if (samplePoints.isNotEmpty()) {
                            val lastPoint = samplePoints.last()
                            if ((mouse - lastPoint.position).length < radiusOuter) {
                                lastPoint.alpha = 1.0
                                return@run
                            }
                        }

                        samplePoints.addLast(SamplePoint(mouse, 1.0))
                    }

                    if (samplePoints.isEmpty()) return@longRun

                    event.widgets +=
                        CursorTrailWidget(
                            samplePoints,
                            mouse,
                            radiusInner,
                            radiusOuter,
                            color,
                        )
                }
            }
        }
    }
}
