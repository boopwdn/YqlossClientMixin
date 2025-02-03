package yqloss.yqlossclientmixinkt.impl.option

import cc.polyfrost.oneconfig.config.annotations.Header
import cc.polyfrost.oneconfig.config.annotations.Switch
import yqloss.yqlossclientmixinkt.module.tweaks.INFO_TWEAKS
import yqloss.yqlossclientmixinkt.module.tweaks.TweaksOptions

class TweaksOptionsImpl :
    OptionsImpl(INFO_TWEAKS),
    TweaksOptions {
    @JvmField
    @Header(
        text = "Tweaks",
        size = 2,
    )
    var headerModule = false

    @Switch(
        name = "Enable Instant Aim",
        description = "Set prevRotationYawHead and rotationYawHead of EntityPlayerSP to prevRotationYaw and rotationYaw.",
        size = 2,
    )
    var enableInstantAimOption = false

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
