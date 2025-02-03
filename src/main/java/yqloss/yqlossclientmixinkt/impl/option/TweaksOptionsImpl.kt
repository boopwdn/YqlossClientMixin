package yqloss.yqlossclientmixinkt.impl.option

import cc.polyfrost.oneconfig.config.Config
import cc.polyfrost.oneconfig.config.annotations.Header
import cc.polyfrost.oneconfig.config.annotations.Switch
import cc.polyfrost.oneconfig.config.data.ModType
import yqloss.yqlossclientmixinkt.YC
import yqloss.yqlossclientmixinkt.impl.getConfigFileName
import yqloss.yqlossclientmixinkt.impl.getConfigMod
import yqloss.yqlossclientmixinkt.module.tweaks.TweaksOptions

class TweaksOptionsImpl :
    Config(getConfigMod(YC.tweaks, ModType.THIRD_PARTY), getConfigFileName(YC.tweaks)),
    TweaksOptions {
    override val enabled by Config::enabled

    init {
        super.enabled = false
    }

    @JvmField
    @Header(
        text = "Tweaks",
        size = 2,
    )
    var headerModule = false

    @Switch(
        name = "Enable Instant Aim",
        description = "Set prevRotationYawHead and rotationYawHead of EntityPlayerSP to prevRotationYaw and rotationYaw",
        size = 2,
    )
    override val enableInstantAim = false
}
