package com.dhht.controller.web;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * 访问分发
 * @author whx
 *
 */
@Controller
@RequestMapping("")
@Scope(value = "prototype")
public class VisitCotroller {
	
	/** 根目录访问 */
//	@RequestMapping(value = "")
//	public String visitPage(HttpServletRequest request) {
//		return "redirect:/login";
//	}

}
