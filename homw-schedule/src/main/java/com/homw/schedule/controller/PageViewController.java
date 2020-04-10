package com.homw.schedule.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @description 页面视图
 * @author Hom
 * @version 1.0
 * @since 2020-04-01
 */
@Controller
public class PageViewController {

	@RequestMapping("sys/{url}.html")
	public String sysPage(@PathVariable("url") String url) {
		return "sys/" + url + ".html";
	}
	
	@RequestMapping("schedule/{url}.html")
	public String page(@PathVariable("url") String url) {
		return url + ".html";
	}
}
