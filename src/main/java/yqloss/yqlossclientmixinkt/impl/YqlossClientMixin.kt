package yqloss.yqlossclientmixinkt.impl

import yqloss.yqlossclientmixinkt.YC_LOGGER
import yqloss.yqlossclientmixinkt.YqlossClient
import yqloss.yqlossclientmixinkt.event.minecraft.YCMinecraftEvent
import yqloss.yqlossclientmixinkt.event.register
import yqloss.yqlossclientmixinkt.impl.api.YCAPIImpl
import yqloss.yqlossclientmixinkt.impl.event.EventDispatcherImpl
import yqloss.yqlossclientmixinkt.impl.gui.hud.MiningPredictionHUD
import yqloss.yqlossclientmixinkt.impl.hypixel.loadHypixelModAPI
import yqloss.yqlossclientmixinkt.impl.option.YqlossClientConfig
import yqloss.yqlossclientmixinkt.module.corpsefinder.CorpseFinder
import yqloss.yqlossclientmixinkt.module.miningprediction.MiningPrediction
import yqloss.yqlossclientmixinkt.module.option.YCModuleOptions
import yqloss.yqlossclientmixinkt.module.rawinput.RawInput
import yqloss.yqlossclientmixinkt.module.ssmotionblur.SSMotionBlur
import yqloss.yqlossclientmixinkt.module.tweaks.Tweaks
import yqloss.yqlossclientmixinkt.theYC
import yqloss.yqlossclientmixinkt.util.property.latelet
import kotlin.reflect.KClass

const val MOD_ID = "@ID@"
const val MOD_NAME = "@NAME@"
const val MOD_VERSION = "@VER@"

val initYqlossClientMixin by lazy {
    YC_LOGGER.info("creating YqlossClientMixin instance")
    YqlossClientMixin()
    YC_LOGGER.info("created YqlossClientMixin instance")
}

var theYCMixin: YqlossClientMixin by latelet()

val YCMixin by ::theYCMixin

class YqlossClientMixin : YqlossClient {
    init {
        theYC = this
        theYCMixin = this
    }

    override val modID = MOD_ID
    override val modName = MOD_NAME
    override val modVersion = MOD_VERSION
    override val workingDirectory = "."

    override val api = YCAPIImpl()
    override val eventDispatcher = EventDispatcherImpl()

    override var configVersion = 0

    override fun <T : YCModuleOptions> getOptionsImpl(type: KClass<T>) = YqlossClientConfig.getOptionsImpl(type)

    init {
        YqlossClientConfig

        RawInput
        SSMotionBlur
        Tweaks
        CorpseFinder
        MiningPrediction

        MiningPredictionHUD

        eventDispatcher.register<YCMinecraftEvent.Load.Post> {
            loadHypixelModAPI
        }
    }
}
