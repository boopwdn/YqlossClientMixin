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

@file:JvmName("WindowsX64NativeAPI")

package yqloss.yqlossclientmixinkt.nativeapi

import org.lwjgl.input.Mouse
import yqloss.yqlossclientmixinkt.YqlossClient
import yqloss.yqlossclientmixinkt.module.rawinput.NativeRawInputProvider
import yqloss.yqlossclientmixinkt.module.rawinput.RawInput
import yqloss.yqlossclientmixinkt.util.math.double
import yqloss.yqlossclientmixinkt.util.scope.nothrow
import yqloss.yqlossclientmixinkt.util.scope.withscope
import yqloss.yqlossclientmixinkt.ycLogger
import java.io.File

external fun registerRawInputDevices()

external fun unregisterRawInputDevices()

external fun getRawInputData(
    lParam: Long,
    result: LongArray = LongArray(64),
): LongArray

external fun clipCursorAtCenter(hwnd: Long)

external fun cancelClipCursor()

fun handleWindowMessage(
    hwnd: Long,
    msg: Int,
    wParam: Long,
    lParam: Long,
): Long? {
    when (msg) {
        255 -> {
            val data = getRawInputData(lParam)
            if (data[0] == 0L) {
                NativeRawInputProvider.handleMouseMove(data[21].double, data[22].double)
                if (NativeRawInputProvider.rawInputMode) {
                    clipCursorAtCenter(hwnd)
                }
            }
        }

        512 -> {
            if (RawInput.provider === NativeRawInputProvider && Mouse.isGrabbed()) return 0
        }
    }
    return null
}

fun loadWindowsX64NativeAPI() {
    nothrow(ycLogger("Windows X64 Native API Loader")::catching) {
        val extractedFile = File("./yqlossclient-native/client.exe")
        withscope {
            File("./yqlossclient-native").mkdirs()
            val resource =
                YqlossClient::class.java
                    .getResourceAsStream("/assets/yqlossclientmixin/native/client.exe")!!
                    .using()
            val extracted = extractedFile.outputStream().using()
            resource.copyTo(extracted)
        }
        System.load(extractedFile.absolutePath)
    }
}
