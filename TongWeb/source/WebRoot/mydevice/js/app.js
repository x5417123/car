/**
 * Created by xty 
 */
angular.module('device', ['ionic','device.controllers','device.config','device.services','device.directive','ngResource'])
    .config(function($stateProvider, $urlRouterProvider,$ionicConfigProvider) {
        $ionicConfigProvider.platform.ios.tabs.style('standard');
        $ionicConfigProvider.platform.ios.tabs.position('bottom');
        $ionicConfigProvider.platform.android.tabs.style('standard');
        $ionicConfigProvider.platform.android.tabs.position('standard');

        $ionicConfigProvider.platform.ios.navBar.alignTitle('center');
        $ionicConfigProvider.platform.android.navBar.alignTitle('left');
        $ionicConfigProvider.navBar.alignTitle('center');
        
        $ionicConfigProvider.platform.ios.backButton.previousTitleText('').icon('ion-ios-arrow-thin-left');
        $ionicConfigProvider.platform.android.backButton.previousTitleText('').icon('ion-android-arrow-back');

        $ionicConfigProvider.backButton.previousTitleText(false).text('返回');
        
        $ionicConfigProvider.platform.ios.views.transition('ios');
        $ionicConfigProvider.platform.android.views.transition('android');
        $stateProvider
            .state('devicehome', {
                 url: '/devicehome',
                 templateUrl: 'mydevice/templates/device/device.html',
                 controller: 'DeviceHomeCtrl'
            })
            .state('bluetooth', {
                 url: '/bluetooth',
                 templateUrl: 'mydevice/templates/bluetooth/bluetooth.html',
                 controller: 'BluetoothCtrl'
            })
            .state('version', {
                 url: '/version',
                 templateUrl: 'mydevice/templates/version/version.html',
                 controller: 'VersionCtrl'
            })
            .state('setpassword', {
                url: '/setpassword',
                templateUrl: 'mydevice/templates/password/password.html',
                controller: 'setpasswordCtrl'
           })
        $urlRouterProvider.otherwise('/devicehome');

    });
