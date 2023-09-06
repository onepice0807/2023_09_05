package com.ray.service.reply;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.ray.persistence.MemberDAO;
import com.ray.persistence.PointLogDAO;
import com.ray.persistence.ReplyDAO;
import com.ray.vodto.PointLog;
import com.ray.vodto.Reply;

@Service
public class ReplyServiceImpl implements ReplyService {

	@Inject
	private ReplyDAO rDao;
	
	@Inject
	private MemberDAO mdao;
	@Inject
	private PointLogDAO pdao;
	
	@Override
	public List<Reply> getAllReplies(int boardNo) throws Exception {
		
		List<Reply> lst = rDao.selectAllReplies(boardNo);
		
		return lst;
	}

	@Override
	@Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
	public boolean saveReply(Reply reply) throws Exception {
		boolean result = false;
		
		// 1) 댓글 저장
		if(rDao.insertReply(reply) == 1) {
			// 2) member테이블에 userPoint update
			if(mdao.updateUserPoint("답글작성", reply.getReplyer()) == 1) {
				// 3) pointLog 테이블에 insert
				System.out.println("to : " + reply.toString());
				System.out.println("replyer : " + reply.getReplyer());
				  if(pdao.insertPointLog(new PointLog(-1, null, "답글작성", 2, reply.getReplyer())) == 1) {
					result = true;
				 }
			}
		}
				
		return result;
	}

}
