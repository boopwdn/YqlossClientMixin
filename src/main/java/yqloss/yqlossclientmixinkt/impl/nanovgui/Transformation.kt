package yqloss.yqlossclientmixinkt.impl.nanovgui

import net.minecraft.client.gui.ScaledResolution
import yqloss.yqlossclientmixinkt.util.MC
import yqloss.yqlossclientmixinkt.util.math.Vec2D
import yqloss.yqlossclientmixinkt.util.math.asDouble

data class Transformation(
    val offset: Vec2D = Vec2D(0.0, 0.0),
    val scale: Double = 1.0,
) {
    infix fun translate(vec: Vec2D) = Transformation(offset + vec * scale, scale)

    infix fun scale(factor: Double) = Transformation(offset, scale * factor)

    infix fun pos(vec: Vec2D) = offset + vec * scale

    infix fun size(num: Double) = num * scale

    infix fun size(vec: Vec2D) = vec * scale

    fun scaleMC() = scale(ScaledResolution(MC).scaleFactor.asDouble)
}
