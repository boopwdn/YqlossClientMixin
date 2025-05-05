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

package yqloss.yqlossclientmixinkt.util.accessor.outs

import kotlinx.serialization.Serializable
import yqloss.yqlossclientmixinkt.util.accessor.Out
import yqloss.yqlossclientmixinkt.util.accessor.value

@JvmInline
@Serializable
value class Box<out T>(
    val content: T,
) : Out<T> {
    override fun get() = content
}

inline val <T> Box<T>.value get() = content

@Suppress("UNCHECKED_CAST")
inline fun <R> Box<*>.cast() = content as R

inline val <T> T.inBox get() = Box(this)

inline val <T> Out<T>.reBox get() = Box(value)
