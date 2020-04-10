package com.homw.common.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringEscapeUtils;

/**
 * @description 编解码工具类
 * @author Hom
 * @version 1.0
 * @since 2018-12-12
 */
public class CodecUtil {
	public static final String DEFAULT_ENCODING = "UTF-8";
	private static final char[] BASE62 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();

	/**
	 * Hex编码.
	 */
	public static String encodeHex(byte[] input) {
		return new String(Hex.encodeHex(input));
	}
	
	/**
	 * Hex编码.
	 */
	public static String encodeHex(short input) {
		return new String(Hex.encodeHex(short2Bytes(input)));
	}
	
	/**
	 * Hex编码.
	 */
	public static String encodeHex(int input) {
		return new String(Hex.encodeHex(int2Bytes(input)));
	}

	/**
	 * Hex解码.
	 */
	public static byte[] decodeHex(String input) {
		try {
			return Hex.decodeHex(input.toCharArray());
		} catch (DecoderException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Base64编码.
	 */
	public static String encodeBase64(byte[] input) {
		return new String(Base64.encodeBase64(input));
	}

	/**
	 * Base64编码.
	 */
	public static String encodeBase64(String input) {
		try {
			return new String(Base64.encodeBase64(input.getBytes(DEFAULT_ENCODING)));
		} catch (UnsupportedEncodingException e) {
			return "";
		}
	}

	/**
	 * Base64解码.
	 */
	public static byte[] decodeBase64(String input) {
		return Base64.decodeBase64(input.getBytes());
	}

	/**
	 * Base64解码.
	 */
	public static String decodeBase64Str(String input) {
		try {
			return new String(Base64.decodeBase64(input.getBytes()), DEFAULT_ENCODING);
		} catch (UnsupportedEncodingException e) {
			return "";
		}
	}

	/**
	 * Base62编码。
	 */
	public static String encodeBase62(byte[] input) {
		char[] chars = new char[input.length];
		for (int i = 0; i < input.length; i++) {
			chars[i] = BASE62[((input[i] & 0xFF) % BASE62.length)];
		}
		return new String(chars);
	}

	/**
	 * Html 转码.
	 */
	public static String escapeHtml(String html) {
		return StringEscapeUtils.escapeHtml4(html);
	}

	/**
	 * Html 解码.
	 */
	public static String unescapeHtml(String htmlEscaped) {
		return StringEscapeUtils.unescapeHtml4(htmlEscaped);
	}

	/**
	 * Xml 转码.
	 */
	public static String escapeXml(String xml) {
		return StringEscapeUtils.escapeXml10(xml);
	}

	/**
	 * Xml 解码.
	 */
	public static String unescapeXml(String xmlEscaped) {
		return StringEscapeUtils.unescapeXml(xmlEscaped);
	}

	/**
	 * URL 编码, Encode默认为UTF-8.
	 */
	public static String urlEncode(String part) {
		try {
			return URLEncoder.encode(part, DEFAULT_ENCODING);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * URL 解码, Encode默认为UTF-8.
	 */
	public static String urlDecode(String part) {
		try {
			return URLDecoder.decode(part, DEFAULT_ENCODING);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * short转byte数组
	 * 
	 * @param s
	 * @return
	 */
	public static byte[] short2Bytes(short s) {
		byte[] arr = new byte[2];
		arr[0] = (byte) (0xFF & (s >> 8));// high
		arr[1] = (byte) (0xFF & s);// low
		return arr;
	}

	/**
	 * 无符号short转byte数组
	 * 
	 * @param i
	 * @return
	 */
	public static byte[] unsignedShort2Bytes(int i) {
		byte[] arr = new byte[2];
		arr[0] = (byte) (0xFF & (i >>> 8));// high
		arr[1] = (byte) (0xFF & i);// low
		return arr;
	}

	/**
	 * int转byte数组
	 * 
	 * @param i
	 * @return
	 */
	public static byte[] int2Bytes(int i) {
		byte[] arr = new byte[4];
		// 由高位到低位
		arr[0] = (byte) ((i >> 24) & 0xFF);
		arr[1] = (byte) ((i >> 16) & 0xFF);
		arr[2] = (byte) ((i >> 8) & 0xFF);
		arr[3] = (byte) (i & 0xFF);
		return arr;
	}

	/**
	 * byte数组转short
	 * 
	 * @param bArr
	 * @return
	 */
	public static short bytes2short(byte[] bArr) {
		return (short) (((bArr[0] & 0x00FF) << 8) | (0x00FF & bArr[1]));
	}
}
