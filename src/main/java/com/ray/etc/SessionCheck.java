package com.ray.etc;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.ray.vodto.Member;

@WebListener // 웹 서버에게 SessionListener의 존재를 알림
public class SessionCheck implements HttpSessionListener {
	
	// Map<MAC주소, 유저아이디>
	
	// Map<세션아이디, 세션객체>
	private static Map<String, HttpSession> sessions = new ConcurrentHashMap<String, HttpSession>();
	

	/**
	 * @MethodName : duplicateLoginCheck
	 * @author : ray
	 * @param : loginUserSessionId
	 * @returnValue : boolean
	 * @description : 로그인한 세션과 동일한 세션이 있는지 검사
	 * @date : 2023. 9. 12.
	 */
	public static synchronized void replaceSessionKey(HttpSession ses, String loginUserId) {
		if(!sessions.containsKey(loginUserId) && sessions.containsValue(ses)) { 
			System.out.println(loginUserId + "최초의 로그인");
			sessions.put(loginUserId, ses);
			sessions.remove(ses.getId()); // 기존의 로그인하기 이전 값 지우기
		}else if (sessions.containsKey(loginUserId)) {
			System.out.println(loginUserId + "로 중복로그인 하려고함");
			removeKey(loginUserId); // 기존 로그인 기록 로그아웃
			sessions.put(loginUserId, ses);
		}
		
		printSessionsMap();
	}


	private static void printSessionsMap() {
		System.out.println("===현재 생성된 세션 리스트====");
		Set<String> keys = sessions.keySet();
		for(String key : keys) {
			System.out.println(key + ", " + sessions.get(key).toString());
		}
		System.out.println("===========================================");
	}
	
	
	@Override
	public synchronized void sessionCreated(HttpSessionEvent se) {
		System.out.println("세션이 생성되었습니다");
		System.out.println("생성된 세션 ID" + se.getSession().getId());

		// 세션이 생성되면 Map에 해당 세션 등록
		sessions.put(se.getSession().getId(), se.getSession());
		
		printSessionsMap();
	}

	@Override
	public synchronized void sessionDestroyed(HttpSessionEvent se) {
		// TODO Auto-generated method stub
		System.out.println("세션이 종료되었습니다"+ se.getSession().getId());
		
		// 세션이 종료되면 Map에서도 해당 세션 삭제
		if(sessions.containsKey(se.getSession().getId())) {
			se.getSession().invalidate();
			sessions.remove(se.getSession().getId());
			
			printSessionsMap();
			
		}
	}


	public static void removeKey(String userId) {
		if(sessions.containsKey(userId)) {
			(sessions.get(userId)).removeAttribute("loginMember");
			if((sessions.get(userId)).getAttribute("returnPath") != null) {
				(sessions.get(userId)).removeAttribute("returnPath");
			}
			(sessions.get(userId)).invalidate();
			
			sessions.remove(userId);
			
			
		}
		printSessionsMap();
	}

}
