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

package yqloss.yqlossclientmixinkt.module.repository

import okhttp3.OkHttpClient
import yqloss.yqlossclientmixinkt.event.YCEventRegistry
import yqloss.yqlossclientmixinkt.event.minecraft.YCMinecraftEvent
import yqloss.yqlossclientmixinkt.event.register
import yqloss.yqlossclientmixinkt.module.YCModuleBase
import yqloss.yqlossclientmixinkt.module.ensureEnabled
import yqloss.yqlossclientmixinkt.module.moduleInfo
import yqloss.yqlossclientmixinkt.util.scope.longRun

val INFO_REPOSITORY = moduleInfo<RepositoryOptions>("repository", "Repository")

object Repository : YCModuleBase<RepositoryOptions>(INFO_REPOSITORY) {
    val httpClient = OkHttpClient()

    var version = Version()
    var capes = Capes()

    fun reloadVersion() {
        version = Version()
    }

    fun reloadCapes() {
        capes = Capes()
    }

    val repositoryData: List<RepositoryData<*>>
        get() {
            return listOfNotNull(
                version.takeIf { options.versionEnabled },
                capes.takeIf { options.capeEnabled },
            )
        }

    override fun registerEvents(registry: YCEventRegistry) {
        registry.run {
            register<YCMinecraftEvent.Tick.Pre> {
                longRun {
                    ensureEnabled()

                    repositoryData.forEach {
                        if (it.requireNewRequest) {
                            it.request()
                        }
                    }

                    version.onTickPre()
                }
            }
        }
    }
}
