var aMailServices= angular.module('AMail',['ngRoute']);

//在URL，模板和控制器直接建立映射关系

aMailServices.config(function($routeProvider){
	$routeProvider
		.when('/',{
			controller:'ListController',
			templateUrl:'list.html'
		})
		.when('/view/:id',{
			controller:'DetailController',
			templateUrl:'detail.html'
		})
		.otherwise({
			redirectTo:'/'
		});
});

aMailServices.controller('ListController',function($scope,ENV){
	$scope.messages=ENV.message;
});

aMailServices.controller('DetailController',function($scope,$routeParams,ENV){
	$scope.message=ENV.message[$routeParams.id];
});

aMailServices.constant("ENV",{
	"debug":false,
	"version":"1.0.0",
	"message":[{
		id:0,sender:"jesa@123.com",subject:'Hi jesa',
		date:'Dec 7 ,2013 12:32:00',recipients:['greg@somecompany.com'],
		message:'Hey , we should get asd'
	},{
		id:1,sender:"fesda@123.com",subject:'Hi fesda',
		date:'Dec 7 ,2013 12:32:00',recipients:['greg@somecompany.com'],
		message:'Hey , we should get asd'
	},{
		id:2,sender:"xwee@123.com",subject:'Hi xwee',
		date:'Dec 7 ,2013 12:32:00',recipients:['greg@somecompany.com'],
		message:'Hey , we should get asd'
	}]
})