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

package yqloss.yqlossclientmixinkt.util.math

import yqloss.yqlossclientmixinkt.util.extension.bigInt
import yqloss.yqlossclientmixinkt.util.extension.double
import java.math.BigInteger

sealed interface Fraction {
    val num: BigInteger
    val den: BigInteger

    operator fun plus(other: Fraction) = (num * other.den + den * other.num) over den * other.den

    operator fun minus(other: Fraction) = (num * other.den - den * other.num) over den * other.den

    operator fun times(other: Fraction) = num * other.num over den * other.den

    operator fun times(other: BigInteger) = num * other over den

    operator fun div(other: Fraction) = num * other.den over den * other.num

    operator fun div(other: BigInteger) = num over den * other

    operator fun rem(other: Fraction) = this - (this / other).bigInt.frac

    operator fun unaryPlus() = this

    operator fun unaryMinus(): Fraction = FractionImpl(-num, den)

    operator fun compareTo(other: Fraction) = (num * other.den).compareTo(den * other.num)

    val bigInt get() = num / den

    val double get() = num.double / den.double

    companion object {
        val NEG_ONE: Fraction = -1 over 1
        val ZERO: Fraction = 0 over 1
        val ONE: Fraction = 1 over 1
        val TWO: Fraction = 2 over 1
    }
}

data class FractionImpl(
    override val num: BigInteger,
    override val den: BigInteger,
) : Fraction

inline infix fun BigInteger.over(den: BigInteger): Fraction {
    val gcd = gcd(den)
    return when {
        den > BigInteger.ZERO -> FractionImpl(this / gcd, den / gcd)
        den < BigInteger.ZERO -> FractionImpl(-this / gcd, -den / gcd)
        else -> throw ArithmeticException("denominator cannot be zero")
    }
}

inline infix fun Long.over(den: Long) = bigInt over den.bigInt

inline infix fun Int.over(den: Int) = bigInt over den.bigInt

inline infix fun String.over(den: String) = BigInteger(this) over BigInteger(den)

inline val BigInteger.frac: Fraction get() = FractionImpl(this, BigInteger.ONE)

inline val Long.frac: Fraction get() = FractionImpl(this.bigInt, BigInteger.ONE)

inline val Int.frac: Fraction get() = FractionImpl(this.bigInt, BigInteger.ONE)
