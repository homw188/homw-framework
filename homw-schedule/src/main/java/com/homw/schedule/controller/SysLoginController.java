package com.homw.schedule.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import com.homw.common.bean.SystemMessage;
import com.homw.schedule.shiro.ShiroUtil;

/**
 * @description 登录
 * @author Hom
 * @version 1.0
 * @since 2020-03-31
 */
@Controller
public class SysLoginController {
	@Autowired
	private Producer producer;

	@RequestMapping("captcha.jpg")
	public void captcha(HttpServletResponse response) throws ServletException, IOException {
		response.setHeader("Cache-Control", "no-store, no-cache");
		response.setContentType("image/jpeg");

		// 生成文字验证码
		String text = producer.createText();
		// 生成图片验证码
		BufferedImage image = producer.createImage(text);
		// 保存到shiro session
		ShiroUtil.setSessionAttribute(Constants.KAPTCHA_SESSION_KEY, text);

		ServletOutputStream out = response.getOutputStream();
		ImageIO.write(image, "jpg", out);
	}

	@ResponseBody
	@RequestMapping(value = "/sys/login", method = RequestMethod.POST)
	public SystemMessage login(String username, String password, String captcha) throws IOException {
		String kaptcha = ShiroUtil.getKaptcha(Constants.KAPTCHA_SESSION_KEY);
		if (!captcha.equalsIgnoreCase(kaptcha)) {
			return SystemMessage.error("验证码不正确");
		}

		try {
			Subject subject = ShiroUtil.getSubject();
			// sha256加密
			password = new Sha256Hash(password).toHex();
			UsernamePasswordToken token = new UsernamePasswordToken(username, password);
			subject.login(token);
		} catch (UnknownAccountException e) {
			return SystemMessage.error(e.getMessage());
		} catch (IncorrectCredentialsException e) {
			return SystemMessage.error(e.getMessage());
		} catch (LockedAccountException e) {
			return SystemMessage.error(e.getMessage());
		} catch (AuthenticationException e) {
			return SystemMessage.error("账户验证失败");
		}
		return SystemMessage.ok();
	}

	@RequestMapping(value = "logout", method = RequestMethod.GET)
	public String logout() {
		ShiroUtil.logout();
		return "redirect:login.html";
	}
}
