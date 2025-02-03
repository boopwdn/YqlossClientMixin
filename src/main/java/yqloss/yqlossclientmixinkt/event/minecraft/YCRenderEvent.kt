package yqloss.yqlossclientmixinkt.event.minecraft

import yqloss.yqlossclientmixinkt.event.YCEvent

sealed interface YCRenderEvent : YCEvent {
    val partialTicks: Double

    class Pre(
        override val partialTicks: Double,
    ) : YCRenderEvent
}
