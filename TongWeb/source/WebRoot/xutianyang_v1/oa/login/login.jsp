<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=utf-8"%>
<%@ include file="./taglib.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<%@ include file="./headlib.jsp"%>
<script type="text/javascript" src="./oa/js/login.js"></script>
<link rel="stylesheet" type="text/css" href="./oa/css/login.css">
</head>
<body onload="createCode();intext()">
	<table width="100%" height="100%" border="0" cellspacing="0"
		cellpadding="0">
		<tr>
			<td bgcolor="#1075b1">&nbsp;</td>
		</tr>
		<tr>
			<td height="608" background="./oa/img/login/login_03.gif">
				<table width="847" border="0" align="center" cellpadding="0"
					cellspacing="0">
					<tr>
						<td height="318" background="./oa/img/login/login1.png">&nbsp;</td>
					</tr>
					<tr>
						<td height="84"><table width="100%" border="0"
								cellspacing="0" cellpadding="0">
								<tr>

									<td class="turn" width="270" height="84"
										background="./oa/img/login/login_06.gif">&nbsp;</td>
									<td class="turn" width="10" height="84"
										background="./oa/img/login/login_06_1.gif">&nbsp;</td>
									<td width="162" valign="middle"
										background="./oa/img/login/login_07.gif">

										<form name="from1" method="post" action="login">

											<table width="100%" border="0" cellspacing="0"
												cellpadding="0">
												<tr id="username">
													<td width="44" height="24" valign="bottom"><div
															align="right">
															<span class="STYLE3">用户</span>
														</div></td>
													<td width="10" valign="bottom">&nbsp;</td>
													<td height="24" colspan="2" valign="bottom">
														<div align="left">
															<input type="text" name="textfield" id="textfield"
																style="width:100px; height:17px; background-color:#87adbf; border:solid 1px #153966; font-size:12px; color:#283439;">
														</div>
													</td>
												</tr>
												<tr id="password">
													<td height="24" valign="bottom"><div align="right">
															<span class="STYLE3">密码</span>
														</div></td>
													<td width="10" valign="bottom">&nbsp;</td>
													<td height="24" colspan="2" valign="bottom"><input
														type="password" name="textfield2" id="textfield2"
														style="width:100px; height:17px; background-color:#87adbf; border:solid 1px #153966; font-size:12px; color:#283439; "></td>
												</tr>
												<tr id="testing">
													<td height="24" valign="bottom">
														<div align="right">
															<span class="STYLE3">验证</span>
														</div>
													</td>
													<td width="10" valign="bottom">&nbsp;</td>
													<td width="52" height="24" valign="bottom"><input
														type="text" name="textfield3" id="textfield3"
														style="width:50px; height:17px; background-color:#87adbf; border:solid 1px #153966; font-size:12px; color:#283439; "></td>
													<td width="62" valign="bottom">
														<div class="验证" align="left" onLoad="createCode();">
															<input type="button" id="checkCode" class="code"
																style="width:50px" onClick="createCode()">
														</div>
													</td>
												</tr>
												<tr></tr>
											</table>

										</form>

									</td>
									<td width="26"><img src="./oa/img/login/login_08.gif" width="26"
										height="84"></td>
									<td width="67" background="./oa/img/login/login_09.gif">
										<table width="100%" border="0" cellspacing="0" cellpadding="0">
											<tr>
												<td height="25"><div align="center"
														onclick="submitForm()">
														<img src="./oa/img/login/dl.gif" width="57" height="20">
													</div></td>
											</tr>
											<tr>
												<td height="25"><div align="center"
														onclick="clearForm()">
														<img src="./oa/img/login/cz.gif" width="57" height="20">
													</div></td>
											</tr>
										</table>
									</td>
									<td class="turn" width="10" background="./oa/img/login/login_10_1.gif">&nbsp;</td>
									<td class="turn" width="91" background="./oa/img/login/login_101.png">&nbsp;</td>
									<td class="turn" width="211" background="./oa/img/login/login_101.png"
										style="">&nbsp;</td>
								</tr>
							</table></td>
					</tr>
					<tr>
						<td height="206" background="./oa/img/login/login_11.gif">&nbsp;</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td bgcolor="#152753">&nbsp;</td>
		</tr>
	</table>
</body>
</html>
