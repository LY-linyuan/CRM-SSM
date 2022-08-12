<%@page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath()+"/";
%>
<!DOCTYPE html>
<html>
<head>
    <base href="<%=basePath%>">
    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js" ></script>

    <title>Title</title>
</head>
<body>
    <%--文件上传标签
            1. 表单组件标签只能用<input type="file">
            2. 请求方式只能用post
            3. form表单额编码格式只能用: multipart/form-data
                    默认采用的编码格式是urlencoded
         缺一不可

         --%>
    <form action="workbench/activity/fileUpload.do" method="post" enctype="multipart/form-data">
        <input type="file" name="myFile">
        <input type="text" name="userName" />
        <input type="submit" value="提交">
    </form>
</body>
</html>
