/**
 * GF
 */
$(function(){
	gfapp = new gf.core.impl.AppLoader();
	registerModule = function(mod) {
		gfapp.registerModule(mod);
	};
	initConfig = function(config) {
		gfapp.getAppContext().setConfig(config);
	};
	gfapp.addModule("./module.js");
	gfapp.start();
});