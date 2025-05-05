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

operator fun <R1, R2> (() -> R1).div(other: (R1) -> R2): () -> R2 {
    return (
        this as? ChainedFunctionHolder
            ?: ChainedFunctionHolder(listOf { this() })
    ).append(other.castTo<(Any?) -> Any?>()).castTo()
}

operator fun <T, R1, R2> ((T) -> R1).div(other: (R1) -> R2): (T) -> R2 {
    return (
        this as? ChainedFunctionHolder
            ?: ChainedFunctionHolder(listOf(this).castTo())
    ).append(other.castTo<(Any?) -> Any?>()).castTo()
}
