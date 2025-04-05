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

#include <windows.h>

void registerRawInputDevices(void) {
    static RAWINPUTDEVICE devices[2] = {
        {
            .usUsagePage = 1,
            .usUsage = 2,
            .dwFlags = 0,
            .hwndTarget = NULL
        },
        {
            .usUsagePage = 1,
            .usUsage = 6,
            .dwFlags = 0,
            .hwndTarget = NULL
        }
    };

    RegisterRawInputDevices(devices, 2, sizeof(RAWINPUTDEVICE));
}

void unregisterRawInputDevices(void) {
    static RAWINPUTDEVICE devices[2] = {
        {
            .usUsagePage = 1,
            .usUsage = 2,
            .dwFlags = RIDEV_REMOVE,
            .hwndTarget = NULL
        },
        {
            .usUsagePage = 1,
            .usUsage = 6,
            .dwFlags = RIDEV_REMOVE,
            .hwndTarget = NULL
        }
    };

    RegisterRawInputDevices(devices, 2, sizeof(RAWINPUTDEVICE));
}

void getRawInputData(int64_t *result, LPARAM lParam) {
    UINT dataSize;

    GetRawInputData(
        (HRAWINPUT) lParam,
        RID_INPUT,
        NULL,
        &dataSize,
        sizeof(RAWINPUTHEADER)
    );

    RAWINPUT *rawData = (RAWINPUT*) malloc(dataSize);

    GetRawInputData(
        (HRAWINPUT) lParam,
        RID_INPUT,
        rawData,
        &dataSize,
        sizeof(RAWINPUTHEADER)
    );

    result[0] = rawData->header.dwType;
    result[1] = rawData->header.dwSize;
    result[2] = (uint64_t) rawData->header.hDevice;
    result[3] = rawData->header.wParam;

    result[16] = rawData->data.mouse.usFlags;
    result[17] = rawData->data.mouse.ulButtons;
    result[18] = rawData->data.mouse.usButtonFlags;
    result[19] = rawData->data.mouse.usButtonData;
    result[20] = rawData->data.mouse.ulRawButtons;
    result[21] = rawData->data.mouse.lLastX;
    result[22] = rawData->data.mouse.lLastY;
    result[23] = rawData->data.mouse.ulExtraInformation;

    result[32] = rawData->data.keyboard.MakeCode;
    result[33] = rawData->data.keyboard.Flags;
    result[34] = rawData->data.keyboard.VKey;
    result[35] = rawData->data.keyboard.Message;
    result[36] = rawData->data.keyboard.ExtraInformation;
}
