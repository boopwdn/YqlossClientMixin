package yqloss.yqlossclientmixinkt.impl.option

import cc.polyfrost.oneconfig.config.elements.SubConfig
import yqloss.yqlossclientmixinkt.impl.MainConfig
import yqloss.yqlossclientmixinkt.module.YCModuleInfo
import yqloss.yqlossclientmixinkt.module.YCModuleOptions

abstract class OptionsImpl(
    info: YCModuleInfo<*>,
) : SubConfig(info.name, info.configFile),
    YCModuleOptions {
    init {
        super.enabled = false
    }

    override val enabled get() = MainConfig.enabled && super.enabled
}
