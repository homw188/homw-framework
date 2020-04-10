package com.homw.common.util;

import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @description 校验工具类
 * @author Hom
 * @version 1.0
 * @since 2020-03-17
 */
public final class VerifyUtil {
	/**
	 * 手机号校验.
	 * 
	 * @param phoneNumber
	 * @return
	 */
	public static boolean verifyPhoneNumber(String phoneNumber) {
		String regular = "^1\\d{10}$";
		return verifyByRegex(phoneNumber, regular);
	}

	/**
	 * ip地址校验.
	 * 
	 * @param ip
	 * @return
	 */
	public static boolean verifyIpAddress(String ip) {
		String regular = "^(\\d|[1-9]\\d|1\\d{2}|2[0-5][0-5])\\.(\\d|[1-9]\\d|1\\d{2}|2[0-5][0-5])"
				+ "\\.(\\d|[1-9]\\d|1\\d{2}|2[0-5][0-5])\\.(\\d|[1-9]\\d|1\\d{2}|2[0-5][0-5])$";
		return verifyByRegex(ip, regular);
	}

	/**
	 * 端口校验
	 * 
	 * @param port
	 * @return
	 */
	public static boolean verifyPort(int port) {
		String regular = "^([0-9]|[1-9]\\d{1,3}|[1-5]\\d{4}|6[0-5]{2}[0-3][0-5])$";
		return verifyByRegex(port + "", regular);
	}

	/**
	 * 数字校验.
	 * 
	 * @param num
	 * @return
	 */
	public static boolean verifyNum(String num) {
		String regular = "[0-9]\\d*";
		return verifyByRegex(num, regular);
	}

	/**
	 * 浮点型数字校验.
	 * 
	 * @param floa
	 * @return
	 */
	public static boolean verifyFloat(String floa) {
		String regular = "[0-9]\\d*.\\d*|0.\\d*[0-9]\\d*";
		return verifyByRegex(floa, regular);
	}

	/**
	 * 校验坐标点是否在多边形中.
	 * 
	 * @param target  point
	 * @param polygon
	 * @return if true, contains
	 */
	public static boolean verifyPointInPolygon(Point2D.Double target, List<Point2D.Double> polygon) {
		if (polygon == null)
			return false;

		GeneralPath path = new GeneralPath();

		Point2D.Double first = polygon.get(0);
		path.moveTo(first.x, first.y);

		for (Point2D.Double point : polygon)
			path.lineTo(point.x, point.y);

		path.lineTo(first.x, first.y);
		path.closePath();
		return path.contains(target);
	}

	/**
	 * 正则表达式校验.
	 * 
	 * @param input
	 * @param regular
	 * @return
	 */
	public static boolean verifyByRegex(String input, String regular) {
		return Pattern.compile(regular).matcher(input).matches();
	}

	/**
	 * 数值校验.
	 * 
	 * @param numFloat
	 * @return
	 */
	public static boolean verifNumFloat(String numFloat) {
		String pattern = "^[0-9]+(\\.[0-9]+){0,1}$";
		return verifyByRegex(numFloat, pattern);
	}
}