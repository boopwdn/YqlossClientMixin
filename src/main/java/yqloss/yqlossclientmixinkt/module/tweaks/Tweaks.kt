package yqloss.yqlossclientmixinkt.module.tweaks

import net.minecraft.client.entity.EntityPlayerSP
import yqloss.yqlossclientmixinkt.YqlossClient
import yqloss.yqlossclientmixinkt.event.YCEventRegistration
import yqloss.yqlossclientmixinkt.event.buildEventEntries
import yqloss.yqlossclientmixinkt.event.register
import yqloss.yqlossclientmixinkt.event.registerEventEntries
import yqloss.yqlossclientmixinkt.module.YCModule
import yqloss.yqlossclientmixinkt.util.noexcept
import yqloss.yqlossclientmixinkt.ycLogger

private val LOGGER = ycLogger("Tweaks")

class Tweaks(
    private val yc: YqlossClient,
) : YCModule<TweaksOptions>,
    YCEventRegistration {
    override val id = "tweaks"
    override val name = "Tweaks"
    override val options by lazy { yc.getOptionsImpl(TweaksOptions::class) }

    override val eventEntries =
        buildEventEntries {
            register<TweaksEvent.SetAnglesPost> { onEvent(it) }
        }

    init {
        registerEventEntries(yc.eventDispatcher)
    }

    private fun onEvent(event: TweaksEvent.SetAnglesPost) {
        if (!options.enabled || !options.enableInstantAim) return

        noexcept(LOGGER::catching) {
            val entity = event.entity
            if (entity !is EntityPlayerSP) return

            entity.prevRotationYawHead = entity.prevRotationYaw
            entity.rotationYawHead = entity.rotationYaw
        }
    }
}
