package yqloss.yqlossclientmixinkt

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import yqloss.yqlossclientmixinkt.api.YCAPI
import yqloss.yqlossclientmixinkt.event.YCEventDispatcher
import yqloss.yqlossclientmixinkt.module.YCModuleOptions
import yqloss.yqlossclientmixinkt.module.rawinput.RawInput
import yqloss.yqlossclientmixinkt.module.ssmotionblur.SSMotionBlur
import yqloss.yqlossclientmixinkt.module.tweaks.Tweaks
import yqloss.yqlossclientmixinkt.util.property.latelet
import kotlin.reflect.KClass

fun ycLogger(name: String): Logger = LogManager.getLogger("YqlossClient $name")

val YC_LOGGER: Logger = LogManager.getLogger("YqlossClient")

var theYC: YqlossClient by latelet()

val YC by ::theYC

interface YqlossClient {
    val modID: String
    val modName: String
    val modVersion: String
    val workingDirectory: String

    val api: YCAPI
    val eventDispatcher: YCEventDispatcher

    val configVersion: Int

    val ssMotionBlur: SSMotionBlur
    val rawInput: RawInput
    val tweaks: Tweaks

    fun <T : YCModuleOptions> getOptionsImpl(type: KClass<T>): T
}
