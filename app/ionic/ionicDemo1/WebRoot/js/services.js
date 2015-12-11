angular.module('starter.services',[])

.factory('ArticleFactory',function($rootScope,$resource,ENV){

	console.log(ENV);
	
	var apiUrl=ENV.api,
	topics = {},
    catid = 20;
	
});