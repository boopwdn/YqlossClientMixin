package yqloss.yqlossclientmixinkt.impl.option.notification

import cc.polyfrost.oneconfig.config.annotations.Checkbox
import cc.polyfrost.oneconfig.config.annotations.Switch
import cc.polyfrost.oneconfig.config.annotations.Text
import yqloss.yqlossclientmixinkt.module.option.YCActionBarOption

class ActionBarOption : YCActionBarOption {
    @Switch(
        name = "Enable Action Bar Notification",
        size = 2,
    )
    var enabledOption = false

    @Text(
        name = "Action Bar Text",
        size = 1,
    )
    var textOption = ""

    @Checkbox(
        name = "Chroma Action Bar",
        size = 1,
    )
    var chromaOption = false

    override val enabled by ::enabledOption
    override val text by ::textOption
    override val chroma by ::chromaOption
}
