package com.homw.web.support.interceptor;

import java.io.StringWriter;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * @description 访问日志拦截器
 * @author Hom
 * @version 1.0
 * @since 2020-04-02
 */
public class AccessLogInterceptor implements HandlerInterceptor {

	private static final Logger logger = LoggerFactory.getLogger(AccessLogInterceptor.class);

	@Override
	public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			Object handler) throws Exception {
		StringWriter sw = new StringWriter();
		// 解析请求头
		sw.append("<request>\t" + httpServletRequest.getMethod() + "\turl:" + httpServletRequest.getRequestURL() + "\theads:");
		Enumeration<String> it = httpServletRequest.getHeaderNames();
		while (it.hasMoreElements()) {
			String key = it.nextElement();
			sw.append(key).append("=").append(httpServletRequest.getHeader(key)).append("&");
		}
		// 解析请求参数
		sw.append("\tparams:");
		if (CollectionUtils.isEmpty(httpServletRequest.getParameterMap())) {
			sw.append("NONE");
		} else {
			for (Map.Entry<String, String[]> entry : httpServletRequest.getParameterMap().entrySet()) {
				for (String value : entry.getValue()) {
					sw.append(entry.getKey()).append("=").append(value).append("&");
				}
			}
		}
		// 记录日志
		logger.info(sw.toString());
		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			Object o, Exception e) throws Exception {
		logger.info("<response>\t" + httpServletRequest.getMethod() + "\turl:" + httpServletRequest.getRequestURL()
				+ "\tstatusCode:" + httpServletResponse.getStatus() + "\tcontent-type:"
				+ httpServletResponse.getContentType() + "\texception:" + (e == null ? "NONE" : e.getMessage()));
	}
	
	@Override
	public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o,
			ModelAndView modelAndView) throws Exception {
	}
}
