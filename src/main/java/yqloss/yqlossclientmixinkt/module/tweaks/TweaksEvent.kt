package yqloss.yqlossclientmixinkt.module.tweaks

import net.minecraft.client.multiplayer.WorldClient
import net.minecraft.entity.Entity
import net.minecraft.util.BlockPos
import yqloss.yqlossclientmixinkt.event.YCCancelableEvent
import yqloss.yqlossclientmixinkt.event.YCEvent

sealed interface TweaksEvent : YCEvent {
    data class SetAnglesPost(
        val entity: Entity,
    ) : TweaksEvent

    data class RightClickBlockPre(
        val world: WorldClient,
        val blockPos: BlockPos,
        override var canceled: Boolean = false,
    ) : TweaksEvent,
        YCCancelableEvent
}
