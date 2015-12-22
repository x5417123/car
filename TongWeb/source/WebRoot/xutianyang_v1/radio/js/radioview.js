var treegrid, menu;
var radioIcon,areaIcon,chinaIcon;
$(function() {
	areaIcon="icon-city";
	radioIcon="icon-radio";
	chinaIcon="icon-china";
	treegrid = $("#radioTreegrid");
	menu = $("#menu");
	gfmod.radioInitTreegrid = radioInitTreegrid;
	gfmod.radioInitTreegrid();
});
function radioInitTreegrid() {
	treegrid.treegrid({
		fit : true,
		treeField : 'name',
		idField : 'uuid',
		columns : [ [ {
			field : 'name',
			title : '名称',
			width : "40%",
			align : 'left'
		}, {
			field : 'collection',
			title : '推荐状态',
			width : "10%",
			align : 'center',
			formatter:function(value,rowData,rowIndex){
				if (rowData.area_uuid) {
					if(rowData.collection){
						return "已推荐";
					}else{
						return "未推荐";
					}
				}
			}
		} ,{
			field : 'url',
			title : 'url',
			width : "50%",
			align : 'center'
		}] ],
		onContextMenu : function(e, node) {
			e.preventDefault();
			if (node.area_uuid) {
				checkRadio(e, node);
			} else {
				checkArea(e, node);
			}
		},onClickRow:function(row){
				treegrid.treegrid('toggle',row.uuid);
		}
	});
	treegrid.treegrid('loadData', {
		total : 0,
		rows : []
	});// 先清空treegrid
	var areaUrl = "/tong/servlet/area?act=ls_area";
	$.post(areaUrl, function(areadata) {
		if(areadata.responseCode==100){
			for (var i = 0; i < areadata.data.length; i++) {
				if(areadata.data[i].name=="中国"){
					areadata.data[i].iconCls=chinaIcon;
				}else{
					areadata.data[i].iconCls=areaIcon;
				}
				treegrid.treegrid("append", {
					parent : areadata.data[i].parentUuid,
					data : [ areadata.data[i] ]
				});
			}
			var radioUrl = "/tong/servlet/radio?act=ls_radio";
			$.post(radioUrl, function(radiodata) {
				if(radiodata.responseCode==100){
					if(radiodata.data.length>0){
						var radioArray=[];
						for (var i = 0; i < radiodata.data.length; i++) {
							radiodata.data[i].iconCls=radioIcon;
							radioArray.push(radiodata.data[i]);
						}
						var radioColle="/tong/servlet/radio?act=ls_radio_colle";
						$.post(radioColle,function(radioColledata){
							if(radioColledata.responseCode==100){
								radioColledata=radioColledata.data;
								if(radioColledata.length>0){
									for(var i=0;i<radioColledata.length;i++){
										var radioColle=radioColledata[i].uuid;
										out:for(var j=0;j<radioArray.length;j++){
											if(radioArray[j].uuid==radioColle){
												radioArray[j].collection=true;
												break out;
											}
										}
									}
									for(var i=0;i<radioArray.length;i++){
										if(!radioArray[i].collection){
											radioArray[i].collection=false;
										}
										treegrid.treegrid("append", {
											parent : radioArray[i].area_uuid,
											data : [ radioArray[i] ]
										});
									}
								}else{
									for(var k=0;k<radioArray.length;k++){
										radioArray[k].collection=false;
										treegrid.treegrid("append", {
											parent : radioArray[k].area_uuid,
											data : [ radioArray[k] ]
										});
									}
								}
							}else{
								$.messager.alert("错误！",radioColledata.message,"error");
							}
						  },"JSON");
					}
				}else{
					$.messager.alert("错误！",radiodata.message,"error");
				}
			}, "JSON");
		}else{
			$.messager.alert("错误！",radiodata.message,"error");
		}
	}, "JSON");
}
function checkRadio(e, node) {
	if(node.collection){
		menu.menu('appendItem', {
			text : '取消推荐电台',
			onclick : function() {
				delRadioColle(node);
			}
		});
	}else{
		menu.menu('appendItem', {
			text : '设置为推荐电台',
			onclick : function() {
				addRadioColle(node);
			}
		});
	}
	menu.menu('appendItem', {
		text : '修改电台',
		onclick : function() {
			updRadio(node);
		}
	});
	menu.menu('appendItem', {
		text : '删除电台',
		onclick : function() {
			delRadio(node);
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
function checkArea(e, node) {
	if (node.parentUuid != 0) {
		menu.menu('appendItem', {
			text : '删除区域',
			onclick : function() {
				delArea(node);
			}
		});
	}
	menu.menu('appendItem', {
		text : '添加子区域',
		onclick : function() {
			addArea(node);
		}
	});
	menu.menu('appendItem', {
		text : '区域重命名',
		onclick : function() {
			updArea(node);
		}
	});
	menu.menu('appendItem', {
		text : '添加电台',
		onclick : function() {
			addRadio(node);
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

function addRadio(node){
	gfmod.TreegridNode = node;
	var page = "./radio/page/addradio.html";
	var title = "新建电台";
	var width = 300;
	var height = 200;
	var left = (gfmod.width() - width) / 2;
	var top = (gfmod.Height() - height) / 2;
	gfmod.addDialog(page, title, true, width, height, left, top);
};

function delRadio(node){
	$.messager.confirm("删除","确定删除吗？",function(r){
		if(r){
			var url="/tong/servlet/radio?act=del_radio&radio_uuid="+node.uuid;
			$.post(url,function(data){
				if(data.responseCode==100){
					$.messager.alert("操作","删除成功！","info",function(){
						gfmod.closeDialog();
						gfmod.radioInitTreegrid();
					});
				}else{
					$.messager.alert("错误！",data.message,"error");
				}
			},"JSON");
		}
	});
};
//新增推荐电台
function addRadioColle(node){
	$.messager.confirm("设置","确定设置为推荐电台吗？",function(r){
		if(r){
			var url="/tong/servlet/radio?act=add_radio_colle&area_uuid="+node.area_uuid+"&radio_uuid="+node.uuid;
			$.post(url,function(data){
				if(data.responseCode==100){
					$.messager.alert("操作","设置成功！","info",function(){
						gfmod.closeDialog();
						gfmod.radioInitTreegrid();
					});
				}else{
					$.messager.alert("错误！",data.message,"error");
				}
			},"JSON");
		}
	});
}
//删除推荐电台
function delRadioColle(node){
	$.messager.confirm("设置","确定取消推荐电台吗？",function(r){
		if(r){
			var url="/tong/servlet/radio?act=del_radio_colle&area_uuid="+node.area_uuid+"&radio_uuid="+node.uuid;
			$.post(url,function(data){
				if(data.responseCode==100){
					$.messager.alert("操作","设置成功！","info",function(){
						gfmod.closeDialog();
						gfmod.radioInitTreegrid();
					});
				}else{
					$.messager.alert("错误！",data.message,"error");
				}
			},"JSON");
		}
	});
}
//更新电台
function updRadio(node){
	gfmod.TreegridNode = node;
	var page = "./radio/page/upradio.html";
	var title = "更新电台";
	var width = 300;
	var height = 200;
	var left = (gfmod.width() - width) / 2;
	var top = (gfmod.Height() - height) / 2;
	gfmod.addDialog(page, title, true, width, height, left, top);
}
//删除地区
function delArea(node){
	$.messager.confirm("设置","确定要删除当前地区吗？",function(r){
		if(r){
			var url="/tong/servlet/area?act=del_area&uuid="+node.uuid;
			$.post(url,function(data){
				if(data.responseCode==100){
					$.messager.alert("操作","删除成功！","info",function(){
						gfmod.closeDialog();
						gfmod.radioInitTreegrid();
					});
				}else{
					$.messager.alert("错误！",data.message,"error");
				}
			},"JSON");
		}
	});
}
//修改地区
function updArea(node){
	gfmod.TreegridNode = node;
	var page = "./radio/page/uparea.html";
	var title = "修改区域";
	var width = 300;
	var height = 150;
	var left = (gfmod.width() - width) / 2;
	var top = (gfmod.Height() - height) / 2;
	gfmod.addDialog(page, title, true, width, height, left, top);
}
//添加区域
function addArea(node){
	gfmod.TreegridNode = node;
	var page = "./radio/page/addarea.html";
	var title = "添加子区域";
	var width = 300;
	var height = 150;
	var left = (gfmod.width() - width) / 2;
	var top = (gfmod.Height() - height) / 2;
	gfmod.addDialog(page, title, true, width, height, left, top);
}