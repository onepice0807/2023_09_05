package com.ray.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class Testinterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// 컨트롤러가 호출되기 이전에 호출됨
		
		System.out.println("Testinterceptor - preHandle() 호출");
		
		return true; // true:원래의 Controller단 호출, false :원래의 Controller단 호출x
		
		
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		System.out.println("Testinterceptor - postHandle() 호출!");
		super.postHandle(request, response, handler, modelAndView);
	}
	
}
