package com.homw.test.rpc.http;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.GZIPInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.homw.common.bean.NestClass;
import com.homw.common.util.JacksonConverter;
import com.homw.common.util.Platform;
import com.homw.common.util.ReflectUtil;

/**
 * @description rpc请求
 * @author Hom
 * @version 1.0
 * @since 2020-03-24
 */
public class RpcConsumer {
	private static final Logger logger = LoggerFactory.getLogger(RpcConsumer.class);

	/**
	 * 响应结果是否经过GZIP压缩传输；
	 * {TOMCAT_HOME}/conf/server.xml中Connector节点新增
	 * [compression="on" compressionMinSize="2048" noCompressionUserAgents="gozilla, traviata" 
	 * compressableMimeType="application/json"]配置
	 */
	private static boolean compressByGzip = false;
	private static boolean responseToFile = false;// 是否将响应结果保存至文件

	/**
	 * 远程方法调用
	 * 
	 * @param            <T>
	 * @param url        地址
	 * @param method
	 * @param param
	 * @param jsonFormat 参数是否JSON格式
	 * @return
	 */
	@SuppressWarnings({ "unchecked" })
	public static <T> T rpc(String url, Method method, String param, boolean jsonFormat) {
		// print param
		logger.info("rpc参数：" + param);

		// send request
		String response = null;
		if (jsonFormat) {
			response = RpcConsumer.sendJsonPost(url, param);
		} else {
			response = RpcConsumer.sendPost(url, param);
		}

		// print result
		// logger.info("RMI结果：" + response);

		if (response.contains(RpcExceptionWrapper.EXCEPTION_FIELD)) {
			try {
				RpcExceptionWrapper wrapper = (RpcExceptionWrapper) JacksonConverter.json2Object(response,
						new NestClass(RpcExceptionWrapper.class));
				throw wrapper.getServiceException();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			try {
				return (T) JacksonConverter.json2Object(response, ReflectUtil.getNestReturnType(method));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 构建JSON格式的请求参数
	 * 
	 * @param className
	 * @param method
	 * @param args
	 * @return
	 */
	public static String buildRpcJsonParam(String className, Method method, Object[] args) {
		RpcParam param = new RpcParam();
		param.setClassName(className);
		param.setMethod(method.getName());
		param.setArgs(args == null ? new Object[0] : args);
		param.setParamTypes(ReflectUtil.getNestParamterType(method));

		try {
			return JacksonConverter.object2Json(param);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 构建请求参数
	 * 
	 * @param className
	 * @param method
	 * @param args
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static String buildRpcParam(String className, Method method, Object[] args) {
		StringBuilder param = new StringBuilder();
		param.append("className");
		param.append("=");
		param.append(className);
		param.append("&");

		param.append("method");
		param.append("=");
		param.append(method.getName());
		param.append("&");

		param.append("args");
		param.append("=");

		if (args != null && args.length != 0) {
			int i = 0;
			for (Object o : args) {
				param.append(o.toString());
				if (i++ < (args.length - 1)) {
					param.append(",");
				}
			}
		} else {
			param.append("null");
		}
		param.append("&");

		param.append("paramTypes");
		param.append("=");

		if (method.getParameterTypes().length != 0) {
			int i = 0;
			for (Class o : method.getParameterTypes()) {
				param.append(o.getName());
				if (i++ < (args.length - 1)) {
					param.append(",");
				}
			}
		} else {
			param.append("null");
		}
		return param.toString();
	}

	/**
	 * 发送GET请求
	 * 
	 * @param root  发送请求的URL
	 * @param param 请求参数，format:name1=value1&name2=value2
	 * @return
	 */
	public static String sendGet(String root, String param) {
		String result = "";
		HttpURLConnection conn = null;
		try {
			String path = root + "?" + param;
			URL url = new URL(path);

			// 打开和URL之间的连接
			conn = (HttpURLConnection) url.openConnection();

			commonSettings(conn);

			// 建立实际的连接
			conn.connect();

			result = getResponse(conn);
		} catch (Exception e) {
			logger.warn("发送GET请求出现异常！" + e.getMessage());
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
		return result;
	}

	/**
	 * 发送POST请求
	 * 
	 * @param root  发送请求的URL
	 * @param param 请求参数，format:name1=value1&name2=value2
	 * @return
	 */
	public static String sendPost(String root, String param) {
		String result = "";
		HttpURLConnection conn = null;
		OutputStreamWriter out = null;
		try {
			URL url = new URL(root);

			// 打开和URL之间的连接
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");

			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);

			commonSettings(conn);

			conn.connect();

			// 获取URLConnection对象对应的输出流
			out = new OutputStreamWriter(conn.getOutputStream(), "utf-8");
			// 发送请求参数
			out.write(param);
			// flush输出流的缓冲
			out.flush();

			result = getResponse(conn);
		} catch (Exception e) {
			logger.warn("发送 POST 请求出现异常！" + e);
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

			if (conn != null) {
				conn.disconnect();
			}
		}
		return result;
	}

	/**
	 * 发送POST请求，参数为JSON格式
	 * 
	 * @param root  发送请求的URL
	 * @param param 请求参数，JSON format
	 * @return
	 */
	public static String sendJsonPost(String root, String param) {
		String result = "";
		HttpURLConnection conn = null;
		OutputStreamWriter out = null;
		try {
			URL url = new URL(root);

			// 打开和URL之间的连接
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");

			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);

			commonSettings(conn);
			conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

			if (compressByGzip) {
				conn.setRequestProperty("Accept-Encoding", "gzip");
			}

			conn.connect();

			// 获取URLConnection对象对应的输出流
			out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
			// 发送请求参数
			out.write(param);
			// flush输出流的缓冲
			out.flush();

			if (compressByGzip) {
				result = getResponseFromGZip(conn);
			} else {
				result = getResponse(conn);
			}
		} catch (Exception e) {
			logger.warn("发送 POST 请求出现异常！" + e);
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

			if (conn != null) {
				conn.disconnect();
			}
		}
		return result;
	}

	/**
	 * 获取响应结果
	 * 
	 * @param conn
	 * @return
	 * @throws IOException
	 */
	private static String getResponse(HttpURLConnection conn) throws IOException {
		if (responseToFile) {
			saveResponseToFile(conn.getInputStream(), "response_plain.txt");
			return "";
		}

		String result = "";
		BufferedReader in = null;
		try {
			// 定义 BufferedReader输入流来读取URL的响应
			if (conn.getResponseCode() == 200) {
				in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			} else if (conn.getResponseCode() == 400) {
				in = new BufferedReader(new InputStreamReader(conn.getErrorStream(), "UTF-8"));
			}

			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 获取响应结果，GZIP压缩传输
	 * 
	 * @param conn
	 * @return
	 * @throws IOException
	 */
	private static String getResponseFromGZip(HttpURLConnection conn) throws IOException {
		if (responseToFile) {
			saveResponseToFile(conn.getInputStream(), "response_gzip.txt");
			return "";
		}

		String result = "";
		BufferedReader in = null;

		try {
			// 定义 BufferedReader输入流来读取URL的响应
			if (conn.getResponseCode() == 200) {
				in = new BufferedReader(new InputStreamReader(new GZIPInputStream(conn.getInputStream()), "UTF-8"));
			} else if (conn.getResponseCode() == 400 || conn.getResponseCode() == 500) {
				in = new BufferedReader(new InputStreamReader(new GZIPInputStream(conn.getErrorStream()), "UTF-8"));
			}

			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 保存响应结果至文件
	 * 
	 * @param response
	 * @param fileName
	 */
	private static void saveResponseToFile(InputStream response, String fileName) {
		BufferedReader reader = null;
		BufferedWriter writer = null;
		try {
			reader = new BufferedReader(new InputStreamReader(response));
			writer = new BufferedWriter(new FileWriter(createFile(fileName)));

			String line = null;
			while ((line = reader.readLine()) != null) {
				writer.write(line);
			}
			writer.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
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

			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 在{user.home}目录下创建文件
	 * 
	 * @param fileName
	 * @return
	 */
	private static File createFile(String fileName) {
		File file = new File(Platform.getUserHome() + File.separator + fileName);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			file.delete();
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return file;
	}

	/**
	 * 通用连接配置
	 * 
	 * @param conn
	 */
	private static void commonSettings(HttpURLConnection conn) {
		conn.setReadTimeout(30000);
		conn.setConnectTimeout(30000);

		// 设置通用的请求属性
		conn.setRequestProperty("Accept", "application/json");
		conn.setRequestProperty("Charset", "UTF-8");
		// conn.setRequestProperty("Connection", "Keep-Alive");
		conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
	}

}