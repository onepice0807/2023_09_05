<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.0/jquery.min.js"></script>
<script>
	$(function(){
		getAllReplies();
	});
	
	function getAllReplies() {
		let boardNo = ${board.no};
		$.ajax({
			url : "/reply/all/" + boardNo, // 데이터를 수신받을 서버 주소
			type : "get", // 통신방식(GET, POST, PUT, DELETE)
			dataType : "json",
			async : false,
			success : function(data) {
				console.log(data);
				
				displayAllReplies(data);
			}, error : function	() {
				alert("error발생");
			}
		});
	}
	
	function displayAllReplies(replies) {
		let output = "<ul class='list-group'>";
		if(replies.length > 0) {
			$.each(replies, function(i, elt) {
				output += `<li class="list-group-item">`;
				output += `<div class='replyText'>\${elt.replyText}</div>`;
				output += `<div class='replyInfo'><span class='replyer'>\${elt.replyer}</span>`;
				let betweenTime = procPostDate(elt.postDate);
				output += `<span class='postDate'>\${betweenTime}</span></div>`;
				output += `<div class='btns'><img src='../resources/images/modify.png' onclick='modiReply(\${elt.replyNo})'>`;
				output += `<img src='../resources/images/delete.png'  onclick='delReply(\${elt.replyNo})'></div>`;
				output += "</li>";
			});	
		}
		output += "</ul>"
		$(".allReplies").html(output);
		
	}
	
	function procPostDate(data) {
		let postDate = new Date(data); // 댓글 작성일
		let now = new Date(); // 현재 날짜시간
		
		let diff = (now - postDate) / 1000; // 시간 차(초 단위)
		
		let times = [
			{name : "일", time : 60 * 60 * 24},
			{name : "시간", time : 60 * 60},
			{name : "분", time : 60}
		];
		
		for(let val of times) {
			// 시간차(초 단위)가 기준시간(val.time)으로 나누어보자
			let betweenTime = Math.floor(diff / val.time);
			console.log(diff, betweenTime);
			if(betweenTime > 0) {
				if(diff > 60*60*24) { // 1일이 지났다
					return postDate.toLocaleString();
				}
				
				return betweenTime + val.name + "전";
			}
		}
		return "방금전";
	}
	
	function saveReply() {
		let parentNo = '${board.no}';
		let replyText = $('#replyText').val();
		let replyer = 'ray1234';
		
		let newReply = {
				"parentNo" : parentNo,
				"replyText" : replyText,
				"replyer" : replyer	
		};
		console.log(JSON.stringify(newReply));
		
		$.ajax({
			url : "/reply/", // 데이터를 수신받을 서버 주소
			type : "POST", // 통신방식(GET, POST, PUT, DELETE)
			data : JSON.stringify(newReply), // 보낼 데이터
			headers : {
				// 송신하는 데이터의 MINE TYPE
				"Content-Type" : "application/json",
				
				// PUT, DELETE, PATCH 등의 REST에서 사용되는 HTTP Methpod가 동작하지 않는 과거의 웹브라우저
				// 에서 post 방식처럼 동작하도록 한다
				"X-HTTP-Method-Override" : "POST"
			},
			dataType : "text", // 수신되는 데이터 타입
			async : false,
			success : function(data) {
				console.log(data);
				if(data == "success") {
					getAllReplies();
					$('#replyText').val("");
				}
			}, error : function	() {
				alert("error발생");
			}
		});
	}
	
</script>
<style>
.content {
	padding: 20px;
	border: 1px dashed #333;
}
.replyText{
	padding: 10px;
	background-color: #FBFBFB;
}
.replyInfo{
	display: flex;
	justify-content: space-between;
}
.btns{
	float: right;
	margin-right: 10px; 
}
.btns img{
	width: 23px;
	margin: 10px;
}


</style>
</head>
<body>
	<jsp:include page="../header.jsp"></jsp:include>
	<div class="container">
		<h1>게시판 상세 글 조회</h1>

		<div class="board">
			<div class="mb-3 mt-3">
				<label for="no" class="form-label">글번호 :</label> <input
					type="text" class="form-control" id="no" 
					value="${board.no}" readonly />
			</div>
		
			<div class="mb-3 mt-3">
				<label for="writer" class="form-label">작성자 :</label> <input
					type="text" class="form-control" id="writer" name="writer"
					value="${board.writer }" readonly />
			</div>

			<div class="mb-3">
				<label for="title" class="form-label">제목:</label> <input type="text"
					class="form-control" id="title" name="title"
					value="${board.title }" readonly />
			</div>

			<div class="readLikeCnt">
				<div class="readCount">
					조회수 : <span class="badge rounded-pill bg-success">${board.readcount }</span>
				</div>
				<div class="likeCount">
					좋아요 : <span class="badge rounded-pill bg-info">${board.likecount }</span>
				</div>
			</div>

			<div class="mb-3 content">${board.content }</div>
		</div>

		<div>
			<c:forEach var="uf" items="${upFileList }">
				<c:choose>
					<c:when test="${not empty uf.thumbFileName }">
						<img src='../resources/uploads${uf.thumbFileName }' />
					</c:when>
					<c:otherwise>
						<a href='../resources/uploads${uf.newFileName }'>${uf.originalFileName }</a>
					</c:otherwise>
				</c:choose>


			</c:forEach>

		</div>


		<div class="mb-3" style="margin-top: 20px">
			<button type="button" class="btn btn-secondary">수정</button>
			<button type="button" class="btn btn-warning">삭제</button>
			<button type="button" class="btn btn-info"
				onclick="location.href='listAll';">리스트페이지로</button>
		</div>

		<!-- 댓글 처리 -->
		<div class="replyInputDiv mb-3">
			<div class="form-check">
				<label for="replyText"> 댓글 : </label>
				<textarea class="form-control" rows="5" id="replyText"></textarea>
			</div>
			<div class="replyBtns">
				<button type="button" class="btn btn-info" onclick="saveReply()">댓글달기</button>
				<button type="button" class="btn btn-danger">취소</button>
			</div>
		</div>
		
		<div class="allReplies" style="margin-top: 10px; padding : 5px;">
		</div>

	</div>



	<jsp:include page="../footer.jsp"></jsp:include>
</body>
</html>