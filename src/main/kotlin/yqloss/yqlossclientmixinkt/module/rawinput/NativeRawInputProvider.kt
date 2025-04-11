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
import org.lwjgl.opengl.Display
import yqloss.yqlossclientmixinkt.nativeapi.cancelClipCursor
import yqloss.yqlossclientmixinkt.nativeapi.registerRawInputDevices
import yqloss.yqlossclientmixinkt.nativeapi.unregisterRawInputDevices
import yqloss.yqlossclientmixinkt.util.property.trigger
import yqloss.yqlossclientmixinkt.util.scope.nothrow

object NativeRawInputProvider : RawInputProvider {
    var rawInputMode = false
        private set

    private val onGrabStateChange: Unit by trigger({ RawInput.provider === this && Mouse.isGrabbed() }) {
        nothrow {
            val grabbed = Mouse.isGrabbed()
            Mouse.setGrabbed(!grabbed)
            Mouse.setCursorPosition(Display.getWidth() / 2, Display.getHeight() / 2)
            Mouse.setGrabbed(grabbed)
            Mouse.setCursorPosition(Display.getWidth() / 2, Display.getHeight() / 2)
            if (RawInput.provider === this && Mouse.isGrabbed()) {
                registerRawInputDevices()
                rawInputMode = true
            } else {
                unregisterRawInputDevices()
                rawInputMode = false
                cancelClipCursor()
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
