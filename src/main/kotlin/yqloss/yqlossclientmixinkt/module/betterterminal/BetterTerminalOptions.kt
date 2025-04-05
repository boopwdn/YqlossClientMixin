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

package yqloss.yqlossclientmixinkt.module.betterterminal

import yqloss.yqlossclientmixinkt.module.option.YCModuleOptions
import yqloss.yqlossclientmixinkt.module.option.YCNotificationOption

interface BetterTerminalOptions : YCModuleOptions {
    val enableQueue: Boolean
    val preventFail: Boolean
    val preventMisclick: Boolean
    val reloadOnMismatch: Boolean
    val clickDelayFrom: Int
    val clickDelayUntil: Int
    val orderEnabled: Boolean
    val orderShowNumber: Boolean
    val orderShowClickedNumber: Boolean
    val panesEnabled: Boolean
    val startEnabled: Boolean
    val colorEnabled: Boolean
    val rubixEnabled: Boolean
    val rubixShowNumber: Boolean
    val rubixCorrectDirection: Boolean
    val alignEnabled: Boolean
    val onCorrectClick: YCNotificationOption
    val onCanceledClick: YCNotificationOption
    val onWrongClick: YCNotificationOption
    val onFailClick: YCNotificationOption
    val onNonQueuedClick: YCNotificationOption
    val onActualClick: YCNotificationOption
    val showChest: Boolean
    val chestScale: Double
    val forceEnabled: Boolean
}
