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
import cc.polyfrost.oneconfig.config.annotations.Info
import cc.polyfrost.oneconfig.config.annotations.Switch
import cc.polyfrost.oneconfig.config.data.InfoType
import yqloss.yqlossclientmixinkt.YC
import yqloss.yqlossclientmixinkt.api.YCHypixelLocation
import yqloss.yqlossclientmixinkt.api.YCHypixelServerType
import yqloss.yqlossclientmixinkt.impl.YCMixin
import yqloss.yqlossclientmixinkt.impl.option.adapter.Extract
import yqloss.yqlossclientmixinkt.impl.option.disclaimer.DisclaimerAtOwnRisk
import yqloss.yqlossclientmixinkt.module.moduleInfo
import yqloss.yqlossclientmixinkt.module.option.YCModuleOptions
import yqloss.yqlossclientmixinkt.util.MC
import yqloss.yqlossclientmixinkt.util.printChat

class MainConfig : OptionsImpl(moduleInfo<YCModuleOptions>("main", "# Yqloss Client #")) {
    @Transient
    @Info(type = InfoType.INFO, text = "Ciallo\uFF5E(\u2220\u30FB\u03C9< )\u2312\u2606", size = 2)
    val infoCiallo = false

    @Transient
    @Info(type = InfoType.INFO, text = "已为 OneConfig 添加中文支持", size = 2)
    val infoChineseFont = false

    @Transient
    @Extract
    val disclaimer = DisclaimerAtOwnRisk()

    @Transient
    @Header(
        text = "Flags",
        size = 2,
    )
    val headerFlag = false

    @Switch(
        name = "Disable Yqloss Client Commands",
        size = 1,
    )
    var disableCommands = false

    @Switch(
        name = "Disable IBlockAccess Wrapping",
        size = 1,
    )
    var disableBlockAccess = false

    @Transient
    @Header(
        text = "Debug Flags",
        size = 2,
    )
    val headerDebugFlag = false

    @Switch(
        name = "Verbose Hypixel Mod API Wrapper",
        size = 1,
    )
    var verboseHypixelModAPI = false

    @Transient
    @Header(
        text = "Utilities",
        size = 2,
    )
    val headerUtilities = false

    @Transient
    @Extract
    val loadAllCharacters =
        @Button(
            name = "Load All Characters",
            text = "Load",
            size = 1,
        )
        {
            repeat(65536) {
                MC.fontRendererObj.drawString(Char(it).toString(), 0, 0, -1)
            }
        }

    @Transient
    @Header(
        text = "Hypixel Mod API Location",
        size = 2,
    )
    val headerHypixelModAPILocation = false

    @Transient
    @Extract
    val printHypixelModAPILocation =
        @Button(
            name = "Print Hypixel Mod API Location",
            text = "Print",
            size = 1,
        )
        {
            printChat(YC.api.hypixelLocation.toString())
        }

    @Transient
    @Extract
    val setMineshaft =
        @Button(
            name = "Mineshaft",
            text = "Set",
            size = 1,
        )
        {
            YCMixin.api.hypixelLocation =
                YCHypixelLocation(
                    "mini0721KLOON",
                    serverType = YCHypixelServerType("SkyBlock"),
                    null,
                    "mineshaft",
                    null,
                )
        }

    @Transient
    @Extract
    val setDungeon =
        @Button(
            name = "Dungeon",
            text = "Set",
            size = 1,
        )
        {
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
