package yqloss.yqlossclientmixinkt.impl.option.disclaimer

import cc.polyfrost.oneconfig.config.annotations.Info
import cc.polyfrost.oneconfig.config.data.InfoType

class DisclaimerSafeMacro {
    @Info(
        text = "The features below are considered as macros but are not likely to get you banned on most servers.",
        type = InfoType.INFO,
        size = 2,
    )
    var info = false
}
