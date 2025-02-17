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

package yqloss.yqlossclientmixinkt.impl.mixincallback

import net.minecraft.client.renderer.DestroyBlockProgress
import yqloss.yqlossclientmixinkt.YC
import yqloss.yqlossclientmixinkt.event.minecraft.YCRenderEvent
import yqloss.yqlossclientmixinkt.module.miningprediction.MiningPredictionEvent

object CallbackRenderGlobal {
    object YqlossClient {
        fun renderEntitiesPost() {
            YC.eventDispatcher(YCRenderEvent.Entity.Post)
        }
    }

    object MiningPrediction {
        private var savedDamageMap: Map<Int, DestroyBlockProgress> = mapOf()

        fun drawBlockDamageTexturePre(damageMap: MutableMap<Int, DestroyBlockProgress>) {
            val damages = damageMap.toMap()
            MiningPredictionEvent
                .RenderBlockDamage(damages)
                .also(YC.eventDispatcher)
                .let { event ->
                    savedDamageMap = damageMap.toMap()
                    damageMap.clear()
                    event.mutableDamages.forEach { (i, v) -> damageMap[i] = v }
                }
        }

        fun drawBlockDamageTexturePost(damageMap: MutableMap<Int, DestroyBlockProgress>) {
            damageMap.clear()
            damageMap.putAll(savedDamageMap)
            savedDamageMap = mapOf()
        }
    }
}
