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

package yqloss.yqlossclientmixinkt.util.general

import kotlinx.serialization.Serializable
import kotlin.reflect.KMutableProperty0

sealed interface BoxType<out T> {
    val value: T

    @Suppress("UNCHECKED_CAST")
    fun <R> cast() = value as R
}

sealed interface Ref<T> : BoxType<T> {
    override var value: T
}

data class GetterBox<T>(
    private val getter: () -> T,
) : BoxType<T> {
    override val value get() = getter()
}

@JvmInline
@Serializable
value class Box<out T>(
    override val value: T,
) : BoxType<T> {
    @Suppress("UNCHECKED_CAST", "OVERRIDE_BY_INLINE", "NOTHING_TO_INLINE")
    override inline fun <R> cast() = value as R
}

data class AccessorRef<T>(
    private val getter: () -> T,
    private val setter: (T) -> Unit,
) : Ref<T> {
    override var value: T
        get() = getter()
        set(value) {
            setter(value)
        }
}

@Serializable
data class FieldRef<T>(
    override var value: T,
) : Ref<T>

inline val <T> T.inBox get() = Box(this)

inline val <T> T.inRef get(): Ref<T> = FieldRef(this)

inline val <T> BoxType<T>.reBox get() = Box(value)

inline val <T> BoxType<T>.reRef get(): Ref<T> = FieldRef(value)

fun <T> makeBox(getter: () -> T): BoxType<T> = GetterBox(getter)

fun <T> makeRef(
    getter: () -> T,
    setter: (T) -> Unit,
): Ref<T> = AccessorRef(getter, setter)

inline val <T> (() -> T).asBox get(): BoxType<T> = makeBox(this)

inline val <T> KMutableProperty0<T>.asRef get(): Ref<T> = makeRef(this, this::set)
