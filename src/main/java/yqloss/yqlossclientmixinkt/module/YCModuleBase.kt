package yqloss.yqlossclientmixinkt.module

import yqloss.yqlossclientmixinkt.event.RegistryEventDispatcher
import yqloss.yqlossclientmixinkt.event.YCEventRegistration
import yqloss.yqlossclientmixinkt.module.option.YCModuleOptions
import yqloss.yqlossclientmixinkt.ycLogger

abstract class YCModuleBase<T : YCModuleOptions>(
    moduleInfo: YCModule<T>,
) : YCModule<T> by moduleInfo,
    YCEventRegistration {
    val logger = ycLogger(moduleInfo.name)

    override val eventEntries = buildRegisterEventEntries { registerEvents(this) }

    protected abstract fun registerEvents(registry: RegistryEventDispatcher)
}
