package yqloss.yqlossclientmixinkt.impl.hypixel

import net.hypixel.modapi.HypixelModAPI
import net.hypixel.modapi.packet.EventPacket
import net.hypixel.modapi.packet.impl.clientbound.event.ClientboundLocationPacket
import yqloss.yqlossclientmixinkt.YC
import yqloss.yqlossclientmixinkt.api.YCHypixelLocation
import yqloss.yqlossclientmixinkt.api.YCHypixelServerType
import yqloss.yqlossclientmixinkt.event.RegistrationEventDispatcher
import yqloss.yqlossclientmixinkt.event.hypixel.YCHypixelAPIEvent
import yqloss.yqlossclientmixinkt.event.minecraft.YCMinecraftEvent
import yqloss.yqlossclientmixinkt.event.register
import yqloss.yqlossclientmixinkt.impl.YCMixin
import yqloss.yqlossclientmixinkt.module.YCModuleBase
import yqloss.yqlossclientmixinkt.module.moduleInfo
import yqloss.yqlossclientmixinkt.module.option.YCModuleOptions
import kotlin.jvm.optionals.getOrNull

object HypixelModAPIWrapper :
    YCModuleBase<YCModuleOptions>(moduleInfo("hypixel_mod_api_wrapper", "Hypixel Mod API Wrapper")) {
    private val api by lazy { HypixelModAPI.getInstance() }

    private inline fun <reified T : EventPacket> registerPacket(noinline handler: (T) -> Unit) {
        api.subscribeToEventPacket(T::class.java)
        api.createHandler(T::class.java, handler)
    }

    override fun RegistrationEventDispatcher.registerEvents() {
        register<YCMinecraftEvent.Load.Post> {
            registerPacket<ClientboundLocationPacket> { packet ->
                val location =
                    YCHypixelLocation(
                        packet.serverName,
                        packet.serverType.getOrNull()?.let { YCHypixelServerType(it.name) },
                        packet.lobbyName.getOrNull(),
                        packet.mode.getOrNull(),
                        packet.map.getOrNull(),
                    )
                YCMixin.api.hypixelLocation = location
                YC.eventDispatcher(YCHypixelAPIEvent.Location(location))
            }
        }

        register<YCMinecraftEvent.LoadWorld.Pre> {
            YCMixin.api.hypixelLocation = null
            YC.eventDispatcher(YCHypixelAPIEvent.Location(null))
        }
    }
}
