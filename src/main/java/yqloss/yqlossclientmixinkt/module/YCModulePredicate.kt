package yqloss.yqlossclientmixinkt.module

import yqloss.yqlossclientmixinkt.event.YCCancelableEvent
import yqloss.yqlossclientmixinkt.module.option.YCModuleOptions
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
) = ensure { !event.canceled }
