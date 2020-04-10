package com.homw.test.shiro;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;

/**
 * @description 请求认证过滤器
 * @author Hom
 * @version 1.0
 * @since 2020-03-18
 */
public class UserFormAuthenticationFilter extends FormAuthenticationFilter {

	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
		HttpServletResponse httpServletResponse = WebUtils.toHttp(response);

		if (isLoginRequest(request, response)) {
			// POST请求，则返回true
			if (isLoginSubmission(request, response)) {
				return executeLogin(request, response);
			}
		} else {
			// 处理ajax请求，显示层需根据响应头跳转至登录页
			if (httpServletRequest.getHeader("x-requested-with") != null
					&& httpServletRequest.getHeader("x-requested-with").equalsIgnoreCase("XMLHttpRequest")) {
				httpServletResponse.setHeader("ajax-session-status", "invalid");
			}
			// 重定向至登录页
			saveRequestAndRedirectToLogin(request, response);
		}
		return false;
	}
}
