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

package yqloss.yqlossclientmixinkt.impl.nanovgui.widget

import yqloss.yqlossclientmixinkt.impl.module.cursor.CursorOverlay.SamplePoint
import yqloss.yqlossclientmixinkt.impl.nanovgui.NanoVGUIContext
import yqloss.yqlossclientmixinkt.impl.nanovgui.Widget
import yqloss.yqlossclientmixinkt.impl.oneconfiginternal.nvg
import yqloss.yqlossclientmixinkt.impl.util.alphaScale
import yqloss.yqlossclientmixinkt.util.math.Vec2D
import kotlin.math.atan2

data class CursorTrailWidget(
    private val samplePoints: List<SamplePoint>,
    private val mouse: Vec2D,
    private val radiusInner: Double,
    private val radiusOuter: Double,
    private val color: Int,
) : Widget<CursorTrailWidget> {
    override fun draw(context: NanoVGUIContext) {
        context.run {
            if (samplePoints.isEmpty()) return

            fun getPoint(i: Int): SamplePoint {
                return if (i == samplePoints.lastIndex) samplePoints[i].copy(position = mouse) else samplePoints[i]
            }

            if (samplePoints.size == 1) {
                val point = getPoint(0)
                nvg.drawTrailCircle(
                    vg,
                    point.position.x,
                    point.position.y,
                    radiusInner,
                    radiusOuter,
                    color,
                )
                return
            }

            fun inAngle(i: Int): Double {
                return if (i == 0) {
                    getAngle(getPoint(0), getPoint(1))
                } else {
                    getAngle(getPoint(i), getPoint(i - 1))
                }
            }

            fun outAngle(i: Int): Double {
                return if (i == samplePoints.lastIndex) {
                    getAngle(getPoint(samplePoints.lastIndex), getPoint(samplePoints.size - 2))
                } else {
                    getAngle(getPoint(i), getPoint(i + 1))
                }
            }

            samplePoints.dropLast(1).forEachIndexed { i, it ->
                val next = getPoint(i + 1)
                nvg.drawTrailCorner(
                    vg,
                    it.position.x,
                    it.position.y,
                    inAngle(i),
                    outAngle(i),
                    radiusInner,
                    radiusOuter,
                    color alphaScale it.alpha,
                )
                nvg.drawTrailLine(
                    vg,
                    it.position.x,
                    it.position.y,
                    next.position.x,
                    next.position.y,
                    inAngle(i),
                    outAngle(i + 1),
                    radiusInner,
                    radiusOuter,
                    color alphaScale it.alpha,
                )
            }

            val last = getPoint(samplePoints.lastIndex)
            nvg.drawTrailCorner(
                vg,
                last.position.x,
                last.position.y,
                inAngle(samplePoints.lastIndex),
                outAngle(samplePoints.lastIndex),
                radiusInner,
                radiusOuter,
                color alphaScale last.alpha,
            )
        }
    }

    private fun getAngle(
        p1: SamplePoint,
        p2: SamplePoint,
    ) = atan2(p2.position.y - p1.position.y, p2.position.x - p1.position.x)

    override fun alphaScale(alpha: Double) = throw NotImplementedError()

    override fun scale(
        scale: Double,
        origin: Vec2D,
    ) = throw NotImplementedError()
}
