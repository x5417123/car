/**
 * Created by xty
 */
var deviceControllers = angular.module('device.controllers', []);

deviceControllers.controller('DeviceHomeCtrl', function($scope,$ionicPopup,$ionicActionSheet,$timeout,SetWifi,UserLoginFactory,GetConfigInfo,configVolumeOutputType,adjustVolume,SetTermPassword,flow,ENV,configFmFrequency) {
	    UserLoginFactory.login();
		$scope.deviceData = {};
	 	$scope.Rang=0;
	 	$scope.deviceData=ENV.initValue;
	 	$scope.ifFm=false;
		$scope.setWifiPassword=function(){
			var title = "修改WIFI密码";
        	var subTitle=null;
        	var fn=function(data){
        	 	 console.log(data);
        	 	 var name = ENV.initValue.hotName;
        	 	 console.log(name)
        	 	 SetWifi.init(name,data)
        	}
        	type = "password";
			popup($scope,$ionicPopup,title,subTitle,fn,type);
		}
		$scope.setVideoSharePassword=function(){
			var title = "修改视频分享密码(无效)";
        	var subTitle=null;
        	var fn=function(data){
        	 	 console.log(data);
        	 	 
        	}
			popup($scope,$ionicPopup,title,subTitle,fn);
		}
		$scope.setFm=function(){
			var title = "FM频点";
	    	var subTitle=null;
	    	var fn=function(data){
	    	 	configFmFrequency.init(data);
	    	}
	    	type="number";
			popup($scope,$ionicPopup,title,subTitle,fn,type);
		}
		$scope.setMusicType=function(){
			  var hideSheet = $ionicActionSheet.show({
                  buttons: [
                    { text: "<i class='icon ion-ios-volume-high'></i> 喇叭" },
                    { text: "<i class='icon ion-bluetooth'></i> 蓝牙" },
                    { text: "<i class='icon ion-easel'></i> FM" },
                    { text: "<i class='icon ion-wifi'></i> WIFI" }
                  ],
                  titleText: '请选择声音输出方式',
                  cancelText: '取消',
                  cancel: function() {
                	  
                     },
                  buttonClicked: function(index) {
                	  console.log(index+1);
                	  configVolumeOutputType.init(index+1);
                      return true;
                  }
              });
              $timeout(function() {
                  hideSheet();
              }, 30000);
		}
		$scope.getRang=function(){
			$scope.Rang = document.getElementById('myRange').value;
			adjustVolume.init($scope.Rang);
		}
	    $scope.$on('login', function() {
	    	GetConfigInfo.init();
	    	flow.init();
        });
	    $scope.$on('GetConfigInfo',function(){
	    	var data=GetConfigInfo.get();
	    	for(k in data){
	    		if($scope.deviceData[k]){
	    			$scope.deviceData[k]=data[k];
	    		}
	    	}
	    })
	     $scope.$on('flow',function(){
	    	$scope.deviceData.flow = flow.get();
	    })
	    $scope.$on('SetDeviceSuccess',function(){
	    	GetConfigInfo.init();
	    	flow.init();
	    })
});
		
deviceControllers.controller('BluetoothCtrl', function($scope,$timeout,ENV,ScanBtdevices,queryBondedBtdevices,queryUnbondedBtdevices,pairBtdevice,unpairBtdevice) {
		var queryDevicesTime=ENV.queryDevicesTime;
		var refreshDevicesTime=ENV.refreshDevicesTime;
		$scope.refreshDevice=function(){
			ScanBtdevices.login();
		}
		ScanBtdevices.login();
		 $scope.$on('scanBtdevices',function(){
			//查询Term已配对的蓝牙外设列表
			 queryBondedBtdevices.init();
			//查询Term的可配对蓝牙外设列表
			 queryUnbondedBtdevices.init();
			  $timeout(function(){
				  ScanBtdevices.login();
			  },refreshDevicesTime);
		 })
		  $scope.$on('queryBondedBtdevices',function(){
			  $scope.bondedBtDevicesList=queryBondedBtdevices.get();
			  $timeout(function(){
				  queryBondedBtdevices.init()
			  },queryDevicesTime);
		 })
		  $scope.$on('queryUnbondedBtdevices',function(){
			  $scope.unbondedBtDevicesList=queryUnbondedBtdevices.get();
			  $timeout(function(){
				  queryUnbondedBtdevices.init();
			  },queryDevicesTime);
		 })
        $scope.bondedBtDevicesClick=function(device){
			 unpairBtdevice.init(device);
        }
        $scope.unbondedBtDevicesClick=function(device){
             pairBtdevice.init(device);
        }
    });
    
deviceControllers.controller('VersionCtrl', function($scope,ENV) {

        $scope.name='VersionCtrl';
    });
deviceControllers.controller('setpasswordCtrl', function($scope,$state,$ionicPopup,ENV,SetTermPassword) {
    $scope.showResult=false;
    $scope.resultcontent="密码不能为空";
    $scope.user={
    		oldpassword:"",
    		newpassword:""
    }
    $scope.result = function(){
    	console.log($scope.user);
    	console.log(ENV.initValue)
    	if($scope.user.oldpassword.length>0&&$scope.user.newpassword.length>0){
    		SetTermPassword.init($scope.user.oldpassword,$scope.user.newpassword);
    	}else{
    		$scope.showResult=true;
    	}
    }
    $scope.passwordChange=function(){
    	$scope.showResult=false;
    }
    $scope.$on('SetPasswordSuccess',function(){
    	  var alertPopup=$ionicPopup.alert({
    	        title: '提示',
    	        template: "修改密码成功！"
    	    });
    	   alertPopup.then(function(res) {
    		   $scope.user={
    		    		oldpassword:"",
    		    		newpassword:""
    		    }
    	        alertPopup.close();
    	        $state.go("devicehome");
    	    });
    })
    $scope.$on('SetPasswordfail',function(){
    	  var alertPopup=$ionicPopup.alert({
  	        title: '提示',
  	        template: "修改密码失败！"
  	    });
  	   alertPopup.then(function(res) {
  	        alertPopup.close();
  	        $state.go("devicehome");
  	    });
    	
    })
});
var popup=function($scope,$ionicPopup,title,subTitle,fn,type){
			var template='<input type="text" ng-model="deviceData.temporary">';
			if(type){
				if(type=="password"){
					template='<input type="password" ng-model="deviceData.temporary">';
				}else if(type=="number"){
					template='<div style="width: 100%;height:40px;line-height: 40px;">'
						+'<input type="number" min="88.0" max="108.0" style="width: 80%;float:left;" ng-model="deviceData.temporary">MHz</div>'
						+'<h5 ng-if="ifFm" class="assertive">请不要大于108.0或者小于88.0</h5>';
				}
			}
			$scope.ifFm=false;
		   // 一个精心制作的自定义弹窗
		   myPopup = $ionicPopup.show({
		     template: template,
		     title: title?title:'提示',
		     subTitle: subTitle?subTitle:'请输入',
		     scope: $scope,
		     buttons: [
		       { text: '取消' },
		       {
		         text: '<b>确定</b>',
		         type: 'button-positive',
		         onTap: function(e) {
		           if (!$scope.deviceData.temporary&&type!="number") {
		                 e.preventDefault();
		           } else {
		        	   console.log($scope.deviceData.temporary)
		        	   if(!$scope.deviceData.temporary){
		        		   $scope.ifFm=true;
		        		   e.preventDefault();
		        	   }else{
		        		   fn($scope.deviceData.temporary);
				           	 $scope.deviceData.temporary = null; 
		        	   }
		           }
		         }
		       }
		     ]
		   });
} 
