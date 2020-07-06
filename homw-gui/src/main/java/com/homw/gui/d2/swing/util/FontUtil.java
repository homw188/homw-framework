package com.homw.gui.d2.swing.util;

import cn.hutool.core.io.IoUtil;
import com.homw.gui.d2.swing.SWT;
import sun.swing.SwingUtilities2;

import java.awt.*;
import java.io.InputStream;

/**
 * @description 字体工具类
 * @author Hom
 * @version 1.0
 * @since 2020-07-05
 */
@SuppressWarnings("restriction")
public class FontUtil {

    /**
     * 获取屏幕自适应大小字体
     * @param isBlod
     * @return
     */
    public static Font getPreferedFont(boolean isBlod) {
        if (SWT.Size.getScreen().width > 1600) {
            return isBlod ? SWT.FontConst.BLOD_20 : SWT.FontConst.PLAIN_20;
        } else if (SWT.Size.getScreen().width > 1400) {
            return isBlod ? SWT.FontConst.BLOD_18 : SWT.FontConst.PLAIN_18;
        } else if (SWT.Size.getScreen().width > 1200) {
            return isBlod ? SWT.FontConst.BLOD_16 : SWT.FontConst.PLAIN_16;
        } else {
            return isBlod ? SWT.FontConst.BLOD_14 : SWT.FontConst.PLAIN_14;
        }
    }

    /**
     * 创建字体
     * @param fontFile
     * @return
     */
    public static Font createFont(String fontFile) {
        Font font = null;
        InputStream input = null;
        try {
            input = FontUtil.class.getResourceAsStream(fontFile);
            font = Font.createFont(Font.TRUETYPE_FONT, input);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IoUtil.close(input);
        }
        return font;
    }

    /**
     * 获取字符串像素长度
     * @param font
     * @param text
     * @return
     */
    public static int getTextWidth(Font font, String text) {
        return SwingUtilities2.getFontMetrics(null, font).stringWidth(text);
    }

    /**
     * 获取字体像素高度
     * @param font
     * @return
     */
    public static int getFontHeight(Font font) {
        return SwingUtilities2.getFontMetrics(null, font).getHeight();
    }
}
