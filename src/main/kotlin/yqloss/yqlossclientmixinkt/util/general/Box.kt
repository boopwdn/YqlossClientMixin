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

sealed interface BoxType<out T> {
    val value: T

    @Suppress("UNCHECKED_CAST")
    fun <R> cast() = value as R
}

data class Box<out T>(
    override val value: T,
) : BoxType<T>

data class MutableBox<T>(
    override var value: T,
) : BoxType<T>

inline val <T> T.inBox get() = Box(this)

inline val <T> T.inMutableBox get() = MutableBox(this)

inline val <T> BoxType<T>.reBox get() = Box(value)

inline val <T> BoxType<T>.reMutableBox get() = MutableBox(value)
