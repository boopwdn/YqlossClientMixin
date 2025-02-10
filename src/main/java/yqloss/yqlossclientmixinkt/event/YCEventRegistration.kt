package yqloss.yqlossclientmixinkt.event

import yqloss.yqlossclientmixinkt.event.YCEventRegistration.Entry
import kotlin.reflect.KClass

interface YCEventRegistration {
    sealed interface Entry : (YCEventDispatcher) -> Unit {
        val handler: YCEventHandler<*>
    }

    val eventEntries: List<Entry>
}

class RegistryEventDispatcher(
    private val list: MutableList<Entry>,
) : YCEventDispatcher {
    private data class Register<T : YCEvent>(
        val type: KClass<T>,
        val priority: Int,
        override val handler: YCEventHandler<T>,
    ) : Entry {
        override fun invoke(dispatcher: YCEventDispatcher) {
            dispatcher.register(type, priority, handler)
        }
    }

    private data class RegisterOnly<T : YCEvent>(
        val type: KClass<T>,
        val priority: Int,
        override val handler: YCEventHandler<T>,
    ) : Entry {
        override fun invoke(dispatcher: YCEventDispatcher) {
            dispatcher.registerOnly(type, priority, handler)
        }
    }

    override fun <T : YCEvent> register(
        type: KClass<T>,
        priority: Int,
        handler: YCEventHandler<T>,
    ) {
        list.add(Register(type, priority, handler))
    }

    override fun <T : YCEvent> registerOnly(
        type: KClass<T>,
        priority: Int,
        handler: YCEventHandler<T>,
    ) {
        list.add(RegisterOnly(type, priority, handler))
    }

    override fun <T : YCEvent> unregister(
        type: KClass<T>,
        handler: YCEventHandler<T>,
    ) = throw NotImplementedError()

    override fun <T : YCEvent> unregisterOnly(
        type: KClass<T>,
        handler: YCEventHandler<T>,
    ) = throw NotImplementedError()

    override fun clear() = throw NotImplementedError()

    override fun unregisterAll(handler: YCEventHandler<*>) = throw NotImplementedError()

    override fun <T : YCEvent> getHandler(type: KClass<T>) = throw NotImplementedError()

    override fun <T : YCEvent> getHandlerOnly(type: KClass<T>) = throw NotImplementedError()
}

fun buildEventEntries(function: RegistryEventDispatcher.() -> Unit): List<Entry> {
    return mutableListOf<Entry>().also { function(RegistryEventDispatcher(it)) }
}

fun YCEventRegistration.buildRegisterEventEntries(
    dispatcher: YCEventDispatcher,
    function: RegistryEventDispatcher.() -> Unit,
): List<Entry> {
    return mutableListOf<Entry>()
        .also { function(RegistryEventDispatcher(it)) }
        .also { registerEventEntries(dispatcher) }
}

fun YCEventRegistration.registerEventEntries(dispatcher: YCEventDispatcher) {
    eventEntries.registerEventEntries(dispatcher)
}

fun YCEventRegistration.unregisterEventEntries(dispatcher: YCEventDispatcher) {
    eventEntries.unregisterEventEntries(dispatcher)
}

fun List<Entry>.registerEventEntries(dispatcher: YCEventDispatcher) {
    forEach { it(dispatcher) }
}

fun List<Entry>.unregisterEventEntries(dispatcher: YCEventDispatcher) {
    val set = mutableSetOf<YCEventHandler<*>>()
    forEach { set.add(it.handler) }
    set.forEach(dispatcher::unregisterAll)
}
