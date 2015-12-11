angular.module('starter', ['ionic','starter.controllers','starter.config','starter.services','ngResource'])

.config(function($stateProvider,$urlRouterProvider,$ionicConfigProvider){
		$ionicConfigProvider.platform.ios.tabs.style('standard');
        $ionicConfigProvider.platform.ios.tabs.position('bottom');
        $ionicConfigProvider.platform.android.tabs.style('standard');
        $ionicConfigProvider.platform.android.tabs.position('standard');

        $ionicConfigProvider.platform.ios.navBar.alignTitle('center');
        $ionicConfigProvider.platform.android.navBar.alignTitle('left');

        $ionicConfigProvider.platform.ios.backButton.previousTitleText('').icon('ion-ios-arrow-thin-left');
        $ionicConfigProvider.platform.android.backButton.previousTitleText('').icon('ion-android-arrow-back');

        $ionicConfigProvider.platform.ios.views.transition('ios');
        $ionicConfigProvider.platform.android.views.transition('android');


	$stateProvider

			.state('tab',{
				url:"/tab",
				abstract:true,
				templateUrl:'templates/tabs.html'
			})
			.state('tab.home',{
				url:'/home',
				views:{
					'tab-home':{
						templateUrl:'templates/home/home.html',
						controller:'HomeCtrl'
					}
				}
			})
			.state('tab.article',{
				url:'/article',
				views:{
					'tab-article':{
						templateUrl:'templates/article/article.html',
						controller:'ArticleCtrl'
					}
				}
			})
			.state('tab.thread',{
				url:'/thread',
				views:{
					'tab-thread':{
						templateUrl:'templates/thread/thread.html',
						controller:'ThreadCtrl'
					}
				}
			})
			.state('tab.user',{
				url:'/user',
				views:{
					'tab-user':{
						templateUrl:'templates/user/user.html',
						controller:'UserCtrl'
					}
				}
			})
		
		$urlRouterProvider.otherwise('/tab/home');
})