<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=utf-8"%>
<html>
<head>
 <meta charset="UTF-8" /> 
  <meta http-equiv="pragma" content="no-cache" /> 
  <meta http-equiv="Cache-Control" content="no-cache, must-revalidate" /> 
  <meta http-equiv="expires" content="0" /> 
  <title>瞳</title>
<link rel="shortcut icon" type="image/png"
	href="./oa/img/kehang_32x32.png" />
<link rel="stylesheet" type="text/css"
	href="/GF/lib/easyui/themes/bootstrap/easyui.css">
	<link rel="stylesheet" type="text/css"
	href="/GF/lib/easyui/themes/bootstrap/datagrid.css">
<link rel="stylesheet" type="text/css"
	href="/GF/lib/easyui/themes/icon.css">
<script type="text/javascript" src="/GF/lib/easyui/jquery.min.js"></script>
<script type="text/javascript" src="/GF/lib/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript"
	src="/GF/lib/easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="./loader.js"></script>
<script type="text/javascript" src="/GF/gf.core.js"></script>
<script type="text/javascript" src="/GF/gf.core.util.js"></script>
<script type="text/javascript" src="/GF/gf.core.impl.js"></script>
<style type="text/css">
.yunpan {
	display: block;
	float: left;
	height: 48px;
	font: 16px "Microsoft YaHei", "Microsoft JhengHei", "黑体";
	color: white;
	text-align: center;
	width: 90px;
	line-height: 48px;
}

.northdiv {
	height: 70px;
	border-bottom: 0;
	position: relative;
	padding: 0;
	box-shadow: 0 0 5px #333;
}

.head-left {
	margin: 0px;
	float: left;
	padding-left: 10px;
}

.head-right {
	margin: 0px;
	float: right;
	padding-right: 10px;
}

.active:hover {
	background: #0F0F0F;
}

.center {
	height: 130px;
	width: 300px;
	position: absolute;
	left: 0;
	top: 10px;
	right: 0;
	bottom: 0;
	margin: auto;
}
</style>
</head>

<body>
	<div class="easyui-layout heard" id="globallayout" fit=true>
		<div region="north" id="ban_bg" class="northdiv">
			<img alt="" src="./oa/img/banner_left.png" style="float:left;height: 100%;">
			<img alt="" src="./oa/img/banner_right.png" style="float:right;width:480px;height: 100%;">
		</div>
		<div region="west" id="nav" title="" border="true" split="false" collapsible=true
			style="width:200px;">
			<div class="easyui-accordion" data-options="fit:true,border:false"
				id='gf_accordion'></div>
		</div>
		<div region="center" id="view" title="" style="background:white">
			<div class="easyui-tabs" fit="true" style="height:auto"
				id="gf_viewtabs"></div>
		</div>
		<div region="east" selected="true" animate="false" split="true"
			border="false" id="control" title=""
			style="background:white;width:300px;overflow: auto;">
			<div class="easyui-tabs" tabPosition="top" headerWidth="50px"
				id="gf_controls" fit="true" selected="false"></div>
		</div>
	</div>
	<div class="easyui-dialog" id="gf-dialog" closed=true>
	</div>
	<div id="dialay1"></div>
     <div id="dialay2"></div>
</body>
</html>