package yqloss.yqlossclientmixinkt.module.ssmotionblur

import yqloss.yqlossclientmixinkt.module.option.YCModuleOptions

interface SSMotionBlurOptions : YCModuleOptions {
    val strength: Double
    val balanced: Boolean
}
