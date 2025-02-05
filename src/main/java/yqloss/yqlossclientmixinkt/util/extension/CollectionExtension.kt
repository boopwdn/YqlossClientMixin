package yqloss.yqlossclientmixinkt.util.extension

infix fun <T> Collection<T>.prepend(element: T): List<T> {
    return buildList(this.size + 1) {
        add(element)
        addAll(this@prepend)
    }
}

infix fun <T> T.prependTo(collection: Collection<T>): List<T> {
    return buildList(collection.size + 1) {
        add(this@prependTo)
        addAll(collection)
    }
}
