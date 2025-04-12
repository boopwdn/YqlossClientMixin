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

package yqloss.yqlossclientmixinkt.impl.module

import yqloss.yqlossclientmixinkt.event.YCEventRegistry
import yqloss.yqlossclientmixinkt.impl.nanovgui.Transformation
import yqloss.yqlossclientmixinkt.impl.nanovgui.Widget
import yqloss.yqlossclientmixinkt.impl.nanovgui.WindowAnimation
import yqloss.yqlossclientmixinkt.module.YCModule
import yqloss.yqlossclientmixinkt.module.YCModuleBase
import yqloss.yqlossclientmixinkt.module.option.YCModuleOptions
import yqloss.yqlossclientmixinkt.util.general.inBox
import yqloss.yqlossclientmixinkt.util.math.Vec2D
import yqloss.yqlossclientmixinkt.util.scope.longrun

abstract class YCModuleGUIBase<TO : YCModuleOptions, TM : YCModule<in TO>>(
    protected val module: TM,
) : YCModuleBase<TO>(module.inBox.cast()) {
    protected abstract val width: Double
    protected abstract val height: Double
    protected open val fadeOut = 0L

    protected abstract fun ensureShow()

    protected abstract fun draw(
        widgets: MutableList<Widget<*>>,
        box: Vec2D,
        tr: Transformation,
    )

    protected open fun doesShow(): Boolean {
        var show = false
        longrun {
            ensureShow()
            show = true
        }
        return show
    }

    protected open fun reset() {}

    protected open val scaledWidth get() = width
    protected open val scaledHeight get() = height
    protected open val size get() = Vec2D(width, height)
    protected abstract val transformation: Transformation
    protected open var widgets = mutableListOf<Widget<*>>()
    protected open val animation = WindowAnimation()

    protected open fun getFadeOutOrigin(tr: Transformation) = tr pos size / 2.0

    protected open fun redraw(
        box: Vec2D,
        tr: Transformation,
    ) {
        widgets.clear()
        draw(widgets, box, tr)
    }

    protected open fun onRender(eventWidgets: MutableList<Widget<*>>) {
        val show = doesShow()
        val box = size
        val tr = transformation
        if (animation.update(show, box, tr, fadeOut)) {
            redraw(box, tr)
        }
        animation.mapWidgets(widgets, eventWidgets, getFadeOutOrigin(tr))
        if (!doesShow()) {
            reset()
        }
    }

    override fun registerEvents(registry: YCEventRegistry) {
    }
}
