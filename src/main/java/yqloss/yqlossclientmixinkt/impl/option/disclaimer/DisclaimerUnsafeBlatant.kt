package yqloss.yqlossclientmixinkt.impl.option.disclaimer

import cc.polyfrost.oneconfig.config.annotations.Info
import cc.polyfrost.oneconfig.config.data.InfoType

class DisclaimerUnsafeBlatant {
    @Info(
        text = "THE FEATURES BELOW CHANGE VANILLA BEHAVIOR AND MAY GET YOU BANNED!",
        type = InfoType.ERROR,
        size = 2,
    )
    var info = false
}
