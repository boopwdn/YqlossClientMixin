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
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.BlockPos;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.PixelFormat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yqloss.yqlossclientmixinkt.impl.mixincallback.CallbackMinecraft;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft {
    @Redirect(method = "createDisplay", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/Display;create(Lorg/lwjgl/opengl/PixelFormat;)V", remap = false))
    private void yc$createDisplayAddStencilBuffer(PixelFormat pixelFormat) throws Exception {
        Display.create(pixelFormat.withStencilBits(8));
    }

    @Inject(method = "startGame", at = @At("RETURN"))
    private void yc$startGamePost(CallbackInfo ci) {
        CallbackMinecraft.YqlossClient.INSTANCE.startGamePost();
    }

    @Inject(method = "runGameLoop", at = @At("HEAD"))
    private void yc$runGameLoopPre(CallbackInfo ci) {
        CallbackMinecraft.YqlossClient.INSTANCE.runGameLoopPre();
    }

    @Inject(method = "runTick", at = @At("HEAD"))
    private void yc$runTickPre(CallbackInfo ci) {
        CallbackMinecraft.YqlossClient.INSTANCE.runTickPre();
    }

    @Inject(method = "loadWorld(Lnet/minecraft/client/multiplayer/WorldClient;Ljava/lang/String;)V", at = @At("HEAD"))
    private void yc$loadWorldPre(WorldClient worldClientIn, String loadingMessage, CallbackInfo ci) {
        CallbackMinecraft.YqlossClient.INSTANCE.loadWorldPre(worldClientIn);
    }

    @Redirect(method = "runTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiScreen;handleInput()V"))
    private void yc$runTickHandleInput(GuiScreen instance) {
        CallbackMinecraft.YqlossClient.INSTANCE.runTickHandleInput(instance);
    }

    @Redirect(method = "runTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiScreen;handleKeyboardInput()V"))
    private void yc$runTickHandleKeyboardInput(GuiScreen instance) {
        CallbackMinecraft.YqlossClient.INSTANCE.runTickHandleKeyboardInput(instance);
    }

    @Redirect(method = "runTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiScreen;handleMouseInput()V"))
    private void yc$runTickHandleMouseInput(GuiScreen instance) {
        CallbackMinecraft.YqlossClient.INSTANCE.runTickHandleMouseInput(instance);
    }

    @Redirect(
        method = "rightClickMouse",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/WorldClient;isAirBlock(Lnet/minecraft/util/BlockPos;)Z")
    )
    private boolean yc$tweaks$rightClickMouseClickBlockPre(WorldClient instance, BlockPos blockPos) {
        return instance.isAirBlock(blockPos) || CallbackMinecraft.Tweaks.INSTANCE.rightClickMouseClickBlockPre(instance, blockPos);
    }

    @Inject(method = "sendClickBlockToController", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/MovingObjectPosition;getBlockPos()Lnet/minecraft/util/BlockPos;"))
    private void yc$miningprediction$sendClickBlockToControllerMining(boolean leftClick, CallbackInfo ci) {
        CallbackMinecraft.MiningPrediction.INSTANCE.sendClickBlockToControllerMining(((Minecraft) (Object) this).objectMouseOver.getBlockPos());
    }

    @Inject(method = "sendClickBlockToController", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/PlayerControllerMP;resetBlockRemoving()V"))
    private void yc$miningprediction$sendClickBlockToControllerNotMining(boolean leftClick, CallbackInfo ci) {
        CallbackMinecraft.MiningPrediction.INSTANCE.sendClickBlockToControllerNotMining();
    }

    @Inject(method = "toggleFullscreen", at = @At(value = "HEAD"), cancellable = true)
    private void yc$windowproperties$toggleFullscreenPre(CallbackInfo ci) {
        CallbackMinecraft.WindowProperties.INSTANCE.toggleFullscreenPre(ci);
    }
}
