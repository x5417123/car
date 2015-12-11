angular.module("starter.controllers",[])

.controller('HomeCtrl',function($scope,ENV){
	console.log(ENV);
	$scope.name="HomeCtrl";
})
.controller('ThreadCtrl',function($scope){
	$scope.name="ThreadCtrl";
})
.controller('UserCtrl',function($scope){
	$scope.name="UserCtrl";
})
.controller('ArticleCtrl',function($scope,ArticleFactory,ENV){
	console.log(ENV);
	$scope.name="ArticleCtrl";
});