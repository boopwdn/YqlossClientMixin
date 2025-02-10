package yqloss.yqlossclientmixinkt.impl.hypixel

import net.hypixel.modapi.HypixelModAPI
import net.hypixel.modapi.packet.EventPacket
import net.hypixel.modapi.packet.impl.clientbound.event.ClientboundLocationPacket
import yqloss.yqlossclientmixinkt.YC
import yqloss.yqlossclientmixinkt.api.YCHypixelLocation
import yqloss.yqlossclientmixinkt.api.YCHypixelServerType
import yqloss.yqlossclientmixinkt.event.RegistryEventDispatcher
import yqloss.yqlossclientmixinkt.event.hypixel.YCHypixelAPIEvent
import yqloss.yqlossclientmixinkt.event.minecraft.YCMinecraftEvent
import yqloss.yqlossclientmixinkt.event.register
import yqloss.yqlossclientmixinkt.impl.YCMixin
import yqloss.yqlossclientmixinkt.module.NO_MODULE_INFO
import yqloss.yqlossclientmixinkt.module.YCModuleBase
import yqloss.yqlossclientmixinkt.module.option.YCModuleOptions
import kotlin.jvm.optionals.getOrNull

class HypixelModAPIWrapper(
    private val api: HypixelModAPI,
) : YCModuleBase<YCModuleOptions>(NO_MODULE_INFO) {
    private inline fun <reified T : EventPacket> registerPacket(noinline handler: (T) -> Unit) {
        api.subscribeToEventPacket(T::class.java)
        api.createHandler(T::class.java, handler)
    }

    init {
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

    override fun registerEvents(registry: RegistryEventDispatcher) {
        registry.run {
            register<YCMinecraftEvent.LoadWorld.Pre> {
                YCMixin.api.hypixelLocation = null
                YC.eventDispatcher(YCHypixelAPIEvent.Location(null))
            }
        }
    }
}
