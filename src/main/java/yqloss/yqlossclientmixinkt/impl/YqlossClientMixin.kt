package yqloss.yqlossclientmixinkt.impl

import yqloss.yqlossclientmixinkt.YC_LOGGER
import yqloss.yqlossclientmixinkt.YqlossClient
import yqloss.yqlossclientmixinkt.impl.api.YCAPIImpl
import yqloss.yqlossclientmixinkt.impl.event.EventDispatcherImpl
import yqloss.yqlossclientmixinkt.impl.option.MainConfig
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

val InitYqlossClientMixin by lazy {
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
        MainConfig
    }

    override val modID = MOD_ID
    override val modName = MOD_NAME
    override val modVersion = MOD_VERSION
    override val workingDirectory = "."

    override val api = YCAPIImpl()
    override val eventDispatcher = EventDispatcherImpl()

    override var configVersion = 0

    override val rawInput = RawInput()
    override val ssMotionBlur = SSMotionBlur()
    override val tweaks = Tweaks()

    override fun <T : YCModuleOptions> getOptionsImpl(type: KClass<T>) = MainConfig.getOptionsImpl(type)
}
