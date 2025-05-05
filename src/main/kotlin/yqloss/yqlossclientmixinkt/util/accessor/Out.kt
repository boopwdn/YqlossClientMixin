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

package yqloss.yqlossclientmixinkt.util.accessor

import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

interface Out<out T> : () -> T {
    fun get(): T

    override fun invoke() = get()
}

inline val <T> Out<T>.value get() = get()

@Suppress("UNCHECKED_CAST")
inline fun <R> Out<*>.cast() = get() as R

data class OutProperty<X, T>(
    val accessor: Out<T>,
) : ReadOnlyProperty<X, T> {
    override fun getValue(
        thisRef: X,
        property: KProperty<*>,
    ) = accessor.value
}

inline operator fun <X, T> Out<T>.provideDelegate(
    thisRef: X,
    prop: KProperty<*>,
): ReadOnlyProperty<X, T> = OutProperty(this)
