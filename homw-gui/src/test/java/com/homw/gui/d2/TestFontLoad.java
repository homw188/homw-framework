package com.homw.gui.d2;

import com.homw.gui.d2.swing.util.FontUtil;
import com.homw.gui.d2.swing.UIInitializer;
import com.homw.gui.d2.swing.util.WindowUtil;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;

/**
 * @description AWT字体加载测试
 * @author Hom
 * @version 1.0
 * @since 2020-07-01
 */
public class TestFontLoad {
    @Test
    public void test() throws Exception {
        UIInitializer.init();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                WindowUtil.showFrame(drawContent(), "Test Font");
            }
        });

        // block...
        System.in.read();
    }

    private static JComponent drawContent() {
        JLabel numLabel = new JLabel("1234567890");
        numLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // 1、加载.ttf字体
        Font LEDFont = FontUtil.createFont("/fonts/UnidreamLED.ttf");

        // 2、设置字体大小和样式
        LEDFont = LEDFont.deriveFont(Font.ITALIC, 120f);

        // 3、应用LED字体
        numLabel.setFont(LEDFont);

        return numLabel;
    }
}
