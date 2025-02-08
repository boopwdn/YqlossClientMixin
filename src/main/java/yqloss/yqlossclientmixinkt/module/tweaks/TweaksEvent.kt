package yqloss.yqlossclientmixinkt.module.tweaks

import net.minecraft.client.multiplayer.WorldClient
import net.minecraft.entity.Entity
import net.minecraft.item.ItemStack
import yqloss.yqlossclientmixinkt.event.YCCancelableEvent
import yqloss.yqlossclientmixinkt.event.YCEvent
import yqloss.yqlossclientmixinkt.util.math.Vec3I

sealed interface TweaksEvent : YCEvent {
    data class SetAnglesPost(
        val entity: Entity,
    ) : TweaksEvent

    data class RightClickBlockPre(
        val world: WorldClient,
        val blockPos: Vec3I,
        override var canceled: Boolean = false,
    ) : TweaksEvent,
        YCCancelableEvent

    data class IsHittingPositionCheck(
        val pos: Vec3I,
        val currentBlock: Vec3I,
        val currentItemHittingBlock: ItemStack?,
        var returnValue: Boolean = false,
        override var canceled: Boolean = false,
    ) : TweaksEvent,
        YCCancelableEvent
}
