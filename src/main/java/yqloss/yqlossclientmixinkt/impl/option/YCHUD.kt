package yqloss.yqlossclientmixinkt.impl.option

import cc.polyfrost.oneconfig.hud.Hud
import cc.polyfrost.oneconfig.libs.universal.UMatrixStack
import yqloss.yqlossclientmixinkt.YC
import yqloss.yqlossclientmixinkt.event.YCEvent
import yqloss.yqlossclientmixinkt.util.math.Vec2D
import yqloss.yqlossclientmixinkt.util.math.asDouble
import yqloss.yqlossclientmixinkt.util.math.asFloat

class YCHUD(
    enabled: Boolean = false,
    x: Double = 0.0,
    y: Double = 0.0,
    positionAlignment: Int = 0,
    scale: Double = 1.0,
) : Hud(enabled, x.asFloat, y.asFloat, positionAlignment, scale.asFloat) {
    @Transient
    var renderX = 0.0
        private set

    @Transient
    var renderY = 0.0
        private set

    @Transient
    var isExample = false
        private set

    val renderScale get() = scale.asDouble
    val renderPos get() = Vec2D(renderX, renderY)

    override fun draw(
        matrices: UMatrixStack,
        x: Float,
        y: Float,
        scale: Float,
        example: Boolean,
    ) {
        renderX = x.asDouble
        renderY = y.asDouble
        isExample = example
    }

    override fun getWidth(
        scale: Float,
        example: Boolean,
    ) = GetWidthEvent(this).also(YC.eventDispatcher).width.asFloat

    override fun getHeight(
        scale: Float,
        example: Boolean,
    ) = GetHeightEvent(this).also(YC.eventDispatcher).height.asFloat

    data class GetWidthEvent(
        val hud: Hud,
        var width: Double = 0.0,
    ) : YCEvent

    data class GetHeightEvent(
        val hud: Hud,
        var height: Double = 0.0,
    ) : YCEvent
}
