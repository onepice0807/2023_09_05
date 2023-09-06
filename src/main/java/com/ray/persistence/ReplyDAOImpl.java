package com.ray.persistence;

import java.util.List;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.ray.vodto.Reply;

@Repository
public class ReplyDAOImpl implements ReplyDAO {
	
	@Inject
	private SqlSession ses;
	
	private String ns = "com.ray.mappers.ReplyMapper";

	@Override
	public List<Reply> selectAllReplies(int boardNo) throws Exception {
		// TODO Auto-generated method stub
		return ses.selectList(ns + ".getAllReplies", boardNo);
	}

}
