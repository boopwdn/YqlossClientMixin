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
import yqloss.yqlossclientmixinkt.util.accessor.Ref
import yqloss.yqlossclientmixinkt.util.accessor.value

private val throwCallInitializerAfterInitialization: (Any?) -> Nothing = {
    throw IllegalStateException("call initializer after initialization")
}

@Serializable
data class LateVar<T>(
    private var initializer: (T) -> Ref<T>,
    private var wrapped: Ref<T>? = null,
) : Ref<T> {
    override fun get(): T {
        return (wrapped ?: throw IllegalStateException("get value before initialization")).value
    }

    override fun set(value: T) {
        val wrapped = wrapped
        if (wrapped === null) {
            this.wrapped = initializer(value)
            initializer = throwCallInitializerAfterInitialization
        } else {
            wrapped.set(value)
        }
    }
}

inline fun <T> lateVar(noinline initializer: (T) -> Ref<T> = { it.inMut }) = LateVar(initializer)
