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

package yqloss.yqlossclientmixinkt.impl.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import yqloss.yqlossclientmixinkt.nativeapi.WindowsX64NativeAPI;

@Mixin(targets = "org.lwjgl.opengl.WindowsDisplay")
public abstract class MixinWindowsDisplay {
    @Inject(method = "doHandleMessage", at = @At("HEAD"), remap = false, cancellable = true)
    private void yc$rawInput$doHandleMessagePre(long hwnd, int msg, long wParam, long lParam, long millis, CallbackInfoReturnable<Long> cir) {
        Long result = WindowsX64NativeAPI.handleWindowMessage(hwnd, msg, wParam, lParam);
        if (result != null) {
            cir.setReturnValue(result);
        }
    }
}
