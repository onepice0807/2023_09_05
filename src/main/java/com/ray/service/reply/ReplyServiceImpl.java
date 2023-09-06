package com.ray.service.reply;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.ray.persistence.ReplyDAO;
import com.ray.vodto.Reply;

@Service
public class ReplyServiceImpl implements ReplyService {

	@Inject
	private ReplyDAO rDao;
	
	@Override
	public List<Reply> getAllReplies(int boardNo) throws Exception {
		
		List<Reply> lst = rDao.selectAllReplies(boardNo);
		
		return lst;
	}

}
