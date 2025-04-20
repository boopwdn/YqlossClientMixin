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

package yqloss.yqlossclientmixinkt.util

import yqloss.yqlossclientmixinkt.util.general.Box
import yqloss.yqlossclientmixinkt.util.general.BoxType
import yqloss.yqlossclientmixinkt.util.general.inBox

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

private data class ChainedFunctionHolder(
    val functions: List<(Any?) -> Any?>,
) : () -> Any?,
    (Any?) -> Any? {
    override fun invoke(): Any? {
        var result: Any? = Unit
        functions.forEach { result = it(result) }
        return result
    }

    override fun invoke(arg: Any?): Any? {
        var result: Any? = arg
        functions.forEach { result = it(result) }
        return result
    }

    fun append(function: (Any?) -> Any?): ChainedFunctionHolder {
        return if (function is ChainedFunctionHolder) {
            ChainedFunctionHolder(functions + function.functions)
        } else {
            ChainedFunctionHolder(functions + function)
        }
    }

    fun append(function: () -> Any?): ChainedFunctionHolder {
        return if (function is ChainedFunctionHolder) {
            ChainedFunctionHolder(functions + function.functions)
        } else {
            ChainedFunctionHolder(functions + { function() })
        }
    }
}

operator fun <R> (() -> R)?.plus(other: () -> R): () -> R {
    return (
        this as? SequentialFunctionHolder
            ?: SequentialFunctionHolder(listOfNotNull(this?.let { { this() } }))
    ).append(other).inBox.cast()
}

operator fun <T, R1, R2> ((T) -> R1)?.plus(other: (T) -> R2): (T) -> R2 {
    return (
        this as? SequentialFunctionHolder
            ?: SequentialFunctionHolder(listOfNotNull(this).inBox.cast())
    ).append(other.inBox.cast<(Any?) -> Any?>()).inBox.cast()
}

operator fun <R1, R2> (() -> R1).div(other: (R1) -> R2): () -> R2 {
    return (
        this as? ChainedFunctionHolder
            ?: ChainedFunctionHolder(listOf { this() })
    ).append(other.inBox.cast<(Any?) -> Any?>()).inBox.cast()
}

operator fun <T, R1, R2> ((T) -> R1).div(other: (R1) -> R2): (T) -> R2 {
    return (
        this as? ChainedFunctionHolder
            ?: ChainedFunctionHolder(listOf(this).inBox.cast())
    ).append(other.inBox.cast<(Any?) -> Any?>()).inBox.cast()
}

inline fun <R> limit(
    count: Int,
    crossinline function: () -> R,
): () -> R {
    var remaining = count
    lateinit var result: BoxType<R>
    return {
        if (remaining > 0) {
            --remaining
            result = function().inBox
        }
        result.value
    }
}

inline fun <R> once(crossinline function: () -> R) = limit(1, function)

inline fun <T, R> limitUnary(
    count: Int,
    crossinline function: (T) -> R,
): (T) -> R {
    var remaining = count
    lateinit var result: BoxType<R>
    return {
        if (remaining > 0) {
            --remaining
            result = function(it).inBox
        }
        result.value
    }
}

inline fun <T, R> onceUnary(crossinline function: (T) -> R) = limitUnary(1, function)

inline fun loop(function: () -> Unit): Nothing {
    while (true) {
        function()
    }
}

inline fun <R> loop(function: () -> Box<R>?): R {
    while (true) {
        function()?.let { return it.value }
    }
}
