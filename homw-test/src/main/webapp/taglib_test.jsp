<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="fi" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="m" uri="/WEB-INF/tlds/menu"%>
<%@taglib prefix="fns" uri="/WEB-INF/tlds/function"%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>TagLib测试</title>
</head>
<body>
	<form method="post" action="#">
		<!-- 自定义标签formInput，.tag方式 -->
		<fi:formInput name="username" label="姓名" type="text"/><br>
		<fi:formInput name="password" label="密码" type="password"/><br>
		<input type="submit" value="提交">
	</form>
	
	<!-- 自定义标签menu，.tld方式 -->
	<m:menu/>
	
	<!-- 静态Java方法调用 -->
	${fns:call() }
</body>
</html>