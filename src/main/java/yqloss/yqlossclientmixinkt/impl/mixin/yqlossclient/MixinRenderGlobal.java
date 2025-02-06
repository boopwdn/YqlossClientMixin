package yqloss.yqlossclientmixinkt.impl.mixin.yqlossclient;

import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yqloss.yqlossclientmixinkt.impl.mixincallback.yqlossclient.CallbackRenderGlobalKt;

@Mixin(RenderGlobal.class)
public abstract class MixinRenderGlobal {
    @Inject(method = "renderEntities", at = @At("RETURN"))
    private void yc$renderEntitiesPost(Entity renderViewEntity, ICamera camera, float partialTicks, CallbackInfo ci) {
        CallbackRenderGlobalKt.renderEntitiesPost();
    }
}
