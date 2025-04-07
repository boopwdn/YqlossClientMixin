/*
 * Copyright (C) 2025 Yqloss
 *
 * This file is part of Yqloss Client (Mixin).
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2 (GPLv2)
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Yqloss Client (Mixin). If not, see <https://www.gnu.org/licenses/old-licenses/gpl-2.0.html>.
 */

#include <stdint.h>
#include <stdlib.h>

#include "include/jni.h"

#include "RawInput.h"

JNIEXPORT void JNICALL
Java_yqloss_yqlossclientmixinkt_nativeapi_WindowsX64NativeAPI_registerRawInputDevices(
    JNIEnv *env,
    jclass thisClass
) {
    registerRawInputDevices();
}

JNIEXPORT void JNICALL
Java_yqloss_yqlossclientmixinkt_nativeapi_WindowsX64NativeAPI_unregisterRawInputDevices(
    JNIEnv *env,
    jclass thisClass
) {
    unregisterRawInputDevices();
}

JNIEXPORT jlongArray JNICALL
Java_yqloss_yqlossclientmixinkt_nativeapi_WindowsX64NativeAPI_getRawInputData(
    JNIEnv *env,
    jclass thisClass,
    jlong lParam,
    jlongArray resultArray
) {
    jlong *result = (*env)->GetLongArrayElements(env, resultArray, NULL);
    getRawInputData(result, lParam);
    (*env)->ReleaseLongArrayElements(env, resultArray, result, 0);
    return resultArray;
}

JNIEXPORT void JNICALL
Java_yqloss_yqlossclientmixinkt_nativeapi_WindowsX64NativeAPI_clipCursorAtCenter(
    JNIEnv *env,
    jclass thisClass,
    jlong hwnd
) {
    clipCursorAtCenter((void*) hwnd);
}

JNIEXPORT void JNICALL
Java_yqloss_yqlossclientmixinkt_nativeapi_WindowsX64NativeAPI_cancelClipCursor(
    JNIEnv *env,
    jclass thisClass
) {
    cancelClipCursor();
}
