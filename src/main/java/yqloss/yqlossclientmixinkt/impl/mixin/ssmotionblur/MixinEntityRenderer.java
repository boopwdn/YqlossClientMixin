package yqloss.yqlossclientmixinkt.impl.mixin.ssmotionblur;

import net.minecraft.client.renderer.EntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yqloss.yqlossclientmixinkt.impl.mixincallback.ssmotionblur.CallbackEntityRendererKt;

@Mixin(EntityRenderer.class)
public abstract class MixinEntityRenderer {
    @Inject(method = "updateCameraAndRender", at = @At(value = "INVOKE", target = "Lnet/minecraft/profiler/Profiler;endStartSection(Ljava/lang/String;)V"))
    private void yc$ssmotionblur$updateCameraAndRenderRenderMotionBlur(float partialTicks, long nanoTime, CallbackInfo ci) {
        CallbackEntityRendererKt.updateCameraAndRenderRenderMotionBlur();
    }
}
