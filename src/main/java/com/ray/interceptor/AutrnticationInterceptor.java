package com.ray.interceptor;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.ray.etc.DestinationPath;
import com.ray.service.board.BoardService;
import com.ray.vodto.Member;

public class AutrnticationInterceptor extends HandlerInterceptorAdapter {
	
	@Inject
	private BoardService service;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		boolean result = false;
		System.out.println("AutrnticationInterceptor - preHandle()");

		
		String requestUri = request.getRequestURI();

		HttpSession ses = request.getSession();
		// 로그인을 했는지 안했는지 확인
		if (((Member) ses.getAttribute("loginMember")) != null) {
			// 로그인 했다면 (하던일 지속되게 하기)
			
			
			if(requestUri.contains("modifyBoard") || requestUri.contains("remBoard")) {
				// 만약 게시판 글 수정/ 글 삭제 페이지에서 온것이라면...
				// 로그인 한 유저가 글 수정 삭제 권한이 있는 유저인지 판별
				String loginUser = ((Member) ses.getAttribute("loginMember")).getUserId();
				int no = Integer.parseInt(request.getParameter("no"));
				if(!service.getBoardWriterByNo(no).equals(loginUser)) {
					// 글 작성자와 로그인 유저가 같지 않다 => 수정 삭제 불가
					response.sendRedirect("viewBoard?status=notPermission&no=" + no);
					return false;
				}
			}
				
			result = true;
		} else if (((Member) ses.getAttribute("loginMember")) == null) {
			// 아니라면 로그인 페이지로 보내기

			DestinationPath.savePrePath(request);

			response.sendRedirect("/member/login");
			return false;
			// 로그인을 하면 (하던일 지속되게 하기)
		}

		return result;
	}

}
