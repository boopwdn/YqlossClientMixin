package yqloss.yqlossclientmixinkt.util.general

inline fun <T> intervalAction(
    intervalNanos: Long,
    crossinline function: (T) -> Unit,
): (T) -> Unit {
    var last: Long? = null

    return { arg ->
        val time = System.nanoTime()
        last?.takeIf { time - it < intervalNanos } ?: function(arg)
        last = time
    }
}

inline fun intervalAction(
    intervalNanos: Long,
    crossinline function: () -> Unit,
): () -> Unit {
    var last: Long? = null

    return {
        val time = System.nanoTime()
        last?.takeIf { time - it < intervalNanos } ?: run {
            try {
                function()
            } finally {
                last = time
            }
        }
    }
}
