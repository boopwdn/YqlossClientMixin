/*
 * Copyright (C) 2025 Yqloss
 *
 * This file is part of Yqloss Client (Mixin).
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2 (GPLv2)
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Yqloss Client (Mixin). If not, see <https://www.gnu.org/licenses/old-licenses/gpl-2.0.html>.
 */

package yqloss.yqlossclientmixinkt.module.option

import yqloss.yqlossclientmixinkt.YC
import yqloss.yqlossclientmixinkt.api.YCTemplate
import yqloss.yqlossclientmixinkt.api.format
import yqloss.yqlossclientmixinkt.event.RegistryEventDispatcher
import yqloss.yqlossclientmixinkt.event.minecraft.YCMinecraftEvent
import yqloss.yqlossclientmixinkt.event.register
import yqloss.yqlossclientmixinkt.module.YCModuleBase
import yqloss.yqlossclientmixinkt.module.moduleInfo
import yqloss.yqlossclientmixinkt.util.MC
import yqloss.yqlossclientmixinkt.util.general.Ref
import yqloss.yqlossclientmixinkt.util.general.inRef

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
    val poolMap = mutableMapOf<String, ArrayDeque<Pair<String, Ref<Int>>>>()

    fun add(
        pool: String,
        interval: Int,
        message: String,
        max: Int,
    ) {
        synchronized(this) {
            if (poolMap.size < max) {
                poolMap.getOrPut(pool) { ArrayDeque() }.addLast(message to (-interval).inRef)
            }
        }
    }

    fun clear(pool: String) {
        synchronized(this) {
            poolMap[pool]?.clear()
        }
    }

    override fun registerEvents(registry: RegistryEventDispatcher) {
        registry.run {
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
}

inline operator fun YCSendMessageOption.invoke(placeholder: YCTemplate.() -> Unit) {
    if (enabled && MC.theWorld != null) {
        val messages = YC.api.format(text, placeholder).split("\n")
        if (enableInterval) {
            messages.forEach { SendMessagePool.add(intervalPool, interval, it, maxPoolSize) }
        } else {
            messages.forEach(MC.thePlayer::sendChatMessage)
        }
    }
}
