package yqloss.yqlossclientmixinkt.util.scope

inline fun <R> noexcept(
    exceptionHandler: (Exception) -> R? = { null },
    function: () -> R,
): R? {
    return try {
        function()
    } catch (exception: Exception) {
        exceptionHandler(exception)
    }
}

inline fun <R> nothrow(
    throwableHandler: (Throwable) -> R? = { null },
    function: () -> R,
): R? {
    return try {
        function()
    } catch (exception: Throwable) {
        throwableHandler(exception)
    }
}
