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

inline fun <T> intervalAction(
    intervalNanos: Long,
    crossinline function: (T) -> Unit,
): (T) -> Unit {
    var last: Long? = null

    return { arg ->
        val time = System.nanoTime()
        last?.takeIf { time - it < intervalNanos } ?: function(arg)
        last = time
    }
}

inline fun intervalAction(
    intervalNanos: Long,
    crossinline function: () -> Unit,
): () -> Unit {
    var last: Long? = null

    return {
        val time = System.nanoTime()
        last?.takeIf { time - it < intervalNanos } ?: run {
            try {
                function()
            } finally {
                last = time
            }
        }
    }
}
