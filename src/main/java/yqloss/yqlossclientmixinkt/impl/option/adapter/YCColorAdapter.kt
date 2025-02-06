package yqloss.yqlossclientmixinkt.impl.option.adapter

import cc.polyfrost.oneconfig.config.core.OneColor
import yqloss.yqlossclientmixinkt.module.option.YCColor
import yqloss.yqlossclientmixinkt.module.option.YCColorImpl
import yqloss.yqlossclientmixinkt.util.math.asInt

val OneColor.asYCColor: YCColor get() = YCColorImpl(red / 255.0, green / 255.0, blue / 255.0, alpha / 255.0)

val YCColor.asOneColor: OneColor get() = OneColor((r * 255).asInt, (g * 255).asInt, (b * 255).asInt, (a * 255).asInt)
