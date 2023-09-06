package com.ray.service.reply;

import java.util.List;

import com.ray.vodto.Reply;

public interface ReplyService {
	// 모든 댓글 가져오기
	List<Reply> getAllReplies(int boardNo) throws Exception;
}
