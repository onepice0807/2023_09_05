package com.ray.persistence;

import java.util.List;

import com.ray.vodto.Reply;

public interface ReplyDAO {
	// 모든 댓글 긁어오기
	List<Reply> selectAllReplies(int boardNo) throws Exception;
	
	// 신규 댓글 저장
	int insertReply(Reply reply) throws Exception;
}
