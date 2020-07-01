package com.homw.gui.d2;

import org.junit.Test;

/**
 * @description 图片查看器测试
 * @author Hom
 * @version 1.0
 * @since 2020-07-01
 */
public class TestImageViewer {

	@Test
	public void test() throws Exception {
		ImageViewer.appSettings("images/panda_640x480.jpg");
		ImageViewer.launch(ImageViewer.class, new String[] {});
		// block...
		System.in.read();
	}
}
