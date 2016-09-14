var $userservice = function() {
	var url = "http://bigdata.blazer.org:8010";
	var checkuser = url + "/userservice/checkuser.do";
	var getlogin = url + "/login.html";

	var init = function() {
		// alert(checkuser);
		$.ajax({
			url : checkuser,
			type : "GET",
			data : {
				MYSESSIONID : getCookie("MYSESSIONID")
			},
			success : function(data) {
				if (data == false || data == "false") {
					alert("对不起，您没有登录，请您登录。");
					location.href = getlogin + "?url=" + encodeURIComponent(location.href);
				} else {
				}
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				alert(XMLHttpRequest.status);
				alert(XMLHttpRequest.readyState);
				alert(textStatus);
			}
		});
	};

	function getCookie(c_name) {
		if (document.cookie.length > 0) {
			c_start = document.cookie.indexOf(c_name + "=")
			if (c_start != -1) {
				c_start = c_start + c_name.length + 1
				c_end = document.cookie.indexOf(";", c_start)
				if (c_end == -1)
					c_end = document.cookie.length
				return unescape(document.cookie.substring(c_start, c_end))
			}
		}
		return ""
	}
	init();
};

$userservice();