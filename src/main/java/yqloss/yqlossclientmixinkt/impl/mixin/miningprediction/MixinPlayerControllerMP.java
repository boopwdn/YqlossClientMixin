package yqloss.yqlossclientmixinkt.impl.mixin.miningprediction;

import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import yqloss.yqlossclientmixinkt.impl.mixincallback.miningprediction.CallbackMinecraftKt;

@Mixin(PlayerControllerMP.class)
public class MixinPlayerControllerMP {
    @Inject(method = "onPlayerDamageBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/PlayerControllerMP;clickBlock(Lnet/minecraft/util/BlockPos;Lnet/minecraft/util/EnumFacing;)Z"))
    private void yc$miningprediction$onPlayerDamageBlockNotMining(BlockPos posBlock, EnumFacing directionFacing, CallbackInfoReturnable<Boolean> cir) {
        CallbackMinecraftKt.sendClickBlockToControllerNotMining();
    }
}
