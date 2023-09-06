package com.ray.persistence;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.ray.vodto.Board;
import com.ray.vodto.ReadCountProcess;
import com.ray.vodto.UploadedFile;

@Repository
public class BoardDAOImpl implements BoardDAO {
	
	@Inject
	private SqlSession ses;
	
	private static String ns = "com.ray.mappers.BoardMapper";

	@Override
	public List<Board> selectAllBoard() throws Exception {
		return ses.selectList(ns + ".getAllBoard");
	}

	@Override
	public int insertNewBoard(Board newBoard) throws Exception {
		
		return ses.insert(ns + ".insertNewBoard", newBoard);
	}

	@Override
	public int selectRecentlyBoardNo() throws Exception {
		return ses.selectOne(ns + ".getNo");
		
	}

	@Override
	public void insertUploadedFile(int boardNo, UploadedFile uf) throws Exception {
		Map<String, Object> param = new HashMap<String, Object>();
		
		param.put("originalFileName", uf.getOriginalFileName());
		param.put("newFileName", uf.getNewFileName());
		param.put("size" , uf.getSize());
		param.put("boardNo", boardNo);
		param.put("thumbFileName", uf.getThumbFileName());
		
		ses.insert(ns + ".insertUploadedFile", param);
	}

	@Override
	public ReadCountProcess selectReadCountProcess(int no, String ipAddr) throws Exception {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("ipAddr", ipAddr);
		param.put("boardNo", no);
	
		
		
		return ses.selectOne(ns + ".getReadCountProcess", param);
	}

	@Override
	public int getHourDiffReadTime(int no, String ipAddr) throws Exception {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("ipAddr", ipAddr);
		param.put("no", no);
		return ses.selectOne(ns + ".getHourDiffReadTime", param);
	}

	@Override
	public int updateReadCountProcsss(ReadCountProcess rcp) throws Exception {
		
		return ses.update(ns + ".updateReadCountProcess", rcp);
	}

	@Override
	public int updateReadCount(int no) throws Exception {
		
		return ses.update(ns + ".updateReadcount", no);
	}

	@Override
	public int insertReadCountProcess(ReadCountProcess rcp) throws Exception {
		
		return ses.insert(ns + ".insertReadCountProcess", rcp);
	}

	@Override
	public Board selectBoardByNo(int no) throws Exception {
		
		return ses.selectOne(ns + ".getBoardByNo", no);
	}

	@Override
	public List<UploadedFile> selectUploadedFile(int no) throws Exception {
		
		return ses.selectList(ns + ".getUploadedFiles", no);
	}
	
	
	

}
