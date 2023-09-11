package com.ray.controller;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * @packageName : com.ray.controller
 * @fileName : HomeController.java
 * @author : webshjin
 * @date : 2023. 9. 1.
 * @description : 기본 컨트롤러 단 클래스
 */
@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("index.jsp", locale);
		
		
		
		return "index";
	}
	
	@RequestMapping(value="/doInterceptorA")
	public void doInterceptorA() {
		logger.info("doInterceptorA() 컨트롤러가 호출");
	}
	
}
