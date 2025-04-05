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

import org.lwjgl.input.Mouse
import yqloss.yqlossclientmixinkt.nativeapi.registerRawInputDevices
import yqloss.yqlossclientmixinkt.nativeapi.unregisterRawInputDevices
import yqloss.yqlossclientmixinkt.util.property.versionedLazy
import yqloss.yqlossclientmixinkt.util.scope.nothrow

object NativeRawInputProvider : RawInputProvider {
    private val onGrabStateChange: Unit by versionedLazy({ RawInput.provider === this && Mouse.isGrabbed() }) {
        nothrow {
            if (RawInput.provider === this && Mouse.isGrabbed()) {
                registerRawInputDevices()
            } else {
                unregisterRawInputDevices()
            }
        }
        Unit
    }

    override fun update() {
        onGrabStateChange
    }

    fun handleMouseMove(
        x: Double,
        y: Double,
    ) {
        if (RawInput.provider !== this) return

        RawInput.mouseHelper.x += x
        RawInput.mouseHelper.y += y
    }
}
