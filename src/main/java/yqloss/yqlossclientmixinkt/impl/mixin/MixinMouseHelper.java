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

import kotlin.Pair;
import net.minecraft.util.MouseHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yqloss.yqlossclientmixinkt.impl.mixincallback.CallbackMouseHelper;

@Mixin(value = MouseHelper.class, priority = 900)
public abstract class MixinMouseHelper {
    @Shadow
    public int deltaX;

    @Shadow
    public int deltaY;

    @Inject(method = "mouseXYChange", at = @At("RETURN"))
    private void yc$rawInput$mouseXYChangePre(CallbackInfo ci) {
        Pair<Integer, Integer> result = CallbackMouseHelper.RawInput.INSTANCE.mouseXYChangePre(deltaX, deltaY);
        deltaX = result.getFirst();
        deltaY = result.getSecond();
    }
}
