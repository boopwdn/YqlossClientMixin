package yqloss.yqlossclientmixinkt.module.tweaks

import net.minecraft.client.entity.EntityPlayerSP
import net.minecraft.init.Items
import yqloss.yqlossclientmixinkt.event.YCEventRegistration
import yqloss.yqlossclientmixinkt.event.register
import yqloss.yqlossclientmixinkt.module.YCModule
import yqloss.yqlossclientmixinkt.module.buildRegisterEventEntries
import yqloss.yqlossclientmixinkt.module.moduleInfo
import yqloss.yqlossclientmixinkt.util.MC
import yqloss.yqlossclientmixinkt.util.noexcept
import yqloss.yqlossclientmixinkt.ycLogger

val INFO_TWEAKS = moduleInfo<TweaksOptions>("tweaks", "Tweaks")

private val LOGGER = ycLogger(INFO_TWEAKS.name)

class Tweaks :
    YCModule<TweaksOptions> by INFO_TWEAKS,
    YCEventRegistration {
    override val eventEntries =
        buildRegisterEventEntries {
            register<TweaksEvent.SetAnglesPost> { event ->
                if (!options.enabled || !options.enableInstantAim) return@register

                noexcept(LOGGER::catching) {
                    val entity = event.entity
                    if (entity !is EntityPlayerSP) return@register

                    entity.prevRotationYawHead = entity.prevRotationYaw
                    entity.rotationYawHead = entity.rotationYaw
                }
            }

            register<TweaksEvent.RightClickBlockPre> { event ->
                if (event.canceled || !options.enabled || !options.disablePearlClickBlock) return@register

                noexcept(LOGGER::catching) {
                    if (MC.thePlayer.inventory
                            .getCurrentItem()
                            ?.item === Items.ender_pearl
                    ) {
                        event.canceled = true
                    }
                }
            }
        }
}
