package yqloss.yqlossclientmixinkt.impl.mixincallback.yqlossclient

import yqloss.yqlossclientmixinkt.YC
import yqloss.yqlossclientmixinkt.event.minecraft.YCMinecraftEvent

fun startGamePost() {
    YC.eventDispatcher(YCMinecraftEvent.Load.Post)
}

fun runGameLoopPre() {
    YC.eventDispatcher(YCMinecraftEvent.Loop.Pre)
}
