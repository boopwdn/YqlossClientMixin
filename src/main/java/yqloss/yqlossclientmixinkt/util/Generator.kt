package yqloss.yqlossclientmixinkt.util

import kotlin.coroutines.Continuation
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.createCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

sealed interface GeneratorState {
    data class Waiting(
        val continuation: Continuation<Unit>,
    ) : GeneratorState

    data class Ready<T>(
        val continuation: Continuation<Unit>,
        val result: T,
    ) : GeneratorState

    data object Ended : GeneratorState
}

class Generator<out T>(
    block: suspend Generator<T>.() -> Unit,
) : Iterator<T>,
    Continuation<Unit> {
    override val context = EmptyCoroutineContext

    private var state: GeneratorState = GeneratorState.Waiting(block.createCoroutine(this, this))

    private fun resume() {
        when (val state = state) {
            is GeneratorState.Waiting -> state.continuation.resume(Unit)
            else -> Unit
        }
    }

    override fun hasNext() = resume().let { state != GeneratorState.Ended }

    override fun next(): T {
        return when (val state = state) {
            is GeneratorState.Waiting -> resume().let { next() }
            is GeneratorState.Ready<*> ->
                state.inBox.cast<GeneratorState.Ready<T>>().run {
                    result.also { this@Generator.state = GeneratorState.Waiting(continuation) }
                }

            else -> throw IllegalStateException()
        }
    }

    override fun resumeWith(result: Result<Unit>) {
        state = GeneratorState.Ended
        result.getOrThrow()
    }

    suspend fun yield(result: @UnsafeVariance T): T {
        suspendCoroutine { continuation ->
            state =
                when (state) {
                    is GeneratorState.Waiting -> GeneratorState.Ready(continuation, result)
                    else -> throw IllegalStateException()
                }
        }
        return result
    }
}

fun <T> generator(block: suspend Generator<T>.() -> Unit): Iterator<T> = Generator(block)
