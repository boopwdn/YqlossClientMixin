package yqloss.yqlossclientmixinkt.module.option

import yqloss.yqlossclientmixinkt.YC
import yqloss.yqlossclientmixinkt.api.YCTemplate
import yqloss.yqlossclientmixinkt.util.MC

interface YCTitleOption {
    val enabled: Boolean
    val setTime: Boolean
    val fadeIn: Int
    val stay: Int
    val fadeOut: Int
    val text: String
    val setSubtitle: Boolean
    val subtitle: String
}

inline operator fun YCTitleOption.invoke(placeholder: YCTemplate.() -> Unit) {
    if (enabled && MC.theWorld !== null) {
        if (setTime) {
            MC.ingameGUI.displayTitle(null, null, fadeIn, stay, fadeOut)
        }
        MC.ingameGUI.displayTitle(
            YC.api
                .templateProvider(text)
                .also(placeholder)
                .format(),
            null,
            0,
            0,
            0,
        )
        if (setSubtitle) {
            MC.ingameGUI.displayTitle(
                null,
                YC.api
                    .templateProvider(subtitle)
                    .also(placeholder)
                    .format(),
                0,
                0,
                0,
            )
        }
    }
}
