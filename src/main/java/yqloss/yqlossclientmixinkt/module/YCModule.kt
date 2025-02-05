package yqloss.yqlossclientmixinkt.module

import yqloss.yqlossclientmixinkt.YC
import yqloss.yqlossclientmixinkt.event.RegistrationEventDispatcher
import yqloss.yqlossclientmixinkt.event.YCEventRegistration
import yqloss.yqlossclientmixinkt.event.YCEventRegistration.Entry
import yqloss.yqlossclientmixinkt.event.registerEventEntries
import yqloss.yqlossclientmixinkt.module.option.YCModuleOptions
import yqloss.yqlossclientmixinkt.util.property.versionedLazy

interface YCModule<T : YCModuleOptions> {
    val id: String
    val name: String
    val options: T
}

abstract class YCModuleInfo<T : YCModuleOptions> : YCModule<T> {
    inline val configFile get() = "yqlossclient-$id.json"
}

inline fun <reified T : YCModuleOptions> moduleInfo(
    id: String,
    name: String,
): YCModuleInfo<T> {
    return object : YCModuleInfo<T>() {
        override val id = id
        override val name = name
        override val options by versionedLazy(YC::configVersion) { YC.getOptionsImpl(T::class) }
    }
}

fun <T> T.buildRegisterEventEntries(
    function: RegistrationEventDispatcher.() -> Unit,
): List<Entry> where T : YCEventRegistration, T : YCModule<*> {
    return mutableListOf<Entry>()
        .also { function(RegistrationEventDispatcher(it)) }
        .also { it.registerEventEntries(YC.eventDispatcher) }
}
