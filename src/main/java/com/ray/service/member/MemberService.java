package com.ray.service.member;

import com.ray.vodto.Member;
import com.ray.vodto.MemberDTO;

public interface MemberService {
	
	// 로그인
	Member login(MemberDTO tmpMember)throws Exception;
}
