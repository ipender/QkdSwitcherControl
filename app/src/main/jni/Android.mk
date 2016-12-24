LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)
LOCAL_MODULE    := pl2303_driver
LOCAL_SRC_FILES := com_topeet_serialtest_serial.c
LOCAL_LDLIBS += -llog 
LOCAL_LDLIBS += -lm
include $(BUILD_SHARED_LIBRARY)