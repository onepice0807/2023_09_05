package com.ray.persistence;

public interface MemberDAO {
	// 현재날짜와 현재 시간을 얻어오는
	public String getDate();
	

	public int updateUserPoint(int howmuch, String userId) throws Exception;
}
