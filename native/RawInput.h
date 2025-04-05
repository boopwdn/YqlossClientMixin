#ifndef __YQLOSS_CLIENT_RAW_INPUT_H__
#define __YQLOSS_CLIENT_RAW_INPUT_H__

#include <stdint.h>

#include <windows.h>

void registerRawInputDevices(void);

void unregisterRawInputDevices(void);

void getRawInputData(int64_t*, LPARAM);

#endif
