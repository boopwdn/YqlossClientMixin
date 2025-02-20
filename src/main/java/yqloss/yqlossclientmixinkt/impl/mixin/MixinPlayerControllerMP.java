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

import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import yqloss.yqlossclientmixinkt.impl.mixincallback.CallbackPlayerControllerMP;

@Mixin(PlayerControllerMP.class)
public abstract class MixinPlayerControllerMP {
    @Shadow
    private ItemStack currentItemHittingBlock;

    @Shadow
    private BlockPos currentBlock;

    @Inject(method = "onPlayerDamageBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/PlayerControllerMP;clickBlock(Lnet/minecraft/util/BlockPos;Lnet/minecraft/util/EnumFacing;)Z"))
    private void yc$miningprediction$onPlayerDamageBlockNotMining(BlockPos posBlock, EnumFacing directionFacing, CallbackInfoReturnable<Boolean> cir) {
        CallbackPlayerControllerMP.MiningPrediction.INSTANCE.sendClickBlockToControllerNotMining();
    }

    @Inject(method = "isHittingPosition", at = @At("HEAD"), cancellable = true)
    private void yc$tweaks$isHittingPositionCheck(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        CallbackPlayerControllerMP.Tweaks.INSTANCE.isHittingPositionCheck(pos, currentBlock, currentItemHittingBlock, cir);
    }
}
