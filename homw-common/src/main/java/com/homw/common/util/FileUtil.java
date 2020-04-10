package com.homw.common.util;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang3.StringUtils;

/**
 * @description 文件工具类
 * @author Hom
 * @version 1.0
 * @since 2019-05-20
 */
public final class FileUtil {
	private FileUtil() {
	}

	/**
	 * 检查文件扩展名
	 * 
	 * @param fileName   文件名
	 * @param extensions 扩展名集
	 * @return
	 */
	public static boolean checkExtension(String fileName, String... extensions) {
		if (StringUtils.isEmpty(fileName) || !fileName.contains(".") || extensions == null) {
			return false;
		}
		String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
		List<String> extList = Arrays.asList(extensions);
		return extList.contains(ext);
	}

	/**
	 * 获取配置
	 * 
	 * @param properties
	 * @return
	 */
	public static Configuration getPropertiesConfig(String properties) {
		try {
			return new PropertiesConfiguration(properties);
		} catch (ConfigurationException e) {
			throw new RuntimeException("获取配置文件失败，", e);
		}
	}

	/**
	 * 读取文件内容
	 * 
	 * @param file path
	 * @return
	 */
	public static String readFileContent(String file) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			reader = new BufferedReader(
					new InputStreamReader(FileUtil.class.getClassLoader().getResourceAsStream(file)));
		}

		StringBuffer content = new StringBuffer();
		String line = "";
		try {
			while ((line = reader.readLine()) != null) {
				content.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return content.toString();
	}

	/**
	 * 下载文件
	 * 
	 * @param fileUrl
	 * @param dir
	 */
	public static void downloadFile(String fileUrl, String dir) {
		InputStream in = null;
		OutputStream out = null;
		try {
			// 创建图片目录
			File fileDir = new File(dir);
			if (!fileDir.exists()) {
				fileDir.mkdirs();
			}

			String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
			File file = new File(dir + File.separator + fileName);
			// 图片不存在，则下载
			if (!file.exists()) {
				URL url = new URL(fileUrl);
				in = url.openStream();
				out = new FileOutputStream(file);

				byte[] buff = new byte[1024];
				int len = 0;
				while ((len = in.read(buff)) != -1) {
					out.write(buff, 0, len);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			closeAll(out, in);
		}
	}

	/**
	 * 关闭资源
	 * 
	 * @param resources
	 */
	private static void closeAll(Closeable... resources) {
		for (Closeable src : resources) {
			if (src != null) {
				try {
					src.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}