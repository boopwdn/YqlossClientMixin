package yqloss.yqlossclientmixinkt.util

import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.WorldRenderer
import net.minecraft.client.renderer.vertex.VertexFormat

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
