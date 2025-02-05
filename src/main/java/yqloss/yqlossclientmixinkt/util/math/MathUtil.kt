package yqloss.yqlossclientmixinkt.util.math

import kotlin.math.ceil
import kotlin.math.floor

inline val Number.asByte get() = toByte()
inline val Number.asShort get() = toShort()
inline val Number.asInt get() = toInt()
inline val Number.asLong get() = toLong()
inline val Number.asFloat get() = toFloat()
inline val Number.asDouble get() = toDouble()
inline val Number.floorInt get() = floor(toDouble()).toInt()
inline val Number.ceilInt get() = ceil(toDouble()).toInt()
