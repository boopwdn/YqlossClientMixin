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
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.chunk.ChunkCompileTaskGenerator;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yqloss.yqlossclientmixinkt.impl.mixincallback.CallbackRenderChunk;

@Mixin(RenderChunk.class)
public abstract class MixinRenderChunk {
    @Shadow
    private BlockPos position;

    @Unique
    private final CallbackRenderChunk.YqlossClient.Wrapper yc$wrappedBlockAccess = new CallbackRenderChunk.YqlossClient.Wrapper();

    @Inject(method = "rebuildChunk", at = @At("HEAD"))
    private void yc$rebuildChunkPre(float x, float y, float z, ChunkCompileTaskGenerator generator, CallbackInfo ci) {
        CallbackRenderChunk.YqlossClient.INSTANCE.rebuildChunkPre(yc$wrappedBlockAccess, position);
    }

    @Redirect(method = "rebuildChunk", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/IBlockAccess;getBlockState(Lnet/minecraft/util/BlockPos;)Lnet/minecraft/block/state/IBlockState;"))
    private IBlockState yc$rebuildChunkGetBlockState(IBlockAccess instance, BlockPos blockPos) {
        return CallbackRenderChunk.YqlossClient.INSTANCE.rebuildChunkGetBlockState(yc$wrappedBlockAccess, instance, blockPos);
    }

    @Redirect(method = "rebuildChunk", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/IBlockAccess;getTileEntity(Lnet/minecraft/util/BlockPos;)Lnet/minecraft/tileentity/TileEntity;"))
    private TileEntity yc$rebuildChunkGetTileEntity(IBlockAccess instance, BlockPos blockPos) {
        return CallbackRenderChunk.YqlossClient.INSTANCE.rebuildChunkGetTileEntity(yc$wrappedBlockAccess, instance, blockPos);
    }

    @Redirect(method = "rebuildChunk", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/BlockRendererDispatcher;renderBlock(Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/util/BlockPos;Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/client/renderer/WorldRenderer;)Z"))
    private boolean yc$rebuildChunkRenderBlock(BlockRendererDispatcher instance, IBlockState blockState, BlockPos blockPos, IBlockAccess blockAccess, WorldRenderer worldRenderer) {
        return CallbackRenderChunk.YqlossClient.INSTANCE.rebuildChunkRenderBlock(yc$wrappedBlockAccess, instance, blockState, blockPos, blockAccess, worldRenderer);
    }
}
