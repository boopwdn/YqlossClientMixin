package yqloss.yqlossclientmixinkt.impl.nanovgui.widget

import yqloss.yqlossclientmixinkt.impl.nanovgui.NanoVGUIContext
import yqloss.yqlossclientmixinkt.impl.nanovgui.Widget
import yqloss.yqlossclientmixinkt.util.math.Vec2D
import yqloss.yqlossclientmixinkt.util.math.asFloat
import yqloss.yqlossclientmixinkt.util.math.lerp

data class ShadowWidget(
    private val pos1: Vec2D,
    private val pos2: Vec2D,
    private val blur: Double,
    private val spread: Double,
    private val radius: Double,
    private val opacity: Double,
) : Widget<ShadowWidget> {
    override fun draw(context: NanoVGUIContext) {
        context.run {
            val box = pos2 - pos1
            helper.setAlpha(vg, opacity.asFloat)
            helper.drawDropShadow(
                vg,
                pos1.x.asFloat,
                pos1.y.asFloat,
                box.x.asFloat,
                box.y.asFloat,
                blur.asFloat,
                spread.asFloat,
                radius.asFloat,
            )
            helper.setAlpha(vg, 1.0F)
        }
    }

    override fun alphaScale(alpha: Double) = copy(opacity = opacity * alpha)

    override fun scale(
        scale: Double,
        origin: Vec2D,
    ) = copy(pos1 = lerp(origin, pos1, scale), pos2 = lerp(origin, pos2, scale))
}
