package yqloss.yqlossclientmixinkt.impl.mixin.tweaks;

import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import yqloss.yqlossclientmixinkt.impl.mixincallback.tweaks.CallbackPlayerControllerMPKt;

@Mixin(PlayerControllerMP.class)
public abstract class MixinPlayerControllerMP {
    @Shadow
    private ItemStack currentItemHittingBlock;

    @Shadow
    private BlockPos currentBlock;

    @Inject(method = "isHittingPosition", at = @At("HEAD"), cancellable = true)
    private void yc$tweaks$isHittingPositionCheck(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        CallbackPlayerControllerMPKt.isHittingPositionCheck(pos, currentBlock, currentItemHittingBlock, cir);
    }
}
