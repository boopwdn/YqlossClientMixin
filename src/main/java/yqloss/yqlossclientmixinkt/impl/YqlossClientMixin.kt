package yqloss.yqlossclientmixinkt.impl

import cc.polyfrost.oneconfig.config.Config
import cc.polyfrost.oneconfig.config.data.Mod
import cc.polyfrost.oneconfig.config.data.ModType
import yqloss.yqlossclientmixinkt.YC_LOGGER
import yqloss.yqlossclientmixinkt.YqlossClient
import yqloss.yqlossclientmixinkt.impl.event.EventDispatcherImpl
import yqloss.yqlossclientmixinkt.impl.option.RawInputOptionsImpl
import yqloss.yqlossclientmixinkt.impl.option.SSMotionBlurOptionsImpl
import yqloss.yqlossclientmixinkt.impl.option.TweaksOptionsImpl
import yqloss.yqlossclientmixinkt.module.YCModule
import yqloss.yqlossclientmixinkt.module.YCModuleOptions
import yqloss.yqlossclientmixinkt.module.rawinput.RawInput
import yqloss.yqlossclientmixinkt.module.ssmotionblur.SSMotionBlur
import yqloss.yqlossclientmixinkt.module.tweaks.Tweaks
import yqloss.yqlossclientmixinkt.theYC
import yqloss.yqlossclientmixinkt.util.inBox
import kotlin.reflect.KClass
import kotlin.reflect.full.allSuperclasses

private const val MOD_ID = "@ID@"
private const val MOD_NAME = "@NAME@"
private const val MOD_VERSION = "@VER@"

private fun <T> MutableMap<KClass<*>, YCModuleOptions>.implOptions(instance: T)
    where T : YCModuleOptions, T : Config {
    instance.initialize()
    instance::class.allSuperclasses.plus(instance::class).forEach {
        this[it] = instance
    }
}

fun getConfigMod(
    module: YCModule<*>,
    type: ModType,
) = Mod("YC ${module.name}", type)

fun getConfigFileName(module: YCModule<*>) = "$MOD_ID-${module.id}.json"

val InitYqlossClientMixin by lazy {
    YC_LOGGER.info("creating YqlossClientMixin instance")
    YqlossClientMixin()
    YC_LOGGER.info("created YqlossClientMixin instance")
}

class YqlossClientMixin : YqlossClient {
    init {
        theYC = this
    }

    override val modID = MOD_ID
    override val modName = MOD_NAME
    override val modVersion = MOD_VERSION
    override val workingDirectory = "."

    override val api = YCAPIImpl()
    override val eventDispatcher = EventDispatcherImpl()

    override val rawInput = RawInput(this)
    override val ssMotionBlur = SSMotionBlur(this)
    override val tweaks = Tweaks(this)

    private val optionsImplMap: Map<KClass<*>, YCModuleOptions> =
        buildMap {
            implOptions(RawInputOptionsImpl())
            implOptions(SSMotionBlurOptionsImpl())
            implOptions(TweaksOptionsImpl())
        }

    override fun <T : YCModuleOptions> getOptionsImpl(type: KClass<T>): T {
        return optionsImplMap[type]?.inBox?.cast() ?: throw NotImplementedError("$type")
    }
}
