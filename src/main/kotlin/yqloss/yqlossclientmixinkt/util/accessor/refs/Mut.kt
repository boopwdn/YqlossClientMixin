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

import kotlinx.serialization.Serializable
import yqloss.yqlossclientmixinkt.util.accessor.Out
import yqloss.yqlossclientmixinkt.util.accessor.Ref
import yqloss.yqlossclientmixinkt.util.accessor.value

@Serializable
data class Mut<T>(
    @JvmField var content: T,
) : Ref<T> {
    override fun get() = content

    override fun set(value: T) {
        content = value
    }
}

inline var <T> Mut<T>.value
    get() = content
    set(value) {
        content = value
    }

@Suppress("UNCHECKED_CAST")
inline fun <R> Mut<*>.cast() = content as R

inline val <T> T.inMut get() = Mut(this)

inline val <T> Out<T>.reMut get() = Mut(value)

inline fun <T> nullMut(): Mut<T?> = null.inMut
