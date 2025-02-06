package yqloss.yqlossclientmixinkt.util

import org.lwjgl.opengl.GL11
import yqloss.yqlossclientmixinkt.module.option.YCColor
import yqloss.yqlossclientmixinkt.util.math.Vec3D

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

fun Vec3D.glTranslate() {
    GL11.glTranslated(x, y, z)
}

fun YCColor.glColor() {
    GL11.glColor4d(r, g, b, a)
}
