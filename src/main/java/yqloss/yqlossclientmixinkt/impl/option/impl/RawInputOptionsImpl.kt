package yqloss.yqlossclientmixinkt.impl.option.impl

import cc.polyfrost.oneconfig.config.annotations.Header
import cc.polyfrost.oneconfig.config.annotations.Info
import cc.polyfrost.oneconfig.config.data.InfoType
import yqloss.yqlossclientmixinkt.impl.option.INFO_DISCLAIMER
import yqloss.yqlossclientmixinkt.impl.option.LEGITIMACY_LEGIT
import yqloss.yqlossclientmixinkt.impl.option.OptionsImpl
import yqloss.yqlossclientmixinkt.module.rawinput.INFO_RAW_INPUT
import yqloss.yqlossclientmixinkt.module.rawinput.RawInputOptions

class RawInputOptionsImpl :
    OptionsImpl(INFO_RAW_INPUT),
    RawInputOptions {
    @JvmField
    @Info(
        text = INFO_DISCLAIMER,
        type = InfoType.WARNING,
        size = 2,
    )
    var infoDisclaimer = false

    @JvmField
    @Info(
        text = LEGITIMACY_LEGIT,
        type = InfoType.INFO,
        size = 2,
    )
    var infoLegitimacy = false

    @JvmField
    @Header(
        text = "Raw Input",
        size = 2,
    )
    var headerModule = false
}
