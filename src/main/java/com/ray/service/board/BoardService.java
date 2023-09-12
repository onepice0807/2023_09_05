package com.ray.service.board;

import java.util.List;
import java.util.Map;

import com.ray.vodto.Board;
import com.ray.vodto.UploadedFile;

public interface BoardService {
	// 게시글 전체 조회
	List<Board> getEntireBoard() throws Exception;
	
	// 게시글 저장
	void saveNewBoard(Board newBoard, List<UploadedFile> lst) throws Exception;

	// 게시글 상세조회
	Map<String, Object> getBoardByNo(int no, String ipAddr) throws Exception;
	
	// 글번호로 작성자
	String getBoardWriterByNo(int no) throws Exception;
}
