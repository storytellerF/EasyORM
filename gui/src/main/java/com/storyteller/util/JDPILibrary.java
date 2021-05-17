package com.storyteller.util;

import java.io.File;

public class JDPILibrary  {
    public native static int getDPI();
    static {
        System.load(new File("DPIDLL.dll").getAbsolutePath());
    }
    public static double scale(int DpiIndex) {
        switch (DpiIndex) {
            case 96: return 1;
            case 120: return 1.25;
            case 144: return 1.5;
            case 168: return 1.75;
            case 192: return 2;
        }
        return 1;
    }
}
