package com.homw.common.util;

import java.io.IOException;
import java.text.SimpleDateFormat;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.homw.common.bean.NestClass;

/**
 * @description 基于Jackson的JSON转换器
 * @author Hom
 * @version 1.0
 * @since 2020-03-17
 */
public class JacksonConverter {
	private static ObjectMapper mapper = new ObjectMapper();
	static {
		// 日期格式化
		mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"));
		// Map按Key值排序
		mapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
		// JSON格式美化
		mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
		// 忽略空属性
		mapper.setSerializationInclusion(Include.NON_EMPTY);
		// 禁用非法属性异常
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
	}

	/**
	 * Json字符串转Object
	 * 
	 * @param jsonStr
	 * @param baseClass
	 * @return
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	public static Object json2Object(String jsonStr, NestClass baseClass)
			throws JsonParseException, JsonMappingException, IOException {
		return mapper.readValue(jsonStr, getJavaType(baseClass));
	}

	/**
	 * 获取Java 类别
	 * 
	 * @param baseClass
	 * @return
	 */
	private static JavaType getJavaType(NestClass baseClass) {
		if (baseClass.getKeyClass() == null) {
			return mapper.getTypeFactory().constructType(baseClass.getRootClass());
		} else {
			if (baseClass.getValClass() == null) {
				return mapper.getTypeFactory().constructParametricType(baseClass.getRootClass(),
						getJavaType(baseClass.getKeyClass()));
			} else {
				return mapper.getTypeFactory().constructParametricType(baseClass.getRootClass(),
						getJavaType(baseClass.getKeyClass()), getJavaType(baseClass.getValClass()));
			}
		}
	}

	/**
	 * Object转Json字符串
	 * 
	 * @param obj
	 * @return
	 * @throws JsonProcessingException
	 */
	public static String object2Json(Object obj) throws JsonProcessingException {
		return mapper.writeValueAsString(obj);
	}

	/**
	 * 获取Json Node，用于节点遍历
	 * 
	 * @param jsonStr
	 * @return
	 * @throws IOException
	 */
	public static JsonNode getJsonNode(String jsonStr) throws IOException {
		return mapper.readTree(jsonStr);
	}

	public static ObjectMapper getMapper() {
		return mapper;
	}

}
