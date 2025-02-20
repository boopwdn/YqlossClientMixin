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

package yqloss.yqlossclientmixinkt.impl.option.module

import cc.polyfrost.oneconfig.config.annotations.Header
import cc.polyfrost.oneconfig.config.annotations.Switch
import yqloss.yqlossclientmixinkt.impl.option.OptionsImpl
import yqloss.yqlossclientmixinkt.impl.option.adapter.Extract
import yqloss.yqlossclientmixinkt.impl.option.disclaimer.DisclaimerAtOwnRisk
import yqloss.yqlossclientmixinkt.impl.option.disclaimer.DisclaimerLegit
import yqloss.yqlossclientmixinkt.impl.option.disclaimer.DisclaimerRequireHypixelModAPI
import yqloss.yqlossclientmixinkt.impl.option.impl.NotificationOption
import yqloss.yqlossclientmixinkt.impl.option.impl.ScreenScaleOption
import yqloss.yqlossclientmixinkt.module.ycleapmenu.INFO_YC_LEAP_MENU
import yqloss.yqlossclientmixinkt.module.ycleapmenu.YCLeapMenuOptions

class YCLeapMenuOptionsImpl :
    OptionsImpl(INFO_YC_LEAP_MENU),
    YCLeapMenuOptions {
    @Transient
    @Extract
    val disclaimer = DisclaimerAtOwnRisk()

    @Transient
    @Extract
    val legit = DisclaimerLegit()

    @Transient
    @Extract
    val requireHypixelModAPI = DisclaimerRequireHypixelModAPI()

    @Transient
    @Header(
        text = "YC Leap Menu",
        size = 2,
    )
    val headerModule = false

    @Extract
    var scaleOverride = ScreenScaleOption()

    @Switch(
        name = "Smooth GUI",
        size = 1,
    )
    var smoothGUI = true

    @Transient
    @Header(
        text = "Notification On Clicking Leap Button",
        size = 2,
    )
    val headerNotification = false

    @Extract
    var onClickLeapOption = NotificationOption()

    @Transient
    @Header(
        text = "Debug",
        size = 2,
    )
    val headerDebug = false

    @Switch(
        name = "Force Enabled",
        size = 1,
    )
    var forceEnabledOption = false

    override val onClickLeap by ::onClickLeapOption
    override val forceEnabled by ::forceEnabledOption
}
