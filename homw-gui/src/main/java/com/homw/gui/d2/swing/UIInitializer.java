package com.homw.gui.d2.swing;

import com.alee.laf.WebLookAndFeel;

import javax.swing.*;
import java.awt.*;
import java.util.Enumeration;

/**
 * @description UI初始化器
 * @author Hom
 * @version 1.0
 * @since 2020-07-05
 */
public class UIInitializer {

    /**
     * UI样式初始化，汉化处理
     */
    public static void init() {
        try {
            WebLookAndFeel.install();
            // UIManager.setLookAndFeel(WebLookAndFeel.class.getCanonicalName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Enumeration<?> enumeration = UIManager.getDefaults().keys();
        String keyStr = null;
        while (enumeration.hasMoreElements()) {
            keyStr = enumeration.nextElement().toString();
            if (keyStr.toLowerCase().contains("font")) {
                UIManager.put(keyStr, SWT.FontConst.PLAIN_14);
            }

            if (keyStr.equals("TextComponent.copy")) {
                UIManager.put(keyStr, "复制");
            } else if (keyStr.equals("TextComponent.paste")) {
                UIManager.put(keyStr, "粘贴");
            } else if (keyStr.equals("TextComponent.cut")) {
                UIManager.put(keyStr, "剪切");
            }
        }
        UIManager.put("ProgressBar.selectionBackground", Color.black);
        UIManager.put("ProgressBar.selectionForeground", Color.black);
        UIManager.put("OptionPane.yesButtonText", "是");
        UIManager.put("OptionPane.noButtonText", "否");
        UIManager.put("OptionPane.okButtonText", "确认");
        UIManager.put("OptionPane.cancelButtonText", "取消");
        UIManager.put("TableUI", "javax.swing.plaf.basic.BasicTableUI");
    }
}