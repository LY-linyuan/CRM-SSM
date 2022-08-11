<%@page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath()+"/";
%>
<!DOCTYPE html>
<html>
<head>
    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js" ></script>

    <link rel="stylesheet" href="jquery/bootstrap_3.3.0/css/bootstrap.min.css">
    <script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js" ></script>

    <link rel="stylesheet" href="jquery/bs_pagination-master/css/jquery.bs_pagination.min.css">
    <script type="text/javascript" src="jquery/bs_pagination-master/js/jquery.bs_pagination.min.js" ></script>
    <script type="text/javascript" src="jquery/bs_pagination-master/localization/en.js" ></script>
    <base href="<%=basePath%>">
    <title>演示bs_pagination插件</title>

    <script type="text/javascript" >
        $(function () {
            $("#demo_pag1").bs_pagination({
                currentPage: 2,  // 当前页号  默认是1
                rowsPerPage: 20, // 每页显示条数 默认是10
                totalRows: 500, // 总条数 默认是1000
                totalPages: 25, // 总页数

                visiblePageLinks: 6, // 最多可以显示的卡片数 默认是5

                showGoToPage: true, // 是否显示跳转到第几页 默认是true
                shouldRecordRange: true, // 是否显示'每页显示条数' 默认是true
                showRowsInfo: true, // 手否显示记录信息默认是true

                // 切换页号的时候能返回 pageNo pageSize
                onChangePage: function (event, pageObj) {  // 当用户切换页码的时候触发的事件
                    alert(pageObj.currentPage);
                    alert(pageObj.rowsPerPage);
                }
            });
        });
    </script>

</head>
<body>
    <div id="demo_pag1"></div>
</body>
</html>
