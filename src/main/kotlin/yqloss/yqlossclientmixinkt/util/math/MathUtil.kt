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

package yqloss.yqlossclientmixinkt.util.math

import java.math.BigInteger
import kotlin.math.ceil
import kotlin.math.floor

inline val Number.byte get() = toByte()
inline val Number.short get() = toShort()
inline val Number.int get() = toInt()
inline val Number.long get() = toLong()
inline val Number.float get() = toFloat()
inline val Number.double get() = toDouble()
inline val Number.floorInt get() = floor(toDouble()).toInt()
inline val Number.ceilInt get() = ceil(toDouble()).toInt()
inline val Long.bigInt: BigInteger get() = BigInteger.valueOf(this)
inline val Int.bigInt: BigInteger get() = BigInteger.valueOf(this.long)

fun lerp(
    from: Double,
    to: Double,
    progress: Double,
) = from + (to - from) * progress
