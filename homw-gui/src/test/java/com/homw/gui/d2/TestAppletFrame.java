package com.homw.gui.d2;

import com.homw.gui.d2.swing.AppletFrame;
import com.homw.gui.d2.swing.SWT;
import com.homw.gui.d2.swing.UIInitializer;
import org.junit.Test;

import javax.swing.*;

/**
 * @description Applet Window 测试
 * @author Hom
 * @version 1.0
 * @since 2020-07-29
 */
public class TestAppletFrame {
    @Test
    public void test() throws Exception {
        UIInitializer.init();

        AppletFrame frame = new AppletFrame(new JApplet(){
            @Override
            public void init() {
                getContentPane().add(new JLabel("Applet frame test"));
            }
        }, SWT.Size.getScreen().width, SWT.Size.getScreen().height);
        frame.run();

        // block...
        System.in.read();
    }
}
