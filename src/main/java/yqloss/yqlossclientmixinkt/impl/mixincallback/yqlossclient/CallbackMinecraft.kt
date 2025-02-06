package yqloss.yqlossclientmixinkt.impl.mixincallback.yqlossclient

import net.minecraft.client.multiplayer.WorldClient
import yqloss.yqlossclientmixinkt.YC
import yqloss.yqlossclientmixinkt.event.minecraft.YCMinecraftEvent

fun startGamePost() {
    YC.eventDispatcher(YCMinecraftEvent.Load.Post)
}

fun runGameLoopPre() {
    YC.eventDispatcher(YCMinecraftEvent.Loop.Pre)
}

fun runTickPre() {
    YC.eventDispatcher(YCMinecraftEvent.Tick.Pre)
}

fun loadWorldPre(world: WorldClient?) {
    YC.eventDispatcher(YCMinecraftEvent.LoadWorld.Pre(world))
}
