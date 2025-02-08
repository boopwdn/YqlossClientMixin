package yqloss.yqlossclientmixinkt.impl.option.disclaimer

import cc.polyfrost.oneconfig.config.annotations.Info
import cc.polyfrost.oneconfig.config.data.InfoType

class DisclaimerRequireHypixelModAPI {
    @Info(
        text = "The features below require Hypixel Mod API to be installed. https://modrinth.com/mod/hypixel-mod-api",
        type = InfoType.WARNING,
        size = 2,
    )
    var info = false
}
