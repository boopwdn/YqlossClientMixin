package yqloss.yqlossclientmixinkt.impl.mixincallback.yqlossclient

import yqloss.yqlossclientmixinkt.YC
import yqloss.yqlossclientmixinkt.event.minecraft.YCRenderEvent

fun renderEntitiesPost() {
    YC.eventDispatcher(YCRenderEvent.Entity.Post)
}
