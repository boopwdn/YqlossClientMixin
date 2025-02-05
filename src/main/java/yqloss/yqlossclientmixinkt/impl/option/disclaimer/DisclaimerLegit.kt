package yqloss.yqlossclientmixinkt.impl.option.disclaimer

import cc.polyfrost.oneconfig.config.annotations.Info
import cc.polyfrost.oneconfig.config.data.InfoType

class DisclaimerLegit {
    @Info(
        text = "The features below are safe to use.",
        type = InfoType.INFO,
        size = 2,
    )
    var info = false
}
