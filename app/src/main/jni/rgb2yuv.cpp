#include <jni.h>
#include <cstddef>

extern "C" {

JNIEXPORT void JNICALL
Java_team_birdhead_rgb2yuv_converter_JNIConverter_rgbToYuvInternal(JNIEnv *env, jobject,
                                                                jbyteArray rgbArray, jint width,
                                                                jint height, jbyteArray yuvArray) {
    jbyte *rgb = env->GetByteArrayElements(rgbArray, NULL);
    jbyte *yuv = env->GetByteArrayElements(yuvArray, NULL);

    int rgbIndex = 0;
    int yIndex = 0;
    int uvIndex = width * height;
    for (int j = 0; j < height; ++j) {
        for (int i = 0; i < width; ++i) {
            int r = rgb[rgbIndex] & 0xFF;
            int g = rgb[rgbIndex + 1] & 0xFF;
            int b = rgb[rgbIndex + 2] & 0xFF;

            int y = (int) (0.257 * r + 0.504 * g + 0.098 * b + 16);
            int u = (int) (-0.148 * r - 0.291 * g + 0.439 * b + 128);
            int v = (int) (0.439 * r - 0.368 * g - 0.071 * b + 128);

            yuv[yIndex++] = (jbyte) (y < 0 ? 0 : y > 255 ? 255 : y);
            if ((i & 0x01) == 0 && (j & 0x01) == 0) {
                yuv[uvIndex++] = (jbyte) (v < 0 ? 0 : v > 255 ? 255 : v);
                yuv[uvIndex++] = (jbyte) (u < 0 ? 0 : u > 255 ? 255 : u);
            }

            rgbIndex += 4;
        }
    }

    env->ReleaseByteArrayElements(yuvArray, yuv, 0);
    env->ReleaseByteArrayElements(rgbArray, rgb, 0);
}

} // extern "C"
