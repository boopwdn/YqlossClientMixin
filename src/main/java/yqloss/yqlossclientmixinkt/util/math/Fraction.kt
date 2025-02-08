package yqloss.yqlossclientmixinkt.util.math

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

    operator fun rem(other: Fraction) = this - (this / other).asBigInteger.asFraction

    operator fun unaryPlus() = this

    operator fun unaryMinus(): Fraction = FractionImpl(-num, den)

    operator fun compareTo(other: Fraction) = (num * other.den).compareTo(den * other.num)

    val asBigInteger get() = num / den

    val asDouble get() = num.asDouble / den.asDouble

    companion object {
        val NEG_ONE: Fraction = -1 over 1
        val ZERO: Fraction = 0 over 1
        val ONE: Fraction = 1 over 1
        val TWO: Fraction = 2 over 1
    }
}

private data class FractionImpl(
    override val num: BigInteger,
    override val den: BigInteger,
) : Fraction

infix fun BigInteger.over(den: BigInteger): Fraction {
    val gcd = gcd(den)
    return when {
        den > BigInteger.ZERO -> FractionImpl(this / gcd, den / gcd)
        den < BigInteger.ZERO -> FractionImpl(-this / gcd, -den / gcd)
        else -> throw ArithmeticException("denominator cannot be zero")
    }
}

infix fun Long.over(den: Long) = asBigInteger over den.asBigInteger

infix fun Int.over(den: Int) = asBigInteger over den.asBigInteger

infix fun String.over(den: String) = BigInteger(this) over BigInteger(den)

val BigInteger.asFraction: Fraction get() = FractionImpl(this, BigInteger.ONE)

val Long.asFraction: Fraction get() = FractionImpl(this.asBigInteger, BigInteger.ONE)

val Int.asFraction: Fraction get() = FractionImpl(this.asBigInteger, BigInteger.ONE)
