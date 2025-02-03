package yqloss.yqlossclientmixinkt.impl.mixin.tweaks;

import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yqloss.yqlossclientmixinkt.impl.mixincallback.tweaks.CallbackEntityKt;

@Mixin(Entity.class)
public class MixinEntity {
    @Inject(method = "setAngles", at = @At("RETURN"))
    private void yqloss_clientmixin_setAnglesPost(float yaw, float pitch, CallbackInfo ci) {
        CallbackEntityKt.setAnglesPost((Entity) (Object) this);
    }
}
