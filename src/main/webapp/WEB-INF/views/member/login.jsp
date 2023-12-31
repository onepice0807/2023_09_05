<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.0/jquery.min.js"></script>
</head>
<body>
	<jsp:include page="./../header.jsp"></jsp:include>


	<div class="container">
		<h1>로그인</h1>

		<form action="login" method="post">
			<div class="mb-3 mt-3">
				<label for="userId" class="form-label">UserId:</label> <input
					type="text" class="form-control" id="userId" name="userId">
			</div>

			<div class="mb-3">
				<label for="userPwd" class="form-label">Password:</label> <input
					type="password" class="form-control" id="userPwd" name="userPwd">
			</div>

			<div class="mb-3">
				<label class="form-check-label" for="remember"> <input
					class="form-check-input" type="checkbox" name="remember" id="remember">
					자동로그인
				</label>
			</div>



			<div class="mb-3">
				<button type="reset" class="btn btn-secondary">취소</button>
				<button type="submit" class="btn btn-success">로그인</button>
			</div>
		</form>
	</div>

	<jsp:include page="./../footer.jsp"></jsp:include>
</body>
</html>