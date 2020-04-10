package com.homw.web.support.resolver;

import java.lang.annotation.Annotation;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.homw.web.support.annotation.RequestSession;

/**
 * @description <code>@RequestSession<code>参数解析
 * @author Hom
 * @version 1.0
 * @since 2020-04-02
 */
public class RequestSessionArgumentResolver implements HandlerMethodArgumentResolver {
	
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(RequestSession.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer container,
			NativeWebRequest webRequest, WebDataBinderFactory factory) throws Exception {
		Annotation[] paramAnns = parameter.getParameterAnnotations();
		for (Annotation paramAnn : paramAnns) {
			if (RequestSession.class.isInstance(paramAnn)) {
				HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
				return request.getSession().getAttribute("USER_SESSION_KEY");
			}
		}
		return WebArgumentResolver.UNRESOLVED;
	}
}
