package yqloss.yqlossclientmixinkt.impl.api

import yqloss.yqlossclientmixinkt.api.YCAPI
import yqloss.yqlossclientmixinkt.api.YCHypixelLocation
import yqloss.yqlossclientmixinkt.ycLogger

private val logger = ycLogger("API")

class YCAPIImpl : YCAPI {
    override var hypixelLocation: YCHypixelLocation? = null
    override val templateProvider = ::YCTemplateImpl
}
