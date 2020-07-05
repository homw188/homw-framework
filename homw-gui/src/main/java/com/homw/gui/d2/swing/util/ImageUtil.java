package com.homw.gui.d2.swing.util;

import cn.hutool.core.io.FileUtil;
import com.homw.gui.d2.swing.SWT;
import org.apache.commons.lang3.StringUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

/**
 * @description 图形工具类
 * @author Hom
 * @version 1.0
 * @since 2020-07-01
 */
public class ImageUtil {

	// 截图用
	private static Robot robot = null;

	/**
	 * {@link Image} 转换为 {@link BufferedImage}
	 * @param image
	 * @return
	 */
    public static BufferedImage bufferedImage(Image image) {
        if (image instanceof BufferedImage) {
            return (BufferedImage) image;
        }
        // copy
        BufferedImage bufferedImage = new BufferedImage(image.getWidth(null),
                        image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics g = bufferedImage.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return bufferedImage;
    }

	/**
	 * 图片拷贝
	 * @param image
	 * @return
	 */
	public static BufferedImage copyBufferedImage(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int[] pixels = new int[width * height];

        BufferedImage destImage = new BufferedImage(width, height, image.getType());
        image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, width);
        destImage.setRGB(0, 0, width, height, pixels, 0, width);
        return destImage;
    }

	/**
	 * 保存图片，存在则替换
	 * @param image
	 * @param filePath 默认png格式
	 */
	public static void saveImage(BufferedImage image, String filePath) {
		try {
			File file = new File(filePath);
			if (file.exists()) {
				file.delete();
			}
			String ext  = FileUtil.extName(filePath);
			if (StringUtils.isEmpty(ext)) {
				ext = "png";
			}
			ImageIO.write(image, ext, file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 截屏并保存
	 * @param filePath
	 */
	public static void saveCaptureImage(String filePath) {
		Dimension screen = SWT.Size.getScreen();
		BufferedImage captureImage = captureImage(0, 0, screen.width, screen.height);
		ImageUtil.saveImage(captureImage, filePath);
	}

	/**
	 * 截图，根据矩形坐标
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 * @return
	 */
	public static synchronized BufferedImage captureImage(int x, int y, int w, int h) {
		if (robot == null) {
			try {
				robot = new Robot();
			} catch (AWTException e) {
				return null;
			}
		}
		return robot.createScreenCapture(new Rectangle(x, y, w, h));
	}

	/**
	 * 绘制带文字的图片
	 * @param text
	 * @return
	 */
    public static BufferedImage drawDataImage(String text) {
    	int arc = 8;
    	int padding = 5;
    	float vAlignMiddleFactor = 0.1F;
		Font textFont = SWT.FontConst.BLOD_18;

		// 计算画布大小
		int width = 100;// default
    	if (StringUtils.isNotEmpty(text)) {
			width = FontUtil.getTextWidth(textFont, text) + padding * 2;
		}
		int height = FontUtil.getFontHeight(textFont) + padding * 2;

    	// 创建画布
        RoundRectangle2D.Float imageRec = new RoundRectangle2D.Float(0, 0, width, height, arc, arc);
        BufferedImage dataImage = new BufferedImage((int) (imageRec.width) + 2,
				(int) (imageRec.height) + 2, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g2d = dataImage.createGraphics();
        g2d.setColor(Color.black);

        // 绘制背景
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f));
        g2d.fill(imageRec);

        // 绘制文字
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        g2d.setFont(textFont);
        g2d.setColor(Color.white);
        g2d.drawString(text, padding + 1, height - padding - 1 - (int) (height * vAlignMiddleFactor));

        // 绘制边框
        g2d.drawRoundRect(0, 0, dataImage.getWidth(), dataImage.getHeight(), arc, arc);
        g2d.dispose();
        return dataImage;
    }

	/**
	 * 获取图片像素数据
	 * @param image
	 * @return
	 */
	public static ByteBuffer imageData(BufferedImage image) {
		ByteBuffer byteBuf = null;
		DataBuffer dataBuf = image.getRaster().getDataBuffer();
		if (dataBuf instanceof DataBufferByte) {
			byte[] pixelData = ((DataBufferByte) dataBuf).getData();
			byteBuf = ByteBuffer.wrap(pixelData);
		} else if (dataBuf instanceof DataBufferUShort) {
			short[] pixelData = ((DataBufferUShort) dataBuf).getData();
			byteBuf = ByteBuffer.allocate(pixelData.length * 2);
			byteBuf.asShortBuffer().put(ShortBuffer.wrap(pixelData));
		} else if (dataBuf instanceof DataBufferShort) {
			short[] pixelData = ((DataBufferShort) dataBuf).getData();
			byteBuf = ByteBuffer.allocate(pixelData.length * 2);
			byteBuf.asShortBuffer().put(ShortBuffer.wrap(pixelData));
		} else if (dataBuf instanceof DataBufferInt) {
			int[] pixelData = ((DataBufferInt) dataBuf).getData();
			byteBuf = ByteBuffer.allocate(pixelData.length * 4);
			byteBuf.asIntBuffer().put(IntBuffer.wrap(pixelData));
		} else {
			throw new IllegalArgumentException("Not implemented for data buffer type: " + dataBuf.getClass());
		}
		return byteBuf;
	}
}
