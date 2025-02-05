package yqloss.yqlossclientmixinkt.util.scope

inline fun noexcept(
    exceptionHandler: (Exception) -> Unit = {},
    function: () -> Unit,
) {
    try {
        function()
    } catch (exception: Exception) {
        exceptionHandler(exception)
    }
}

inline fun nothrow(
    throwableHandler: (Throwable) -> Unit = {},
    function: () -> Unit,
) {
    try {
        function()
    } catch (exception: Throwable) {
        throwableHandler(exception)
    }
}
