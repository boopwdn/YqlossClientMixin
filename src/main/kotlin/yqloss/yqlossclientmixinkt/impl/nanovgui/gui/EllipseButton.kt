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

package yqloss.yqlossclientmixinkt.impl.nanovgui.gui

import yqloss.yqlossclientmixinkt.impl.nanovgui.Transformation
import yqloss.yqlossclientmixinkt.impl.nanovgui.Widget
import yqloss.yqlossclientmixinkt.impl.nanovgui.widget.EllipseWidget
import yqloss.yqlossclientmixinkt.util.mousePosition
import kotlin.math.abs

abstract class EllipseButton<T>(
    info: T,
) : Button<T>(info) {
    override fun isHovered(tr: Transformation): Boolean {
        val halfSize = collisionSize / 2.0
        val ttr = tr + halfSize
        val mouse = !ttr pos mousePosition
        val x = abs(mouse.x / halfSize.x)
        val y = abs(mouse.y / halfSize.y)
        return x * x + y * y < 1
    }

    override fun renderBackground(
        widgets: MutableList<Widget<*>>,
        tr: Transformation,
        hovered: Boolean,
    ) {
        val argb = getActualColor(hovered)
        val renderOffset = getRenderSize(hovered) / 2.0
        widgets.add(
            EllipseWidget(
                tr pos -renderOffset,
                tr pos renderOffset,
                argb,
            ),
        )
    }
}
