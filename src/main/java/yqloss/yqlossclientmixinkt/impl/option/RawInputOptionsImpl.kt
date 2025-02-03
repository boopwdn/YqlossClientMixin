package yqloss.yqlossclientmixinkt.impl.option

import cc.polyfrost.oneconfig.config.Config
import cc.polyfrost.oneconfig.config.annotations.Header
import cc.polyfrost.oneconfig.config.annotations.Switch
import cc.polyfrost.oneconfig.config.data.ModType
import yqloss.yqlossclientmixinkt.YC
import yqloss.yqlossclientmixinkt.impl.getConfigFileName
import yqloss.yqlossclientmixinkt.impl.getConfigMod
import yqloss.yqlossclientmixinkt.module.rawinput.RawInputOptions

class RawInputOptionsImpl :
    Config(getConfigMod(YC.rawInput, ModType.THIRD_PARTY), getConfigFileName(YC.rawInput)),
    RawInputOptions {
    override val enabled by Config::enabled

    init {
        super.enabled = false
    }

    @JvmField
    @Header(
        text = "Raw Input",
        size = 2,
    )
    var headerModule = false

    @Switch(
        name = "Reserved",
        size = 2,
    )
    override val nativeRawInput = false
}
