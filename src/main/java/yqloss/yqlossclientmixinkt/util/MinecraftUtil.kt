package yqloss.yqlossclientmixinkt.util

import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.WorldRenderer
import net.minecraft.client.renderer.vertex.VertexFormat
import net.minecraft.util.BlockPos
import yqloss.yqlossclientmixinkt.util.math.Vec3I

val MC: Minecraft by lazy { Minecraft.getMinecraft() }

inline val BlockPos.asVec3I get() = Vec3I(x, y, z)

inline val Vec3I.asBlockPos get() = BlockPos(x, y, z)

inline fun mcRenderScope(
    mode: Int,
    vertexFormat: VertexFormat,
    function: WorldRenderer.() -> Unit,
) {
    val tessellator = Tessellator.getInstance()
    val worldRenderer = tessellator.worldRenderer
    worldRenderer.begin(mode, vertexFormat)
    try {
        function(worldRenderer)
    } finally {
        tessellator.draw()
    }
}
