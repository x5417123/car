var treegrid , menu;
$(function(){
	treegrid=$("#treegrid");
	menu = $("#menu");
	gfmod.UsergroupInitTreegrid=UsergroupInitTreegrid;
	gfmod.UsergroupInitTreegrid();
	$("#addUg").click(function(){
		addUserGroup();
	});
});
function UsergroupInitTreegrid() {
	treegrid.treegrid({
		fit : true,
		treeField : 'name',
		idField : 'idField',
		singleSelect:true,
		columns : [ [ {
			field : 'name',
			title : '名称',
			width : "20%",
			align : 'left'
		},{
			field : 'auth',
			title : '权限',
			width : "35%",
			align : 'center'
		},{
			field : 'remark',
			title : '备注',
			width : "25%",
			align : 'center'
		},{
			field : 'createTime',
			title : '创建时间',
			width : "20%",
			align : 'center'
		}] ],
		onContextMenu : function(e, node) {
			console.log(node);
			e.preventDefault();
			if(node.children){
				checkUserGroup(e, node);
			}else{
				checkUser(e, node);
			}
		},
		onClickRow:function(row){
				treegrid.treegrid('toggle',row.uuid);
		}
	});
	treegrid.treegrid('loadData', {
		total : 0,
		rows : []
	});// 先清空treegrid
	var UserGroupUrl = "/tong/servlet/usergroup?act=ls_grp";
	$.post(UserGroupUrl, function(UserGroup) {
		if(UserGroup.responseCode==100){
			for(var i=0;i<UserGroup.data.length;i++){
				UserGroup.data[i].idField=UserGroup.data[i].uuid+Math.random();
				UserGroup.data[i].createTime=gfmod.changetime(UserGroup.data[i].createTime);
				UserGroup.data[i].auth="";
				for(var j=0;j<UserGroup.data[i].oaAuth.length;j++){
					if(j!=0){
						UserGroup.data[i].auth += ","+UserGroup.data[i].oaAuth[j].name;
					}else{
						UserGroup.data[i].auth += UserGroup.data[i].oaAuth[j].name;
					}
				}
				UserGroup.data[i].children=[];
				for(var j=0;j<UserGroup.data[i].oaUser.length;j++){
					UserGroup.data[i].children.push(UserGroup.data[i].oaUser[j]);
					UserGroup.data[i].children[j].idField=UserGroup.data[i].children[j].uuid+Math.random();
					UserGroup.data[i].children[j].parentId=UserGroup.data[i].uuid;
				}
				treegrid.treegrid("append", {
					parent : UserGroup.data[i].uuid,
					data : [ UserGroup.data[i] ]
				});
			}
		}else{
			$.messager.alert("错误！",UserGroup.message,"info",function(){
				top.location.href="/tong";
			});
		}
	}, "JSON");
}
function addUserGroup(){
	    var page ="oa/page/addusergroup.html";
		var title = "新建用户组";
		var width=400;
		var height=250;
		var left=(gfmod.width()-width)/2;
		var top=(gfmod.Height()-height)/2;
		gfmod.addDialog(page,title,true,width,height,left,top);
}
function checkUser(e, node) {
	menu.menu('appendItem', {
		text : '删除用户',
		onclick : function() {
			delUser(node);
		}
	});
	menu.menu({
		onHide : function() {
			menu.html("");
		}
	});
	menu.menu('show', {
		left : e.pageX - 10,
		top : e.pageY - 10
	});
}
function checkUserGroup(e, node) {
	menu.menu('appendItem', {
			text : '删除组',
			onclick : function() {
				delUserGroup(node);
			}
	});
	menu.menu('appendItem', {
		text : '添加权限',
		onclick : function() {
			addUserGroupAuth(node);
		}
	});
	menu.menu('appendItem', {
		text : '删除权限',
		onclick : function() {
			delUserGroupAuth(node);
		}
	});
	menu.menu('appendItem', {
		text : '添加用户',
		onclick : function() {
			addUserInGroup(node);
		}
	});
	menu.menu('appendItem', {
		text : '修改',
		onclick : function() {
			upUserGroup(node);
		}
	});
	menu.menu({
		onHide : function() {
			menu.html("");
		}
	});
	menu.menu('show', {
		left : e.pageX - 10,
		top : e.pageY - 10
	});
};
function delUser(node){
	$.messager.confirm("操作","确定要把当前用户从组里去除吗？",function(r){
		if(r){
			if(node.name=="admin"){
				$.messager.alert("错误！","管理员权限不可以删除！","error");
			}else{
				var userUuidLs=new Array();
				userUuidLs.push(node.uuid);
				userUuidLs="["+userUuidLs+"]";
				var url="/tong/servlet/usergroup?act=grp_del_usr&uuid="+node.parentId+"&user_uuid_ls="+userUuidLs;
				console.log(url);
				$.post(url,function(data){
					if(data.responseCode==100){
						$.messager.alert("操作","删除成功！","info",function(){
							gfmod.closeDialog();
							gfmod.UsergroupInitTreegrid();
						});
					}else{
						$.messager.alert("错误！",data.message,"error");
					};
				},"JSON");
			};
		};
	});
};
function delUserGroup(node){
	$.messager.confirm("操作","确定要删除当前用户组吗？",function(r){
		if(r){
			if(node.children.length !=0){
				$.messager.alert("错误！","请先清空用户组中用户！","error");
			}else{
				if(node.uuid == "admin"){
					$.messager.alert("错误！","管理员组不可以删除！","error");
				}else{
					var url="/tong/servlet/usergroup?act=del_grp&uuid="+node.uuid;
					$.post(url,function(data){
						if(data.responseCode==100){
							$.messager.alert("操作","删除成功！","info",function(){
								gfmod.closeDialog();
								gfmod.UsergroupInitTreegrid();
							});
						}else{
							$.messager.alert("错误！",data.message,"error");
						};
					},"JSON");
				}
			}
		};
	});
}
function addUserInGroup(node){
		gfmod.userGroupNode=node;
	    var page ="oa/page/addUserInGroup.html";
		var title = "添加用户";
		var width=400;
		var height=600;
		var left=(gfmod.width()-width)/2;
		var top=(gfmod.Height()-height)/2;
		gfmod.addDialog(page,title,true,width,height,left,top);
};
function addUserGroupAuth(node){
	gfmod.userGroupNode=node;
    var page ="oa/page/addAuth.html";
	var title = "添加权限";
	var width=400;
	var height=600;
	var left=(gfmod.width()-width)/2;
	var top=(gfmod.Height()-height)/2;
	gfmod.addDialog(page,title,true,width,height,left,top);
}  
function delUserGroupAuth(node){
	gfmod.userGroupNode=node;
    var page ="oa/page/delAuth.html";
	var title = "删除权限";
	var width=400;
	var height=600;
	var left=(gfmod.width()-width)/2;
	var top=(gfmod.Height()-height)/2;
	gfmod.addDialog(page,title,true,width,height,left,top);
}
function upUserGroup(node){
	gfmod.userGroupNode=node;
	 var page ="oa/page/upusergroup.html";
	 var title = "修改用户组";
	 var width=400;
	 var height=250;
	 var left=(gfmod.width()-width)/2;
	 var top=(gfmod.Height()-height)/2;
	 gfmod.addDialog(page,title,true,width,height,left,top);
}