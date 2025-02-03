package yqloss.yqlossclientmixinkt.util

import kotlinx.atomicfu.atomic
import java.util.WeakHashMap

class ConcurrentUniqueHash : (Any?) -> Long {
    private val counter = atomic(0L)

    private val uniqueHash = WeakHashMap<Any, Long>()

    override fun invoke(obj: Any?): Long {
        return obj?.let {
            synchronized(this) {
                uniqueHash.getOrPut(obj) { counter.addAndGet(1) }
            }
        } ?: 0L
    }
}
