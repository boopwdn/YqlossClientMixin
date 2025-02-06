package yqloss.yqlossclientmixinkt.module.option

import yqloss.yqlossclientmixinkt.YC
import yqloss.yqlossclientmixinkt.api.YCTemplate
import yqloss.yqlossclientmixinkt.util.MC

interface YCActionBarOption {
    val enabled: Boolean
    val chroma: Boolean
    val text: String
}

inline operator fun YCActionBarOption.invoke(placeholder: YCTemplate.() -> Unit) {
    if (enabled && MC.theWorld !== null) {
        MC.ingameGUI.setRecordPlaying(
            YC.api
                .templateProvider(text)
                .also(placeholder)
                .format(),
            chroma,
        )
    }
}
