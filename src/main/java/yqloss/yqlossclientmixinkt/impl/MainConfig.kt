package yqloss.yqlossclientmixinkt.impl

import cc.polyfrost.oneconfig.config.Config
import cc.polyfrost.oneconfig.config.annotations.SubConfig
import cc.polyfrost.oneconfig.config.data.Mod
import cc.polyfrost.oneconfig.config.data.ModType
import yqloss.yqlossclientmixinkt.impl.option.RawInputOptionsImpl
import yqloss.yqlossclientmixinkt.impl.option.SSMotionBlurOptionsImpl
import yqloss.yqlossclientmixinkt.impl.option.TweaksOptionsImpl
import yqloss.yqlossclientmixinkt.module.YCModuleOptions
import yqloss.yqlossclientmixinkt.util.inBox
import yqloss.yqlossclientmixinkt.ycLogger
import kotlin.reflect.KClass
import kotlin.reflect.full.allSuperclasses
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.full.starProjectedType

private val optionsImplMap = mutableMapOf<KClass<*>, () -> YCModuleOptions>()

private val LOGGER = ycLogger("Config")

object MainConfig : Config(Mod("Yqloss Client $MOD_VERSION", ModType.THIRD_PARTY), "yqlossclient.json") {
    @JvmField
    @SubConfig
    var rawInput = RawInputOptionsImpl()

    @JvmField
    @SubConfig
    var ssMotionBlur = SSMotionBlurOptionsImpl()

    @JvmField
    @SubConfig
    var tweaks = TweaksOptionsImpl()

    init {
        initialize()

        MainConfig::class
            .declaredMemberProperties
            .filter { it.returnType.isSubtypeOf(YCModuleOptions::class.starProjectedType) }
            .forEach { property ->
                property.call()?.let { instance ->
                    LOGGER.info("detected Options implementation $instance")
                    (instance as Config).initialize()
                    instance::class.allSuperclasses.plus(instance::class).forEach {
                        optionsImplMap[it] = { property.call() as YCModuleOptions }
                    }
                }
            }
    }

    override fun load() {
        super.load()
        val version = ++YCMixin.configVersion
        LOGGER.info("increased config version to $version")
    }

    fun <T : YCModuleOptions> getOptionsImpl(type: KClass<T>): T {
        return optionsImplMap[type]?.invoke()?.inBox?.cast<T>()?.also {
            LOGGER.info("loaded Options implementation $it for type $type")
        } ?: throw NotImplementedError("$type")
    }
}
