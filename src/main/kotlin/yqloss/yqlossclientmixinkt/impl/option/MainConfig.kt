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

package yqloss.yqlossclientmixinkt.impl.option

import cc.polyfrost.oneconfig.config.annotations.Button
import cc.polyfrost.oneconfig.config.annotations.Header
import cc.polyfrost.oneconfig.config.annotations.Switch
import cc.polyfrost.oneconfig.config.elements.SubConfig
import yqloss.yqlossclientmixinkt.YC
import yqloss.yqlossclientmixinkt.api.YCHypixelLocation
import yqloss.yqlossclientmixinkt.api.YCHypixelServerType
import yqloss.yqlossclientmixinkt.impl.YCMixin
import yqloss.yqlossclientmixinkt.impl.option.adapter.Extract
import yqloss.yqlossclientmixinkt.impl.option.disclaimer.DisclaimerAtOwnRisk
import yqloss.yqlossclientmixinkt.util.printChat

class MainConfig : SubConfig("!Yqloss Client", "yqlossclient-main.json") {
    @Extract
    var disclaimer = DisclaimerAtOwnRisk()

    @Header(
        text = "Debug Flags",
        size = 2,
    )
    var headerDebugFlag = false

    @Switch(
        name = "Verbose Hypixel Mod API Wrapper",
        size = 1,
    )
    var verboseHypixelModAPI = false

    @Button(
        name = "Print Hypixel Mod API Location",
        text = "Print",
        size = 1,
    )
    fun printHypixelModAPILocation() {
        printChat(YC.api.hypixelLocation.toString())
    }

    @Header(
        text = "Set Hypixel Mod API Location",
        size = 2,
    )
    var headerSetHypixelModAPILocation = false

    @Button(
        name = "Mineshaft",
        text = "Set",
        size = 1,
    )
    fun setMineshaft() {
        YCMixin.api.hypixelLocation =
            YCHypixelLocation(
                "mini0721KLOON",
                serverType = YCHypixelServerType("SkyBlock"),
                null,
                "mineshaft",
                null,
            )
    }

    @Button(
        name = "Dungeon",
        text = "Set",
        size = 1,
    )
    fun setDungeon() {
        YCMixin.api.hypixelLocation =
            YCHypixelLocation(
                "mini0721KLOON",
                serverType = YCHypixelServerType("SkyBlock"),
                null,
                "dungeon",
                "Dungeon",
            )
    }
}
