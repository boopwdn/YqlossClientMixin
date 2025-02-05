package yqloss.yqlossclientmixinkt.impl.option.impl

import cc.polyfrost.oneconfig.config.annotations.CustomOption
import cc.polyfrost.oneconfig.config.annotations.Header
import yqloss.yqlossclientmixinkt.impl.option.OptionsImpl
import yqloss.yqlossclientmixinkt.impl.option.disclaimer.DisclaimerAtOwnRisk
import yqloss.yqlossclientmixinkt.impl.option.disclaimer.DisclaimerLegit
import yqloss.yqlossclientmixinkt.module.rawinput.INFO_RAW_INPUT
import yqloss.yqlossclientmixinkt.module.rawinput.RawInputOptions

class RawInputOptionsImpl :
    OptionsImpl(INFO_RAW_INPUT),
    RawInputOptions {
    @CustomOption(id = "extract")
    var disclaimer = DisclaimerAtOwnRisk()

    @CustomOption(id = "extract")
    var legit = DisclaimerLegit()

    @Header(
        text = "Raw Input",
        size = 2,
    )
    var headerModule = false
}
