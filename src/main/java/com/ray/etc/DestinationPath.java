package com.ray.etc;

import javax.servlet.http.HttpServletRequest;

public class DestinationPath {
	public static void savePrePath(HttpServletRequest request) {
		String uri = request.getRequestURI();
		String queryStr = "";
		if(request.getQueryString() != null) {
			queryStr = request.getQueryString();
		}
		
		
		System.out.println(uri + ", "  + queryStr);
		
		if(!queryStr.equals("")) {
			queryStr = "?" + queryStr;
		}
		
		if(request.getMethod().equals("GET")) { // GET방식으로 요청 되었다면
			System.out.println("로그인 후 이동할 페이지 :" + uri + queryStr);
			request.getSession().setAttribute("returnPath", uri + queryStr);
		}
		
	}
}
