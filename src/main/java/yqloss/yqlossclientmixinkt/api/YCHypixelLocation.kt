package yqloss.yqlossclientmixinkt.api

data class YCHypixelServerType(
    val name: String,
)

data class YCHypixelLocation(
    val serverName: String,
    val serverType: YCHypixelServerType?,
    val lobbyName: String?,
    val mode: String?,
    val map: String?,
)
