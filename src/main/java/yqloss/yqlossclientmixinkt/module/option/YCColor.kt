package yqloss.yqlossclientmixinkt.module.option

interface YCColor {
    val r: Double
    val g: Double
    val b: Double
    val a: Double
}

data class YCColorImpl(
    override val r: Double,
    override val g: Double,
    override val b: Double,
    override val a: Double,
) : YCColor
