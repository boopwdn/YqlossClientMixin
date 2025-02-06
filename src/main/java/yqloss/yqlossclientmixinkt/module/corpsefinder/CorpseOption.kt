package yqloss.yqlossclientmixinkt.module.corpsefinder

import yqloss.yqlossclientmixinkt.module.option.YCColor
import yqloss.yqlossclientmixinkt.module.option.YCNotificationOption

interface CorpseOption {
    val color: YCColor
    val notification: YCNotificationOption
}
