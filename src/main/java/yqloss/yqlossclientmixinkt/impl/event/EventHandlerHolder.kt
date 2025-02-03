package yqloss.yqlossclientmixinkt.impl.event

import yqloss.yqlossclientmixinkt.event.YCEvent
import yqloss.yqlossclientmixinkt.event.YCEventHandler

class EventHandlerHolder<in T : YCEvent>(
    private val handlers: List<YCEventHandler<T>>,
) : YCEventHandler<T> {
    override fun invoke(event: T) {
        handlers.forEach { it(event) }
    }
}
