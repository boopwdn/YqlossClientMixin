package yqloss.yqlossclientmixinkt.impl.option

import cc.polyfrost.oneconfig.config.annotations.Header
import cc.polyfrost.oneconfig.config.annotations.Slider
import cc.polyfrost.oneconfig.config.annotations.Switch
import yqloss.yqlossclientmixinkt.module.ssmotionblur.INFO_SS_MOTION_BLUR
import yqloss.yqlossclientmixinkt.module.ssmotionblur.SSMotionBlurOptions

class SSMotionBlurOptionsImpl :
    OptionsImpl(INFO_SS_MOTION_BLUR),
    SSMotionBlurOptions {
    @JvmField
    @Header(
        text = "SS Motion Blur",
        size = 2,
    )
    var headerModule = false

    @JvmField
    @Slider(
        name = "Strength",
        min = 0.0F,
        max = 100.0F,
        description = "0 means there won't be any effect. 100 means the screen will freeze.",
    )
    var strengthOption = 50.0F

    @JvmField
    @Switch(
        name = "FPS Balanced",
        description = "Make the blur on whatever FPS look as if it's 256 FPS.",
        size = 2,
    )
    var balancedOption = true

    override val strength get() = strengthOption / 100.0
    override val balanced by ::balancedOption
}
