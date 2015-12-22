/**
 * Created by xty
 */
var deviceServices=angular.module('device.services', []);
var userUUID = getQueryString("user_uuid");
var password = getQueryString("password");
     //用户授权
deviceServices.factory('UserLoginFactory', function($resource, $rootScope,ENV) {
        var UserLoginUrl = ENV.ip;
        var resource = $resource(UserLoginUrl, {}, {
            query: {
                method: 'get',
                params: {
                	act:'user_login',
                    user:"@user",
                    password: '@password'
                },
                timeout: 20000
            }
        });

        return {
        	login: function() {
                return resource.query({
                	  user:"{uuid:"+userUUID+"}",
                      password: password
                }, function(response) {
                    console.log(response)
                    if(response.responseCode==100){
                    	$rootScope.$broadcast('login');
                    }
                });
            }
        };
    })
      //获取终端配置信息
 deviceServices.factory('GetConfigInfo', function($resource, $rootScope,ENV) {

        var GetConfigInfo = ENV.ip;
        var deviceDate=null;
        var resource = $resource(GetConfigInfo, {}, {
            query: {
                method: 'get',
                params: {
                	act:'query_termconfiginfo',
                	user_uuid:"@user"
                },
                timeout: 20000
            }
        });

        return {
        	init: function() {
                return resource.query({
                	user_uuid:userUUID
                },function(response) {
                    console.log(response);
                    if(response.responseCode==100){
                    	deviceDate = response.data;
                    	$rootScope.$broadcast('GetConfigInfo');
                    }
                });
            },get:function(){
            	if(!deviceDate){
            		return false;
            	}
            	return deviceDate;
            }
        };
    })
       //3G网络流量
 deviceServices.factory('flow', function($resource, $rootScope,ENV) {

        var flowUrl = ENV.ip;
        var deviceDate=null;
        var resource = $resource(flowUrl, {}, {
            query: {
                method: 'get',
                params: {
                	act:'query_3g_network_usage',
                	user_uuid:"@user"
                },
                timeout: 20000
            }
        });

        return {
        	init: function() {
                return resource.query({
                	user_uuid:userUUID
                },function(response) {
                    console.log(response);
                    if(response.responseCode==100){
                    	if(response.currentMonthLimit>0&&response.downlinkData>0&&response.uplinkData>0){
                    		deviceDate = (response.currentMonthLimit - response.downlinkData - response.uplinkData) + "/" + response.currentMonthLimit;
                        	$rootScope.$broadcast('flow');
                    	}
                    }
                });
            },get:function(){
            	if(!deviceDate){
            		return "无";
            	}
            	return deviceDate;
            }
        };
    })
           //设置TermWIFI热点密码
 deviceServices.factory('SetWifi', function($resource, $rootScope,ENV) {

        var SetWifiUrl = ENV.ip;
        var deviceDate=null;
        var resource = $resource(SetWifiUrl, {}, {
            query: {
                method: 'get',
                params: {
                	act:'config_hostspot_password',
                	name:"@name",
                	new_password:"@newpassword",
                	user_uuid:"@user"
                },
                timeout: 20000
            }
        });

        return {
        	init: function(name,password) {
                return resource.query({
                	name:name,
                	new_password:password,
                	user_uuid:userUUID
                },function(response) {
                    console.log(response);
                    if(response.responseCode==100){
                    	deviceDate=response;
                    	$rootScope.$broadcast('SetDeviceSuccess');
                    }
                });
            },get:function(){
            	if(!deviceDate){
            		return false;
            	}
            	return deviceDate;
            }
        };
    })
       //设置Term访问密码
 deviceServices.factory('SetTermPassword', function($resource, $rootScope,ENV) {

        var SetWifiUrl = ENV.ip;
        var deviceDate=null;
        var resource = $resource(SetWifiUrl, {}, {
            query: {
                method: 'get',
                params: {
                	act:'config_term_password',
                	old_password:"@old_password",
                	new_password:"@new_password",
                	user_uuid:"@user"
                },
                timeout: 20000
            }
        });

        return {
        	init: function(oldPassword,newPassword) {
                return resource.query({
                	old_password:oldPassword,
                	new_password:newPassword,
                	user_uuid:userUUID
                },function(response) {
                    console.log(response);
                    if(response.responseCode==100){
                    	deviceDate=response;
                    	$rootScope.$broadcast('SetPasswordSuccess');
                    }else{
                    	$rootScope.$broadcast('SetPasswordfail');
                    }
                });
            },get:function(){
            	if(!deviceDate){
            		return false;
            	}
            	return deviceDate;
            }
        };
    })
      //设置FM发射频点
 deviceServices.factory('configFmFrequency', function($resource, $rootScope,ENV) {

        var SetWifiUrl = ENV.ip;
        var deviceDate=null;
        var resource = $resource(SetWifiUrl, {}, {
            query: {
                method: 'get',
                params: {
                	act:'config_fm_frequency',
                	frequency:"frequency",
                	user_uuid:"@user"
                },
                timeout: 20000
            }
        });

        return {
        	init: function(frequency_) {
                return resource.query({
                	frequency:frequency_,
                	user_uuid:userUUID
                },function(response) {
                    console.log(response);
                    if(response.responseCode==100){
                    	deviceDate=response;
                    	$rootScope.$broadcast('SetDeviceSuccess');
                    }
                });
            },get:function(){
            	if(!deviceDate){
            		return false;
            	}
            	return deviceDate;
            }
        };
    })
      //设置输出音量
 deviceServices.factory('adjustVolume', function($resource, $rootScope,ENV) {

        var SetWifiUrl = ENV.ip;
        var deviceDate=null;
        var resource = $resource(SetWifiUrl, {}, {
            query: {
                method: 'get',
                params: {
                	act:'adjust_volume',
                	size:"@size",
                	user_uuid:"@user"
                },
                timeout: 20000
            }
        });

        return {
        	init: function(size) {
                return resource.query({
                	size:size,
                	user_uuid:userUUID
                },function(response) {
                    console.log(response);
                    if(response.responseCode==100){
                    	deviceDate=response;
                    	$rootScope.$broadcast('SetDeviceSuccess');
                    }
                });
            },get:function(){
            	if(!deviceDate){
            		return false;
            	}
            	return deviceDate;
            }
        };
    })
  //设置声音输出方式
 deviceServices.factory('configVolumeOutputType', function($resource, $rootScope,ENV) {

        var SetWifiUrl = ENV.ip;
        var deviceDate=null;
        var resource = $resource(SetWifiUrl, {}, {
            query: {
                method: 'get',
                params: {
                	act:'config_volume_output_type',
                	type:"@type",
                	user_uuid:"@user"
                },
                timeout: 20000
            }
        });

        return {
        	init: function(type_) {
                return resource.query({
                	type:type_,
                	user_uuid:userUUID
                },function(response) {
                    console.log(response);
                    if(response.responseCode==100){
                    	deviceDate=response;
                    	$rootScope.$broadcast('SetDeviceSuccess');
                    }
                });
            },get:function(){
            	if(!deviceDate){
            		return false;
            	}
            	return deviceDate;
            }
        };
    })
       //扫描蓝牙设备
