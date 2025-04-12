/*
 * Copyright (C) 2025 Yqloss
 *
 * This file is part of Yqloss Client (Mixin).
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2 (GPLv2)
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Yqloss Client (Mixin). If not, see <https://www.gnu.org/licenses/old-licenses/gpl-2.0.html>.
 */

package yqloss.yqlossclientmixinkt.event

import yqloss.yqlossclientmixinkt.event.YCEventRegistration.Entry
import kotlin.reflect.KClass

interface YCEventRegistration {
    sealed interface Entry : (YCEventRegistry) -> Unit {
        val handler: YCEventHandler<*>
    }

    val eventEntries: List<Entry>
}

class OperationEventRegistry(
    private val list: MutableList<Entry>,
) : YCEventRegistry {
    private data class Register<T : YCEvent>(
        val type: KClass<T>,
        val priority: Int,
        override val handler: YCEventHandler<T>,
    ) : Entry {
        override fun invoke(registry: YCEventRegistry) {
            registry.register(type, priority, handler)
        }
    }

    private data class RegisterOnly<T : YCEvent>(
        val type: KClass<T>,
        val priority: Int,
        override val handler: YCEventHandler<T>,
    ) : Entry {
        override fun invoke(registry: YCEventRegistry) {
            registry.registerOnly(type, priority, handler)
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

    override fun unregister(handler: YCEventHandler<*>) = throw NotImplementedError()

    override fun unregisterOnly(handler: YCEventHandler<*>) = throw NotImplementedError()

    override fun unregisterAll(handler: YCEventHandler<*>) = throw NotImplementedError()

    override fun clear() = throw NotImplementedError()
}

inline fun buildEventEntries(function: OperationEventRegistry.() -> Unit): List<Entry> {
    return mutableListOf<Entry>().also { function(OperationEventRegistry(it)) }
}

inline fun YCEventRegistration.buildRegisterEventEntries(
    registry: YCEventRegistry,
    function: OperationEventRegistry.() -> Unit,
): List<Entry> {
    return mutableListOf<Entry>()
        .also { function(OperationEventRegistry(it)) }
        .also { registerEventEntries(registry) }
}

fun YCEventRegistration.registerEventEntries(registry: YCEventRegistry) {
    eventEntries.registerEventEntries(registry)
}

fun YCEventRegistration.unregisterEventEntries(registry: YCEventRegistry) {
    eventEntries.unregisterEventEntries(registry)
}

fun List<Entry>.registerEventEntries(registry: YCEventRegistry) {
    forEach { it(registry) }
}

fun List<Entry>.unregisterEventEntries(registry: YCEventRegistry) {
    val set = mutableSetOf<YCEventHandler<*>>()
    forEach { set.add(it.handler) }
    set.forEach(registry::unregisterAll)
}
