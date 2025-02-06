package yqloss.yqlossclientmixinkt.impl.mixincallback.yqlossclient

import yqloss.yqlossclientmixinkt.YC
import yqloss.yqlossclientmixinkt.event.minecraft.YCRenderEvent

var mcPartialTicks = 0.0
    private set

fun updateCameraAndRenderPre(partialTicks: Double) {
    mcPartialTicks = partialTicks
    YC.eventDispatcher(YCRenderEvent.Render.Pre)
}
