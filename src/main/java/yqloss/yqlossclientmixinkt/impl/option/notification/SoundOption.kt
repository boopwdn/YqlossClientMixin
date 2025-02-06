package yqloss.yqlossclientmixinkt.impl.option.notification

import cc.polyfrost.oneconfig.config.annotations.Number
import cc.polyfrost.oneconfig.config.annotations.Switch
import cc.polyfrost.oneconfig.config.annotations.Text
import yqloss.yqlossclientmixinkt.module.option.YCSoundOption
import yqloss.yqlossclientmixinkt.util.math.asDouble

class SoundOption : YCSoundOption {
    @Switch(
        name = "Enable Sound Notification",
        size = 1,
    )
    var enabledOption = false

    @Text(
        name = "Sound",
        size = 1,
    )
    var nameOption = ""

    @Number(
        name = "Volume",
        min = 0.0F,
        max = 1.0F,
        size = 1,
    )
    var volumeOption = 1.0F

    @Number(
        name = "Pitch",
        min = 0.0F,
        max = 2.0F,
        size = 1,
    )
    var pitchOption = 1.0F

    override val enabled by ::enabledOption
    override val name by ::nameOption
    override val volume get() = volumeOption.asDouble
    override val pitch get() = pitchOption.asDouble
}
