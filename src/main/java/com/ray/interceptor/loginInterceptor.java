package com.ray.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.ray.vodto.Member;

public class loginInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		System.out.println("loginInterceptor - preHandle() 호출");
		if (request.getMethod().equals("GET")) { // GET방식일때만 동작
			
			
			if (!request.getParameter("redirectUrl").equals("") || request.getParameter("redirectUrl") != null) {

				if (request.getParameter("redirectUrl").contains("viewBoard")) {
					String uri = "/board/viewBoard";
					String queryStr = "?no=" + request.getParameter("no");
					
					request.getSession().setAttribute("returnPath", uri + queryStr);
				}
			}

		}

		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		System.out.println("loginInterceptor - postHandle() 호출");

		HttpSession ses = request.getSession();
		ModelMap modelMap = modelAndView.getModelMap();
		Member loginMember = (Member) modelMap.get("loginMember");

		if (loginMember != null) {
			ses.setAttribute("loginMember", loginMember); // 로그인 기록 세션에 남기기
			System.out.println(loginMember.toString());

			// 만약 게시판 글 수정/ 글 삭제 페이지에서 온것이라면...
			// 로그인 한 유저가 글 수정 삭제 권한이 있는 유저인지 판별

			String returnPath = "";
			if (ses.getAttribute("returnPath") != null) {
				returnPath = (String) ses.getAttribute("returnPath");
			}

			// 로그인 하지 않은상태로 게시판(글작성/수정/삭제)에 접근했을 경우에는 이전 경로로 가고
			// 이전경로가 없을때 로그이 했다면 / 로
			response.sendRedirect(!returnPath.equals("") ? returnPath : "/");
		}

	}

}
