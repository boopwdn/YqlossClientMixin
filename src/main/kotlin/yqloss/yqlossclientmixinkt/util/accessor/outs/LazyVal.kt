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

import yqloss.yqlossclientmixinkt.util.accessor.Out
import yqloss.yqlossclientmixinkt.util.accessor.value

private val throwCallInitializerAfterInitialization: () -> Nothing = {
    throw IllegalStateException("call initializer after initialization")
}

data class LazyVal<out T>(
    private var initializer: () -> Out<T>,
    private var wrapped: Out<T>? = null,
) : Out<T> {
    override fun get(): T {
        var wrapped = wrapped
        if (wrapped === null) {
            wrapped = initializer()
            initializer = throwCallInitializerAfterInitialization
            this.wrapped = wrapped
        }
        return wrapped.value
    }
}

inline fun <T> lazyVal(noinline initializer: () -> Out<T>) = LazyVal(initializer)

inline fun <T> lazyValOf(noinline initializer: () -> T) = LazyVal({ initializer().inBox })
