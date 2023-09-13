package com.ray.persistence;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.ray.vodto.Member;
import com.ray.vodto.MemberDTO;
import com.ray.vodto.SessionDTO;

@Repository   // 아래의 클래스가 DAO객체임을 명시
public class MemberDAOImpl implements MemberDAO {

	@Inject
	private SqlSession ses;  // SqlSessionTemplate 객체 주입
	
	private static String ns = "com.webshjin.mappers.MemberMapper";
	
	@Override
	public String getDate() {
		// Connection 
		
		String q = ns + ".curDate";
		
		return ses.selectOne(q);
	}

	@Override
	public int updateUserPoint(String why, String userId) throws Exception {
		Map<String, Object> param = new HashMap<String, Object>();
		
		param.put("why" , why);
		param.put("userId" , userId);
		
		return ses.update(ns + ".updateUserPoint", param);
	}

	@Override
	public Member login(MemberDTO tmpLogin) throws Exception {
		
		return ses.selectOne(ns + ".login", tmpLogin);
	}

	@Override
	public int insertSession(SessionDTO sesDTO) throws Exception {
		// TODO Auto-generated method stub
		return ses.update(ns + ".insertSessionKey", sesDTO);
	}

	@Override
	public Member selectAutoLoginUser(String sessionKey) throws Exception {
		// TODO Auto-generated method stub
		return ses.selectOne(ns + ".selectAuto", sessionKey);
	}

	

}
