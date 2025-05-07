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

import kotlinx.coroutines.*
import kotlinx.serialization.KSerializer
import okhttp3.Request
import okhttp3.coroutines.executeAsync
import yqloss.yqlossclientmixinkt.YCJson
import yqloss.yqlossclientmixinkt.util.accessor.outs.Box
import yqloss.yqlossclientmixinkt.util.accessor.outs.inBox
import yqloss.yqlossclientmixinkt.util.scope.usingScope

abstract class HttpData<T>(
    val url: String,
) : RepositoryData<T> {
    override var requesting = false

    override var data: Box<T>? = null

    abstract val serializer: KSerializer<T>

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun request() {
        synchronized(this) {
            if (requesting) return
            requesting = true
        }
        CoroutineScope(Dispatchers.IO + SupervisorJob()).launch {
            try {
                usingScope {
                    Repository.logger.info("requesting $url")
                    val response =
                        Repository.httpClient
                            .newCall(Request.Builder().url(url).build())
                            .executeAsync()
                            .using
                    Repository.logger.info("response code ${response.code} from $url")
                    if (response.code != 200) return@launch
                    data =
                        YCJson
                            .decodeFromString(
                                serializer,
                                response.body
                                    .charStream()
                                    .using
                                    .readText(),
                            ).inBox
                    println(data)
                }
            } finally {
                synchronized(this@HttpData) {
                    requesting = false
                }
            }
        }
    }
}
