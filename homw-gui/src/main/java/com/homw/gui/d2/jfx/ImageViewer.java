package com.homw.gui.d2.jfx;

import cn.hutool.core.io.FileUtil;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.stage.Stage;

/**
 * @description 图片查看器，支持缩放、拖拽操作
 * @author Hom
 * @version 1.0
 * @since 2020-07-01
 */
public class ImageViewer extends Application {
	private static String title, imagePath;
	private static int width, height;
	private int imgWidth, imgHeight;
	private ImageView imageView;

	@Override
	public void init() throws Exception {
		imageView = new ImageView();
		updateImage(new Image(FileUtil.getInputStream(imagePath)));
	}

	/**
	 * get viewer content.
	 * 
	 * @return
	 */
	private ScrollPane getContent() {
		ScrollPane scrollPane = new ScrollPane();
		//scrollPane.setScaleShape(false);
		scrollPane.setStyle("-fx-base: transparent ;-fx-background-color: #0f3d64e7;");

		DragEventHanlder dragEventHanlder = new DragEventHanlder();
		imageView.addEventHandler(MouseEvent.MOUSE_PRESSED, dragEventHanlder);
		imageView.addEventHandler(MouseEvent.MOUSE_DRAGGED, dragEventHanlder);
		imageView.addEventHandler(ScrollEvent.SCROLL, new ScaleEventHanlder());
		scrollPane.setContent(imageView);
		return scrollPane;
	}

	/**
	 * update image params.
	 * 
	 * @param img
	 */
	private void updateImage(Image img) {
		imageView.setImage(img);
		imgWidth = (int) imageView.getImage().getWidth();
		imgHeight = (int) imageView.getImage().getHeight();
	}

	/**
	 * Drag and drop to monitor treatment image.
	 * 
	 */
	private class DragEventHanlder implements EventHandler<MouseEvent> {
		private double x, y;

		@Override
		public void handle(MouseEvent event) {
			ImageView imageView = (ImageView) event.getSource();

			if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
				x = event.getX();
				y = event.getY();
			}

			if (event.getEventType() == MouseEvent.MOUSE_DRAGGED) {
				double deltax = event.getX() - x;
				double deltay = event.getY() - y;

				double scalex = (imageView.getScaleX() - 1) * imgWidth / 2;
				double scaley = (imageView.getScaleY() - 1) * imgHeight / 2;

				if (imageView.getTranslateX() + deltax - scalex > 0) {// left bounds
					imageView.setTranslateX(scalex);
				} else if (imageView.getTranslateX() + deltax < width - imgWidth - scalex) {// right bounds
					imageView.setTranslateX(width - imgWidth - scalex);
				} else {
					imageView.setTranslateX(imageView.getTranslateX() + deltax);
				}

				if (imageView.getTranslateY() + deltay - scaley > 0) {// top bounds
					imageView.setTranslateY(scaley);
				} else if (imageView.getTranslateY() + deltay < height - imgHeight - scaley) {// bottom bounds
					imageView.setTranslateY(height - imgHeight - scaley);
				} else {
					imageView.setTranslateY(imageView.getTranslateY() + deltay);
				}

				x = event.getX();
				y = event.getY();
			}
		}
	}

	/**
	 * Zooming to monitor processing.
	 * 
	 * @author James
	 * @version 1.0
	 */
	private class ScaleEventHanlder implements EventHandler<ScrollEvent> {
		private float factor = 0.01f;

		@Override
		public void handle(ScrollEvent event) {
			event.consume();
			ImageView imageView = (ImageView) event.getSource();

			double scalex = (imageView.getScaleX() - 1) * imgWidth / 2;
			double scaley = (imageView.getScaleY() - 1) * imgHeight / 2;

			if (event.getDeltaY() > 0) {
				imageView.setScaleX(imageView.getScaleX() + factor);
				imageView.setScaleY(imageView.getScaleY() + factor);
			} else {
				if (imageView.getTranslateX() - scalex < 0 && imageView.getTranslateX() + scalex > width - imgWidth) {// horizontal bounds
					imageView.setScaleX(imageView.getScaleX() - factor);
				}

				if (imageView.getTranslateY() - scaley < 0 && imageView.getTranslateY() + scaley > height - imgHeight) {// vertical bounds
					imageView.setScaleY(imageView.getScaleY() - factor);
				}
			}
		}
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Scene scene = new Scene(getContent(), width, height);

		primaryStage.setScene(scene);
		primaryStage.setTitle(title);
		primaryStage.setResizable(false);
		primaryStage.show();
	}

	/**
	 * Application settings.
	 * 
	 * @param imagePath
	 */
	public static void appSettings(String imagePath) {
		appSettings("ImageViewer", imagePath);
	}

	/**
	 * Application settings.
	 * 
	 * @param title
	 * @param imagePath
	 */
	public static void appSettings(String title, String imagePath) {
		appSettings(title, imagePath, 640, 480);
	}

	/**
	 * Application settings.
	 * 
	 * @param title
	 * @param imagePath
	 * @param width
	 * @param height
	 */
	public static void appSettings(String title, String imagePath, int width, int height) {
		ImageViewer.title = title;
		ImageViewer.imagePath = imagePath;
		ImageViewer.width = width;
		ImageViewer.height = height;
	}
}