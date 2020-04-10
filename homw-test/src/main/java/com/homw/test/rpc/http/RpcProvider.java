package com.homw.test.rpc.http;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.homw.common.bean.NestClass;
import com.homw.common.util.JacksonConverter;

/**
 * @description 服务接口，控制转发器
 * @author Hom
 * @version 1.0
 * @since 2020-03-24
 */
@Controller
public class RpcProvider {
	private Map<String, Object> beanCache = new HashMap<String, Object>();

	/**
	 * 服务统一调用接口, JSON格式
	 * 
	 * @param jsonParam
	 * @return
	 */
	@ResponseBody
	@RequestMapping(path = { "/service/json" }, produces = "application/json; charset=UTF-8")
	public String action(@RequestBody String jsonParam) {
		String result = null;
		try {
			result = invoke((RpcParam) JacksonConverter.json2Object(jsonParam, new NestClass(RpcParam.class)));
		} catch (Exception e) {
			try {
				result = JacksonConverter.object2Json(new RpcExceptionWrapper(e));
			} catch (JsonProcessingException e1) {
				e1.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 服务调用
	 * 
	 * @param param
	 * @return
	 * @throws Exception
	 */
	private String invoke(RpcParam param) throws Exception {
		// 调整参数类别
		param.adapterArgsType();

		Object bean = getBean(param.getClassName());
		if (bean != null) {
			Method method = bean.getClass().getDeclaredMethod(param.getMethod(), param.getParamClasses());
			if (method != null) {
				method.setAccessible(true);
				return JacksonConverter.object2Json(method.invoke(bean, param.getArgs()));
			}
		}
		return null;
	}

	/**
	 * 获取接口实现Bean
	 * 
	 * @param className
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	private synchronized Object getBean(String className) throws Exception {
		Object bean = beanCache.get(className);
		if (bean == null) {
			Class clazz = Class.forName(className);
			bean = clazz.newInstance();
			beanCache.put(className, bean);
		}
		return bean;
	}

}
