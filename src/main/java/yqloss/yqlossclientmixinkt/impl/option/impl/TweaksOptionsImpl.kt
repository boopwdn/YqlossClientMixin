package yqloss.yqlossclientmixinkt.impl.option.impl

import cc.polyfrost.oneconfig.config.annotations.Header
import cc.polyfrost.oneconfig.config.annotations.Info
import cc.polyfrost.oneconfig.config.annotations.Switch
import cc.polyfrost.oneconfig.config.data.InfoType
import yqloss.yqlossclientmixinkt.impl.option.INFO_DISCLAIMER
import yqloss.yqlossclientmixinkt.impl.option.LEGITIMACY_SAFE_BLATANT_SKYBLOCK
import yqloss.yqlossclientmixinkt.impl.option.LEGITIMACY_SAFE_QOL
import yqloss.yqlossclientmixinkt.impl.option.OptionsImpl
import yqloss.yqlossclientmixinkt.module.tweaks.INFO_TWEAKS
import yqloss.yqlossclientmixinkt.module.tweaks.TweaksOptions

class TweaksOptionsImpl :
    OptionsImpl(INFO_TWEAKS),
    TweaksOptions {
    @JvmField
    @Info(
        text = INFO_DISCLAIMER,
        type = InfoType.WARNING,
        size = 2,
    )
    var infoDisclaimer = false

    @JvmField
    @Header(
        text = "Tweaks",
        size = 2,
    )
    var headerModule = false

    @JvmField
    @Info(
        text = LEGITIMACY_SAFE_QOL,
        type = InfoType.INFO,
        size = 2,
    )
    var infoLegitimacy1 = false

    @Switch(
        name = "Enable Instant Aim",
        description = "Set prevRotationYawHead and rotationYawHead of EntityPlayerSP to prevRotationYaw and rotationYaw.",
        size = 2,
    )
    var enableInstantAimOption = false

    @JvmField
    @Info(
        text = LEGITIMACY_SAFE_BLATANT_SKYBLOCK,
        type = InfoType.INFO,
        size = 2,
    )
    var infoLegitimacy2 = false

    @Switch(
        name = "Disable Pearl Click-On-Block Packet",
        description =
            "Cancel the first C08PacketPlayerBlockPlacement packet when throwing an ender pearl while aiming at a block. " +
                "This fixes not being able to throw pearls while aiming at a block on public islands in Hypixel SkyBlock.",
        size = 2,
    )
    var disablePearlClickBlockOption = false

    override val enableInstantAim by ::enableInstantAimOption
    override val disablePearlClickBlock by ::disablePearlClickBlockOption
}
