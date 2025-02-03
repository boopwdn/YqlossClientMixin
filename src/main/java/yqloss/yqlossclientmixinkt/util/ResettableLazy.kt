package yqloss.yqlossclientmixinkt.util

import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class ResettableLazy<T>(
    private val function: () -> T,
    private var initialized: Boolean,
    private var value: T?,
) : ReadOnlyProperty<Any?, T> {
    fun reset() {
        initialized = false
        value = null
    }

    override fun getValue(
        thisRef: Any?,
        property: KProperty<*>,
    ): T {
        return if (initialized) {
            value!!
        } else {
            val newValue = function()
            initialized = true
            value = newValue
            newValue
        }
    }
}

fun <T> resettableLazy(function: () -> T) = ResettableLazy(function, false, null)

fun <T> resettableLazy(
    initial: T,
    function: () -> T,
) = ResettableLazy(function, true, initial)
