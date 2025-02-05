package yqloss.yqlossclientmixinkt.impl.option.disclaimer

import cc.polyfrost.oneconfig.config.annotations.Info
import cc.polyfrost.oneconfig.config.data.InfoType

class DisclaimerUnsafeMacro {
    @Info(
        text = "THE FEATURES BELOW ARE CONSIDERED AS MACROS AND MAY GET YOU BANNED!",
        type = InfoType.ERROR,
        size = 2,
    )
    var info = false
}
