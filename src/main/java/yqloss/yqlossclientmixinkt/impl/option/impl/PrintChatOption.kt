package yqloss.yqlossclientmixinkt.impl.option.impl

import cc.polyfrost.oneconfig.config.annotations.Switch
import cc.polyfrost.oneconfig.config.annotations.Text
import yqloss.yqlossclientmixinkt.module.option.YCPrintChatOption

class PrintChatOption : YCPrintChatOption {
    @Switch(
        name = "Enable Print Chat Message Notification",
        size = 1,
    )
    var enabledOption = false

    @Text(
        name = "Chat Message",
        size = 1,
    )
    var textOption = ""

    override val enabled by ::enabledOption
    override val text by ::textOption
}
