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

package yqloss.yqlossclientmixinkt.util

import kotlinx.atomicfu.atomic
import java.util.*

class ConcurrentUniqueHash<T> : (T) -> Long {
    private val counter = atomic(0L)

    private val uniqueHash = WeakHashMap<Any, Long>()

    override fun invoke(obj: T): Long {
        return obj?.let {
            synchronized(this) {
                uniqueHash.getOrPut(obj) { counter.addAndGet(1) }
            }
        } ?: 0L
    }
}
