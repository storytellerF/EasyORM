#include "pch.h"
JNIEXPORT jint JNICALL Java_com_storyteller_1f_uiscale_JDPILibrary_getDPI(JNIEnv *, jclass)
{
  HDC hDC = GetDC(NULL);

  int r = GetDeviceCaps(hDC, LOGPIXELSX);
  return r;
}
//ceshi