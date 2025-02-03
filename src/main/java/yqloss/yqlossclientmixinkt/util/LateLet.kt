package yqloss.yqlossclientmixinkt.util

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class LateLet<T> : ReadWriteProperty<Any?, T> {
    private var box: Box<T>? = null

    override fun getValue(
        thisRef: Any?,
        property: KProperty<*>,
    ): T {
        return box?.cast() ?: throw IllegalStateException()
    }

    override fun setValue(
        thisRef: Any?,
        property: KProperty<*>,
        value: T,
    ) {
        if (box !== null) throw IllegalStateException()
        box = value.inBox
    }
}

fun <T> latelet() = LateLet<T>()
