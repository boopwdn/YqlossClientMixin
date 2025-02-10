package yqloss.yqlossclientmixinkt.util.math

import kotlin.math.pow

class ExponentialSmooth(
    valueIn: Double,
) {
    var value = valueIn
        private set

    private var last: Long? = null

    fun approach(
        target: Double,
        speed: Double,
    ): Double {
        val time = System.nanoTime()
        val diff = time - (last ?: time)
        last = time
        value = target - (target - value) * (1.0 - speed).pow(diff / 50000000.0)
        return value
    }

    fun set(target: Double) {
        value = target
        last = System.nanoTime()
    }

    fun reset(target: Double) {
        value = target
        last = null
    }
}
