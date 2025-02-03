package yqloss.yqlossclientmixinkt.util

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class ThreadLocalProperty<T>(
    initializer: () -> T,
) : ReadWriteProperty<Any?, T> {
    private val value = ThreadLocal.withInitial(initializer)

    override fun getValue(
        thisRef: Any?,
        property: KProperty<*>,
    ): T = value.get()

    override fun setValue(
        thisRef: Any?,
        property: KProperty<*>,
        value: T,
    ) {
        this.value.set(value)
    }
}

fun <T> threadlocal(initializer: () -> T) = ThreadLocalProperty(initializer)
