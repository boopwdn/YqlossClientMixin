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

import net.minecraft.item.ItemStack
import net.minecraft.util.BlockPos
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
import yqloss.yqlossclientmixinkt.YC
import yqloss.yqlossclientmixinkt.module.miningprediction.MiningPredictionEvent
import yqloss.yqlossclientmixinkt.module.tweaks.TweaksEvent
import yqloss.yqlossclientmixinkt.util.asVec3I

object CallbackPlayerControllerMP {
    object MiningPrediction {
        fun sendClickBlockToControllerNotMining() {
            YC.eventDispatcher(MiningPredictionEvent.NotMining)
        }
    }

    object Tweaks {
        fun isHittingPositionCheck(
            pos: BlockPos?,
            currentBlock: BlockPos?,
            currentItemHittingBlock: ItemStack?,
            cir: CallbackInfoReturnable<Boolean>,
        ) {
            TweaksEvent
                .IsHittingPositionCheck(pos?.asVec3I, currentBlock?.asVec3I, currentItemHittingBlock)
                .also(YC.eventDispatcher)
                .also {
                    if (it.canceled) {
                        cir.returnValue = it.returnValue
                    }
                }
        }
    }
}
