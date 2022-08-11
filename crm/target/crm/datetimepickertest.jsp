<%@page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath()+"/";
%>
<!DOCTYPE html>
<html>
<head>
    <base href="<%=basePath%>">
    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js" ></script>
    <%--引入bookstrap框架--%>
    <link rel="stylesheet" href="jquery/bootstrap_3.3.0/css/bootstrap.min.css">
    <script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js" ></script>
    <link rel="stylesheet" href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css">
    <script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js" ></script>
    <script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js" ></script>
    <title>Title</title>

    <script type="text/javascript" >
        $(function () {
            $("#myDate").datetimepicker({
                language: 'zh-CN', // 语言
                format: 'yyyy-mm-dd', // 格式
                minView: 'month', // 可以选择的最小视图
                initData: new Date(), // 初始化显示的时间
                autoclose: true, // 选址日期之后是否自动关闭  默认false
                todayBtn: true, // 时候显示'今天' 按钮 默认false
                clearBtn: true // 设置是否显示'清空'按钮 默认false
            });
        });
    </script>
</head>
<body>
    <input type="text" id="myDate" readonly>

</body>
</html>
