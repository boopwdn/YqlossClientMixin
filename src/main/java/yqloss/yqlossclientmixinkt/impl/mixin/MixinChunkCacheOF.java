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

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeGenBase;
import net.optifine.override.ChunkCacheOF;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import yqloss.yqlossclientmixinkt.impl.mixincallback.CallbackRenderChunk;

import java.util.Objects;

@Mixin(value = ChunkCacheOF.class, remap = false)
public abstract class MixinChunkCacheOF implements CallbackRenderChunk.YqlossClient.BlockAccessWrapper {
    @Unique
    private ChunkCacheOF yc$wrapped = null;

    @Unique
    private CallbackRenderChunk.YqlossClient.Wrapper yc$wrapper = null;

    @Unique
    private CallbackRenderChunk.YqlossClient.Wrapper yc$wrapper() {
        if (yc$wrapper == null) {
            yc$wrapper = Objects.requireNonNull(CallbackRenderChunk.YqlossClient.INSTANCE.getWrapperRef().get());
        }
        return yc$wrapper;
    }

    @Override
    public IBlockAccess getWrappedBlockAccess() {
        return yc$wrapped;
    }

    @Unique
    private IBlockAccess yc$wrap() {
        return yc$wrapper().wrap(yc$wrapped);
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void yc$initPre(ChunkCache chunkCache, BlockPos posFromIn, BlockPos posToIn, int subIn, CallbackInfo ci) {
        if (subIn == 1) {
            yc$wrapped = new ChunkCacheOF(chunkCache, posFromIn, posToIn, 2);
        }
    }

    @Inject(method = "func_180495_p", at = @At("HEAD"), cancellable = true, remap = false)
    private void yc$getBlockStatePre(BlockPos pos, CallbackInfoReturnable<IBlockState> cir) {
        if (yc$wrapped != null) {
            cir.setReturnValue(CallbackRenderChunk.YqlossClient.INSTANCE.rebuildChunkGetBlockState(yc$wrapper(), yc$wrapped, pos));
        }
    }

    @Inject(method = "func_175625_s", at = @At("HEAD"), cancellable = true, remap = false)
    private void yc$getTileEntityPre(BlockPos pos, CallbackInfoReturnable<TileEntity> cir) {
        if (yc$wrapped != null) {
            cir.setReturnValue(CallbackRenderChunk.YqlossClient.INSTANCE.rebuildChunkGetTileEntity(yc$wrapper(), yc$wrapped, pos));
        }
    }

    @Inject(method = "renderStart", at = @At("HEAD"), cancellable = true, remap = false)
    private void yc$renderStartPre(CallbackInfo ci) {
        if (yc$wrapped != null) {
            yc$wrapped.renderStart();
            ci.cancel();
        }
    }

    @Inject(method = "renderFinish", at = @At("HEAD"), cancellable = true, remap = false)
    private void yc$renderFinishPre(CallbackInfo ci) {
        if (yc$wrapped != null) {
            yc$wrapped.renderFinish();
            ci.cancel();
        }
    }

    @Inject(method = "isSideSolid", at = @At("HEAD"), cancellable = true, remap = false)
    private void yc$isSideSolidPre(BlockPos pos, EnumFacing side, boolean _default, CallbackInfoReturnable<Boolean> cir) {
        if (yc$wrapped != null) {
            cir.setReturnValue(yc$wrapped.isSideSolid(pos, side, _default));
        }
    }

    @Inject(method = "func_175626_b", at = @At("HEAD"), cancellable = true, remap = false)
    private void yc$getCombinedLightPre(BlockPos pos, int lightValue, CallbackInfoReturnable<Integer> cir) {
        if (yc$wrapped != null) {
            cir.setReturnValue(yc$wrap().getCombinedLight(pos, lightValue));
        }
    }

    @Inject(method = "func_175623_d", at = @At("HEAD"), cancellable = true, remap = false)
    private void yc$isAirBlockPre(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if (yc$wrapped != null) {
            cir.setReturnValue(yc$wrap().isAirBlock(pos));
        }
    }

    @Inject(method = "func_180494_b", at = @At("HEAD"), cancellable = true, remap = false)
    private void yc$getBiomeGenForCoordsPre(BlockPos pos, CallbackInfoReturnable<BiomeGenBase> cir) {
        if (yc$wrapped != null) {
            cir.setReturnValue(yc$wrap().getBiomeGenForCoords(pos));
        }
    }

    @Inject(method = "func_175624_G", at = @At("HEAD"), cancellable = true, remap = false)
    private void yc$getWorldTypePre(CallbackInfoReturnable<WorldType> cir) {
        if (yc$wrapped != null) {
            cir.setReturnValue(yc$wrap().getWorldType());
        }
    }

    @Inject(method = "func_175627_a", at = @At("HEAD"), cancellable = true, remap = false)
    private void yc$getStrongPowerPre(BlockPos pos, EnumFacing direction, CallbackInfoReturnable<Integer> cir) {
        if (yc$wrapped != null) {
            cir.setReturnValue(yc$wrap().getStrongPower(pos, direction));
        }
    }

    @Inject(method = "func_72806_N", at = @At("HEAD"), cancellable = true, remap = false)
    private void yc$extendedLevelsInChunkCachePre(CallbackInfoReturnable<Boolean> cir) {
        if (yc$wrapped != null) {
            cir.setReturnValue(yc$wrap().extendedLevelsInChunkCache());
        }
    }
}
