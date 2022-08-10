<%@page contentType="text/html;charset=UTF-8" language="java" %>
<%
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath()+"/";
%>
<!DOCTYPE html>
<html>
<head>
	<base href="<%=basePath%>">
	<meta charset="UTF-8">
	<title>index.jsp</title>
</head>
<body>
	<script type="text/javascript">
		document.location.href = "settings/qx/user/toLogin.do";

	</script>

pages index
</body>
</html>