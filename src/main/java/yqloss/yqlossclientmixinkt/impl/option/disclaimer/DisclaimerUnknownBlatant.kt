package yqloss.yqlossclientmixinkt.impl.option.disclaimer

import cc.polyfrost.oneconfig.config.annotations.Info
import cc.polyfrost.oneconfig.config.data.InfoType

class DisclaimerUnknownBlatant {
    @Info(
        text = "The features below change vanilla behavior. Use them at your own risk!",
        type = InfoType.WARNING,
        size = 2,
    )
    var info = false
}
