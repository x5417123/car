if (!gf.modules) {
	gf.modules = {};
}
if (!gf.modules.app) {
	gf.modules.app = {};
}
gf.modules.app.tong = function() {
	this.temp = gf.core.AbstractModule;
	this.temp();
	delete this.temp;
	this.Vendor = "科航";
	this.moduleName = "gf.modules.app.tong";
	this.ui = null;
	this._moduleInfo = new gf.core.ModuleInfo();
	this._moduleInfo.Vendor = this.Vendor;
	this._moduleInfo.Name = this.moduleName;
	this._depends = [];
	this.getModuleInfo = function() {
		return this._moduleInfo;
	};
	this.getModuleDependency = function() {
		return _depends;
	};
	this.Height=function(){
		return $(window).height();
	};
	this.width=function(){
		return $(window).width();
	};
	this.doInit = function(context) {
		// 作为成员对象调用的function是指上级
		console.log("gf.modules.app.tong doInit");
		this.ui = context.getUiManager();
		this.addOaMenu();
		this.addRadioMenu();
		console.log("gf.modules.app.tong doInit ok");
	};
	this.addOaMenu = function() {
		var menuName = "后台管理";
		var menuHtml = "./oa/page/menu.html";
		var menuID = "后台管理";
		var order = 1;
		this.ui.addAccordionMenu(this, menuHtml, menuName, menuID, '100%',
				order);
	};
	this.addRadioMenu = function() {
		var menuName = "广播电台管理";
		var menuHtml = "./radio/page/menu.html";
		var menuID = "广播电台管理";
		var order = 1;
		this.ui.addAccordionMenu(this, menuHtml, menuName, menuID, '100%',
				order);
	};
	this.addViewPage = function() {
		this.viewPath = "";
		this.viewID = "";
		this.viewName = "";
		this.ui.addViewPage(this, this.viewPath, this.viewName, this.viewID,
				true);
	};
	this.addClosableViewPage = function(page, _title) {
		ifCheckTitle = true;// 判断是否要多开
		var frameID = _title + "_id" + Math.floor(Math.random() * (100000 + 1));
		name = _title;
		this.ui.addClosableViewPage(this, page, _title, frameID, ifCheckTitle,
				name);
	};
	this.addDialog = function(page, title, modal, width, height, left, top) {
		var frameID = title + "_id" + Math.floor(Math.random() * (100000 + 1));
		gfmeta = {};
		this.ui.addDialogView(this, page, title, frameID, modal, gfmeta, width,
				height, left, top);
	};
	this.closeDialog = function() {
		$("#gf-dialog").dialog('close');
	};
	this.getUrlParameter = function(sParam) {
		var sPageURL = window.location.search.substring(1);
		var sURLVariables = sPageURL.split('&');
		for (var i = 0; i < sURLVariables.length; i++) {
			var sParameterName = sURLVariables[i].split('=');
			if (sParameterName[0] == sParam) {
				return sParameterName[1];
			}
		}
	};
	//转换时间字符串为2015-04-16 11:11:12
	this.changetime=function(data){
		var time=null;
		if(data!=null){
			time=data;
			time = time.substring(0, 4)+"-"+time.substring(4, 6)+"-"+time.substring(6, 8)
			+" "+time.substring(8,10)+":"+time.substring(10, 12)+":"+time.substring(12, 14);
		}
		return time;
	};
	//icon：显示的图片，可选值：error,question,info,warning
	this.alert=function(title,msg,icon,fn){
		 $.messager.alert(title,msg,icon,fn);
	};
	this.appendprogress=function(){
		var windowDiv=$("<div></div>");
		windowDiv.attr("id","windowDiv");
		windowDiv.css("position","absolute");
		windowDiv.css("top","0px");
		windowDiv.css("left","0px");
		windowDiv.css("background","black");
		windowDiv.css("opacity","0.3");
		windowDiv.width("100%");
		windowDiv.height("100%");
		windowDiv.appendTo($("body"));
		var progress=$("<div></div>");
		progress.attr("id","progressbar");
		progress.width(400);
		progress.height(20);
		progress.css("position","absolute");
		progress.css("left","0px");
		progress.css("top","0px");
		progress.css("right","0px");
		progress.css("bottom","0px");
		progress.css("margin","auto");
		progress.appendTo(windowDiv);
		progress.progressbar({
		    width:400
		});
	};
	this.changeprogress=function(value){
		$("#progressbar").progressbar("setValue",value);
	};	
	this.closeprogress=function(){
		$("#windowDiv").remove();
	};
};
(function() {
	// 模块注册惯用法
	var mod = new gf.modules.app.tong();
	registerModule(mod);
	console.log("gf.modules.app.tong registerModule ok");
	// #########################################################
})();
