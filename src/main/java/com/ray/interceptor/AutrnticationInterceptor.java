package com.ray.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.ray.etc.DestinationPath;
import com.ray.vodto.Member;

public class AutrnticationInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		boolean result = false;
		System.out.println("AutrnticationInterceptor - preHandle()");
		
		HttpSession ses = request.getSession();
		// 로그인을 했는지 안했는지 확인
		if(((Member)ses.getAttribute("loginMember")) != null) {
			// 로그인 했다면 (하던일 지속되게 하기)
			result = true;
		} else if (((Member)ses.getAttribute("loginMember")) == null) {
			// 아니라면 로그인 페이지로 보내기
			
			DestinationPath.savePrePath(request);
			
			response.sendRedirect("/member/login");
				return false;
				// 로그인을 하면 (하던일 지속되게 하기)
		} 
		
		return result;
	}



	
}
