package yqloss.yqlossclientmixinkt.impl.option.impl

import cc.polyfrost.oneconfig.config.annotations.Button
import cc.polyfrost.oneconfig.config.annotations.Checkbox
import cc.polyfrost.oneconfig.config.annotations.Number
import cc.polyfrost.oneconfig.config.annotations.Switch
import cc.polyfrost.oneconfig.config.annotations.Text
import yqloss.yqlossclientmixinkt.module.option.SendMessagePool
import yqloss.yqlossclientmixinkt.module.option.YCSendMessageOption
import yqloss.yqlossclientmixinkt.util.printChat

class SendMessageOption : YCSendMessageOption {
    @Switch(
        name = "Enable Send Message Notification",
        size = 1,
    )
    var enabledOption = false

    @Text(
        name = "Message",
        size = 1,
    )
    var textOption = ""

    @Checkbox(
        name = "Enable Interval",
        size = 1,
    )
    var enableIntervalOption = false

    @Number(
        name = "Interval Since Last Message (in ticks)",
        min = 0.0F,
        max = Float.MAX_VALUE,
        step = 1,
        size = 1,
    )
    var intervalOption = 0

    @Text(
        name = "Interval Pool",
        size = 1,
    )
    var intervalPoolOption = "default"

    @Number(
        name = "Max Pool Size",
        min = 0.0F,
        max = Float.MAX_VALUE,
        step = 1,
        size = 1,
    )
    var maxPoolSizeOption = 2147483647

    override val enabled by ::enabledOption
    override val text by ::textOption
    override val enableInterval by ::enableIntervalOption
    override val interval by ::intervalOption
    override val intervalPool by ::intervalPoolOption
    override val maxPoolSize by ::maxPoolSizeOption

    @Button(
        name = "Clear this Pool",
        text = "Clear",
        size = 1,
    )
    fun clearPool() {
        SendMessagePool.clear(intervalPool)
        printChat("cleared pool $intervalPool")
    }

    @Button(
        name = "Print Pool Sizes",
        text = "Print",
        size = 1,
    )
    fun viewPoolSize() {
        printChat()
        printChat("Pool Sizes:")
        SendMessagePool.poolMap.forEach { (pool, list) ->
            if (list.isNotEmpty()) {
                printChat("    $pool: ${list.size}")
            }
        }
        printChat()
    }
}
