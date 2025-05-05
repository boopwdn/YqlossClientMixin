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

@file:Suppress("NOTHING_TO_INLINE")

package yqloss.yqlossclientmixinkt.util.accessor.refs

import yqloss.yqlossclientmixinkt.util.accessor.Ref

data class ThreadLocalRef<T>(
    private val holder: ThreadLocal<T>,
) : Ref<T> {
    constructor(initializer: () -> T) : this(ThreadLocal.withInitial(initializer))

    override fun get(): T = holder.get()

    override fun set(value: T) {
        holder.set(value)
    }
}

inline fun <T> threadLocal(noinline initializer: () -> T) = ThreadLocalRef(initializer)
