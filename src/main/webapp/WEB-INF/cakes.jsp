<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">
<head>
<title></title>
</head>
<body>
	<div align="center">
		<c:if test="${not empty cakes}">
			<table style="width: 100%">
				<tr>
					<th>Title</th>
					<th>Description</th>
					<th>Image URL</th>
				</tr>
				<c:forEach items="${cakes}" var="cake">
					<tr>
						<td>${cake.title}</td>
						<td>${cake.desc}</td>
						<td>${cake.image}</td>
					</tr>
				</c:forEach>
			</table>
		</c:if>
	</div>
	</br>
	</br>
	<div align="center">
		<c:if test="${not empty duplicateCake}">
			<div>${duplicateCake}</div>
		</c:if>
		<c:if test="${not empty messages}">
			<c:forEach items="${messages}" var="message">
				<div>${message}</div>
			</c:forEach>
		</c:if>
	</div>
	</br>
	</br>
	<div align="center">
		<form action="/" method="post">
			<div>
				<label for="title">Title:</label> <input type="text" id="title"
					name="title">
			</div>
			<div>
				<label for="description">Description:</label> <input
					type="description" id="description" name="description">
			</div>
			<div>
				<label for="imageurl">Image URL:</label> <input type="imageurl"
					id="imageurl" name="imageurl">
			</div>
			<div>
				<input type="submit" value="Submit">
			</div>
		</form>
	</div>
</body>
</html>
