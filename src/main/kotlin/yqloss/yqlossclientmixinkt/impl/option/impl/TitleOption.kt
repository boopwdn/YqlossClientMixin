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

package yqloss.yqlossclientmixinkt.impl.option.impl

import cc.polyfrost.oneconfig.config.annotations.Checkbox
import cc.polyfrost.oneconfig.config.annotations.Number
import cc.polyfrost.oneconfig.config.annotations.Switch
import cc.polyfrost.oneconfig.config.annotations.Text
import yqloss.yqlossclientmixinkt.module.option.YCTitleOption

class TitleOption : YCTitleOption {
    @Switch(
        name = "Enable Title Notification",
        size = 1,
    )
    var enabledOption = false

    @Text(
        name = "Title",
        size = 1,
    )
    var textOption = ""

    @Checkbox(
        name = "Set Subtitle",
        size = 1,
    )
    var setSubtitleOption = true

    @Text(
        name = "Subtitle",
        size = 1,
    )
    var subtitleOption = ""

    @Checkbox(
        name = "Set Fade-In, Display, Fade-Out Duration",
        size = 1,
    )
    var setTimeOption = true

    @Number(
        name = "Display Duration (in ticks)",
        min = 0.0F,
        max = Float.MAX_VALUE,
        step = 1,
        size = 1,
    )
    var stayOption = 60

    @Number(
        name = "Fade-In Duration (in ticks)",
        min = 0.0F,
        max = Float.MAX_VALUE,
        step = 1,
        size = 1,
    )
    var fadeInOption = 0

    @Number(
        name = "Fade-Out Duration (in ticks)",
        min = 0.0F,
        max = Float.MAX_VALUE,
        step = 1,
        size = 1,
    )
    var fadeOutOption = 0

    override val enabled by ::enabledOption
    override val text by ::textOption
    override val setSubtitle by ::setSubtitleOption
    override val subtitle by ::subtitleOption
    override val setTime by ::setTimeOption
    override val stay by ::stayOption
    override val fadeIn by ::fadeInOption
    override val fadeOut by ::fadeOutOption
}
