package yqloss.yqlossclientmixinkt.module.tweaks

import net.minecraft.client.entity.EntityPlayerSP
import net.minecraft.init.Items
import yqloss.yqlossclientmixinkt.event.RegistrationEventDispatcher
import yqloss.yqlossclientmixinkt.event.register
import yqloss.yqlossclientmixinkt.module.YCModuleBase
import yqloss.yqlossclientmixinkt.module.ensureEnabled
import yqloss.yqlossclientmixinkt.module.ensureNotCanceled
import yqloss.yqlossclientmixinkt.module.moduleInfo
import yqloss.yqlossclientmixinkt.util.MC
import yqloss.yqlossclientmixinkt.util.scope.longrun
import yqloss.yqlossclientmixinkt.util.scope.noexcept

val INFO_TWEAKS = moduleInfo<TweaksOptions>("tweaks", "Tweaks")

object Tweaks : YCModuleBase<TweaksOptions>(INFO_TWEAKS) {
    override fun RegistrationEventDispatcher.registerEvents() {
        register<TweaksEvent.SetAnglesPost> { event ->
            longrun {
                ensureEnabled { enableInstantAim }

                noexcept(logger::catching) {
                    if (event.entity !is EntityPlayerSP) return@longrun

                    event.entity.prevRotationYawHead = event.entity.prevRotationYaw
                    event.entity.rotationYawHead = event.entity.rotationYaw
                }
            }
        }

        register<TweaksEvent.RightClickBlockPre> { event ->
            longrun {
                ensureNotCanceled(event)
                ensureEnabled { disablePearlClickBlock }

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
}
