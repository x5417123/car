var datagrid;
$(function() {
	datagrid=$('#userdatagrid');
	if (gfmod) {
		gfmod.UserManageInitdatagrid = initdatagrid;
	}
	gfmod.UserManageInitdatagrid();
	// 隐藏 删除,重命名 按钮
	$("#delUser,#rnmUser").hide();
	//点击行触发事件
	datagrid.datagrid({
		onSelectAll : function() {
			button();
		},
		onUncheckAll : function() {
			button();
		},
		onUnselect : function() {
			button();
		},
		onClickRow : function() {
			button();
		},
		onCheck : function() {
			button();
		}
	});
	
	// 新建用户
	$("#addUser").click(function() {
		var page ="oa/page/adduser.html";
 		var title = "新建用户";
 		var width=450;
 		var height=350;
 		var left=(gfmod.width()-width)/2;
 		var top=(gfmod.Height()-height)/2;
 		gfmod.addDialog(page,title,true,width,height,left,top);
	});
	$("#rnmUser").click(function() {
		var rows = datagrid.datagrid("getSelections");
		if (rows.length == 1) {
			gfmod.oaUserInfo=rows[0];
			var page ="oa/page/upuser.html";
	 		var title = "修改用户";
	 		var width=450;
	 		var height=350;
	 		var left=(gfmod.width()-width)/2;
	 		var top=(gfmod.Height()-height)/2;
	 		gfmod.addDialog(page,title,true,width,height,left,top);
		} 
	});
	$("#delUser").click(function(){
		var rows = datagrid.datagrid("getSelections");
		var confirm="";
		if(rows.length==1){
			confirm="确定要删除"+rows[0].name+"用户吗？";
		}else{
			confirm="确定要删除当前"+rows.length+"个用户吗？";
		}
		$.messager.confirm("设置","确定要删除当前"+rows.length+"个用户吗？",function(r){
			if(r){
				gfmod.appendprogress();
				var rowsDataLength=rows.length;
				var index=0;
				var message;
				for(var i=0;i<rows.length;i++){
					var url="/tong/servlet/oauser?act=del_user&uuid="+rows[i].uuid;
					var ifbreak=true;
					$.ajax({
						async:false,
						url:url,
						dataType:"JSON",
						success:function(data){
							if(data.responseCode==100){
								index++;
								gfmod.changeprogress(index/rowsDataLength);
							}else{
								message=data.message;
								ifbreak=false;
							}
						},
						error:function(){
							ifbreak=false;
						}
					});
					if(!ifbreak){
						break;
					}
				}
				gfmod.closeprogress();
				if(index==rowsDataLength){
					$.messager.alert("操作","删除成功！","info",function(){
						gfmod.UserManageInitdatagrid();
					});
				}else{
					$.messager.alert("错误！","删除第"+(index+1)+"个出错！"+message,"info",function(){
						gfmod.UserManageInitdatagrid();
					});
					
				}
			}
		});
	});
});

//初始化datagrid
function initdatagrid() {
	$("#delUser,#rnmUser").hide();
	datagrid.datagrid({
		fit : true,
		fitColumns : true,
		striped : true,// 交替显示行背景
		singleSelect : false,// 只能选择一行
		checkOnSelect : true,// 点击checkbox将永远选择这行
		columns : [ [ {
			field : 'ck',
			checkbox : true
		}, {
			field : 'name',
			title : '用户名',
			width : '23%',
			align : 'center'
		}, {
			field : 'phone',
			title : '手机号',
			width : '22%',
			align : 'center'
		},{
			field : 'email',
			title : '邮箱号',
			width : '24%',
			align : 'center'
		},{
			field : 'remark',
			title : '备&nbsp;注',
			width : '30%',
			align : 'center'
		} ] ]
	});
	datagrid.datagrid('loadData', {
		total : 0,
		rows : []
	});// 先清空treegrid
	_url = "/tong/servlet/oauser?act=ls_user";
	$.ajax({
		type : "POST",
		url : _url,
		dataType : "json",
		async:true,
		success : function(data) {
			if(data.responseCode==100){
				for (var i = 0; i < data.data.length; i++) {
					datagrid.datagrid("appendRow", data.data[i]);
				}
			}else{
				var title="错误";
				var msg="<p style='text-align:center'>"+data.message+"<p>";
				var icon="error";
				var fn=function(){
					top.location.href="/tong";
				};
				gfmod.alert(title,msg,icon,fn);
			}
		},
		error : function(error) {
			var title="错误";
			var msg="<p style='text-align:center'>"+error+"<p>";
			var icon="error";
			var fn=function(){
				top.location.href="/tong";
			};
			gfmod.alert(title,msg,icon,fn);
		}
	});
}

