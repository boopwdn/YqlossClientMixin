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
import net.minecraftforge.client.ForgeHooksClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yqloss.yqlossclientmixinkt.impl.mixincallback.CallbackForgeHooksClient;

@Mixin(ForgeHooksClient.class)
public class MixinForgeHooksClient {
    @Inject(method = "drawScreen", at = @At("HEAD"), cancellable = true, remap = false)
    private static void yc$drawScreenPre(GuiScreen screen, int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        CallbackForgeHooksClient.YqlossClient.INSTANCE.drawScreenPre(screen, mouseX, mouseY, partialTicks, ci);
    }

    @Inject(method = "drawScreen", at = @At("RETURN"), remap = false)
    private static void yc$drawScreenPost(GuiScreen screen, int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        CallbackForgeHooksClient.YqlossClient.INSTANCE.drawScreenPost();
    }
}
