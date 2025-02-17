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

package yqloss.yqlossclientmixinkt.module.miningprediction

import yqloss.yqlossclientmixinkt.module.option.YCBlockOption
import yqloss.yqlossclientmixinkt.module.option.YCModuleOptions
import yqloss.yqlossclientmixinkt.module.option.YCNotificationOption

interface MiningPredictionOptions : YCModuleOptions {
    val onBreakBlock: YCNotificationOption
    val durationOffset: Int
    val destroyedBlock: YCBlockOption
    val generalMiningSpeedOffset: Int
    val gemstoneMiningSpeedOffset: Int
    val dwarvenMetalMiningSpeedOffset: Int
    val enableGeneralMiningSpeedOverride: Boolean
    val generalMiningSpeedOverride: Int
    val enableGemstoneMiningSpeedOverride: Boolean
    val gemstoneMiningSpeedOverride: Int
    val enableDwarvenMetalMiningSpeedOverride: Boolean
    val dwarvenMetalMiningSpeedOverride: Int
    val forceEnabled: Boolean
}
