package yqloss.yqlossclientmixinkt.impl.mixin.yqlossclient;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yqloss.yqlossclientmixinkt.impl.mixincallback.yqlossclient.CallbackMinecraftKt;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft {
    @Inject(method = "startGame", at = @At("RETURN"))
    private void yqloss_clientmixin_startGamePost(CallbackInfo ci) {
        CallbackMinecraftKt.startGamePost();
    }

    @Inject(method = "runGameLoop", at = @At("HEAD"))
    private void yqloss_clientmixin_runGameLoopPre(CallbackInfo ci) {
        CallbackMinecraftKt.runGameLoopPre();
    }
}
