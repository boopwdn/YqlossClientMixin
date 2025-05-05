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

package yqloss.yqlossclientmixinkt.util.functional

import yqloss.yqlossclientmixinkt.util.extension.castTo

private data class SequentialFunctionHolder(
    val functions: List<(Any?) -> Any?>,
) : () -> Any?,
    (Any?) -> Any? {
    override fun invoke(): Any? {
        var result: Any? = Unit
        functions.forEach { result = it(Unit) }
        return result
    }

    override fun invoke(arg: Any?): Any? {
        var result: Any? = Unit
        functions.forEach { result = it(arg) }
        return result
    }

    fun append(function: (Any?) -> Any?): SequentialFunctionHolder {
        return if (function is SequentialFunctionHolder) {
            SequentialFunctionHolder(functions + function.functions)
        } else {
            SequentialFunctionHolder(functions + function)
        }
    }

    fun append(function: () -> Any?): SequentialFunctionHolder {
        return if (function is SequentialFunctionHolder) {
            SequentialFunctionHolder(functions + function.functions)
        } else {
            SequentialFunctionHolder(functions + { function() })
        }
    }
}

operator fun <R> (() -> R)?.plus(other: () -> R): () -> R {
    return (
        this as? SequentialFunctionHolder
            ?: SequentialFunctionHolder(listOfNotNull(this?.let { { this() } }))
    ).append(other).castTo()
}

operator fun <T, R1, R2> ((T) -> R1)?.plus(other: (T) -> R2): (T) -> R2 {
    return (
        this as? SequentialFunctionHolder
            ?: SequentialFunctionHolder(listOfNotNull(this).castTo())
    ).append(other.castTo<(Any?) -> Any?>()).castTo()
}
