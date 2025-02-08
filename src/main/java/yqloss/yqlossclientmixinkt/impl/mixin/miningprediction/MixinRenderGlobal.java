package yqloss.yqlossclientmixinkt.impl.mixin.miningprediction;

import net.minecraft.client.renderer.DestroyBlockProgress;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yqloss.yqlossclientmixinkt.impl.mixincallback.miningprediction.CallbackRenderGlobalKt;

import java.util.Map;

@Mixin(RenderGlobal.class)
public abstract class MixinRenderGlobal {
    @Final
    @Shadow
    private Map<Integer, DestroyBlockProgress> damagedBlocks;

    @Inject(method = "drawBlockDamageTexture", at = @At("HEAD"))
    private void yc$miningprediction$drawBlockDamageTexturePre(Tessellator tessellatorIn, WorldRenderer worldRendererIn, Entity entityIn, float partialTicks, CallbackInfo ci) {
        CallbackRenderGlobalKt.drawBlockDamageTexturePre(damagedBlocks);
    }

    @Inject(method = "drawBlockDamageTexture", at = @At("RETURN"))
    private void yc$miningprediction$drawBlockDamageTexturePost(Tessellator tessellatorIn, WorldRenderer worldRendererIn, Entity entityIn, float partialTicks, CallbackInfo ci) {
        CallbackRenderGlobalKt.drawBlockDamageTexturePost(damagedBlocks);
    }
}
