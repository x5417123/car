/**
 * Created by xty
 */
var configMod=angular.module("device.config", []);


configMod.constant("ENV", {
    'ip':"http://192.168.1.154:8080/term",
    'queryDevicesTime':2000,
    'refreshDevicesTime':20000,
	"debug": false,
    'version':'1.0.0',
    'initValue':{
			hotName:'查询中..',
 			hotPassword:'查询中..',
 			videoPassword:'查询中..',
 			fmFrequency:'查询中..',
 			flow:'无',
 			sound:"查询中..",
 			deviceData:"查询中..",
 			sn:"查询中..",
 	}
});

