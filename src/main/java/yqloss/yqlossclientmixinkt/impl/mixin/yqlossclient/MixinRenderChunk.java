package yqloss.yqlossclientmixinkt.impl.mixin.yqlossclient;

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
import yqloss.yqlossclientmixinkt.impl.mixincallback.yqlossclient.CallbackRenderChunkKt;
import yqloss.yqlossclientmixinkt.impl.mixincallback.yqlossclient.Wrapper;

@Mixin(RenderChunk.class)
public abstract class MixinRenderChunk {
    @Shadow
    private BlockPos position;

    @Unique
    private final Wrapper yc$wrappedBlockAccess = new Wrapper();

    @Inject(method = "rebuildChunk", at = @At("HEAD"))
    private void yc$rebuildChunkPre(float x, float y, float z, ChunkCompileTaskGenerator generator, CallbackInfo ci) {
        CallbackRenderChunkKt.rebuildChunkPre(yc$wrappedBlockAccess, position);
    }

    @Redirect(method = "rebuildChunk", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/IBlockAccess;getBlockState(Lnet/minecraft/util/BlockPos;)Lnet/minecraft/block/state/IBlockState;"))
    private IBlockState yc$rebuildChunkGetBlockState(IBlockAccess instance, BlockPos blockPos) {
        return CallbackRenderChunkKt.rebuildChunkGetBlockState(yc$wrappedBlockAccess, instance, blockPos);
    }

    @Redirect(method = "rebuildChunk", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/IBlockAccess;getTileEntity(Lnet/minecraft/util/BlockPos;)Lnet/minecraft/tileentity/TileEntity;"))
    private TileEntity yc$rebuildChunkGetTileEntity(IBlockAccess instance, BlockPos blockPos) {
        return CallbackRenderChunkKt.rebuildChunkGetTileEntity(yc$wrappedBlockAccess, instance, blockPos);
    }

    @Redirect(method = "rebuildChunk", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/BlockRendererDispatcher;renderBlock(Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/util/BlockPos;Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/client/renderer/WorldRenderer;)Z"))
    private boolean yc$rebuildChunkRenderBlock(BlockRendererDispatcher instance, IBlockState blockState, BlockPos blockPos, IBlockAccess blockAccess, WorldRenderer worldRenderer) {
        return CallbackRenderChunkKt.rebuildChunkRenderBlock(yc$wrappedBlockAccess, instance, blockState, blockPos, blockAccess, worldRenderer);
    }
}
