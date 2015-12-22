if (!gf.modules) {
	gf.modules = {};
}
if (!gf.modules.app) {
	gf.modules.app = {};
}
gf.modules.app.mod_zhgk_zource = function() {
	this.temp = gf.core.AbstractModule;
	this.temp();
	delete this.temp;
	this.Vendor = "科航";
	this.moduleName = "gf.modules.app.mod_zhgk_zource";
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
		console.log("gf.modules.app.mod_zhgk_zource doInit");
		this.ui = context.getUiManager();
		this.addMenu();
		console.log("gf.modules.app.mod_zhgk_zource doInit ok");
	};
	this.addMenu = function() {
		var menuName = "数据录入";
		var menuHtml = "/mod_zhgk_source/page/menu.html";
		var menuID = "数据录入";
		var order = 1;
		this.ui.addAccordionMenu(this, menuHtml, menuName, menuID, '100%',
				order);
	};
	this.addViewPage = function() {
		this.viewPath = "/mod_zhgk_source/page/clz/clzview.html";
		this.viewID = "菜篮子主产区调查表";
		this.viewName = "菜篮子主产区调查表";
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
	//转换时间字符串为2015-04-16 11:11:12
	this.changetimeType=function(data){
			if(data!="NULL"){
				data = data.substring(0, 19);
			}
			return data;
	};
};
(function() {
	// 模块注册惯用法
	var mod = new gf.modules.app.mod_zhgk_zource();
	registerModule(mod);
	console.log("gf.modules.app.mod_zhgk_zource registerModule ok");
	// #########################################################
})();
