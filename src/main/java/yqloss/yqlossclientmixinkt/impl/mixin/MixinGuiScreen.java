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

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yqloss.yqlossclientmixinkt.impl.mixincallback.CallbackGuiScreen;

@Mixin(GuiScreen.class)
public class MixinGuiScreen {
    @Inject(method = "drawDefaultBackground", at = @At("HEAD"), cancellable = true)
    public void yc$drawDefaultBackgroundPre(CallbackInfo ci) {
        CallbackGuiScreen.BetterTerminal.INSTANCE.drawDefaultBackgroundPre((GuiScreen) (Object) this, ci);
    }

    @Inject(method = "renderToolTip", at = @At("HEAD"), cancellable = true)
    public void yc$renderTooltipPre(ItemStack stack, int x, int y, CallbackInfo ci) {
        CallbackGuiScreen.BetterTerminal.INSTANCE.renderTooltipPre((GuiScreen) (Object) this, ci);
    }
}
