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

import net.minecraft.client.gui.ScaledResolution
import yqloss.yqlossclientmixinkt.event.RegistryEventDispatcher
import yqloss.yqlossclientmixinkt.event.register
import yqloss.yqlossclientmixinkt.impl.nanovgui.GUIEvent
import yqloss.yqlossclientmixinkt.impl.nanovgui.Transformation
import yqloss.yqlossclientmixinkt.module.YCModule
import yqloss.yqlossclientmixinkt.module.option.YCModuleOptions
import yqloss.yqlossclientmixinkt.util.MC
import yqloss.yqlossclientmixinkt.util.general.inBox
import yqloss.yqlossclientmixinkt.util.math.Vec2D

abstract class YCModuleScreenBase<TO : YCModuleOptions, TM : YCModule<in TO>>(
    module: TM,
) : YCModuleGUIBase<TO, TM>(module.inBox.cast()) {
    open val scaleOverride: Double? = null

    override val transformation: Transformation
        get() {
            return ScaledResolution(MC).run {
                Transformation()
                    .translateScreen(Vec2D(0.5, 0.5))
                    .scaleMC(scaleOverride)
                    .translate(-size / 2.0)
            }
        }

    override fun registerEvents(registry: RegistryEventDispatcher) {
        super.registerEvents(registry)
        registry.run {
            register<GUIEvent.Screen> { onRender(it.widgets) }
        }
    }
}
