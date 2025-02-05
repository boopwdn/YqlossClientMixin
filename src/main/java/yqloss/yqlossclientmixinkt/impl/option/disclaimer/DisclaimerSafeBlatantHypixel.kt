package yqloss.yqlossclientmixinkt.impl.option.disclaimer

import cc.polyfrost.oneconfig.config.annotations.Info
import cc.polyfrost.oneconfig.config.data.InfoType

class DisclaimerSafeBlatantHypixel {
    @Info(
        text = "The features below change vanilla behavior but are not likely to get you banned on Hypixel.",
        type = InfoType.INFO,
        size = 2,
    )
    var info = false
}
