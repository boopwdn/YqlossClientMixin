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

package yqloss.yqlossclientmixinkt.impl.event

import yqloss.yqlossclientmixinkt.event.YCEvent
import yqloss.yqlossclientmixinkt.event.YCEventDispatcher
import yqloss.yqlossclientmixinkt.event.YCEventHandler
import yqloss.yqlossclientmixinkt.util.general.AnyComparator
import yqloss.yqlossclientmixinkt.util.general.UniqueHash
import yqloss.yqlossclientmixinkt.util.general.inBox
import yqloss.yqlossclientmixinkt.util.prepend
import java.util.*
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write
import kotlin.reflect.KClass
import kotlin.reflect.full.allSuperclasses
import kotlin.reflect.full.isSubclassOf

class EventDispatcherImpl : YCEventDispatcher {
    private val lock = ReentrantReadWriteLock()

    private val anyComparator = AnyComparator(UniqueHash())

    private inner class RegistryEntry(
        val priority: Int,
        val handler: YCEventHandler<*>,
    ) : Comparable<RegistryEntry> {
        override fun compareTo(other: RegistryEntry): Int {
            return if (handler === other.handler) {
                0
            } else {
                priority
                    .compareTo(other.priority)
                    .takeIf { it != 0 }
                    ?: anyComparator.compare(handler, other.handler)
            }
        }

        override fun equals(other: Any?): Boolean {
            return (other as? RegistryEntry)?.let {
                return handler === other.handler
            } ?: false
        }

        override fun hashCode() = handler.hashCode()
    }

    private val parentTypeHandlerMap = mutableMapOf<KClass<*>, TreeSet<RegistryEntry>>()

    private val onlyTypeHandlerMap = mutableMapOf<KClass<*>, TreeSet<RegistryEntry>>()

    private val parentHandlerCache = mutableMapOf<KClass<*>, YCEventHandler<*>>()

    private val onlyHandlerCache = mutableMapOf<KClass<*>, YCEventHandler<*>>()

    private fun clearCache() {
        parentHandlerCache.clear()
        onlyHandlerCache.clear()
    }

    override fun <T : YCEvent> register(
        type: KClass<T>,
        priority: Int,
        handler: YCEventHandler<T>,
    ) {
        lock.write {
            clearCache()
            unregister(type, handler)
            parentTypeHandlerMap.getOrPut(type) { TreeSet() }.add(RegistryEntry(priority, handler))
        }
    }

    override fun <T : YCEvent> registerOnly(
        type: KClass<T>,
        priority: Int,
        handler: YCEventHandler<T>,
    ) {
        lock.write {
            clearCache()
            unregisterOnly(type, handler)
            onlyTypeHandlerMap.getOrPut(type) { TreeSet() }.add(RegistryEntry(priority, handler))
        }
    }

    override fun <T : YCEvent> unregister(
        type: KClass<T>,
        handler: YCEventHandler<T>,
    ) {
        lock.write {
            clearCache()
            val entry = RegistryEntry(0, handler)
            parentTypeHandlerMap.values.forEach {
                it.remove(entry)
            }
        }
    }

    override fun <T : YCEvent> unregisterOnly(
        type: KClass<T>,
        handler: YCEventHandler<T>,
    ) {
        lock.write {
            clearCache()
            val entry = RegistryEntry(0, handler)
            onlyTypeHandlerMap.values.forEach {
                it.remove(entry)
            }
        }
    }

    override fun unregisterAll(handler: YCEventHandler<*>) {
        lock.write {
            clearCache()
            val entry = RegistryEntry(0, handler)
            (parentTypeHandlerMap.values + onlyTypeHandlerMap.values).forEach {
                it.remove(entry)
            }
        }
    }

    override fun clear() {
        lock.write {
            parentTypeHandlerMap.clear()
            onlyTypeHandlerMap.clear()
            clearCache()
        }
    }

    override fun <T : YCEvent> getHandler(type: KClass<T>): YCEventHandler<T> {
        lock.read {
            return parentHandlerCache
                .getOrPut(type) {
                    val set = TreeSet<RegistryEntry>()
                    type.allSuperclasses
                        .prepend(type)
                        .filter { it.isSubclassOf(YCEvent::class) }
                        .forEach { parentTypeHandlerMap[it]?.let(set::addAll) }
                    onlyTypeHandlerMap[type]?.let(set::addAll)
                    EventHandlerHolder(set.toList().map { it.handler })
                }.inBox
                .cast()
        }
    }

    override fun <T : YCEvent> getHandlerOnly(type: KClass<T>): YCEventHandler<T> {
        lock.read {
            return onlyHandlerCache
                .getOrPut(type) {
                    val set = TreeSet<RegistryEntry>()
                    parentTypeHandlerMap[type]?.let(set::addAll)
                    onlyTypeHandlerMap[type]?.let(set::addAll)
                    EventHandlerHolder(set.toList().map { it.handler })
                }.inBox
                .cast()
        }
    }
}
