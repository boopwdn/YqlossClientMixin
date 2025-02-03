package yqloss.yqlossclientmixinkt.module.ssmotionblur

import yqloss.yqlossclientmixinkt.module.YCModuleOptions

interface SSMotionBlurOptions : YCModuleOptions {
    val strength: Double
    val balanced: Boolean
}
