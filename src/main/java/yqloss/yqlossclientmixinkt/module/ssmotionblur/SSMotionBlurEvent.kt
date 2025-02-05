package yqloss.yqlossclientmixinkt.module.ssmotionblur

import net.minecraft.client.gui.ScaledResolution
import yqloss.yqlossclientmixinkt.event.YCEvent

sealed interface SSMotionBlurEvent : YCEvent {
    data class Render(
        val screenWidth: Int,
        val screenHeight: Int,
        val scaledResolution: ScaledResolution,
    ) : SSMotionBlurEvent
}
