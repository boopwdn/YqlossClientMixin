package yqloss.yqlossclientmixinkt.impl.option

import cc.polyfrost.oneconfig.config.annotations.Button
import cc.polyfrost.oneconfig.config.elements.SubConfig
import yqloss.yqlossclientmixinkt.YC
import yqloss.yqlossclientmixinkt.util.printChat

class MainConfig : SubConfig("!Yqloss Client", "yqlossclient-main.json") {
    @Button(
        name = "Print Hypixel Mod API Location",
        text = "Print",
        size = 1,
    )
    fun printHypixelModAPILocation() {
        printChat(YC.api.hypixelLocation.toString())
    }
}
