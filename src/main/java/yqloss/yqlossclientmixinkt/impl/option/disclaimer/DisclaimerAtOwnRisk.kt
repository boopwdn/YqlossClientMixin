package yqloss.yqlossclientmixinkt.impl.option.disclaimer

import cc.polyfrost.oneconfig.config.annotations.Info
import cc.polyfrost.oneconfig.config.data.InfoType

class DisclaimerAtOwnRisk {
    @Info(
        text = "Use all Yqloss Client features at your own risk! It's not guaranteed that you wouldn't be banned using \"safe\" features!",
        type = InfoType.WARNING,
        size = 2,
    )
    var info = false
}
