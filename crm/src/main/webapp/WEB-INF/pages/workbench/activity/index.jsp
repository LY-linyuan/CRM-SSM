<%@page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath()+"/";
%>
<!DOCTYPE html>
<html>
<head>
	<base href="<%=basePath%>">
<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link rel="stylesheet" href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css">

<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js" ></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js" ></script>

	<link rel="stylesheet" href="jquery/bs_pagination-master/css/jquery.bs_pagination.min.css">
<script type="text/javascript" src="jquery/bs_pagination-master/js/jquery.bs_pagination.min.js" ></script>
<script type="text/javascript" src="jquery/bs_pagination-master/localization/en.js" ></script>

<script type="text/javascript">

	$(function(){
		// 给创建添加单击事件
		$("#createActivityBtn").click(function () {
			// 弹出创建模态窗口
			// 这里可以初始化
			$("#createActivityModal").modal("show");
		});
		// 给保存按钮添加单击事件
		$("#saveCreateActivityBtn").click(function () {
			var owner = $("#create-marketActivityOwner").val();
			var name = $.trim($("#create-marketActivityName").val());
			var startDate = $("#create-startDate").val();
			var endDate = $("#create-endDate").val();
			var cost = $("#create-cost").val();
			var description = $("#create-description").val();
			// 表单验证
			if(owner == null) {
				alert("所有者不能为空");
				return;
			}
			if(name == "") {
				alert("名称不能为空");
				return;
			}
			if (startDate != null && endDate != null) {
				if (endDate < startDate) {
					alert("结束日期不能比开始日期小");
					return;
				}
			}
			/*
			    正则表达式 :  ^(([1-9]\d*)|0)$ 非负整数
			 */
			var regExp = /^(([1-9]\d*)|0)$/;
			if (!regExp.test(cost)) {
				alert("成本只能为非负整数");
				return;
			}
			// 发送请求
			$.ajax({
				url: 'workbench/activity/saveCreateActivity.do',
				data: {
					owner: owner,
					name: name,
					startDate: startDate,
					endDate: endDate,
					cost: cost,
					description: description
				},
				type: 'post',
				dataType: 'json',
				success: function (data) {
					if (data.code == '1') {
						// 关闭模态窗口
						$("#createActivityModal").modal("hide");
						// 刷新市场活动 显示第一页数据
						// 重置表单
						$("#createActivityForm").get(0).reset();
						queryActivityByConditionForPage(1, $("#demo_pag1").bs_pagination("getOption", "rowsPerPage"));
					} else {
						// 提示信息
						alert(data.message);
						// 模态窗口不关闭
						$("#createActivityModal").modal("show");  // 可以不写
					}

				}
			});

		});

		// 当容器加载完成后  对容器调用工具函数
		// $("input[name='mudate']").datetimepicker({
		$(".mydate").datetimepicker({
			language: 'zh-CN', // 语言
			format: 'yyyy-mm-dd', // 格式
			minView: 'month', // 可以选择的最小视图
			initData: new Date(), // 初始化显示的时间
			autoclose: true, // 选址日期之后是否自动关闭  默认false
			todayBtn: true, // 时候显示'今天' 按钮 默认false
			clearBtn: true // 设置是否显示'清空'按钮 默认false
		});


		// 当市场活动主页面加载完成查询所有数据的总条数  并获取第一页数据
		queryActivityByConditionForPage(1, 5);

		// 给查询按钮加单击事件
		$("#queryActivityBtn").click(function () {
			queryActivityByConditionForPage(1, $("#demo_pag1").bs_pagination("getOption", "rowsPerPage"));
		});

		// 给全选按钮绑定事件
		$("#chckAll").click(function () {
			if (this.checked == true) {
				$("#tBody input[type='checkbox']").prop("checked", true);
			} else {
				$("#tBody input[type='checkbox']").prop("checked", false);
			}
		});

		// 这种方法无发给 非固有元素 绑定事件
		/*$("#tBody input[type='checkbox']").click(function () {
			if ($("#tBody input[type='checkbox']").size() == $("#tBody input[type='checkbox']:checked").size()) {
				$("#chckAll").prop("checked", true);
			} else {
				$("#chckAll").prop("checked", false);
			}
		});*/

		$("#tBody").on("click", "input", function () {
			if ($("#tBody input[type='checkbox']").size() == $("#tBody input[type='checkbox']:checked").size()) {
				$("#chckAll").prop("checked", true);
			} else {
				$("#chckAll").prop("checked", false);
			}
		});

		// 给删除按钮绑定事单击事件
		$("#deleteActivityBtn").click(function () {
			// 收集参数
			var checkedIds = $("#tBody input[type='checkbox']:checked");
			if (checkedIds.size() == 0) {
				alert("请选择要删除的市场活动");
				return;
			}
			if (window.confirm("确定删除吗?")) {
				var ids = "";
				$.each(checkedIds, function () {
					ids += "id=" + this.value + "&";
				});
				ids = ids.substr(0, ids.length-1);
				$.ajax({
					url: "workbench/activity/deleteActivityIds.do",
					data: ids,
					type: 'post',
					dataType: 'json',
					success: function (data) {
						if (data.code == "1") {
							queryActivityByConditionForPage($("#demo_pag1").bs_pagination("getOption", "currentPage"), $("#demo_pag1").bs_pagination("getOption", "rowsPerPage"));
						} else {
							alert(data.message);
						}
					}

				});
			}

		});

		$("#editActivityBtn").click(function () {
			// 收集id
			var checkIds = $("#tBody input[type='checkbox']:checked");
			if (checkIds.size() == 0) {
				alert("请选择要修改的市场活动");
				return;
			}
			if (checkIds.size() > 1) {
				alert("一次只能修改一条市场活动哦");
				return;
			}
			// var id = checkIds.val();
			// var id = checkIds.get(0).value;
			var id = checkIds[0].value;
			$.ajax({
				url: 'workbench/activity/queryActivityById.do',
				data: {
					id: id
				},
				type: 'post',
				dataType: 'json',
				success:function (data) {
					$("#edit-id").val(data.id);
					$("#edit-marketActivityOwner").val(data.owner);
					$("#edit-marketActivityName").val(data.name);
					$("#edit-startTime").val(data.startDate);
					$("#edit-endTime").val(data.endDate);
					$("#edit-cost").val(data.cost);
					$("#edit-description").val(data.description);
					// 弹出模态窗口
					$("#editActivityModal").modal("show");
				}
			});
		});

		// 给更新按钮添加单击事件
		$("#saveEditActivityBtn").click(function () {
			var id = $("#edit-id").val();
			var owner = $("#edit-marketActivityOwner").val();
			var name = $("#edit-marketActivityName").val();
			var startDate = $("#edit-startTime").val();
			var endDate = $("#edit-endTime").val();
			var cost = $("#edit-cost").val();
			var description = $("#edit-description").val();
			// 表单验证
			if(owner == "" || owner==null) {
				alert("所有者不能为空");
				return;
			}
			if(name == "") {
				alert("名称不能为空");
				return;
			}
			if (startDate != null && endDate != null) {
				if (endDate < startDate) {
					alert("结束日期不能比开始日期小");
					return;
				}
			}
			/*
			    正则表达式 :  ^(([1-9]\d*)|0)$ 非负整数
			 */
			var regExp = /^(([1-9]\d*)|0)$/;
			if (!regExp.test(cost)) {
				alert("成本只能为非负整数");
				return;
			}
			$.ajax({
				url: 'workbench/activity/saveEditActivity.do',
				data: {
					id: id,
					owner: owner,
					name: name,
					startDate: startDate,
					endDate: endDate,
					cost: cost,
					description: description
				},
				type: 'post',
				dataType: 'json',
				success: function (data) {
					if (data.code == '1') {
						$("#editActivityModal").modal("hide");
						queryActivityByConditionForPage($("#demo_pag1").bs_pagination("getOption", "currentPage"), $("#demo_pag1").bs_pagination("getOption", "rowsPerPage"));
					} else {
						alert(data.message);
						$("#editActivityModal").modal("show");
					}
				}
			});
		});

		// 给批量导出按钮添加单击事件
		$("#exportActivityAllBtn").click(function () {
			// 发送同步请求
			window.location.href = 'workbench/activity/exportAllActivities.do';
		});

		// 给导入按钮添加单击事件
		$("#importActivityBtn").click(function () {
			//收集参数
			var activityFileName = $("#activityFile").val();
			var suffix = activityFileName.substr(activityFileName.lastIndexOf(".") + 1).toLocaleLowerCase();  //xls,XLS,Xls,xLs,......
			if(suffix != "xls"){
				alert("只支持xls文件");
				return;
			}
			var activityFile = $("#activityFile")[0].files[0];
			if(activityFile.size > 1024 * 1024 * 5){
				alert("文件大小不能超过5MB");
				return;
			}

			// FormData是ajax提供的接口,可以模拟键值对向后台提交参数;
			// FormData最大的优势是不但能提交文本数据，还能提交二进制数据
			var formData=new FormData();
			formData.append("activityFile", activityFile);
			formData.append("userName", "张三");

			// 发送请求
			$.ajax({
				url: 'workbench/activity/importActivity.do',
				data: formData,
				processData: false,   // 设置ajax向后台提交参数之前，是否把参数统一转换成字符串：true--是,false--不是,默认是true
				contentType: false,     // 设置ajax向后台提交参数之前，是否把所有的参数统一按urlencoded编码：true--是,false--不是，默认是true
				type: 'post',
				dataType: 'json',
				success: function (data) {
					if(data.code == "1"){
						// 提示成功导入记录条数
						alert("成功导入" + data.retData + "条记录");
						// 关闭模态窗口
						$("#importActivityModal").modal("hide");
						// 刷新市场活动列表,显示第一页数据,保持每页显示条数不变
						queryActivityByConditionForPage(1, $("#demo_pag1").bs_pagination('getOption', 'rowsPerPage'));
					}else{
						// 提示信息
						alert(data.message);
						// 模态窗口不关闭
						$("#importActivityModal").modal("show");
					}
				}
			});
		});

	});

	function queryActivityByConditionForPage (pageNo, pageSize) {
		// 收集参数
		var name = $("#query-name").val();
		var owner = $("#query-owner").val();
		var startDate = $("#query-startDate").val();
		var endDate = $("#query-endDate").val();
		// var pageNo = 1;
		// var pageSize = 10;
		// 发送请求
		$.ajax({
			url: "workbench/activity/queryActivityByConditionForPage.do",
			data:{
				name: name,
				owner: owner,
				startDate: startDate,
				endDate: endDate,
				pageNo: pageNo,
				pageSize: pageSize
			},
			type: 'post',
			dataType: 'json',
			success: function (data) {
				// $("#totalRowsB").text(data.totalRows);
				// 显示市场活动列表
				var htmlStr = '';
				$.each(data.activityList, function (index, obj) {
					htmlStr += '<tr class="active">';
					htmlStr += '<td><input type="checkbox" value="' + obj.id + '" /></td>';
					htmlStr += '<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href=\'workbench/activity/detailActivity.do?id=' + obj.id + '\';">' + obj.name + '</a></td>';
					htmlStr += '<td>' + obj.owner + '</td>';
					htmlStr += '<td>' + obj.startDate + '</td>';
					htmlStr += '<td>' + obj.endDate + '</td>';
					htmlStr += '</tr>';
				});
				$("#tBody").html(htmlStr);

				// 取消全选按钮
				$("#chckAll").prop("checked", false);

				// 计算总页数
				var totalPages = 1;
				if (data.totalRows % pageSize == 0) {
					totalPages = data.totalRows / pageSize;
				} else {
					totalPages = parseInt(data.totalRows / pageSize) + 1;
				}


				// 调用bs_pagination工具函数
				$("#demo_pag1").bs_pagination({
					currentPage: pageNo,  // 当前页号  默认是1
					rowsPerPage: pageSize, // 每页显示条数 默认是10
					totalRows: data.totalRows, // 总条数 默认是1000
					totalPages: totalPages, // 总页数

					visiblePageLinks: 6, // 最多可以显示的卡片数 默认是5

					showGoToPage: true, // 是否显示跳转到第几页 默认是true
					shouldRecordRange: true, // 是否显示'每页显示条数' 默认是true
					showRowsInfo: true, // 手否显示记录信息默认是true

					// 切换页号的时候能返回 pageNo pageSize
					onChangePage: function (event, pageObj) {  // 当用户切换页码的时候触发的事件
						// alert(pageObj.currentPage);
						// alert(pageObj.rowsPerPage);
						queryActivityByConditionForPage(pageObj.currentPage, pageObj.rowsPerPage);
					}
				});
			}
		});
	}
	
