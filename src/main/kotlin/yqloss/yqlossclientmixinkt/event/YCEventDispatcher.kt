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

import yqloss.yqlossclientmixinkt.util.extension.castTo
import kotlin.reflect.KClass

interface YCEventDispatcher : (YCEvent) -> Unit {
    fun <T : YCEvent> getHandler(type: KClass<T>): YCEventHandler<T>

    fun <T : YCEvent> getHandler(event: T) = getHandler(event::class.castTo<KClass<T>>())

    fun <T : YCEvent> getHandlerOnly(type: KClass<T>): YCEventHandler<T>

    fun <T : YCEvent> getHandlerOnly(event: T) = getHandlerOnly(event::class.castTo<KClass<T>>())

    override fun invoke(event: YCEvent) {
        getHandler(event)(event)
    }
}

inline fun <reified T : YCEvent> YCEventDispatcher.getHandler() = getHandler(T::class)

inline fun <reified T : YCEvent> YCEventDispatcher.getHandlerOnly() = getHandlerOnly(T::class)
