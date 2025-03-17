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

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S32PacketConfirmTransaction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yqloss.yqlossclientmixinkt.impl.mixincallback.CallbackNetHandlerPlayClient;

@Mixin(NetHandlerPlayClient.class)
public abstract class MixinNetHandlerPlayClient {
    @Shadow
    private Minecraft gameController;

    @ModifyVariable(method = "handleChat", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private S02PacketChat yc$handleChatPre1(S02PacketChat arg) {
        if (!gameController.isCallingFromMinecraftThread()) return arg;
        return CallbackNetHandlerPlayClient.YqlossClient.INSTANCE.handleChatPre1(arg);
    }

    @Inject(method = "handleChat", at = @At(value = "HEAD"), cancellable = true)
    private void yc$handleChatPre2(S02PacketChat packetIn, CallbackInfo ci) {
        if (!gameController.isCallingFromMinecraftThread()) return;
        CallbackNetHandlerPlayClient.YqlossClient.INSTANCE.handleChatPre2(packetIn, ci);
    }

    @Inject(method = "handleChat", at = @At(value = "RETURN"))
    private void yc$handleChatPost(S02PacketChat packetIn, CallbackInfo ci) {
        if (!gameController.isCallingFromMinecraftThread()) return;
        CallbackNetHandlerPlayClient.YqlossClient.INSTANCE.handleChatPost(packetIn);
    }

    @Inject(method = "handleConfirmTransaction", at = @At(value = "HEAD"))
    private void yc$handleConfirmTransactionPre(S32PacketConfirmTransaction packetIn, CallbackInfo ci) {
        if (!gameController.isCallingFromMinecraftThread()) return;
        CallbackNetHandlerPlayClient.YqlossClient.INSTANCE.handleConfirmTransactionPre();
    }
}
