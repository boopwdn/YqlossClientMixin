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

import yqloss.yqlossclientmixinkt.util.accessor.Out
import yqloss.yqlossclientmixinkt.util.accessor.outs.inBox
import yqloss.yqlossclientmixinkt.util.accessor.value

inline fun <R> limit(
    count: Int,
    crossinline function: () -> R,
): () -> R {
    var remaining = count
    lateinit var result: Out<R>
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
    lateinit var result: Out<R>
    return {
        if (remaining > 0) {
            --remaining
            result = function(it).inBox
        }
        result.value
    }
}

inline fun <T, R> onceUnary(crossinline function: (T) -> R) = limitUnary(1, function)
