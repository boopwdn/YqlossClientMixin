package yqloss.yqlossclientmixinkt.impl.option.disclaimer

import cc.polyfrost.oneconfig.config.annotations.Info
import cc.polyfrost.oneconfig.config.data.InfoType

class DisclaimerQOL {
    @Info(
        text = "The features below change vanilla behavior but are not detectable by servers. They are basically safe to use.",
        type = InfoType.INFO,
        size = 2,
    )
    var info = false
}
