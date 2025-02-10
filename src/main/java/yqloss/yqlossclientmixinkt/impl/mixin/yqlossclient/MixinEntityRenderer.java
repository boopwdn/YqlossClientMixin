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
    private void yc$updateCameraAndRenderPre(float partialTicks, long nanoTime, CallbackInfo ci) {
        CallbackEntityRendererKt.updateCameraAndRenderPre(partialTicks);
    }

    @Inject(method = "updateCameraAndRender", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiIngame;renderGameOverlay(F)V", shift = At.Shift.AFTER))
    private void yc$ssmotionblur$updateCameraAndRenderRenderHUD(float partialTicks, long nanoTime, CallbackInfo ci) {
        CallbackEntityRendererKt.updateCameraAndRenderRenderHUD();
    }

    @Inject(method = "updateCameraAndRender", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/client/ForgeHooksClient;drawScreen(Lnet/minecraft/client/gui/GuiScreen;IIF)V", shift = At.Shift.AFTER), remap = false)
    private void yc$ssmotionblur$updateCameraAndRenderRenderScreen(float partialTicks, long nanoTime, CallbackInfo ci) {
        CallbackEntityRendererKt.updateCameraAndRenderRenderScreen();
    }
}
