package com.ray.controller.board;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.ray.controller.HomeController;
import com.ray.etc.GetUserIPAddr;
import com.ray.etc.UploadFileProcess;
import com.ray.service.board.BoardService;
import com.ray.vodto.Board;
import com.ray.vodto.UploadedFile;

@Controller
@RequestMapping("/board/*")
public class BoardController {
	
	@Inject
	private BoardService bService;  // BoardServcie 객체 주입
	
	private static final Logger logger = LoggerFactory.getLogger(BoardController.class);
	
	private List<UploadedFile> fileList = new ArrayList<UploadedFile>();
	
	@RequestMapping("listAll")
	public void listAll(Model model) throws Exception {
		logger.info("게시판 전제 글 조회");
		// service -> dao -> db -> service 
		
		List<Board> lst = null;
		
		lst = bService.getEntireBoard();
		
		model.addAttribute("boardList", lst);
		
		
	}
	
	@RequestMapping("viewBoard")
	public void viewBoard(@RequestParam("no") int no, HttpServletRequest req, Model model) throws Exception {
		logger.info(no + "번 글을 상세 조회하자!");
			
		Map<String, Object> result = bService.getBoardByNo(no, GetUserIPAddr.getIp(req));
		
		model.addAttribute("board", (Board)result.get("board"));
		model.addAttribute("upFileList", (List<UploadedFile>)result.get("upFileList"));

	}
	
	@RequestMapping("writeBoard")
	public void showWriteBoard(HttpSession ses) {
		String uuid = UUID.randomUUID().toString();
		
		System.out.println(uuid);
		
		ses.setAttribute("csrfToken", uuid);
		
		
	}
	
	
	/**
	 * @MethodName : uploadFile
	 * @author : webshjin
	 * @param : uploadFile - uploaded File
	 * @returnValue : 
	 * @descriptiton
	 * @date : 2023. 9. 1.
	 */
	@RequestMapping(value="uploadFile", method = RequestMethod.POST )
	public @ResponseBody List<UploadedFile> uploadFile(MultipartFile uploadFile, HttpServletRequest req) {
		logger.info("파일을 업로드 했다!");
		// uploadFile.getBytes() : 파일의 2진 데이터
		System.out.println("파일의 오리지날 이름 : " + uploadFile.getOriginalFilename());
		System.out.println("파일의 사이즈 : " + uploadFile.getSize());
		System.out.println("파일의 contentType : " + uploadFile.getContentType());
		
		String realPath = req.getSession().getServletContext().getRealPath("resources/uploads");
		System.out.println(realPath);
		
		
		UploadedFile uf = null;
		
		// 파일 처리 시작
		try {
			uf = UploadFileProcess.fileUpload(uploadFile.getOriginalFilename(), 
					uploadFile.getSize(), uploadFile.getContentType(), uploadFile.getBytes(), realPath);
		
			// 월요일 요기부터 요이땅(여러개 파일 업로드 한꺼번에 할 경우 고민해 보기)
			
			if (uf != null) {
				this.fileList.add(uf);
			}
			
		
		} catch (IOException e) {
			uf = null;
		}
		
		
		for (UploadedFile f: this.fileList) {
			System.out.println("현재 파일 업로드 리스트 : " + f.toString());
		}
		
		return this.fileList;
	}
	
	@RequestMapping("remFile")
	public ResponseEntity<String> removeFile(@RequestParam("removeFile") String remFile, HttpServletRequest req) {
		System.out.println(remFile + " 파일을 삭제하자");
		
		
		String realPath = req.getSession().getServletContext().getRealPath("resources/uploads");
		
		ResponseEntity<String> result = null;
		
		UploadFileProcess.deleteFile(this.fileList, remFile, realPath);
		
		int ind = 0;
		for (UploadedFile uf : fileList) {
			
			if (!remFile.equals(uf.getOriginalFileName())) {
				ind++;
			} else if (remFile.equals(uf.getOriginalFileName())) {
				break;
			}
		}
		
		this.fileList.remove(ind);
		
		result = new ResponseEntity<String>("success", HttpStatus.OK);
		
		
		for (UploadedFile f: this.fileList) {
			System.out.println("현재 파일 업로드 리스트 : " + f.toString());
		}
		
		return result;
	}
	
	@RequestMapping("remAllFile")
	public ResponseEntity<String> remAllFile(HttpServletRequest req) {
		String realPath = req.getSession().getServletContext().getRealPath("resources/uploads");
		UploadFileProcess.deleteAllFile(this.fileList, realPath);
		
		this.fileList.clear();
		
		
		return new ResponseEntity<String>("success", HttpStatus.ACCEPTED);
	}
	
	
	@RequestMapping(value="writeBoard", method=RequestMethod.POST)
	public String writeBoard(Board newBoard, @RequestParam("csrfToken") String inputCsrf, 
			HttpSession ses) {
		logger.info("게시판 글 작성 : " + newBoard.toString() + ", " + inputCsrf);
		
		String redirectPage = "";
		
		if (((String)ses.getAttribute("csrfToken")).equals(inputCsrf)) {
			// 게시글과 업로드된 파일을 저장
			try {
				bService.saveNewBoard(newBoard, fileList);
				redirectPage = "listAll";
			} catch (Exception e) {
				// 업로드된 파일이 있다면 또 지워야 함
				e.printStackTrace();
				
				redirectPage = "listAll?status=fail";
			}
		}
		
		return "redirect:" + redirectPage;
	}
}
