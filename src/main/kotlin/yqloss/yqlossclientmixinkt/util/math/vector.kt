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

@file:Suppress("OVERRIDE_BY_INLINE", "NOTHING_TO_INLINE")

package yqloss.yqlossclientmixinkt.util.math

import kotlinx.serialization.Serializable
import yqloss.yqlossclientmixinkt.util.extension.ceilInt
import yqloss.yqlossclientmixinkt.util.extension.double
import yqloss.yqlossclientmixinkt.util.extension.floorInt
import yqloss.yqlossclientmixinkt.util.extension.int
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

sealed interface Vector<TV : Number, T : Vector<TV, T>> {
    val length: Double

    val lengthSquared: TV

    operator fun plus(vec: T): T

    operator fun minus(vec: T): T

    operator fun times(value: TV): T

    operator fun times(vec: T): TV

    operator fun unaryPlus(): T

    operator fun unaryMinus(): T

    infix fun min(other: T): T

    infix fun max(other: T): T

    infix fun allLess(other: T): Boolean

    infix fun allLessEqual(other: T): Boolean

    infix fun areaTo(other: T) = min(other) to max(other)
}

inline fun <T : Vector<Double, T>> lerp(
    from: T,
    to: T,
    progress: Double,
) = from + (to - from) * progress

typealias Area<T> = Pair<T, T>

inline operator fun <TV : Number, T : Vector<TV, T>> Area<T>.contains(vec: T) = first allLessEqual vec && vec allLess second

@Serializable
data class Vec2I(
    val x: Int,
    val y: Int,
) : Vector<Int, Vec2I> {
    override inline operator fun plus(vec: Vec2I) = Vec2I(x + vec.x, y + vec.y)

    override inline operator fun minus(vec: Vec2I) = Vec2I(x - vec.x, y - vec.y)

    override inline operator fun times(value: Int) = Vec2I(x * value, y * value)

    override inline operator fun times(vec: Vec2I) = x * vec.x + y * vec.y

    override inline operator fun unaryPlus() = this

    override inline operator fun unaryMinus() = Vec2I(-x, -y)

    inline fun plus(
        dX: Int = 0,
        dY: Int = 0,
    ) = Vec2I(x + dX, y + dY)

    inline fun times(
        nX: Int = 1,
        nY: Int = 1,
    ) = Vec2I(x * nX, y * nY)

    override inline val lengthSquared get() = x * x + y * y

    override inline val length get() = sqrt(lengthSquared.double)

    override inline fun min(other: Vec2I) = Vec2I(minOf(x, other.x), minOf(y, other.y))

    override inline fun max(other: Vec2I) = Vec2I(maxOf(x, other.x), maxOf(y, other.y))

    override inline fun allLess(other: Vec2I) = x < other.x && y < other.y

    override inline fun allLessEqual(other: Vec2I) = x <= other.x && y <= other.y
}

inline val Vec2I.asVec2D get() = Vec2D(x.double, y.double)

inline val Vec2I.asCenterVec2D get() = Vec2D(x + 0.5, y + 0.5)

inline val Vec2I.asMaxVec2D get() = Vec2D(x + 1.0, y + 1.0)

typealias Area2I = Area<Vec2I>

inline val Area2I.iterable2I: Iterable<Vec2I>
    get() {
        return object : Iterable<Vec2I> {
            override fun iterator(): Iterator<Vec2I> {
                return iterator {
                    repeat(second.y - first.y) { dy ->
                        repeat(second.x - first.x) { dx ->
                            yield(first + Vec2I(dx, dy))
                        }
                    }
                }
            }
        }
    }

@Serializable
data class Vec2D(
    val x: Double,
    val y: Double,
) : Vector<Double, Vec2D> {
    constructor(x: Float, y: Float) : this(x.double, y.double)

    override inline operator fun plus(vec: Vec2D) = Vec2D(x + vec.x, y + vec.y)

    override inline operator fun minus(vec: Vec2D) = Vec2D(x - vec.x, y - vec.y)

    override inline operator fun times(value: Double) = Vec2D(x * value, y * value)

    override inline operator fun times(vec: Vec2D) = x * vec.x + y * vec.y

    inline operator fun div(value: Double) = Vec2D(x / value, y / value)

    override inline operator fun unaryPlus() = this

    override inline operator fun unaryMinus() = Vec2D(-x, -y)

    inline fun plus(
        dX: Double = 0.0,
        dY: Double = 0.0,
    ) = Vec2D(x + dX, y + dY)

    inline fun times(
        nX: Double = 1.0,
        nY: Double = 1.0,
    ) = Vec2D(x * nX, y * nY)

    inline fun div(
        nX: Double = 1.0,
        nY: Double = 1.0,
    ) = Vec2D(x / nX, y / nY)

    override inline val lengthSquared get() = x * x + y * y

    override inline val length get() = sqrt(lengthSquared.double)

    override inline fun min(other: Vec2D) = Vec2D(minOf(x, other.x), minOf(y, other.y))

    override inline fun max(other: Vec2D) = Vec2D(maxOf(x, other.x), maxOf(y, other.y))

    override inline fun allLess(other: Vec2D) = x < other.x && y < other.y

    override inline fun allLessEqual(other: Vec2D) = x <= other.x && y <= other.y
}

inline val Vec2D.asVec2I get() = Vec2I(x.int, y.int)

inline val Vec2D.asFloorVec2I get() = Vec2I(x.floorInt, y.floorInt)

inline val Vec2D.asCeilVec2I get() = Vec2I(x.ceilInt, y.ceilInt)

