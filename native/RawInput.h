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

#ifndef __YQLOSS_CLIENT_RAW_INPUT_H__
#define __YQLOSS_CLIENT_RAW_INPUT_H__

#include <stdint.h>

#include <windows.h>

void registerRawInputDevices(void);

void unregisterRawInputDevices(void);

void getRawInputData(int64_t*, LPARAM);

void clipCursorAtCenter(HWND hwnd);

void cancelClipCursor();

#endif
