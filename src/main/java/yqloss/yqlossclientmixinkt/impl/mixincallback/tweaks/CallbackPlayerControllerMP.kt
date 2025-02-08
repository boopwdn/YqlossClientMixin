package yqloss.yqlossclientmixinkt.impl.mixincallback.tweaks

import net.minecraft.item.ItemStack
import net.minecraft.util.BlockPos
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
import yqloss.yqlossclientmixinkt.YC
import yqloss.yqlossclientmixinkt.module.tweaks.TweaksEvent
import yqloss.yqlossclientmixinkt.util.asVec3I

fun isHittingPositionCheck(
    pos: BlockPos,
    currentBlock: BlockPos,
    currentItemHittingBlock: ItemStack,
    cir: CallbackInfoReturnable<Boolean>,
) {
    TweaksEvent
        .IsHittingPositionCheck(pos.asVec3I, currentBlock.asVec3I, currentItemHittingBlock)
        .also(YC.eventDispatcher)
        .also {
            if (it.canceled) {
                cir.returnValue = it.returnValue
            }
        }
}
