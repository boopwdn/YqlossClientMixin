package yqloss.yqlossclientmixinkt.module.tweaks

import yqloss.yqlossclientmixinkt.module.option.YCModuleOptions

interface TweaksOptions : YCModuleOptions {
    val enableInstantAim: Boolean
    val disablePearlClickBlock: Boolean
}
