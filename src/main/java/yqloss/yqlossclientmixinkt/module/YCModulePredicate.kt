package yqloss.yqlossclientmixinkt.module

import yqloss.yqlossclientmixinkt.YC
import yqloss.yqlossclientmixinkt.event.YCCancelableEvent
import yqloss.yqlossclientmixinkt.module.option.YCModuleOptions
import yqloss.yqlossclientmixinkt.util.MC
import yqloss.yqlossclientmixinkt.util.scope.longreturn

inline fun ensure(
    frame: Int = 0,
    predicate: () -> Boolean,
) {
    if (!predicate()) longreturn(frame) {}
}

fun YCModule<*>.ensureEnabled(frame: Int = 0) = ensure(frame) { options.enabled }

inline fun <T : YCModuleOptions> YCModule<T>.ensureEnabled(
    frame: Int = 0,
    option: T.() -> Boolean,
) = ensure(frame) { options.enabled && option(options) }

fun ensureNotCanceled(
    event: YCCancelableEvent,
    frame: Int = 0,
) = ensure(frame) { !event.canceled }

fun ensureInWorld(frame: Int = 0) = ensure(frame) { MC.theWorld !== null }

fun ensureSkyBlock(frame: Int = 0) {
    ensure(frame) {
        MC.theWorld !== null &&
            YC.api.hypixelLocation
                ?.serverType
                ?.name == "SkyBlock"
    }
}

fun ensureSkyBlockMode(
    mode: String,
    frame: Int = 0,
) {
    ensure(frame) {
        MC.theWorld !== null &&
            (
                YC.api.hypixelLocation?.run {
                    serverType?.name == "SkyBlock" && this.mode == mode
                } ?: false
            )
    }
}

fun ensureSkyBlockModes(
    modes: Set<String>,
    frame: Int = 0,
) {
    ensure(frame) {
        MC.theWorld !== null &&
            (
                YC.api.hypixelLocation?.run {
                    serverType?.name == "SkyBlock" && mode in modes
                } ?: false
            )
    }
}

val SKYBLOCK_MINING_ISLANDS =
    setOf(
        "mining_1",
        "mining_2",
        "mining_3",
        "crystal_hollows",
        "mineshaft",
        "combat_3",
        "crimson_isle",
    )
