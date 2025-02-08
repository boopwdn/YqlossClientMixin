package yqloss.yqlossclientmixinkt.util.math

import java.math.BigInteger
import kotlin.math.ceil
import kotlin.math.floor

inline val Number.asByte get() = toByte()
inline val Number.asShort get() = toShort()
inline val Number.asInt get() = toInt()
inline val Number.asLong get() = toLong()
inline val Number.asFloat get() = toFloat()
inline val Number.asDouble get() = toDouble()
inline val String.asByte get() = toByte()
inline val String.asShort get() = toShort()
inline val String.asInt get() = toInt()
inline val String.asLong get() = toLong()
inline val String.asFloat get() = toFloat()
inline val String.asDouble get() = toDouble()
inline val Number.floorInt get() = floor(toDouble()).toInt()
inline val Number.ceilInt get() = ceil(toDouble()).toInt()
inline val Long.asBigInteger: BigInteger get() = BigInteger.valueOf(this)
inline val Int.asBigInteger: BigInteger get() = BigInteger.valueOf(this.asLong)
inline val Number.asStringBigInteger get() = BigInteger(toString())
inline val String.asBigInteger get() = BigInteger(this)
