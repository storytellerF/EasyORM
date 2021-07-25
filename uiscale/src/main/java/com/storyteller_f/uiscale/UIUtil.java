package com.storyteller_f.uiscale;

import javax.swing.*;
import java.awt.*;

public class UIUtil {
    private static final UIUtil UI_UTIL = new UIUtil();
    double scale = 0;

    public static UIUtil getInstance() {
        return UI_UTIL;
    }

    public static Font getFont(Font font) {
        double scale = UIUtil.getInstance().getScale();
        return new Font(font.getName(), font.getStyle(), (int) (font.getSize() * scale));
    }

    public static void setFontList(JComponent jPanel) {
        for (Component component : jPanel.getComponents()) {
            setFont(component);
        }
    }

    private static void setFont(Component component) {
        if (component instanceof JTextField || component instanceof JComboBox || component instanceof JButton ||
                component instanceof JTree || component instanceof JTable || component instanceof JList ||
                component instanceof JTabbedPane || component instanceof JRadioButton ||
                component instanceof JCheckBox) {
            component.setFont(getFont(component.getFont()));
        } else if (component instanceof JComponent) {
            setFontList((JComponent) component);
        }
    }

    public static void setFont(JComponent jComponent) {
        jComponent.setFont(getFont(jComponent.getFont()));
    }

    public static void setFont(JComponent... jComponents) {
        for (JComponent jComponent : jComponents) {
            setFont(jComponent);
        }
    }

    public static Font getFont(String fontName, Font font) {
        double scale = UIUtil.getInstance().getScale();
        return new Font(fontName, font.getStyle(), (int) (font.getSize() * scale));
    }

    public static void setFont(String fontName, JComponent jComponent) {
        jComponent.setFont(getFont(fontName, jComponent.getFont()));
    }

    public static void setFont(String fontName, JComponent... jComponents) {
        for (JComponent jComponent : jComponents) {
            setFont(fontName, jComponent);
        }
    }

    public double getScale() {
        if (scale == 0) {
            int dpi = JDPILibrary.getDPI();
            scale = JDPILibrary.scale(dpi);
        }
        return scale;
    }
}
