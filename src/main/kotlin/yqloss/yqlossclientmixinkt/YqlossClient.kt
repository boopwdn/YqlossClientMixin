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

package yqloss.yqlossclientmixinkt

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import yqloss.yqlossclientmixinkt.api.YCAPI
import yqloss.yqlossclientmixinkt.event.YCEventDispatcher
import yqloss.yqlossclientmixinkt.module.option.YCModuleOptions
import yqloss.yqlossclientmixinkt.util.property.latelet
import java.io.File
import java.net.URI
import java.net.URL
import kotlin.reflect.KClass

fun ycLogger(name: String): Logger = LogManager.getLogger("YqlossClient $name")

val YC_LOGGER: Logger = LogManager.getLogger("YqlossClient")

var theYC: YqlossClient by latelet()

val YC by ::theYC

val CLASS_ROOT: URL by lazy {
    val uri = YqlossClient::class.java.getResource("/mixins.yqlossclientmixin.json")!!.toURI()
    if (uri.scheme == "jar") {
        URI(uri.toASCIIString().replace(Regex("^jar:(file:.*[.]jar)!/.*")) { it.groupValues[1] }).toURL()
    } else {
        uri.let {
            if (it.path.endsWith("/java/main/mixins.yqlossclientmixin.json")) {
                File(File(it).parentFile.parentFile.parentFile, "/kotlin/main/").toURI()
            } else {
                it
            }.toURL()
        }
    }
}

interface YqlossClient {
    val modID: String
    val modName: String
    val modVersion: String
    val workingDirectory: String

    val api: YCAPI
    val eventDispatcher: YCEventDispatcher

    val configVersion: Int

    fun <T : YCModuleOptions> getOptionsImpl(type: KClass<T>): T
}
