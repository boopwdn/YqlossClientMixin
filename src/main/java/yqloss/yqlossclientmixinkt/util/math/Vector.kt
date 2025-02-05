package yqloss.yqlossclientmixinkt.util.math

import kotlin.math.sqrt

data class Vec2I(
    val x: Int,
    val y: Int,
) {
    operator fun plus(vec: Vec2I) = Vec2I(x + vec.x, y + vec.y)

    operator fun minus(vec: Vec2I) = Vec2I(x - vec.x, y - vec.y)

    operator fun times(value: Int) = Vec2I(x * value, y * value)

    operator fun times(vec: Vec2I) = x * vec.x + y * vec.y

    operator fun unaryPlus() = this

    operator fun unaryMinus() = Vec2I(-x, -y)

    fun plus(
        dX: Int = 0,
        dY: Int = 0,
    ) = Vec2I(x + dX, y + dY)

    fun times(
        nX: Int = 1,
        nY: Int = 1,
    ) = Vec2I(x * nX, y * nY)

    inline val lengthSquared get() = x * x + y * y

    inline val length get() = sqrt(lengthSquared.asDouble)

    inline val asVec2D get() = Vec2D(x.asDouble, y.asDouble)

    inline val asCenterVec2D get() = Vec2D(x + 0.5, y + 0.5)

    inline val asMaxVec2D get() = Vec2D(x + 1.0, y + 1.0)
}

data class Vec2D(
    val x: Double,
    val y: Double,
) {
    operator fun plus(vec: Vec2D) = Vec2D(x + vec.x, y + vec.y)

    operator fun minus(vec: Vec2D) = Vec2D(x - vec.x, y - vec.y)

    operator fun times(value: Double) = Vec2D(x * value, y * value)

    operator fun times(vec: Vec2D) = x * vec.x + y * vec.y

    operator fun div(value: Double) = Vec2D(x / value, y / value)

    operator fun unaryPlus() = this

    operator fun unaryMinus() = Vec2D(-x, -y)

    fun plus(
        dX: Double = 0.0,
        dY: Double = 0.0,
    ) = Vec2D(x + dX, y + dY)

    fun times(
        nX: Double = 1.0,
        nY: Double = 1.0,
    ) = Vec2D(x * nX, y * nY)

    fun div(
        nX: Double = 1.0,
        nY: Double = 1.0,
    ) = Vec2D(x / nX, y / nY)

    inline val lengthSquared get() = x * x + y * y

    inline val length get() = sqrt(lengthSquared.asDouble)

    inline val asVec2I get() = Vec2I(x.asInt, y.asInt)

    inline val asFloorVec2I get() = Vec2I(x.floorInt, y.floorInt)

    inline val asCeilVec2I get() = Vec2I(x.ceilInt, y.ceilInt)
}

data class Vec3I(
    val x: Int,
    val y: Int,
    val z: Int,
) {
    operator fun plus(vec: Vec3I) = Vec3I(x + vec.x, y + vec.y, z + vec.z)

    operator fun minus(vec: Vec3I) = Vec3I(x - vec.x, y - vec.y, z + vec.z)

    operator fun times(value: Int) = Vec3I(x * value, y * value, z * value)

    operator fun times(vec: Vec3I) = x * vec.x + y * vec.y + z * vec.z

    operator fun unaryPlus() = this

    operator fun unaryMinus() = Vec3I(-x, -y, -z)

    fun plus(
        dX: Int = 0,
        dY: Int = 0,
        dZ: Int = 0,
    ) = Vec3I(x + dX, y + dY, z + dZ)

    fun times(
        nX: Int = 1,
        nY: Int = 1,
        nZ: Int = 1,
    ) = Vec3I(x * nX, y * nY, z * nZ)

    fun cross(vec: Vec3I) = Vec3I(y * vec.z - z * vec.y, z * vec.x - x * vec.z, x * vec.y - y * vec.x)

    inline val lengthSquared get() = x * x + y * y + z * z

    inline val length get() = sqrt(lengthSquared.asDouble)

    inline val asVec3D get() = Vec3D(x.asDouble, y.asDouble, z.asDouble)

    inline val asCenterVec3D get() = Vec3D(x + 0.5, y + 0.5, z + 0.5)

    inline val asMaxVec3D get() = Vec3D(x + 1.0, y + 1.0, z + 1.0)
}

data class Vec3D(
    val x: Double,
    val y: Double,
    val z: Double,
) {
    operator fun plus(vec: Vec3D) = Vec3D(x + vec.x, y + vec.y, z + vec.z)

    operator fun minus(vec: Vec3D) = Vec3D(x - vec.x, y - vec.y, z + vec.z)

    operator fun times(value: Double) = Vec3D(x * value, y * value, z * value)

    operator fun times(vec: Vec3D) = x * vec.x + y * vec.y + z * vec.z

    operator fun div(value: Double) = Vec3D(x / value, y / value, z / value)

    operator fun unaryPlus() = this

    operator fun unaryMinus() = Vec3D(-x, -y, -z)

    fun plus(
        dX: Double = 0.0,
        dY: Double = 0.0,
        dZ: Double = 0.0,
    ) = Vec3D(x + dX, y + dY, z + dZ)

    fun times(
        nX: Double = 1.0,
        nY: Double = 1.0,
        nZ: Double = 1.0,
    ) = Vec3D(x * nX, y * nY, z * nZ)

    fun div(
        nX: Double = 1.0,
        nY: Double = 1.0,
        nZ: Double = 1.0,
    ) = Vec3D(x / nX, y / nY, z / nZ)

    fun cross(vec: Vec3D) = Vec3D(y * vec.z - z * vec.y, z * vec.x - x * vec.z, x * vec.y - y * vec.x)

    inline val lengthSquared get() = x * x + y * y + z * z

    inline val length get() = sqrt(lengthSquared.asDouble)

    inline val asVec3I get() = Vec3I(x.asInt, y.asInt, z.asInt)

    inline val asFloorVec3I get() = Vec3I(x.floorInt, y.floorInt, z.floorInt)

    inline val asCeilVec3I get() = Vec3I(x.ceilInt, y.ceilInt, z.ceilInt)
}
