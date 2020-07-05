package com.homw.gui.d2.swing.util;

import com.homw.gui.d2.swing.SWT;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

/**
 * @description 鼠标图标工具类
 * @author Hom
 * @version 1.0
 * @since 2020-07-05
 */
public class CursorUtil {

    /**
     * 创建鼠标图标
     * @param imagePath
     * @return
     */
    public static Cursor getCursor(String imagePath) {
        try {
            return Toolkit.getDefaultToolkit().createCustomCursor(
                    ImageIO.read(SWT.CursorConst.class.getClassLoader().getResource(imagePath)),
                    new Point(0, 0), imagePath.split("/")[2]);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
