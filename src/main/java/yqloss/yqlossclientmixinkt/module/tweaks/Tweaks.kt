package yqloss.yqlossclientmixinkt.module.tweaks

import net.minecraft.client.entity.EntityPlayerSP
import net.minecraft.init.Items
import yqloss.yqlossclientmixinkt.event.RegistrationEventDispatcher
import yqloss.yqlossclientmixinkt.event.register
import yqloss.yqlossclientmixinkt.module.YCModuleBase
import yqloss.yqlossclientmixinkt.module.moduleInfo
import yqloss.yqlossclientmixinkt.util.MC
import yqloss.yqlossclientmixinkt.util.scope.noexcept

val INFO_TWEAKS = moduleInfo<TweaksOptions>("tweaks", "Tweaks")

object Tweaks : YCModuleBase<TweaksOptions>(INFO_TWEAKS) {
    override fun RegistrationEventDispatcher.registerEvents() {
        register<TweaksEvent.SetAnglesPost> { event ->
            if (!options.enabled || !options.enableInstantAim) return@register

            noexcept(logger::catching) {
                val entity = event.entity
                if (entity !is EntityPlayerSP) return@register

                entity.prevRotationYawHead = entity.prevRotationYaw
                entity.rotationYawHead = entity.rotationYaw
            }
        }

        register<TweaksEvent.RightClickBlockPre> { event ->
            if (event.canceled || !options.enabled || !options.disablePearlClickBlock) return@register

            noexcept(logger::catching) {
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
