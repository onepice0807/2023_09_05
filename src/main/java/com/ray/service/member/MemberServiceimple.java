package com.ray.service.member;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ray.persistence.MemberDAO;
import com.ray.persistence.PointLogDAO;
import com.ray.vodto.Member;
import com.ray.vodto.MemberDTO;
import com.ray.vodto.PointLog;
import com.ray.vodto.SessionDTO;

@Service
public class MemberServiceimple implements MemberService {

	@Inject
	private MemberDAO mdao;
	@Inject
	private PointLogDAO pdao;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Member login(MemberDTO tmpMember) throws Exception {
		// 로그인 해본다
		Member loginMember = mdao.login(tmpMember);
		if(loginMember != null) {
			// 로그인이 성공하면 member테이블에 userpoint update
			if(mdao.updateUserPoint("로그인", loginMember.getUserId()) == 1) {
				// 3) 2번 이후에 pointlog 테이블에 insret
				pdao.insertPointLog(new PointLog(-1, null, "로그인", 5, loginMember.getUserId()));
			}
		}
		
		return loginMember;
	}

	@Override
	public boolean remeber(SessionDTO sesDto) throws Exception {
		boolean result = false;
		
		if(mdao.insertSession(sesDto) == 1) {
			result = true;
		}
		
		return result;
	}

	@Override
	public Member checkAutoLoginUser(String sessionKey) throws Exception {
		
		return mdao.selectAutoLoginUser(sessionKey);
	}

}
