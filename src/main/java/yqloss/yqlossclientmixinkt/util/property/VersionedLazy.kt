package yqloss.yqlossclientmixinkt.util.property

import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class VersionedLazy<T>(
    private val function: () -> T,
    private val versionGetter: () -> Int,
    private var initialized: Boolean,
    private var value: T?,
) : ReadOnlyProperty<Any?, T> {
    private var version: Int? = null

    fun reset() {
        initialized = false
        version = null
        value = null
    }

    override fun getValue(
        thisRef: Any?,
        property: KProperty<*>,
    ): T {
        val currentVersion = versionGetter()
        return if (initialized && version == currentVersion) {
            value!!
        } else {
            val newValue = function()
            initialized = true
            version = currentVersion
            value = newValue
            newValue
        }
    }
}

fun <T> versionedLazy(
    versionGetter: () -> Int,
    function: () -> T,
) = VersionedLazy(function, versionGetter, false, null)

fun <T> versionedLazy(
    initial: T,
    versionGetter: () -> Int,
    function: () -> T,
) = VersionedLazy(function, versionGetter, true, initial)
