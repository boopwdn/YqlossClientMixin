package yqloss.yqlossclientmixinkt.util.property

import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class VersionedLazy<T, TV : Any>(
    private val function: () -> T,
    private val versionGetter: () -> TV,
    private var initialized: Boolean,
    private var value: T?,
) : ReadOnlyProperty<Any?, T> {
    private var version: TV? = null

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

fun <T, TV : Any> versionedLazy(
    versionGetter: () -> TV,
    function: () -> T,
) = VersionedLazy(function, versionGetter, false, null)

fun <T, TV : Any> versionedLazy(
    initial: T,
    versionGetter: () -> TV,
    function: () -> T,
) = VersionedLazy(function, versionGetter, true, initial)
