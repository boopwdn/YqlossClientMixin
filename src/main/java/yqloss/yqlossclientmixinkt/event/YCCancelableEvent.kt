package yqloss.yqlossclientmixinkt.event

interface YCCancelableEvent : YCEvent {
    var canceled: Boolean
}
