package yqloss.yqlossclientmixinkt.module.option

import yqloss.yqlossclientmixinkt.YC
import yqloss.yqlossclientmixinkt.api.YCTemplate
import yqloss.yqlossclientmixinkt.util.MC
import yqloss.yqlossclientmixinkt.util.printChat

interface YCPrintChatOption {
    val enabled: Boolean
    val text: String
}

inline operator fun YCPrintChatOption.invoke(placeholder: YCTemplate.() -> Unit) {
    if (enabled && MC.theWorld != null) {
        printChat(
            YC.api
                .templateProvider(text)
                .also(placeholder)
                .format(),
        )
    }
}
