package yqloss.yqlossclientmixinkt.impl.option

import cc.polyfrost.oneconfig.config.annotations.Header
import yqloss.yqlossclientmixinkt.module.rawinput.INFO_RAW_INPUT
import yqloss.yqlossclientmixinkt.module.rawinput.RawInputOptions

class RawInputOptionsImpl :
    OptionsImpl(INFO_RAW_INPUT),
    RawInputOptions {
    @JvmField
    @Header(
        text = "Raw Input",
        size = 2,
    )
    var headerModule = false
}