deviceServices.factory('ScanBtdevices', function($resource, $rootScope,ENV) {
        var ScanBtdevicesUrl = ENV.ip;
        var resource = $resource(ScanBtdevicesUrl, {}, {
            query: {
                method: 'get',
                params: {
                	act:'scan_btdevices',
                	user_uuid:"@user"
                },
                timeout: 20000
            }
        });

        return {
        	login: function() {
                return resource.query({
                	user_uuid:userUUID
                },function(response) {
                		console.log(response)
                    if(response.responseCode==100){
                    	$rootScope.$broadcast('scanBtdevices');
                    }
                });
            }
        };
    })  
           //查询Term已配对的蓝牙外设列表
deviceServices.factory('queryBondedBtdevices', function($resource, $rootScope,ENV) {
        var BondedBtdevices=null;
		var queryBondedBtdevicesUrl = ENV.ip;
        var resource = $resource(queryBondedBtdevicesUrl, {}, {
            query: {
                method: 'get',
                params: {
                	act:'query_bonded_btdevices',
                	user_uuid:"@user"
                },
                timeout: 20000
            }
        });

        return {
        	init: function() {
                return resource.query({
                	user_uuid:userUUID
                },function(response) {
                		console.log(response)
                    if(response.responseCode==100){
                    	BondedBtdevices=response.data;
                    	$rootScope.$broadcast('queryBondedBtdevices');
                    }
                });
            },get:function(){
            	if(BondedBtdevices == null){
            		return false;
            	}
            	return BondedBtdevices;
            }
        };
    })  
    //查询Term的可配对蓝牙外设列表
deviceServices.factory('queryUnbondedBtdevices', function($resource, $rootScope,ENV) {
		var unBondedBtdevices=null;
		var queryUnbondedBtdevicesUrl = ENV.ip;
        var resource = $resource(queryUnbondedBtdevicesUrl, {}, {
            query: {
                method: 'get',
                params: {
                	act:'query_unbonded_btdevices',
                	user_uuid:"@user"
                },
                timeout: 20000
            }
        });

        return {
        	init: function() {
                return resource.query({
                	user_uuid:userUUID
                },function(response) {
                		console.log(response)
                    if(response.responseCode==100){
                    	unBondedBtdevices=response.data;
                    	$rootScope.$broadcast('queryUnbondedBtdevices');
                    }
                });
            },
            get:function(){
            	if(unBondedBtdevices == null){
            		return false;
            	}
            	return unBondedBtdevices;
            }
        };
    })
        //配对Term蓝牙外设
deviceServices.factory('pairBtdevice', function($resource, $rootScope,ENV) {
        var pairBtdeviceUrl = ENV.ip;
        var resource = $resource(pairBtdeviceUrl, {}, {
            query: {
                method: 'get',
                params: {
                	act:'pair_btdevice',
                	btdevice:"@btdevice",
                	user_uuid:"@user"
                },
                timeout: 20000
            }
        });

        return {
        	init: function(btdevice) {
                return resource.query({
                	btdevice:btdevice,
                	user_uuid:userUUID
                },function(response) {
                		console.log(response)
                    if(response.responseCode==100){
                    	$rootScope.$broadcast('pairBtdevice');
                    }
                });
            }
        };
    })
    //删除Term蓝牙外设配对信息
    deviceServices.factory('unpairBtdevice', function($resource, $rootScope,ENV) {
            var unpairBtdeviceUrl = ENV.ip;
            var resource = $resource(unpairBtdeviceUrl, {}, {
                query: {
                    method: 'get',
                    params: {
                    	act:'unpair_btdevice',
                    	btdevice:"@btdevice",
                    	user_uuid:"@user"
                    },
                    timeout: 20000
                }
            });

            return {
            	init: function(btdevice) {
                    return resource.query({
                    	btdevice:btdevice,
                    	user_uuid:userUUID
                    },function(response) {
                    		console.log(response)
                        if(response.responseCode==100){
                        	$rootScope.$broadcast('unpairBtdevice');
                        }
                    });
                }
            };
        })
    function getQueryString(name) {  
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");  
        var r = window.location.search.substr(1).match(reg);  
        if (r != null) return unescape(r[2]);  
        return null;  
    }  
  