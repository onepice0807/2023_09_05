package com.ray.service.member;

import com.ray.vodto.Member;
import com.ray.vodto.MemberDTO;
import com.ray.vodto.SessionDTO;

public interface MemberService {
	
	// 로그인
	Member login(MemberDTO tmpMember)throws Exception;
	
	// 자동 로그인을 위한 세션키, 세션 limit 저장
	boolean remeber(SessionDTO sesDto)throws Exception;
	
	// 자동 로그인 유저 체크
	Member checkAutoLoginUser(String sessionKey)throws Exception;
	
}
