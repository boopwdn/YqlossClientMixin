package yqloss.yqlossclientmixinkt.util

var frameCounter by threadlocal { 0 }

data class LongReturn(
    val frame: Int,
    val value: Any?,
) : Throwable() {
    val next: LongReturn
        get() = if (frame < 0) LongReturn(frame + 1, value) else this
}

class LongReturnContext1(
    private val value: Any?,
) {
    infix fun scope(frame: Int) {
        throw LongReturn(frame, value)
    }

    infix fun times(count: Int) {
        throw LongReturn(1 - count, value)
    }
}

class LongReturnContext2(
    private val box: Box<*>,
) {
    fun <T> v() = box.cast<T>()
}

class LongReturnContext3(
    val function: (Int) -> Unit,
) {
    inline infix fun onreturn(returnValue: LongReturnContext2.() -> Unit) {
        frameCounter += 1
        val currentFrame = frameCounter
        try {
            function(currentFrame)
        } catch (longReturn: LongReturn) {
            if (longReturn.frame != 0 && longReturn.frame != currentFrame) {
                throw longReturn.next
            }
            returnValue(LongReturnContext2(longReturn.value.inBox))
        } finally {
            frameCounter -= 1
        }
    }
}

inline fun <T> longreturn(getter: () -> T) = LongReturnContext1(getter())

fun longscope(function: (Int) -> Unit) = LongReturnContext3(function)
