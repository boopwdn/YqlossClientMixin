package yqloss.yqlossclientmixinkt.impl.option

import cc.polyfrost.oneconfig.config.Config
import cc.polyfrost.oneconfig.config.annotations.SubConfig
import cc.polyfrost.oneconfig.config.data.Mod
import cc.polyfrost.oneconfig.config.data.ModType
import yqloss.yqlossclientmixinkt.impl.MOD_VERSION
import yqloss.yqlossclientmixinkt.impl.YCMixin
import yqloss.yqlossclientmixinkt.impl.option.module.CorpseFinderOptionsImpl
import yqloss.yqlossclientmixinkt.impl.option.module.MiningPredictionOptionsImpl
import yqloss.yqlossclientmixinkt.impl.option.module.RawInputOptionsImpl
import yqloss.yqlossclientmixinkt.impl.option.module.SSMotionBlurOptionsImpl
import yqloss.yqlossclientmixinkt.impl.option.module.TweaksOptionsImpl
import yqloss.yqlossclientmixinkt.module.option.YCModuleOptions
import yqloss.yqlossclientmixinkt.util.general.inBox
import yqloss.yqlossclientmixinkt.ycLogger
import kotlin.reflect.KClass
import kotlin.reflect.full.allSuperclasses
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.full.starProjectedType

private val optionsImplMap = mutableMapOf<KClass<*>, () -> YCModuleOptions>()

private val logger = ycLogger("Config")

object YqlossClientConfig : Config(Mod("Yqloss Client $MOD_VERSION", ModType.THIRD_PARTY), "yqlossclient.json") {
    @SubConfig
    var main = MainConfig()

    @SubConfig
    var rawInput = RawInputOptionsImpl()

    @SubConfig
    var ssMotionBlur = SSMotionBlurOptionsImpl()

    @SubConfig
    var tweaks = TweaksOptionsImpl()

    @SubConfig
    var corpseFinder = CorpseFinderOptionsImpl()

    @SubConfig
    var miningPrediction = MiningPredictionOptionsImpl()

    init {
        initialize()
        main.initialize()

        YqlossClientConfig::class
            .declaredMemberProperties
            .filter { it.returnType.isSubtypeOf(YCModuleOptions::class.starProjectedType) }
            .forEach { property ->
                property(this).let { instance ->
                    logger.info("detected Options implementation $instance")
                    (instance as Config).initialize()
                    instance::class.allSuperclasses.plus(instance::class).forEach {
                        optionsImplMap[it] = { property(this) as YCModuleOptions }
                    }
                }
            }
    }

    override fun load() {
        super.load()
        val version = ++YCMixin.configVersion
        logger.info("increased config version to $version")
    }

    fun <T : YCModuleOptions> getOptionsImpl(type: KClass<T>): T {
        return optionsImplMap[type]?.invoke()?.inBox?.cast<T>()?.also {
            logger.info("loaded Options implementation $it for type $type")
        } ?: throw NotImplementedError("$type")
    }
}
