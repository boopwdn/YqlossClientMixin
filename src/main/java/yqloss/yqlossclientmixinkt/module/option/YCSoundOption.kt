package yqloss.yqlossclientmixinkt.module.option

import net.minecraft.util.ResourceLocation
import yqloss.yqlossclientmixinkt.YC
import yqloss.yqlossclientmixinkt.api.YCTemplate
import yqloss.yqlossclientmixinkt.util.CustomSound
import yqloss.yqlossclientmixinkt.util.MC

interface YCSoundOption {
    val enabled: Boolean
    val name: String
    val volume: Double
    val pitch: Double
}

inline operator fun YCSoundOption.invoke(placeholder: YCTemplate.() -> Unit) {
    if (enabled && MC.theWorld != null) {
        MC.soundHandler.playSound(
            CustomSound(
                ResourceLocation(
                    YC.api
                        .templateProvider(name)
                        .also(placeholder)
                        .format(),
                ),
                volume,
                pitch,
            ),
        )
    }
}
