package com.ray.controller.reply;

import java.util.List;

import javax.inject.Inject;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ray.service.reply.ReplyService;
import com.ray.vodto.Reply;

@RestController // 현재 클래스가 REST방식으로 동작하는 Controller 객체 임을 의미
@RequestMapping("/reply/*")
public class ReplyController {

	@Inject
	private ReplyService rService;
	
	@RequestMapping(value="all/{boardNo}", method=RequestMethod.GET)
	public ResponseEntity<List<Reply>> getAllReplies(@PathVariable("boardNo") int boardNo) {
		System.out.println(boardNo + "번 글의 댓글을 모두 가져오자");
		
		ResponseEntity<List<Reply>> result = null;
		
		try {
			List<Reply> lst = rService.getAllReplies(boardNo);
			result = new ResponseEntity<List<Reply>>(lst, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			
			// 예외가 발생하면 json으로 응답할 객체가 없기 때문에 상태코드만 전송
			result = new ResponseEntity<>(HttpStatus.CONFLICT);
		}
		
		return result;
	}
	
	// @RequestBody : view단에서 보낸 json을 자바 객체로 변환
	@RequestMapping(value="", method = RequestMethod.POST)
	public ResponseEntity<String> saveReply(@RequestBody Reply newReply) {
		System.out.println(newReply.toString() + "댓글저장");
		ResponseEntity<String> result = null;
		
		try {
			if(rService.saveReply(newReply)) {
				result = new ResponseEntity<String>("success", HttpStatus.OK);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			result = new ResponseEntity<String>("fail", HttpStatus.FORBIDDEN);
			
		}
		return result;
	}
}
