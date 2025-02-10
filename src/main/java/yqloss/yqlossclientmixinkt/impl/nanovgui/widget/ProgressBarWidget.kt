package yqloss.yqlossclientmixinkt.impl.nanovgui.widget

import yqloss.yqlossclientmixinkt.impl.nanovgui.ListWidget
import yqloss.yqlossclientmixinkt.util.math.Vec2D
import yqloss.yqlossclientmixinkt.util.math.lerp

fun progressBarWidget(
    progress: Double,
    pos1: Vec2D,
    pos2: Vec2D,
    colorProgress: Int,
    colorBackground: Int,
): ListWidget {
    return ListWidget(
        RoundedRectWidget(
            pos1,
            pos2,
            colorBackground,
            (pos2 - pos1).y / 2.0,
        ),
        RoundedRectWidget(
            pos1,
            pos2.copy(x = lerp(pos1.x + (pos2 - pos1).y, pos2.x, progress)),
            colorProgress,
            (pos2 - pos1).y / 2.0,
        ),
    )
}
