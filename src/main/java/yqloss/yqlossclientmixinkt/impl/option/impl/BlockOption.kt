package yqloss.yqlossclientmixinkt.impl.option.impl

import cc.polyfrost.oneconfig.config.annotations.Number
import cc.polyfrost.oneconfig.config.annotations.Text
import yqloss.yqlossclientmixinkt.module.option.YCBlockOption

class BlockOption : YCBlockOption {
    @Text(
        name = "Block ID",
        size = 1,
    )
    var idOption = "minecraft:air"

    @Number(
        name = "Block Metadata",
        min = 0.0F,
        max = 65535.0F,
        step = 1,
        size = 1,
    )
    var metaOption = 0

    override val id by ::idOption
    override val meta by ::metaOption
}
