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
import cc.polyfrost.oneconfig.config.annotations.Slider
import cc.polyfrost.oneconfig.config.annotations.Switch
import yqloss.yqlossclientmixinkt.impl.option.OptionsImpl
import yqloss.yqlossclientmixinkt.impl.option.adapter.Extract
import yqloss.yqlossclientmixinkt.impl.option.disclaimer.DisclaimerAtOwnRisk
import yqloss.yqlossclientmixinkt.impl.option.disclaimer.DisclaimerLegit
import yqloss.yqlossclientmixinkt.module.ssmotionblur.INFO_SS_MOTION_BLUR
import yqloss.yqlossclientmixinkt.module.ssmotionblur.SSMotionBlurOptions

class SSMotionBlurOptionsImpl :
    OptionsImpl(INFO_SS_MOTION_BLUR),
    SSMotionBlurOptions {
    @Extract
    var disclaimer = DisclaimerAtOwnRisk()

    @Extract
    var legit = DisclaimerLegit()

    @Header(
        text = "SS Motion Blur",
        size = 2,
    )
    var headerModule = false

    @Slider(
        name = "Strength",
        min = 0.0F,
        max = 100.0F,
        description = "0 means there won't be any effect. 100 means the screen will freeze.",
    )
    var strengthOption = 50.0F

    @Switch(
        name = "FPS Balanced",
        description = "Make the blur on whatever FPS look as if it's 256 FPS.",
        size = 2,
    )
    var balancedOption = true

    override val strength get() = strengthOption / 100.0
    override val balanced by ::balancedOption
}
