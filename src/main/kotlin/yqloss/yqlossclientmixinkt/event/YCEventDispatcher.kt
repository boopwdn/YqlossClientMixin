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

import yqloss.yqlossclientmixinkt.util.general.inBox
import kotlin.reflect.KClass

interface YCEventDispatcher : (YCEvent) -> Unit {
    /*
     * registering the same handler for two classes with direct inheritance is undefined
     * only the first call would take effect if the method is called more than one time with the same arguments
     */
    fun <T : YCEvent> register(
        type: KClass<T>,
        priority: Int = 0,
        handler: YCEventHandler<T>,
    )

    fun <T : YCEvent> registerOnly(
        type: KClass<T>,
        priority: Int = 0,
        handler: YCEventHandler<T>,
    )

    fun <T : YCEvent> unregister(
        type: KClass<T>,
        handler: YCEventHandler<T>,
    )

    fun <T : YCEvent> unregisterOnly(
        type: KClass<T>,
        handler: YCEventHandler<T>,
    )

    fun unregisterAll(handler: YCEventHandler<*>)

    fun clear()

    fun <T : YCEvent> getHandler(type: KClass<T>): YCEventHandler<T>

    fun <T : YCEvent> getHandler(event: T) = getHandler(event::class.inBox.cast<KClass<T>>())

    fun <T : YCEvent> getHandlerOnly(type: KClass<T>): YCEventHandler<T>

    fun <T : YCEvent> getHandlerOnly(event: T) = getHandlerOnly(event::class.inBox.cast<KClass<T>>())

    override fun invoke(event: YCEvent) {
        getHandler(event)(event)
    }
}

inline fun <reified T : YCEvent> YCEventDispatcher.register(
    priority: Int = 0,
    noinline handler: YCEventHandler<T>,
) = register(T::class, priority, handler)

inline fun <reified T : YCEvent> YCEventDispatcher.registerOnly(
    priority: Int = 0,
    noinline handler: YCEventHandler<T>,
) = registerOnly(T::class, priority, handler)

inline fun <reified T : YCEvent> YCEventDispatcher.unregisterOnly(noinline handler: YCEventHandler<T>) = unregisterOnly(T::class, handler)

inline fun <reified T : YCEvent> YCEventDispatcher.getHandler() = getHandler(T::class)

inline fun <reified T : YCEvent> YCEventDispatcher.getHandlerOnly() = getHandlerOnly(T::class)
