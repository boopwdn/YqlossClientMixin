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

import kotlin.reflect.KClass

interface YCEventRegistry {
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

    fun unregister(handler: YCEventHandler<*>)

    fun unregisterOnly(handler: YCEventHandler<*>)

    fun unregisterAll(handler: YCEventHandler<*>)

    fun clear()
}

inline fun <reified T : YCEvent> YCEventRegistry.register(
    priority: Int = 0,
    noinline handler: YCEventHandler<T>,
) = register(T::class, priority, handler)

inline fun <reified T : YCEvent> YCEventRegistry.registerOnly(
    priority: Int = 0,
    noinline handler: YCEventHandler<T>,
) = registerOnly(T::class, priority, handler)
