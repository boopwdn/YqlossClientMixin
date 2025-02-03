package yqloss.yqlossclientmixinkt.impl.mixincallback.yqlossclient

import yqloss.yqlossclientmixinkt.YC
import yqloss.yqlossclientmixinkt.event.minecraft.YCRenderEvent

fun updateCameraAndRenderPre(partialTicks: Double) {
    YC.eventDispatcher(YCRenderEvent.Pre(partialTicks))
}
