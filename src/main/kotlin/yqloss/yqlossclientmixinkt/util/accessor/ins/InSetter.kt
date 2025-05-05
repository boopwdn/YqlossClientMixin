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

package yqloss.yqlossclientmixinkt.util.accessor.ins

import yqloss.yqlossclientmixinkt.util.accessor.In

data class InSetter<in T>(
    private val setter: (T) -> Unit,
) : In<T> {
    override fun set(value: T) = setter(value)
}

inline fun <T> makeIn(noinline setter: (T) -> Unit): In<T> = InSetter(setter)

inline val <T> ((T) -> Unit).asIn get() = makeIn(this)

inline fun <T> nullIn(): In<T> {
    return makeIn { throw UnsupportedOperationException("set value into a null in") }
}
