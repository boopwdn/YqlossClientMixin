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

package yqloss.yqlossclientmixinkt.module

import net.minecraft.client.gui.inventory.GuiChest
import yqloss.yqlossclientmixinkt.YC
import yqloss.yqlossclientmixinkt.event.YCCancelableEvent
import yqloss.yqlossclientmixinkt.module.option.YCModuleOptions
import yqloss.yqlossclientmixinkt.util.MC
import yqloss.yqlossclientmixinkt.util.scope.longreturn
import yqloss.yqlossclientmixinkt.util.trimStyle

inline fun ensure(
    frame: Int = 0,
    predicate: () -> Boolean,
) {
    if (!predicate()) longreturn(frame) {}
}

fun YCModule<*>.ensureEnabled(frame: Int = 0) = ensure(frame) { options.enabled }

inline fun <T : YCModuleOptions> YCModule<T>.ensureEnabled(
    frame: Int = 0,
    option: T.() -> Boolean,
) = ensure(frame) { options.enabled && option(options) }

fun ensureNotCanceled(
    event: YCCancelableEvent,
    frame: Int = 0,
) = ensure(frame) { !event.canceled }

fun ensureInWorld(frame: Int = 0) = ensure(frame) { MC.theWorld !== null }

fun ensureSkyBlock(frame: Int = 0) {
    ensure(frame) {
        MC.theWorld !== null &&
            YC.api.hypixelLocation
                ?.serverType
                ?.name == "SkyBlock"
    }
}

fun ensureSkyBlockMode(
    mode: String,
    frame: Int = 0,
) {
    ensure(frame) {
        MC.theWorld !== null &&
            (
                YC.api.hypixelLocation?.run {
                    serverType?.name == "SkyBlock" && this.mode == mode
                } ?: false
            )
    }
}

fun ensureSkyBlockModes(
    modes: Set<String>,
    frame: Int = 0,
) {
    ensure(frame) {
        MC.theWorld !== null &&
            (
                YC.api.hypixelLocation?.run {
                    serverType?.name == "SkyBlock" && mode in modes
                } ?: false
            )
    }
}

fun ensureWindowTitle(
    chest: GuiChest,
    title: String,
    frame: Int = 0,
) {
    ensure(frame) {
        YC.api
            .get_GuiChest_lowerChestInventory(chest)
            .name.trimStyle == title
    }
}

val SKYBLOCK_MINING_ISLANDS =
    setOf(
        "mining_1",
        "mining_2",
        "mining_3",
        "crystal_hollows",
        "mineshaft",
        "combat_3",
        "crimson_isle",
    )
