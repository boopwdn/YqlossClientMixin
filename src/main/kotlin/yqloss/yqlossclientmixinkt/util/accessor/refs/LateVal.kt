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

import yqloss.yqlossclientmixinkt.util.accessor.Out
import yqloss.yqlossclientmixinkt.util.accessor.Ref
import yqloss.yqlossclientmixinkt.util.accessor.outs.inBox
import yqloss.yqlossclientmixinkt.util.accessor.value
import yqloss.yqlossclientmixinkt.util.extension.invertNull

private val throwCallInitializerAfterInitialization: (Any?) -> Nothing = {
    throw IllegalStateException("call initializer after initialization")
}

data class LateVal<T>(
    private var initializer: (T) -> Out<T>,
    private var wrapped: Out<T>? = null,
) : Ref<T> {
    override fun get(): T {
        return (wrapped ?: throw IllegalStateException("get value before initialization")).value
    }

    override fun set(value: T) {
        wrapped.invertNull ?: throw IllegalStateException("set value after initialization")
        wrapped = initializer(value)
        initializer = throwCallInitializerAfterInitialization
    }
}

inline fun <T> lateVal(noinline initializer: (T) -> Out<T> = { it.inBox }) = LateVal(initializer)
