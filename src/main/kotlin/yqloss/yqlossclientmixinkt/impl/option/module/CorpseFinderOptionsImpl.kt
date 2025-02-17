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

import cc.polyfrost.oneconfig.config.annotations.Color
import cc.polyfrost.oneconfig.config.annotations.Header
import cc.polyfrost.oneconfig.config.annotations.Switch
import cc.polyfrost.oneconfig.config.core.OneColor
import yqloss.yqlossclientmixinkt.impl.option.OptionsImpl
import yqloss.yqlossclientmixinkt.impl.option.adapter.Extract
import yqloss.yqlossclientmixinkt.impl.option.adapter.asYCColor
import yqloss.yqlossclientmixinkt.impl.option.disclaimer.DisclaimerAtOwnRisk
import yqloss.yqlossclientmixinkt.impl.option.disclaimer.DisclaimerLegit
import yqloss.yqlossclientmixinkt.impl.option.disclaimer.DisclaimerRequireHypixelModAPI
import yqloss.yqlossclientmixinkt.impl.option.impl.NotificationOption
import yqloss.yqlossclientmixinkt.module.corpsefinder.CorpseFinderOptions
import yqloss.yqlossclientmixinkt.module.corpsefinder.CorpseOption
import yqloss.yqlossclientmixinkt.module.corpsefinder.INFO_CORPSE_FINDER

class CorpseOptionImpl : CorpseOption {
    @Color(
        name = "Corpse Color",
        size = 1,
    )
    var colorOption = OneColor("FFFFFFFF")

    @Extract
    var notificationOption = NotificationOption()

    override val color get() = colorOption.asYCColor
    override val notification by ::notificationOption
}

class CorpseFinderOptionsImpl :
    OptionsImpl(INFO_CORPSE_FINDER),
    CorpseFinderOptions {
    @Extract
    var disclaimer = DisclaimerAtOwnRisk()

    @Extract
    var legit = DisclaimerLegit()

    @Extract
    var requireHypixelModAPI = DisclaimerRequireHypixelModAPI()

    @Header(
        text = "Corpse Finder",
        size = 2,
    )
    var headerModule = false

    @Switch(
        name = "Show Exit",
        size = 1,
    )
    var showExitOption = true

    @Color(
        name = "Exit Color",
        size = 1,
    )
    var exitColorOption = OneColor("00FF00FF")

    @Header(
        text = "Lapis Corpse Options",
        size = 2,
    )
    var headerLapis = false

    @Extract
    var lapisOption =
        CorpseOptionImpl().apply {
            colorOption = OneColor("5555FFFF")
        }

    @Header(
        text = "Umber Corpse Options",
        size = 2,
    )
    var headerUmber = false

    @Extract
    var umberOption =
        CorpseOptionImpl().apply {
            colorOption = OneColor("FFAA00FF")
        }

    @Header(
        text = "Tungsten Corpse Options",
        size = 2,
    )
    var headerTungsten = false

    @Extract
    var tungstenOption =
        CorpseOptionImpl().apply {
            colorOption = OneColor("AAAAAAFF")
        }

    @Header(
        text = "Vanguard Corpse Options",
        size = 2,
    )
    var headerVanguard = false

    @Extract
    var vanguardOption =
        CorpseOptionImpl().apply {
            colorOption = OneColor("FFFF55FF")
        }

    @Header(
        text = "Debug",
        size = 2,
    )
    var headerDebug = false

    @Switch(
        name = "Force Enabled",
        size = 1,
    )
    var forceEnabledOption = false

    override val showExit by ::showExitOption
    override val exitColor get() = exitColorOption.asYCColor
    override val lapis by ::lapisOption
    override val umber by ::umberOption
    override val tungsten by ::tungstenOption
    override val vanguard by ::vanguardOption
    override val forceEnabled by ::forceEnabledOption
}
