#include <jni.h>
#include "Max.h"
extern "C"
JNIEXPORT jint JNICALL
Java_com_example_qy_q233_lib_Accelerometer_maxFromJNI(JNIEnv *env, jclass type, jintArray arr) {

    // TODO
    jsize len = env->GetArrayLength(arr);
    jint *body = env->GetIntArrayElements(arr, 0);
    return max(body);
}
