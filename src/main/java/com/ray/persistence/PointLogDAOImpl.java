package com.ray.persistence;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.ray.vodto.PointLog;

@Repository
public class PointLogDAOImpl implements PointLogDAO {

	@Inject
	private SqlSession ses;
	
	private static String ns = "com.ray.mappers.PointLogMapper";
	
	@Override
	public int insertPointLog(PointLog pl) throws Exception {
		
		return ses.insert(ns + ".insertPointLog", pl);
	}

}
