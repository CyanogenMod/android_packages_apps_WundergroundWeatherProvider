LOCAL_PATH:= $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := WundergroundWeatherProvider
LOCAL_MODULE_TAGS := optional
LOCAL_PACKAGE_NAME := WundergroundWeatherProvider

wunder_root  := $(LOCAL_PATH)
wunder_dir   := app
wunder_out   := $(OUT_DIR)/target/common/obj/APPS/$(LOCAL_MODULE)_intermediates
wunder_build := $(wunder_root)/$(wunder_dir)/build
wunder_apk   := build/outputs/apk/$(wunder_dir)-release-unsigned.apk

$(wunder_root)/$(wunder_dir)/$(wunder_apk):
	rm -Rf $(wunder_build)
	mkdir -p $(wunder_out)
	ln -sf $(wunder_out) $(wunder_build)
	cd $(wunder_root)/$(wunder_dir) && JAVA_TOOL_OPTIONS="$(JAVA_TOOL_OPTIONS) -Dfile.encoding=UTF8" ../gradlew assembleRelease

LOCAL_CERTIFICATE := platform
LOCAL_SRC_FILES := $(wunder_dir)/$(wunder_apk)
LOCAL_MODULE_CLASS := APPS
LOCAL_MODULE_SUFFIX := $(COMMON_ANDROID_PACKAGE_SUFFIX)

include $(BUILD_PREBUILT)
