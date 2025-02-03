package yqloss.yqlossclientmixinkt.impl.mixin.yqlossclient;

import net.minecraft.client.renderer.EntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yqloss.yqlossclientmixinkt.impl.mixincallback.yqlossclient.CallbackEntityRendererKt;

@Mixin(EntityRenderer.class)
public abstract class MixinEntityRenderer {
    @Inject(method = "updateCameraAndRender", at = @At("HEAD"))
    private void yqloss_clientmixin_updateCameraAndRenderPre(float partialTicks, long nanoTime, CallbackInfo ci) {
        CallbackEntityRendererKt.updateCameraAndRenderPre(partialTicks);
    }
}
