<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<c:set var="url" value="http://www.stonebrewing.com"></c:set>
<c:if test="${fn:indexOf(url, 'http://') >= 0 }">
	<a href="${url }">${url }</a></p>
</c:if>
<c:if test="${fn:indexOf(url, 'http://') < 0 }">
	<a href="http://${url }">${url }</a></p>
</c:if>
</body>
</html>