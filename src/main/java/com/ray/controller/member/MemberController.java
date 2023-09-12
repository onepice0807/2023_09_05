package com.ray.controller.member;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ray.etc.SessionCheck;
import com.ray.service.member.MemberService;
import com.ray.vodto.Member;
import com.ray.vodto.MemberDTO;

@Controller
@RequestMapping("/member/*")
public class MemberController {

	@Inject
	private MemberService mService; // MemberService 객체 주입

	private static Logger logger = LoggerFactory.getLogger(MemberController.class);

	@RequestMapping("login")
	public void loginGET() {
		// loginInterceptor의 preHandle()호출 후

		// /member/login.jsp가 반환

	}

	@RequestMapping(value = "login", method = RequestMethod.POST)
	public void loginPOST(MemberDTO tmpMember, Model model) throws Exception {
		logger.info(tmpMember + "로그인해보자");

		Member loginMember = mService.login(tmpMember);
		if (loginMember != null) {
			logger.info("로그인 성공 :" + loginMember.toString());
			model.addAttribute("loginMember", loginMember);
		} else {
			logger.info("로그인 실패 :");
			return;
		}
		
		// loginInterceptor의 postHandle()이 동작
		
	}

	@RequestMapping("logout")
	public String logout(HttpServletRequest req) {
		HttpSession ses = req.getSession();
			
		
		// 로그 아웃 시에 세션리스트에 담겨진 세션 제거
		SessionCheck.removeKey(((Member)ses.getAttribute("loginMember")).getUserId());

		return "redirect:/";
	}
}
