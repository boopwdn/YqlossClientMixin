package yqloss.yqlossclientmixinkt.impl.mixin.yqlossclient;

import net.minecraft.client.main.Main;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yqloss.yqlossclientmixinkt.impl.mixincallback.yqlossclient.CallbackMainKt;

@Mixin(Main.class)
public abstract class MixinMain {
    @Inject(method = "main", at = @At("HEAD"), remap = false)
    private static void yc$mainPre(String[] strings, CallbackInfo ci) {
        CallbackMainKt.mainPre();
    }
}
