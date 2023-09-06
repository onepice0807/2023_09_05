package com.ray.persistence;

import java.util.List;

import com.ray.vodto.Board;
import com.ray.vodto.ReadCountProcess;
import com.ray.vodto.UploadedFile;

public interface BoardDAO {
	List<Board> selectAllBoard() throws Exception;
	
	int insertNewBoard(Board newBoard) throws Exception;

	int selectRecentlyBoardNo() throws Exception;

	void insertUploadedFile(int boardNo, UploadedFile uf) throws Exception;

	ReadCountProcess selectReadCountProcess(int no, String ipAddr) throws Exception;
	
	int getHourDiffReadTime(int no, String ipAddr) throws Exception;
	
	int updateReadCountProcsss(ReadCountProcess rcp) throws Exception;
	
	int updateReadCount(int no) throws Exception;
	
	int insertReadCountProcess(ReadCountProcess rcp) throws Exception;
	
	// no번 게시글 찾기
	Board selectBoardByNo(int no) throws Exception;
	
	// no번 글의 첨부 파일 가져오기
	List<UploadedFile> selectUploadedFile(int no) throws Exception;
}
