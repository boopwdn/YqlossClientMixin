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
import yqloss.yqlossclientmixinkt.util.accessor.value

private val throwCallInitializerAfterInitialization: () -> Nothing = {
    throw IllegalStateException("call initializer after initialization")
}

private val throwCallInitializerAfterInitializationUnary: (Any?) -> Nothing = {
    throw IllegalStateException("call initializer after initialization")
}

data class LazyVar<T>(
    private var setInitializer: (T) -> Ref<T>,
    private var getInitializer: () -> Ref<T>,
    private var wrapped: Ref<T>? = null,
) : Ref<T> {
    override fun get(): T {
        var wrapped = wrapped
        if (wrapped === null) {
            wrapped = getInitializer()
            getInitializer = throwCallInitializerAfterInitialization
            setInitializer = throwCallInitializerAfterInitializationUnary
            this.wrapped = wrapped
        }
        return wrapped.value
    }

    override fun set(value: T) {
        var wrapped = wrapped
        if (wrapped === null) {
            wrapped = setInitializer(value)
            getInitializer = throwCallInitializerAfterInitialization
            setInitializer = throwCallInitializerAfterInitializationUnary
            this.wrapped = wrapped
        }
        wrapped.value = value
    }
}

inline fun <T> lazyVar(
    noinline setInitializer: (T) -> Ref<T> = { it.inMut },
    noinline getInitializer: () -> Ref<T>,
) = LazyVar(setInitializer, getInitializer)

inline fun <T> lazyVarOf(noinline initializer: () -> T): LazyVar<T> {
    return LazyVar(
        { it.inMut },
        { initializer().inMut },
    )
}
