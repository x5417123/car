function intext() {
	document.getElementById("textfield").focus();
}
window.onload = function() {
	document.onkeydown = keyDown;
	$(this).keydown(function(e) {// jquery获取keycode值
		if (e && e.keyCode == 13) {
			submitForm();

		}
	})
};

function keyDown(e) {
	var code = e.which;
	var key = String.fromCharCode(code);

}
var verificationCode; // 在全局 定义验证码
var controlVerificationCode = 0;// 控制验证码是否出现 1个出现0是不出现
function createCode() {
	if (controlVerificationCode == 0) {
		$("#testing").hide();
		//document.getElementById("testing").style.display = "none";
	}
	verificationCode = new Array();
	var codeLength = 4;// 验证码的长度
	var checkCode = document.getElementById("checkCode");
	checkCode.value = "";
	var selectChar = new Array(2, 3, 4, 5, 6, 7, 8, 9, 'A', 'B', 'C', 'D', 'E',
			'F', 'G', 'H', 'J', 'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'S', 'T',
			'U', 'V', 'W', 'X', 'Y', 'Z');
	for (var i = 0; i < codeLength; i++) {
		var charIndex = Math.floor(Math.random() * 32);
		verificationCode += selectChar[charIndex];
	}
	if (verificationCode.length != codeLength) {
		createCode();
	}
	checkCode.value = verificationCode;
}

function submitForm() {
	var inputCode = document.getElementById("textfield3").value.toUpperCase();
	if (controlVerificationCode == 0) {
		var user = $("#textfield").val();
		var pass = $("#textfield2").val();
		var url="/tong/servlet/login?act=login&user=" + user + "&pass=" + pass;
		$.post(url,function(data) {
					if (data.responseCode == 200) {
						alert("账号密码错误！");
					} else if (data.responseCode == 100) {
						location.href = "./index.jsp";
					};
				}, "JSON");
	} else if (controlVerificationCode == 1) {
		if (inputCode.length <= 0) {
			alert("请输入验证码！");

		} else if (inputCode != verificationCode) {
			alert("验证码输入错误！");
			createCode();
		}
		var user = $("#textfield").val();
		var pass = $("#textfield2").val();
		var url="/tong/servlet/login?act=login&user=" + user + "&pass=" + pass;
		$.post(url,function(data) {
			if (data.responseCode == 200) {
				alert("账号密码错误！");
			} else if (data.responseCode == 100) {
				location.href = "../index.jsp";
			};
				}, "JSON");
	};
}
function clearForm() {
	document.from1.reset();
}