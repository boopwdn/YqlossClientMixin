package yqloss.yqlossclientmixinkt.impl.nanovgui.widget

import cc.polyfrost.oneconfig.renderer.font.Font
import cc.polyfrost.oneconfig.renderer.font.Fonts
import yqloss.yqlossclientmixinkt.impl.nanovgui.NanoVGUIContext
import yqloss.yqlossclientmixinkt.impl.nanovgui.Widget
import yqloss.yqlossclientmixinkt.impl.util.alphaScale
import yqloss.yqlossclientmixinkt.util.math.Vec2D
import yqloss.yqlossclientmixinkt.util.math.asFloat
import yqloss.yqlossclientmixinkt.util.math.lerp

data class TextWidget(
    private val text: String,
    private val pos: Vec2D,
    private val color: Int,
    private val size: Double,
    private val font: Font = Fonts.REGULAR,
    private val anchor: Vec2D = Vec2D(0.0, 0.0),
) : Widget<TextWidget> {
    override fun draw(context: NanoVGUIContext) {
        context.run {
            val width = helper.getTextWidth(vg, text, size.asFloat, font)
            val x = pos.x - width * anchor.x
            val y = pos.y - size * anchor.y + size / 2.0
            helper.drawText(vg, text, x.asFloat, y.asFloat, color, size.asFloat, font)
        }
    }

    override fun alphaScale(alpha: Double) = copy(color = color alphaScale alpha)

    override fun scale(
        scale: Double,
        origin: Vec2D,
    ) = copy(pos = lerp(origin, pos, scale), size = size * scale)
}
