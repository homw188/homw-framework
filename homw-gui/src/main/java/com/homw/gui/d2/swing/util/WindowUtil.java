package com.homw.gui.d2.swing.util;

import javax.swing.JComponent;
import javax.swing.JFrame;

/**
 * @description 窗口工具类
 * @author Hom
 * @version 1.0
 * @since 2020-07-05
 */
public class WindowUtil {

    /**
     * 打开窗口，显示组件
     * @param component
     * @param title
     */
    public static void showFrame(JComponent component, String title) {
        JFrame frame = new JFrame();
        frame.setTitle(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setSize(component.getPreferredSize());
        frame.setContentPane(component);

        // 居中显示
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
