package yqloss.yqlossclientmixinkt.impl.option.impl

import cc.polyfrost.oneconfig.config.annotations.Switch
import yqloss.yqlossclientmixinkt.impl.option.adapter.Extract
import yqloss.yqlossclientmixinkt.module.option.YCNotificationOption

class NotificationOption : YCNotificationOption {
    @Switch(
        name = "Enable Notification",
        size = 2,
    )
    var enabledOption = false

    @Extract
    var logOption = LogOption()

    @Extract
    var printChatOption = PrintChatOption()

    @Extract
    var titleOption = TitleOption()

    @Extract
    var actionBarOption = ActionBarOption()

    @Extract
    var soundOption = SoundOption()

    @Extract
    var sendMessageOption = SendMessageOption()

    override val enabled by ::enabledOption
    override val log by ::logOption
    override val printChat by ::printChatOption
    override val title by ::titleOption
    override val actionBar by ::actionBarOption
    override val sound by ::soundOption
    override val sendMessage by ::sendMessageOption
}
