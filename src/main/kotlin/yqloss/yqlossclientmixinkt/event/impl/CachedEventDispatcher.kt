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

package yqloss.yqlossclientmixinkt.event.impl

import yqloss.yqlossclientmixinkt.event.YCEvent
import yqloss.yqlossclientmixinkt.event.YCEventDispatcher
import yqloss.yqlossclientmixinkt.event.YCEventHandler
import yqloss.yqlossclientmixinkt.util.general.inBox
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass

class CachedEventDispatcher(
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
