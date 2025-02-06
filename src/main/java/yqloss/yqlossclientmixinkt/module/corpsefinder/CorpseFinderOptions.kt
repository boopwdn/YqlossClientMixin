package yqloss.yqlossclientmixinkt.module.corpsefinder

import yqloss.yqlossclientmixinkt.module.option.YCColor
import yqloss.yqlossclientmixinkt.module.option.YCModuleOptions

interface CorpseFinderOptions : YCModuleOptions {
    val showExit: Boolean
    val exitColor: YCColor
    val lapis: CorpseOption
    val umber: CorpseOption
    val tungsten: CorpseOption
    val vanguard: CorpseOption
}
