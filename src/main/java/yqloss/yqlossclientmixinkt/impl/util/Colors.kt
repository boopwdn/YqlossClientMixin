package yqloss.yqlossclientmixinkt.impl.util

import cc.polyfrost.oneconfig.config.core.OneColor
import yqloss.yqlossclientmixinkt.util.math.asInt
import kotlin.math.max
import kotlin.math.min

object Colors {
    val DARK = OneColor("202020FF")
    val LIGHT = OneColor("E0E0E0FF")
    val RED = OneColor("FA5252FF")
    val PINK = OneColor("E64980FF")
    val GRAPE = OneColor("BE4BDBFF")
    val VIOLET = OneColor("7950F2FF")
    val INDIGO = OneColor("4C6EF5FF")
    val BLUE = OneColor("228BE6FF")
    val CYAN = OneColor("15AABFFF")
    val TEAL = OneColor("12B886FF")
    val GREEN = OneColor("40C057FF")
    val LIME = OneColor("82C91EFF")
    val YELLOW = OneColor("FAB005FF")
    val ORANGE = OneColor("FD7E14FF")
}

infix fun Int.alphaScale(scale: Double): Int {
    val alpha = this and 0xFF000000.asInt ushr 24 and 0xFF
    val color = this and 0xFFFFFF
    val scaledAlpha = max(0, min(255, (alpha * scale).asInt))
    return scaledAlpha shl 24 or color
}
