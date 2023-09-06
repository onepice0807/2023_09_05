<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
	
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.0/jquery.min.js"></script>

<script>
	$(function(){
		$(".upFileArea").on("dragenter dragover", function(evt) {
			evt.preventDefault();
		});
		
		$(".upFileArea").on("drop", function(evt) {
			evt.preventDefault();
			
			let files = evt.originalEvent.dataTransfer.files;
			for (let i = 0; i < files.length ; i++) {
				let form = new FormData();
				// 파일의 이름을 컨트롤러 단의 MultipartFile 매개변수명과 동일하도록 한다
				form.append("uploadFile", files[i]); 
				console.log(files[i]);
				
				$.ajax({
					url : "uploadFile", // 데이터를 수신받을 서버 주소
					type : "POST", // 통신방식(GET, POST, PUT, DELETE)
					data : form,
					dataType : "json",
					async : false,
					processData : false, // text데이터에 대해 쿼리스트링 처리를 하지 않겠다
					contentType : false, // x-www-form-urlencoded 처리 안함.(인코딩 하지 않음)
					success : function(data) {
						console.log(data);
						
						if (data != null) {
							showUploadedFile(data);
						}
					}
				});
			}
			
			
			
		});
	});
	
	function showUploadedFile(json) {
		let output = "";
		
		$.each(json, function(i, elt) {
			let name = elt.newFileName.replace("\\", "/");
			
			if (elt.thumbFileName != null) { // 이미지
				let thumb = elt.thumbFileName.replace("\\", "/");
				output += `<img src='../resources/uploads\${thumb}' id='\${elt.originalFileName}' class='upImg' />
				<img src='../resources/images/remove.png' class='remIcon' onclick='remFile(this);' />`;	
			} else {
				output += `<a href='../resources/uploads\${name}' id='\${elt.originalFileName}'>\${elt.originalFileName}</a>
					<img src='../resources/images/remove.png' class='remIcon' onclick='remFile(this);' />`;
			}
			
			
		}); 
		
		$('.uploadFiles').html(output);
	}
	
	function remFile(fileId) {
		let removeFile = $(fileId).prev().attr('id');  // 삭제될 파일의 originalFilename
		
		$.ajax({
			url : "remFile", // 데이터를 수신받을 서버 주소
			type : "GET", // 통신방식(GET, POST, PUT, DELETE)
			data : {
				"removeFile" : removeFile
			},
			dataType : "text",
			async : false,
			success : function(data) {
				console.log(data);
 				if (data == "success") {
 					$(fileId).prev().remove();
 					$(fileId).remove();
 				}
				
			}
		});
	}
	
	function btnCancel() {
		$.ajax({
			url : "remAllFile", // 데이터를 수신받을 서버 주소
			type : "GET", // 통신방식(GET, POST, PUT, DELETE)
			dataType : "text",
			async : false,
			success : function(data) {
				console.log(data);
 				
				location.href='listAll';
			}
		});
	}
	
</script>

<style>
	.upFileArea {
		width : 100%;
		height : 100px;
		border : 1px dotted #333;
		padding : 10px;
		
		font-weight : bold;
		color : #f2efef;
		
		font-size: 20px;
		
		display: flex;
		justify-content: center;
		align-items: center;
		
	}
	
		
	.remIcon {
		width : 15px;
	}
</style>
</head>

<body>
	

	<jsp:include page="./../header.jsp"></jsp:include>


	<div class="container">
		<h1>게시판 글 작성</h1>

		<form action="writeBoard" method="post">
			<div class="mb-3 mt-3">
				<label for="writer" class="form-label">작성자 :</label> <input
					type="text" class="form-control" id="writer" name="writer" 
					value="" />
			</div>

			<div class="mb-3">
				<label for="title" class="form-label">제목:</label> <input
					type="text" class="form-control" id="title" name="title">
			</div>
			

			<div class="mb-3">
				<textarea rows="40" style="width:100%" id="content" name="content"></textarea>
				
			</div>

			<div class="mb-3">
				<label for="upFile" class="form-label">첨부파일:</label>
				<div class="upFileArea">
					업로드할 파일을 드래그앤 드랍 하세요
				</div>
				<div class="uploadFiles">
				
				</div>
			</div>

			<div class="mb-3">
				<button type="button" class="btn btn-secondary" onclick='btnCancel();'>취소</button>
				<button type="submit" class="btn btn-success">저장</button>
			</div>
			
			<input type="hidden" name="csrfToken" value="${sessionScope.csrfToken }" />
			
		</form>
	</div>

	<jsp:include page="./../footer.jsp"></jsp:include>
</body>
</html>