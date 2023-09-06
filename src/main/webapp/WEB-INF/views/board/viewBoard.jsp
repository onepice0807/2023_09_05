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
			}
		});
	}
</script>
<style>
.content {
	padding: 20px;
	border: 1px dashed #333;
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
				<button type="button" class="btn btn-info">댓글달기</button>
				<button type="button" class="btn btn-danger">취소</button>
			</div>
		</div>
		
		<div class="allReplies" style="margin-top: 10px; padding : 5px;">
		</div>

	</div>



	<jsp:include page="../footer.jsp"></jsp:include>
</body>
</html>