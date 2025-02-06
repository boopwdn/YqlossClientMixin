package yqloss.yqlossclientmixinkt.event.hypixel

import yqloss.yqlossclientmixinkt.api.YCHypixelLocation
import yqloss.yqlossclientmixinkt.event.YCEvent

sealed interface YCHypixelAPIEvent : YCEvent {
    data class Location(
        val location: YCHypixelLocation?,
    ) : YCHypixelAPIEvent
}
