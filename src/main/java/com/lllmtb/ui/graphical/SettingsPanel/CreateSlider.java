package com.lllmtb.ui.graphical.SettingsPanel;

import javax.swing.JSlider;

public class CreateSlider {
    public static JSlider createSlider(int min, int max, int value, int spacing) {
        JSlider slider = new JSlider(JSlider.HORIZONTAL, min, max, value);

        slider.setMajorTickSpacing(spacing);
        slider.setSnapToTicks(true);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);

        return slider;
    }
}
