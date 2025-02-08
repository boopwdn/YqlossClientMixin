package yqloss.yqlossclientmixinkt.impl.mixin.miningprediction;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yqloss.yqlossclientmixinkt.impl.mixincallback.miningprediction.CallbackMinecraftKt;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft {
    @Inject(method = "sendClickBlockToController", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/MovingObjectPosition;getBlockPos()Lnet/minecraft/util/BlockPos;"))
    private void yc$miningprediction$sendClickBlockToControllerMining(boolean leftClick, CallbackInfo ci) {
        CallbackMinecraftKt.sendClickBlockToControllerMining(((Minecraft) (Object) this).objectMouseOver.getBlockPos());
    }

    @Inject(method = "sendClickBlockToController", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/PlayerControllerMP;resetBlockRemoving()V"))
    private void yc$miningprediction$sendClickBlockToControllerNotMining(boolean leftClick, CallbackInfo ci) {
        CallbackMinecraftKt.sendClickBlockToControllerNotMining();
    }
}
