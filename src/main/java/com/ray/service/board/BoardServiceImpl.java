package com.ray.service.board;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.ray.persistence.BoardDAO;
import com.ray.persistence.MemberDAO;
import com.ray.persistence.PointLogDAO;
import com.ray.vodto.Board;
import com.ray.vodto.PointLog;
import com.ray.vodto.ReadCountProcess;
import com.ray.vodto.UploadedFile;

@Service  // 아래의 객체가 Service 객체임을 명시
public class BoardServiceImpl implements BoardService {
	
	@Inject
	private BoardDAO bDao;
	
	@Inject
	private MemberDAO mDao;
	
	@Inject
	private PointLogDAO plDao;

	@Override
	public List<Board> getEntireBoard() throws Exception {
		
		List<Board> lst = bDao.selectAllBoard();
		
		return lst;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveNewBoard(Board newBoard, List<UploadedFile> lst) throws Exception {
	
		// 게시물 본문 줄바꿈 처리
		newBoard.setContent(newBoard.getContent().replace("\r\n", "<br />"));
		
		
		// 1) 게시글을 DB에 insert
		if(bDao.insertNewBoard(newBoard) == 1) {
			int boardNo = bDao.selectRecentlyBoardNo();
			
			if (lst.size() > 0) {  // 업로드 한 파일이 있다
				for (UploadedFile uf :lst) {
					bDao.insertUploadedFile(boardNo, uf);
				}
			}
			
			//3) member 테이블에 userpoint update
			mDao.updateUserPoint(2, newBoard.getWriter());

			// 4) pointlog 테이블에 insert
			plDao.insertPointLog(new PointLog(-1, null, "게시물작성", 2, newBoard.getWriter()));
		}
	}

	@Override
	@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
	public Map<String, Object> getBoardByNo(int no, String ipAddr) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		
		int readcntResult = -1; 
		
		if (bDao.selectReadCountProcess(no, ipAddr) != null) { // 조회 한적이 있다.
			// 해당 아이피 주소와 글번호 같은 것이 있다면...
			if (bDao.getHourDiffReadTime(no, ipAddr) > 23) { // 시간이 24시간이 지난경우
//				-> 아이피 주소와 글번호와 읽은시간을 readcountprocees테이블에 (update)
				if(bDao.updateReadCountProcsss(new ReadCountProcess(-1, ipAddr, no, null)) == 1) {
//					-> 해당 글번호의 readcount를 증가 (update)
					readcntResult = bDao.updateReadCount(no);
				};

			} else {
				readcntResult = 1;
			}
			
		} else { // 해당 ipAddr이 no 번글을 최초 조회
//			-> 아이피 주소와 글번호와 읽은시간을 readcountprocees테이블에 (insert)
			if (bDao.insertReadCountProcess(new ReadCountProcess(-1, ipAddr, no, null)) == 1) {
//				-> 해당 글번호의 readcount를 증가 (update)
				readcntResult = bDao.updateReadCount(no);
			}
//			
		}
		
		if (readcntResult == 1) {
			// no번의 글 가져오자
			Board board = bDao.selectBoardByNo(no);
			// uploadedFile도 가져오기
			List<UploadedFile> upFileLst = bDao.selectUploadedFile(no);

			result.put("board", board);
			result.put("upFileList", upFileLst);
		}

		return result;
	}
	
}
