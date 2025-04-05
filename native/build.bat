set LastDirectory=%cd%
cd %~dp0
set Options=-std=c11 -O2 -static -shared -m64 -o ../src/main/resources/assets/yqlossclientmixin/native/client.exe
gcc %Options% WindowsX64NativeAPI.c RawInput.c
cd %LastDirectory%