inline val Vec2D.angle get() = atan2(y, x)

inline fun unitVec(radian: Double) = Vec2D(cos(radian), sin(radian))

typealias Area2D = Area<Vec2D>

@Serializable
data class Vec3I(
    val x: Int,
    val y: Int,
    val z: Int,
) : Vector<Int, Vec3I> {
    override inline operator fun plus(vec: Vec3I) = Vec3I(x + vec.x, y + vec.y, z + vec.z)

    override inline operator fun minus(vec: Vec3I) = Vec3I(x - vec.x, y - vec.y, z - vec.z)

    override inline operator fun times(value: Int) = Vec3I(x * value, y * value, z * value)

    override inline operator fun times(vec: Vec3I) = x * vec.x + y * vec.y + z * vec.z

    override inline operator fun unaryPlus() = this

    override inline operator fun unaryMinus() = Vec3I(-x, -y, -z)

    inline fun plus(
        dX: Int = 0,
        dY: Int = 0,
        dZ: Int = 0,
    ) = Vec3I(x + dX, y + dY, z + dZ)

    inline fun times(
        nX: Int = 1,
        nY: Int = 1,
        nZ: Int = 1,
    ) = Vec3I(x * nX, y * nY, z * nZ)

    inline fun cross(vec: Vec3I) = Vec3I(y * vec.z - z * vec.y, z * vec.x - x * vec.z, x * vec.y - y * vec.x)

    override inline val lengthSquared get() = x * x + y * y + z * z

    override inline val length get() = sqrt(lengthSquared.double)

    override inline fun min(other: Vec3I) = Vec3I(minOf(x, other.x), minOf(y, other.y), minOf(z, other.z))

    override inline fun max(other: Vec3I) = Vec3I(maxOf(x, other.x), maxOf(y, other.y), maxOf(z, other.z))

    override inline fun allLess(other: Vec3I) = x < other.x && y < other.y && z < other.z

    override inline fun allLessEqual(other: Vec3I) = x <= other.x && y <= other.y && z <= other.z
}

inline val Vec3I.asVec3D get() = Vec3D(x.double, y.double, z.double)

inline val Vec3I.asCenterVec3D get() = Vec3D(x + 0.5, y + 0.5, z + 0.5)

inline val Vec3I.asMaxVec3D get() = Vec3D(x + 1.0, y + 1.0, z + 1.0)

typealias Area3I = Area<Vec3I>

inline val Area3I.iterable3I: Iterable<Vec3I>
    get() {
        return object : Iterable<Vec3I> {
            override fun iterator(): Iterator<Vec3I> {
                return iterator {
                    repeat(second.y - first.y) { dy ->
                        repeat(second.z - first.z) { dz ->
                            repeat(second.x - first.x) { dx ->
                                yield(first + Vec3I(dx, dy, dz))
                            }
                        }
                    }
                }
            }
        }
    }

inline val Area3I.volume3I get() = (second - first).run { x * y * z }

@Serializable
data class Vec3D(
    val x: Double,
    val y: Double,
    val z: Double,
) : Vector<Double, Vec3D> {
    constructor(x: Float, y: Float, z: Float) : this(x.double, y.double, z.double)

    override inline operator fun plus(vec: Vec3D) = Vec3D(x + vec.x, y + vec.y, z + vec.z)

    override inline operator fun minus(vec: Vec3D) = Vec3D(x - vec.x, y - vec.y, z - vec.z)

    override inline operator fun times(value: Double) = Vec3D(x * value, y * value, z * value)

    override inline operator fun times(vec: Vec3D) = x * vec.x + y * vec.y + z * vec.z

    inline operator fun div(value: Double) = Vec3D(x / value, y / value, z / value)

    override inline operator fun unaryPlus() = this

    override inline operator fun unaryMinus() = Vec3D(-x, -y, -z)

    inline fun plus(
        dX: Double = 0.0,
        dY: Double = 0.0,
        dZ: Double = 0.0,
    ) = Vec3D(x + dX, y + dY, z + dZ)

    inline fun times(
        nX: Double = 1.0,
        nY: Double = 1.0,
        nZ: Double = 1.0,
    ) = Vec3D(x * nX, y * nY, z * nZ)

    inline fun div(
        nX: Double = 1.0,
        nY: Double = 1.0,
        nZ: Double = 1.0,
    ) = Vec3D(x / nX, y / nY, z / nZ)

    inline fun cross(vec: Vec3D) = Vec3D(y * vec.z - z * vec.y, z * vec.x - x * vec.z, x * vec.y - y * vec.x)

    override inline val lengthSquared get() = x * x + y * y + z * z

    override inline val length get() = sqrt(lengthSquared.double)

    override inline fun min(other: Vec3D) = Vec3D(minOf(x, other.x), minOf(y, other.y), minOf(z, other.z))

    override inline fun max(other: Vec3D) = Vec3D(maxOf(x, other.x), maxOf(y, other.y), maxOf(z, other.z))

    override inline fun allLess(other: Vec3D) = x < other.x && y < other.y && z < other.z

    override inline fun allLessEqual(other: Vec3D) = x <= other.x && y <= other.y && z <= other.z
}

inline val Vec3D.asVec3I get() = Vec3I(x.int, y.int, z.int)

inline val Vec3D.asFloorVec3I get() = Vec3I(x.floorInt, y.floorInt, z.floorInt)

inline val Vec3D.asCeilVec3I get() = Vec3I(x.ceilInt, y.ceilInt, z.ceilInt)

typealias Area3D = Area<Vec3D>
