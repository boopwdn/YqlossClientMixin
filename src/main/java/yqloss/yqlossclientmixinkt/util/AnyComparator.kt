package yqloss.yqlossclientmixinkt.util

class AnyComparator(
    private val hash: (Any?) -> Long,
) : Comparator<Any?> {
    override fun compare(
        a: Any?,
        b: Any?,
    ) = hash(a).compareTo(hash(b))
}