//按钮显示/隐藏
function button() {
	var post = datagrid.datagrid('getSelections');
	if (post.length<=0) {
		$("#delUser,#rnmUser").hide();
	} else if(post.length == 1){
		$("#delUser,#rnmUser").show();
	} else{
		$("#rnmUser").hide();
		$("#delUser").show();
	}
}
	// #################################################################
	// 从数据库删除用户
	/*function delUser(uid) {
		_url = "/zhgk_api/servlet/user?act=del_usr&id=" + uid;
		console.log(_url);
		$.ajax({
			type : "POST",
			url : _url,
			contentType : "application/json;charset=utf-8",
			dataType : "json",
			async : false,
			success : function(data) {
				if (data.responseCode == 100) {
					$('#dataid').datagrid('reload');
				} else {
					$.messager.alert('警告', '无法删除已加入到用户组中的用户！', 'error');
				}
			},
			error : function(error) {
				console.log("ajax error-->" + error);
			}
		});
	}

	// 新建用户输入框事件
	$('#adddlg').dialog({
		iconCls : 'icon-save',
		buttons : [ {
			text : '确定',
			iconCls : 'icon-ok',
			handler : function() {
				var result = true;
				if ($("#newName").val() == "") {
					result = false;
				}
				if ($("#newPassword").val() == "") {
					result = false;
				}
				if ($("#newPassword").val() != $("#confirm").val()) {
					result = false;
				}
				// 新建用户
				if (result) {
					addUser();
					updatedatagrid();
					$('#adddlg').dialog("close");
					$("#adddlg").css("display", "none");
				} else {
					$.messager.alert('警告', '输入的用户名或密码无效！', 'error');
				}
			}
		}, {
			text : '取消',
			iconCls : 'icon-cancel',
			handler : function() {
				$('#adddlg').dialog("close");
				$("#adddlg").css("display", "none");
			}
		} ]
	});

	// 新建用户到数据库
	function addUser() {
		_url = "/zhgk_api/servlet/user?act=add_usr&u=";
		_url += "{id: '0',";
		_url += "name: '" + $('#newName').val() + "',";
		_url += "password: '" + $('#newPassword').val() + "',";
		_url += "phone: ' " + $('#newPhone').val() + "',";
		_url += "remark: '" + $('#newRemark').val() + "'}";
		console.log(_url);
		_url = encodeURI(encodeURI(_url));
		$.ajax({
			type : "POST",
			url : _url,
			contentType : "application/json;charset=utf-8",
			dataType : "json",
			async : false,
			success : function(data) {
				if (data.responseCode == 100) {
				} else {
					$.messager.alert('警告', '用户名重复，添加失败！', 'error');
				}
			},
			error : function(error) {
				console.log("ajax error-->" + error);
			}
		});
	}

	// 重命名用户到数据库
	function rnmUser(uid) {
		_url = "/zhgk_api/servlet/user?act=rnm_usr&id=" + uid;
		_url += "&name=" + $('#rnm').val();
		console.log(_url);
		_url = encodeURI(encodeURI(_url));
		$.ajax({
			type : "POST",
			url : _url,
			contentType : "application/json;charset=utf-8",
			dataType : "json",
			async : false,
			success : function(data) {
				if (data.responseCode == 100) {
					$('#dataid').datagrid('reload');
				} else {
					$.messager.alert('警告', '用户名重复，添加失败！', 'error');
				}
			},
			error : function(error) {
				console.log("ajax error-->" + error);
			}
		});
	}

	// 重命名输入框事件
	$('#rnmdlg').dialog({
		iconCls : 'icon-save',
		buttons : [ {
			text : '确定',
			iconCls : 'icon-ok',
			handler : function() {
				var result = true;
				if ($("#rnm").val() == "") {
					result = false;
				}
				var row = $('#dataid').datagrid("getSelected");
				if (result) {
					rnmUser(row.id);
					updatedatagrid();
					$('#dataid').datagrid('reload');
					$('#rnmdlg').dialog("close");
					$("#rnmdlg").css("display", "none");
				} else {
					$.messager.alert('警告', '输入的用户名不能为空！', 'error');
				}
			}
		}, {
			text : '取消',
			iconCls : 'icon-cancel',
			handler : function() {
				$('#rnmdlg').dialog("close");
				$("#rnmdlg").css("display", "none");
			}
		} ]
	});

	// 密码验证框
	$.extend($.fn.validatebox.defaults.rules, {
		equals : {
			validator : function(value, param) {
				return value == $(param[0]).val();
			},
			message : '两次输入的密码不相同！.'
		}
	});
	// ########################点击上方按钮事件开始##########################
	// 删除用户
	$("#delUser").click(function() {
		var rows = $('#dataid').datagrid("getSelected");
		$.messager.confirm('确认', '您确认想要删除记录吗？', function(r) {
			if (r) {
				delUser(rows.id);
			}
		});
		updatedatagrid();
	});
	// 新建用户
	$("#addUser").click(function() {
		$("#adddlg").css("display", "block");
		$('#adddlg').dialog('resize', {
			top : 50,
			height : 270
		});
		$("#adddlg").dialog("hcenter");
		$("#adddlg").dialog("open");
	});

	// 重命名用户
	$("#rnmUser").click(function() {
		var rows = $('#dataid').datagrid("getSelections");
		if (rows.length == 1) {
			$("#rnmdlg").css("display", "block");
			$('#rnmdlg').dialog('resize', {
				top : 50,
				height : 90
			});
			$("#rnmdlg").dialog("hcenter");
			$("#rnmdlg").dialog("open");
		} else {
			$.messager.alert('警告', '只能对单个用户重命名！', 'error');
		}
	});
	// ########################点击上方按钮事件结束##########################
	// #########################datagrid事件开始#########################
	
	// #########################datagrid事件结束#########################

	
	

	function updatedatagrid() {
		// 清空表格
		var item = $('#dataid').datagrid('getRows');
		if (item) {
			for (var i = item.length - 1; i >= 0; i--) {
				var index = $('#dataid').datagrid('getRowIndex', item[i]);
				$('#dataid').datagrid('deleteRow', index);
			}
		}
		_url = "/zhgk_api/servlet/user?act=ls_usr";
		console.log(_url);
		$.ajax({
			type : "POST",
			url : _url,
			contentType : "application/json;charset=utf-8",
			dataType : "json",
			success : function(data) {
				for (var i = 0; i < data.data.length; i++) {
					$("#dataid").datagrid("appendRow", data.data[i]);
				}
			},
			error : function(error) {
				console.log("ajax error-->" + error);
			}
		});
	}
});*/
