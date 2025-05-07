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

package yqloss.yqlossclientmixinkt.module.ssmotionblur

import kotlin.math.ln
import kotlin.math.pow

enum class AlphaFunction(
    val calculate: (timeDiffNanos: Double, strength: Double) -> Alphas,
) {
    INSTANT({ timeDiffNanos, strength ->
        Alphas(
            null,
            strength
                .coerceIn(0.0..1.0)
                .takeIf { it.isFinite() },
        )
    }),

    INSTANT_BALANCED({ timeDiffNanos, strength ->
        Alphas(
            null,
            strength
                .pow(timeDiffNanos * 256.0 / 1000000000.0)
                .coerceIn(0.0..1.0)
                .takeIf { it.isFinite() },
        )
    }),

    LINEAR_BALANCED({ timeDiffNanos, strength ->
        val t = timeDiffNanos * 256.0 / 1000000000.0
        Alphas(
            (1.0 - 1.0 / (1.0 - strength.pow(t)) - 1.0 / (t * ln(strength)))
                .coerceIn(0.0..1.0)
                .takeIf { it.isFinite() },
            (strength.pow(t))
                .coerceIn(0.0..1.0)
                .takeIf { it.isFinite() },
        )
    }),
    ;

    data class Alphas(
        val lastFrame: Double?,
        val accumulation: Double?,
    )
}
