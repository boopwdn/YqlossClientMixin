package yqloss.yqlossclientmixinkt.impl.hypixel

import net.hypixel.modapi.HypixelModAPI
import yqloss.yqlossclientmixinkt.util.scope.nothrow
import yqloss.yqlossclientmixinkt.ycLogger

private val logger = ycLogger("Hypixel Mod API Wrapper Loader")

val loadHypixelModAPI by lazy {
    nothrow(logger::catching) {
        HypixelModAPIWrapper(HypixelModAPI.getInstance())
    }
}
