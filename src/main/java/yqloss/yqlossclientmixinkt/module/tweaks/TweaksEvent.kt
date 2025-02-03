package yqloss.yqlossclientmixinkt.module.tweaks

import net.minecraft.entity.Entity
import yqloss.yqlossclientmixinkt.event.YCEvent

sealed interface TweaksEvent : YCEvent {
    data class SetAnglesPost(
        val entity: Entity,
    ) : TweaksEvent
}
