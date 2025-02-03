package yqloss.yqlossclientmixinkt.util

import java.io.File
import java.io.FileOutputStream

fun createFileOutputStream(file: File): FileOutputStream {
    file.parentFile.mkdirs()
    return FileOutputStream(file)
}
