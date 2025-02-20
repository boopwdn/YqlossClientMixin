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

package yqloss.yqlossclientmixinkt.impl

import cc.polyfrost.oneconfig.gui.OneConfigGui
import cc.polyfrost.oneconfig.gui.pages.SubModsPage
import cc.polyfrost.oneconfig.utils.gui.GuiUtils
import yqloss.yqlossclientmixinkt.YC_LOGGER
import yqloss.yqlossclientmixinkt.YqlossClient
import yqloss.yqlossclientmixinkt.event.minecraft.YCCommandEvent
import yqloss.yqlossclientmixinkt.event.minecraft.YCMinecraftEvent
import yqloss.yqlossclientmixinkt.event.register
import yqloss.yqlossclientmixinkt.impl.api.YCAPIImpl
import yqloss.yqlossclientmixinkt.impl.event.EventDispatcherImpl
import yqloss.yqlossclientmixinkt.impl.hypixel.loadHypixelModAPI
import yqloss.yqlossclientmixinkt.impl.module.betterterminal.BetterTerminalScreen
import yqloss.yqlossclientmixinkt.impl.module.miningprediction.MiningPredictionHUD
import yqloss.yqlossclientmixinkt.impl.module.ycleapmenu.YCLeapMenuScreen
import yqloss.yqlossclientmixinkt.impl.option.YqlossClientConfig
import yqloss.yqlossclientmixinkt.module.betterterminal.BetterTerminal
import yqloss.yqlossclientmixinkt.module.corpsefinder.CorpseFinder
import yqloss.yqlossclientmixinkt.module.mapmarker.MapMarker
import yqloss.yqlossclientmixinkt.module.miningprediction.MiningPrediction
import yqloss.yqlossclientmixinkt.module.option.YCModuleOptions
import yqloss.yqlossclientmixinkt.module.rawinput.RawInput
import yqloss.yqlossclientmixinkt.module.ssmotionblur.SSMotionBlur
import yqloss.yqlossclientmixinkt.module.tweaks.Tweaks
import yqloss.yqlossclientmixinkt.module.ycleapmenu.YCLeapMenu
import yqloss.yqlossclientmixinkt.theYC
import yqloss.yqlossclientmixinkt.util.property.latelet
import kotlin.reflect.KClass

const val MOD_ID = "@ID@"
const val MOD_NAME = "@NAME@"
const val MOD_VERSION = "@VER@"

val initYqlossClientMixin by lazy {
    YC_LOGGER.info("creating YqlossClientMixin instance")
    YqlossClientMixin()
    YC_LOGGER.info("created YqlossClientMixin instance")
}

var theYCMixin: YqlossClientMixin by latelet()

val YCMixin by ::theYCMixin

class YqlossClientMixin : YqlossClient {
    init {
        theYC = this
        theYCMixin = this
    }

    override val modID = MOD_ID
    override val modName = MOD_NAME
    override val modVersion = MOD_VERSION
    override val workingDirectory = "."

    override val api = YCAPIImpl()
    override val eventDispatcher = EventDispatcherImpl()

    override var configVersion = 0

    override fun <T : YCModuleOptions> getOptionsImpl(type: KClass<T>) = YqlossClientConfig.getOptionsImpl(type)

    init {
        YqlossClientConfig

        RawInput
        SSMotionBlur
        Tweaks
        CorpseFinder
        MiningPrediction
        BetterTerminal
        YCLeapMenu
        MapMarker

        BetterTerminalScreen
        YCLeapMenuScreen

        MiningPredictionHUD

        eventDispatcher.register<YCMinecraftEvent.Load.Post> {
            loadHypixelModAPI
        }

        eventDispatcher.register<YCCommandEvent.Execute> { event ->
            if (!event.canceled && !event.disableClientCommand && event.args.getOrNull(0) == "/yc") {
                when (event.args.getOrNull(0)) {
                    "/yc", "/yqlossclient", "/yqlossclientmixin" -> {
                        event.canceled = true
                        GuiUtils.displayScreen(OneConfigGui(SubModsPage(YqlossClientConfig.mod)))
                    }
                }
            }
        }
    }
}
