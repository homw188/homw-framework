package com.homw.schedule.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.homw.common.bean.SystemMessage;
import com.homw.common.exception.SystemException;
import com.homw.common.exception.SystemRuntimeException;

/**
 * @description Controller异常处理解析器
 * @author Hom
 * @version 1.0
 * @since 2020-03-26
 */
@Component
public class ExceptionHandleResolver implements HandlerExceptionResolver {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		SystemMessage message = new SystemMessage();
		try {
			response.setContentType("application/json;charset=utf-8");
			response.setCharacterEncoding("utf-8");

			if (ex instanceof SystemRuntimeException) {
				message.put("code", ((SystemRuntimeException) ex).getCode());
				message.put("msg", ex.getMessage());
			} else if (ex instanceof DuplicateKeyException) {
				message = SystemMessage.error("数据库中已存在该记录");
			} else if (ex instanceof SystemException) {
				message = SystemMessage.error();
			} else {
				message = SystemMessage.error();
			}

			// 记录异常日志
			logger.error(ex.getMessage(), ex);

			String json = JSON.toJSONString(message);
			response.getWriter().print(json);
		} catch (Exception e) {
			logger.error("ExceptionHandleResolver异常处理失败", e);
		}
		return new ModelAndView();
	}
}
