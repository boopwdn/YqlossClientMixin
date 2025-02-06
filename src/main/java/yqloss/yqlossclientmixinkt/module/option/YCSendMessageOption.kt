package yqloss.yqlossclientmixinkt.module.option

import yqloss.yqlossclientmixinkt.YC
import yqloss.yqlossclientmixinkt.api.YCTemplate
import yqloss.yqlossclientmixinkt.event.RegistrationEventDispatcher
import yqloss.yqlossclientmixinkt.event.minecraft.YCMinecraftEvent
import yqloss.yqlossclientmixinkt.event.register
import yqloss.yqlossclientmixinkt.module.YCModuleBase
import yqloss.yqlossclientmixinkt.module.moduleInfo
import yqloss.yqlossclientmixinkt.util.MC
import yqloss.yqlossclientmixinkt.util.general.MutableBox
import yqloss.yqlossclientmixinkt.util.general.inMutableBox

interface YCSendMessageOption {
    val enabled: Boolean
    val enableInterval: Boolean
    val intervalPool: String
    val interval: Int
    val maxPoolSize: Int
    val text: String
}

object SendMessagePool :
    YCModuleBase<YCModuleOptions>(moduleInfo("send_message_pool", "Send Message Pool")) {
    val poolMap = mutableMapOf<String, ArrayDeque<Pair<String, MutableBox<Int>>>>()

    fun add(
        pool: String,
        interval: Int,
        message: String,
        max: Int,
    ) {
        synchronized(this) {
            if (poolMap.size < max) {
                poolMap.getOrPut(pool) { ArrayDeque() }.addLast(message to (-interval).inMutableBox)
            }
        }
    }

    fun clear(pool: String) {
        synchronized(this) {
            poolMap[pool]?.clear()
        }
    }

    override fun RegistrationEventDispatcher.registerEvents() {
        register<YCMinecraftEvent.LoadWorld.Pre> {
            synchronized(this) {
                poolMap.clear()
            }
        }

        register<YCMinecraftEvent.Tick.Pre> {
            synchronized(this) {
                poolMap.forEach { (_, list) ->
                    while (true) {
                        list.firstOrNull()?.let { (message, interval) ->
                            if (interval.value == 0) {
                                MC.thePlayer.sendChatMessage(message)
                                return@let
                            } else if (interval.value < 0) {
                                interval.value = -interval.value
                                MC.thePlayer.sendChatMessage(message)
                            }
                            --interval.value
                            if (interval.value == 0) {
                                list.removeFirstOrNull()
                            }
                            return@forEach
                        } ?: break
                    }
                }
            }
        }
    }
}

inline operator fun YCSendMessageOption.invoke(placeholder: YCTemplate.() -> Unit) {
    if (enabled && MC.theWorld != null) {
        val messages =
            YC.api
                .templateProvider(text)
                .also(placeholder)
                .format()
                .split("\n")
        if (enableInterval) {
            messages.forEach { SendMessagePool.add(intervalPool, interval, it, maxPoolSize) }
        } else {
            messages.forEach(MC.thePlayer::sendChatMessage)
        }
    }
}
