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
