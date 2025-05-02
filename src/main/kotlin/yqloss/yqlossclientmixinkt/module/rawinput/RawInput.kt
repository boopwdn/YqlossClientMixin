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

package yqloss.yqlossclientmixinkt.module.rawinput

import yqloss.yqlossclientmixinkt.event.YCEventRegistry
import yqloss.yqlossclientmixinkt.event.minecraft.YCMinecraftEvent
import yqloss.yqlossclientmixinkt.event.register
import yqloss.yqlossclientmixinkt.module.YCModuleBase
import yqloss.yqlossclientmixinkt.module.moduleInfo
import yqloss.yqlossclientmixinkt.util.math.int

val INFO_RAW_INPUT = moduleInfo<RawInputOptions>("raw_input", "Raw Input")

object RawInput : YCModuleBase<RawInputOptions>(INFO_RAW_INPUT) {
    var x = 0.0
    var y = 0.0

    val provider get() = if (options.nativeRawInput) NativeRawInputProvider else JInputRawInputProvider

    override fun registerEvents(registry: YCEventRegistry) {
        registry.run {
            register<YCMinecraftEvent.Loop.Pre> {
                NativeRawInputProvider.update()
                JInputRawInputProvider.update()

                provider.poll()
            }

            register<RawInputEvent.ModifyDeltaEvent> { event ->
                if (options.enabled) {
                    val xInt = x.int
                    val yInt = y.int
                    event.mutableDeltaX = xInt
                    event.mutableDeltaY = -yInt
                    x -= xInt
                    y -= yInt
                } else {
                    x = 0.0
                    y = 0.0
                }
            }
        }
    }
}
