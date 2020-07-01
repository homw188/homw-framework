package com.homw.gui.d2;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import org.junit.Test;

/**
 * @description AWT字体加载测试
 * @author Hom
 * @version 1.0
 * @since 2020-07-01
 */
public class TestAWTLoadFont {
	@Test
	public void test() throws Exception {
		new MainFrame().launch();
		// block...
		System.in.read();
	}

	class MainFrame extends JFrame {
		private static final long serialVersionUID = 1L;

		public MainFrame() {
			this.setTitle("Test AWT Load Font");
			this.setSize(640, 280);
			this.setLayout(new BorderLayout());

			drawContent();

			this.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					System.exit(0);
				}
			});
			this.setLocationRelativeTo(null);
		}

		private void drawContent() {
			JLabel numLabel = new JLabel("1234567890");
			numLabel.setHorizontalAlignment(SwingConstants.CENTER);

			// 1、加载.ttf字体
			InputStream input = this.getClass().getResourceAsStream("/fonts/UnidreamLED.ttf");
			Font LEDFont = null;
			try {
				// 2、创建LED字体
				LEDFont = Font.createFont(Font.TRUETYPE_FONT, input);
			} catch (FontFormatException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			// 3、设置字体大小和样式
			LEDFont = LEDFont.deriveFont(Font.ITALIC, 120f);

			// 4、应用LED字体
			numLabel.setFont(LEDFont);

			this.add(numLabel, BorderLayout.CENTER);
		}

		public void launch() {
			this.setVisible(true);
		}
	}
}
