package com.storyteller_f.uiscale;

import javax.swing.*;
import java.awt.*;

public class DataZone {
    private static final DataZone dataZone = new DataZone();
    double scale = 0;

    public static DataZone getInstance() {
        return dataZone;
    }

    public double getScale() {
        if (scale == 0) {
            int dpi = JDPILibrary.getDPI();
            scale = JDPILibrary.scale(dpi);
        }
        return scale;
    }
    public static Font getFont(Font font) {
        double scale=DataZone.getInstance().getScale();
        return new Font(font.getName(), font.getStyle(), (int) (font.getSize() * scale));
    }

    public static void setFont(JComponent jComponent) {
        jComponent.setFont(getFont(jComponent.getFont()));
    }

    public static void setFont(JComponent... jComponents) {
        for (JComponent jComponent : jComponents) {
            setFont(jComponent);
        }
    }
    public static Font getFont(String fontName,Font font) {
        double scale=DataZone.getInstance().getScale();
        return new Font(fontName, font.getStyle(), (int) (font.getSize() * scale));
    }

    public static void setFont(String fontName,JComponent jComponent) {
        jComponent.setFont(getFont(fontName,jComponent.getFont()));
    }

    public static void setFont(String fontName,JComponent... jComponents) {
        for (JComponent jComponent : jComponents) {
            setFont(fontName,jComponent);
        }
    }
}
