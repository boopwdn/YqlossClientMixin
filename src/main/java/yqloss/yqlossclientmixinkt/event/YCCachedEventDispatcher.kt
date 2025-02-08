package yqloss.yqlossclientmixinkt.event

import yqloss.yqlossclientmixinkt.util.general.inBox
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass

class YCCachedEventDispatcher(
    private val dispatcher: YCEventDispatcher,
) : YCEventDispatcher by dispatcher {
    private val handlerCache = ConcurrentHashMap<KClass<*>, YCEventHandler<*>>()
    private val handlerOnlyCache = ConcurrentHashMap<KClass<*>, YCEventHandler<*>>()

    fun clearCache() {
        handlerCache.clear()
        handlerOnlyCache.clear()
    }

    override fun <T : YCEvent> getHandler(type: KClass<T>): YCEventHandler<T> {
        return handlerCache.getOrPut(type) { dispatcher.getHandler(type) }.inBox.cast()
    }

    override fun <T : YCEvent> getHandlerOnly(type: KClass<T>): YCEventHandler<T> {
        return handlerOnlyCache.getOrPut(type) { dispatcher.getHandlerOnly(type) }.inBox.cast()
    }
}
