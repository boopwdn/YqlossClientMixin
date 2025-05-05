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

import yqloss.yqlossclientmixinkt.util.accessor.In
import yqloss.yqlossclientmixinkt.util.accessor.Out
import yqloss.yqlossclientmixinkt.util.accessor.Ref
import kotlin.reflect.KMutableProperty0

data class RefGetterSetter<T>(
    private val getter: () -> T,
    private val setter: (T) -> Unit,
) : Ref<T> {
    override fun get() = getter()

    override fun set(value: T) = setter(value)
}

inline fun <T> makeRef(
    noinline getter: () -> T,
    noinline setter: (T) -> Unit,
): Ref<T> = RefGetterSetter(getter, setter)

inline val <T> Pair<() -> T, (T) -> Unit>.asRef get() = makeRef(first, second)

inline val <T> KMutableProperty0<T>.asRef get() = makeRef(this, this::set)

inline val <T> Out<T>.asReadOnlyRef: Ref<T>
    get() {
        return makeRef(this) {
            throw UnsupportedOperationException("get value from a read-only ref")
        }
    }

inline val <T> In<T>.asWriteOnlyRef: Ref<T>
    get() =
        makeRef(
            { throw UnsupportedOperationException("set value into a write-only ref") },
            { this(it) },
        )

inline fun <T> nullRef(): Ref<T> {
    return makeRef(
        { throw UnsupportedOperationException("get value from a null ref") },
        { throw UnsupportedOperationException("set value into a null ref") },
    )
}
