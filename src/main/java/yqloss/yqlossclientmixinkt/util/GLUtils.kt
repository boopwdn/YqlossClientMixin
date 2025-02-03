package yqloss.yqlossclientmixinkt.util

import org.lwjgl.opengl.GL11

inline fun <R> glMatrixScope(function: () -> R) {
    GL11.glPushMatrix()
    try {
        function()
    } finally {
        GL11.glPopMatrix()
    }
}

inline fun <R> glAttribScope(function: () -> R) {
    GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS)
    try {
        function()
    } finally {
        GL11.glPopAttrib()
    }
}

inline fun <R> glStateScope(function: () -> R) {
    GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS)
    GL11.glPushMatrix()
    try {
        function()
    } finally {
        GL11.glPopMatrix()
        GL11.glPopAttrib()
    }
}