</script>
	<title></title>
</head>
<body>

	<!-- 创建市场活动的模态窗口 -->
	<div class="modal fade" id="createActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel1">创建市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form class="form-horizontal" id="createActivityForm" role="form">
					
						<div class="form-group">
							<label for="create-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-marketActivityOwner">
									<option></option>
									<c:forEach items="${userList}" var="user">
									  <option value="${user.id}">${user.name}</option>
								  </c:forEach>
									<%--<option>zhangsan</option>
								  <option>lisi</option>
								  <option>wangwu</option>--%>
								</select>
							</div>
                            <label for="create-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-marketActivityName">
                            </div>
						</div>
						
						<div class="form-group">
							<label for="create-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control mydate" id="create-startDate" readonly>
							</div>
							<label for="create-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control mydate" id="create-endDate" readonly>
							</div>
						</div>
                        <div class="form-group">

                            <label for="create-cost" class="col-sm-2 control-label">成本</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-cost">
                            </div>
                        </div>
						<div class="form-group">
							<label for="create-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="create-description"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="saveCreateActivityBtn">保存</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 修改市场活动的模态窗口 -->
	<div class="modal fade" id="editActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel2">修改市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form class="form-horizontal" role="form">
						<input type="hidden" id="edit-id">
					
						<div class="form-group">
							<label for="edit-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-marketActivityOwner">
									<option></option>
									<c:forEach items="${userList}" var="user">
										<option value="${user.id}">${user.name}</option>
									</c:forEach>
									<%--<option>zhangsan</option>
								  <option>lisi</option>
								  <option>wangwu</option>--%>
								</select>
							</div>
                            <label for="edit-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-marketActivityName" value="发传单">
                            </div>
						</div>

						<div class="form-group">
							<label for="edit-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control mydate" id="edit-startTime" value="2020-10-10" readonly>
							</div>
							<label for="edit-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control mydate" id="edit-endTime" value="2020-10-20" readonly>
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-cost" class="col-sm-2 control-label">成本</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-cost" value="5,000">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="edit-description">市场活动Marketing，是指品牌主办或参与的展览会议与公关市场活动，包括自行主办的各类研讨会、客户交流会、演示会、新产品发布会、体验会、答谢会、年会和出席参加并布展或演讲的展览会、研讨会、行业交流会、颁奖典礼等</textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="saveEditActivityBtn">更新</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 导入市场活动的模态窗口 -->
    <div class="modal fade" id="importActivityModal" role="dialog">
        <div class="modal-dialog" role="document" style="width: 85%;">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">
                        <span aria-hidden="true">×</span>
                    </button>
                    <h4 class="modal-title" id="myModalLabel">导入市场活动</h4>
                </div>
                <div class="modal-body" style="height: 350px;">
                    <div style="position: relative;top: 20px; left: 50px;">
                        请选择要上传的文件：<small style="color: gray;">[仅支持.xls]</small>
                    </div>
                    <div style="position: relative;top: 40px; left: 50px;">
                        <input type="file" id="activityFile">
                    </div>
                    <div style="position: relative; width: 400px; height: 320px; left: 45% ; top: -40px;" >
                        <h3>重要提示</h3>
                        <ul>
                            <li>操作仅针对Excel，仅支持后缀名为XLS的文件。</li>
                            <li>给定文件的第一行将视为字段名。</li>
                            <li>请确认您的文件大小不超过5MB。</li>
                            <li>日期值以文本形式保存，必须符合yyyy-MM-dd格式。</li>
                            <li>日期时间以文本形式保存，必须符合yyyy-MM-dd HH:mm:ss的格式。</li>
                            <li>默认情况下，字符编码是UTF-8 (统一码)，请确保您导入的文件使用的是正确的字符编码方式。</li>
                            <li>建议您在导入真实数据之前用测试文件测试文件导入功能。</li>
                        </ul>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    <button id="importActivityBtn" type="button" class="btn btn-primary">导入</button>
                </div>
            </div>
        </div>
    </div>
	
	
	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>市场活动列表</h3>
			</div>
		</div>
	</div>
	<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
		<div style="width: 100%; position: absolute;top: 5px; left: 10px;">
		
			<div class="btn-toolbar" role="toolbar" style="height: 80px;">
				<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">名称</div>
				      <input class="form-control" type="text" id="query-name">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input class="form-control" type="text" id="query-owner">
				    </div>
				  </div>


				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">开始日期</div>
					  <input class="form-control" type="text" id="query-startDate" />
				    </div>
				  </div>
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">结束日期</div>
					  <input class="form-control" type="text" id="query-endDate">
				    </div>
				  </div>
				  
				  <button type="button" class="btn btn-default" id="queryActivityBtn">查询</button>
				  
				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
				<div class="btn-group" style="position: relative; top: 18%;">
				  <button type="button" class="btn btn-primary" id="createActivityBtn"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" class="btn btn-default" id="editActivityBtn"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger" id="deleteActivityBtn"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>
				<div class="btn-group" style="position: relative; top: 18%;">
                    <button type="button" class="btn btn-default" data-toggle="modal" data-target="#importActivityModal" ><span class="glyphicon glyphicon-import"></span> 上传列表数据（导入）</button>
                    <button id="exportActivityAllBtn" type="button" class="btn btn-default"><span class="glyphicon glyphicon-export"></span> 下载列表数据（批量导出）</button>
                    <button id="exportActivityXzBtn" type="button" class="btn btn-default"><span class="glyphicon glyphicon-export"></span> 下载列表数据（选择导出）</button>
                </div>
			</div>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" id="chckAll" /></td>
							<td>名称</td>
                            <td>所有者</td>
							<td>开始日期</td>
							<td>结束日期</td>
						</tr>
					</thead>
					<tbody id="tBody">
						<%--<tr class="active">
							<td><input type="checkbox" /></td>
							<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.jsp';">发传单</a></td>
                            <td>zhangsan</td>
							<td>2020-10-10</td>
							<td>2020-10-20</td>
						</tr>
                        <tr class="active">
                            <td><input type="checkbox" /></td>
                            <td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.jsp';">发传单</a></td>
                            <td>zhangsan</td>
                            <td>2020-10-10</td>
                            <td>2020-10-20</td>
                        </tr>--%>
					</tbody>
				</table>

				<div id="demo_pag1"></div>
			</div>


			<%--<div style="height: 50px; position: relative;top: 30px;">
				<div>
					<button type="button" class="btn btn-default" style="cursor: default;">共<b id="totalRowsB">50</b>条记录</button>
				</div>
				<div class="btn-group" style="position: relative;top: -34px; left: 110px;">
					<button type="button" class="btn btn-default" style="cursor: default;">显示</button>
					<div class="btn-group">
						<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
							10
							<span class="caret"></span>
						</button>
						<ul class="dropdown-menu" role="menu">
							<li><a href="#">20</a></li>
							<li><a href="#">30</a></li>
						</ul>
					</div>
					<button type="button" class="btn btn-default" style="cursor: default;">条/页</button>
				</div>
				<div style="position: relative;top: -88px; left: 285px;">
					<nav>
						<ul class="pagination">
							<li class="disabled"><a href="#">首页</a></li>
							<li class="disabled"><a href="#">上一页</a></li>
							<li class="active"><a href="#">1</a></li>
							<li><a href="#">2</a></li>
							<li><a href="#">3</a></li>
							<li><a href="#">4</a></li>
							<li><a href="#">5</a></li>
							<li><a href="#">下一页</a></li>
							<li class="disabled"><a href="#">末页</a></li>
						</ul>
					</nav>
				</div>--%>


			</div>
			
		</div>
		
	</div>
</body>
</html>