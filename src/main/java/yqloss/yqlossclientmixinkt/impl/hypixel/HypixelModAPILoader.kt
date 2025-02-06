package yqloss.yqlossclientmixinkt.impl.hypixel

import yqloss.yqlossclientmixinkt.util.scope.noexcept
import yqloss.yqlossclientmixinkt.ycLogger

private val logger = ycLogger("Hypixel Mod API Wrapper Loader")

val loadHypixelModAPI by lazy {
    noexcept(logger::catching) {
        HypixelModAPIWrapper
    }
}
