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

package yqloss.yqlossclientmixinkt.module.tweaks

import net.minecraft.client.entity.EntityPlayerSP
import net.minecraft.init.Items
import yqloss.yqlossclientmixinkt.event.RegistryEventDispatcher
import yqloss.yqlossclientmixinkt.event.register
import yqloss.yqlossclientmixinkt.module.*
import yqloss.yqlossclientmixinkt.util.MC
import yqloss.yqlossclientmixinkt.util.SKYBLOCK_MINING_TOOLS
import yqloss.yqlossclientmixinkt.util.scope.longrun
import yqloss.yqlossclientmixinkt.util.skyBlockUUID

val INFO_TWEAKS = moduleInfo<TweaksOptions>("tweaks", "Tweaks")

object Tweaks : YCModuleBase<TweaksOptions>(INFO_TWEAKS) {
    override fun registerEvents(registry: RegistryEventDispatcher) {
        registry.run {
            register<TweaksEvent.SetAnglesPost> { event ->
                longrun {
                    ensureEnabled { enableInstantAim }

                    if (event.entity !is EntityPlayerSP) return@longrun

                    event.entity.prevRotationYawHead = event.entity.prevRotationYaw
                    event.entity.rotationYawHead = event.entity.rotationYaw
                }
            }

            register<TweaksEvent.RightClickBlockPre> { event ->
                longrun {
                    ensureNotCanceled(event)
                    ensureEnabled { disablePearlClickBlock }
                    ensureInWorld()

                    if (MC.thePlayer.inventory
                            .getCurrentItem()
                            ?.item === Items.ender_pearl
                    ) {
                        event.canceled = true
                    }
                }
            }

            register<TweaksEvent.IsHittingPositionCheck> { event ->
                longrun {
                    ensureNotCanceled(event)
                    ensureEnabled { disableSkyBlockToolsNBTUpdateResetDigging }

                    val heldItemStack = MC.thePlayer.heldItem
                    if (event.currentItemHittingBlock !== null &&
                        heldItemStack !== null &&
                        heldItemStack.item in SKYBLOCK_MINING_TOOLS &&
                        heldItemStack.item === event.currentItemHittingBlock.item &&
                        heldItemStack.skyBlockUUID !== null &&
                        heldItemStack.skyBlockUUID == event.currentItemHittingBlock.skyBlockUUID
                    ) {
                        event.canceled = true
                        event.returnValue = event.pos !== null && event.pos == event.currentBlock
                    }
                }
            }
        }
    }
}
