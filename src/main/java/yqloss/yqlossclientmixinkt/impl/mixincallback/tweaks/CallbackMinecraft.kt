package yqloss.yqlossclientmixinkt.impl.mixincallback.tweaks

import net.minecraft.client.multiplayer.WorldClient
import net.minecraft.util.BlockPos
import yqloss.yqlossclientmixinkt.YC
import yqloss.yqlossclientmixinkt.module.tweaks.TweaksEvent
import yqloss.yqlossclientmixinkt.util.asVec3I

fun rightClickMouseClickBlockPre(
    world: WorldClient,
    blockPos: BlockPos,
): Boolean {
    return TweaksEvent
        .RightClickBlockPre(world, blockPos.asVec3I)
        .also(YC.eventDispatcher)
        .canceled
}
