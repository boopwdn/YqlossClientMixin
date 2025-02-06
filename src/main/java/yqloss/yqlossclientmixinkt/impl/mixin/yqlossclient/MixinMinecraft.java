package yqloss.yqlossclientmixinkt.impl.mixin.yqlossclient;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yqloss.yqlossclientmixinkt.impl.mixincallback.yqlossclient.CallbackMinecraftKt;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft {
    @Inject(method = "startGame", at = @At("RETURN"))
    private void yc$startGamePost(CallbackInfo ci) {
        CallbackMinecraftKt.startGamePost();
    }

    @Inject(method = "runGameLoop", at = @At("HEAD"))
    private void yc$runGameLoopPre(CallbackInfo ci) {
        CallbackMinecraftKt.runGameLoopPre();
    }

    @Inject(method = "runTick", at = @At("HEAD"))
    private void yc$runTickPre(CallbackInfo ci) {
        CallbackMinecraftKt.runTickPre();
    }

    @Inject(method = "loadWorld(Lnet/minecraft/client/multiplayer/WorldClient;Ljava/lang/String;)V", at = @At("HEAD"))
    private void yc$loadWorldPre(WorldClient worldClientIn, String loadingMessage, CallbackInfo ci) {
        CallbackMinecraftKt.loadWorldPre(worldClientIn);
    }
}
