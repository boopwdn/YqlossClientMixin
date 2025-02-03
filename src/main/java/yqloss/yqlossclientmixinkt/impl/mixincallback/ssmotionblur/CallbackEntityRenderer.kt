package yqloss.yqlossclientmixinkt.impl.mixincallback.ssmotionblur

import net.minecraft.client.gui.ScaledResolution
import yqloss.yqlossclientmixinkt.MC
import yqloss.yqlossclientmixinkt.YC
import yqloss.yqlossclientmixinkt.module.ssmotionblur.SSMotionBlurEvent

fun updateCameraAndRenderRenderMotionBlur() {
    YC.eventDispatcher(SSMotionBlurEvent.Render(MC.displayWidth, MC.displayHeight, ScaledResolution(MC)))
}
