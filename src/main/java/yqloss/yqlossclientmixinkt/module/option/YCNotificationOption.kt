package yqloss.yqlossclientmixinkt.module.option

import org.apache.logging.log4j.Logger
import yqloss.yqlossclientmixinkt.api.YCTemplate

interface YCNotificationOption {
    val enabled: Boolean
    val log: YCLogOption
    val printChat: YCPrintChatOption
    val title: YCTitleOption
    val actionBar: YCActionBarOption
    val sound: YCSoundOption
    val sendMessage: YCSendMessageOption
}

inline operator fun YCNotificationOption.invoke(
    logger: Logger,
    placeholder: YCTemplate.() -> Unit,
) {
    if (enabled) {
        log(logger, placeholder)
        printChat(placeholder)
        title(placeholder)
        actionBar(placeholder)
        sound(placeholder)
        sendMessage(placeholder)
    }
}
