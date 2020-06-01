//
// Created by WangXingxing on 2020/5/20.
//

#ifndef NDKGIFDEMO_LOG_H
#define NDKGIFDEMO_LOG_H

#define LOG_DEBUG true

#define TAG "WXX"

#include <android/log.h>

#ifdef LOG_DEBUG
#define LOGI(...) \
        __android_log_print(ANDROID_LOG_INFO,TAG,__VA_ARGS__)

#define LOGD(...) \
        __android_log_print(ANDROID_LOG_DEBUG,TAG,__VA_ARGS__)

#define LOGW(...) \
        __android_log_print(ANDROID_LOG_WARN,TAG,__VA_ARGS__)

#define LOGE(...) \
        __android_log_print(ANDROID_LOG_ERROR,TAG,__VA_ARGS__)
#else
#define LOGI(...)
#define LOGD(...)
#define LOGW(...)
#define LOGE(...)
#endif


#endif //NDKGIFDEMO_LOG_H
