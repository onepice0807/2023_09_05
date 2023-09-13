package com.ray.interceptor;

import java.sql.Timestamp;

import javax.inject.Inject;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.WebUtils;

import com.ray.etc.SessionCheck;
import com.ray.service.member.MemberService;
import com.ray.vodto.Member;
import com.ray.vodto.SessionDTO;

public class loginInterceptor extends HandlerInterceptorAdapter {

	@Inject
	private MemberService service;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		System.out.println("loginInterceptor - preHandle() 호출");
		boolean showLoginPage = false;
		// 쿠키가 있는지 없는지 검사
		Cookie loginCookie = WebUtils.getCookie(request, "session");
		System.out.println("chk1");
		if (loginCookie != null) {
			System.out.println("chk2");
			// 쿠키가 있다면, 쿠키에 저장된 세션과 DB에 저장된 sessionkey가 같은지 비교하고,
			// DB에 저장된 세션 리미트가 현재시간보다 큰지 비교
			String cookieValue = loginCookie.getValue();

			Member autoLoginUser = service.checkAutoLoginUser(cookieValue);

			if (autoLoginUser != null) {

				// 로그인 처리
				SessionCheck.replaceSessionKey(request.getSession(), autoLoginUser.getUserId());
				WebUtils.setSessionAttribute(request, "loginMember", autoLoginUser);
				

				if (WebUtils.getSessionAttribute(request, "returnPath") != null) {

					response.sendRedirect((String) WebUtils.getSessionAttribute(request, "returnPath"));

				} else {

					response.sendRedirect("/");
				}

			}
	
		} else if (request.getMethod().equals("GET") && request.getParameter("redirectUrl") != null) { // GET방식일때만 동작

			if (!request.getParameter("redirectUrl").equals("")) {

				if (request.getParameter("redirectUrl").contains("viewBoard")) {
					String uri = "/board/viewBoard";
					String queryStr = "?no=" + request.getParameter("no");

					request.getSession().setAttribute("returnPath", uri + queryStr);
				}
			}

			showLoginPage = true;
		} else {
			showLoginPage = true;
		}

		return showLoginPage;
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

			System.out.println("로그인한 유저 :" + loginMember.getUserId() + "세션아이디 :" + ses.getId());

			// 최초 로그인 하면 세션 아이디 값을 로그인한 유저아이디로 교체
			// 중복 로그인 (기존로그인 정보를 지우고 새로 로그인한 아이디로 교체)
			SessionCheck.replaceSessionKey(ses, loginMember.getUserId());

			// 자동로그인을 체크한 유저라면 쿠키 저장, DB에도 저장
			if (request.getParameter("remember") != null) {
				System.out.println("자동로그인 유저");

				String sessionValue = ses.getId();
//				Timestamp now = new Timestamp(System.currentTimeMillis());
				Timestamp sesLimit = new Timestamp(System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 7));

				Cookie loginCookie = new Cookie("session", sessionValue);
				loginCookie.setMaxAge(60 * 60 * 24 * 7);
				loginCookie.setPath("/");

				if (service.remeber(new SessionDTO(loginMember.getUserId(), sesLimit, sessionValue))) {
					response.addCookie(loginCookie); // 쿠키 저장
				}

			}

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
