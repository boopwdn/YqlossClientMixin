package yqloss.yqlossclientmixinkt.util.scope

class WithScopeContext(
    private val resourceSet: MutableSet<AutoCloseable>,
) {
    fun <T : AutoCloseable> using(resource: T): T {
        return resource.also {
            resourceSet.add(it)
        }
    }
}

data class ResourceClosureException(
    val failures: List<Pair<AutoCloseable, Exception>>,
) : Exception()

inline fun <R> withscope(function: WithScopeContext.() -> R) {
    val resourceSet = mutableSetOf<AutoCloseable>()
    val context = WithScopeContext(resourceSet)
    try {
        function(context)
    } finally {
        val exceptionList = mutableListOf<Pair<AutoCloseable, Exception>>()
        resourceSet.forEach { resource ->
            noexcept({ exceptionList.add(resource to it) }) { resource.close() }
        }
        if (exceptionList.isNotEmpty()) {
            throw ResourceClosureException(exceptionList)
        }
    }
}
