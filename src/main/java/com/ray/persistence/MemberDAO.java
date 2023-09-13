package com.ray.persistence;

import com.ray.vodto.Member;
import com.ray.vodto.MemberDTO;
import com.ray.vodto.SessionDTO;

public interface MemberDAO {
	// 현재날짜와 현재 시간을 얻어오는
	public String getDate();
	
	// member 테이블에 포인트 증감
	public int updateUserPoint(String why, String userId) throws Exception;
	
	//로그인
	Member login(MemberDTO tmpLogin) throws Exception;
	
	// 자동로그인을 위한 세션키, 세션만료일 저장
	int insertSession(SessionDTO sesDTO) throws Exception;

	public Member selectAutoLoginUser(String sessionKey) throws Exception ;

}
