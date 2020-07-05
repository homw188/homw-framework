package com.homw.gui.d2;

import com.homw.gui.d2.swing.util.ImageUtil;
import org.junit.Test;

import java.awt.image.BufferedImage;

/**
 * @description 图片绘制测试
 * @author Hom
 * @version 1.0
 * @since 2020-07-05
 */
public class TestImageDraw {
    @Test
    public void test() {
        BufferedImage image = ImageUtil.drawDataImage("draw image 测试");
        ImageUtil.saveImage(image, "/Users/admin/test_image_draw.png");
    }
}
