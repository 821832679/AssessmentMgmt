var seconds = 60;
var timer;
$(function(){
	var smscode = "${smscode}";
	if(smscode == "false"){
		var diff = seconds - Math.floor((currTime - smsTime)/1000);
		$("#clocker").text(diff + "秒").attr("name", diff).show();
		$("#btn_smscode").attr("onclick", "").hide();
		clockDo();
	}
});

function addClocker(){
	timer = setTimeout("clockDo", 1000);
}

function clockDo(){
	var seconds = $("#clocker").attr("name");
	seconds -= 1;
	$("#clocker").text(seconds + "秒");
	$("#clocker").attr("name", seconds);
	if(seconds <= 0){
		clearTimeout(timer);
		$("#btn_smscode").attr("onclick", "sendSmscode()").show();
		$("#clocker").hide();
	}
	if(timer){
		clearTimeout(timer);
	}
	timer = setTimeout("clockDo()", 1000);
}

function sendSmscode(path){
	var mobileNumber = $("#phone").val();
	if($("#phone").val()==""){
		mobileNumber = $("#phone1").val();
		$("#loginForm").find("input[name='phone']").val(mobileNumber);
	}
	var userchk = $("#userchk").val();
	if(mobileNumber == undefined || $.trim(mobileNumber) == ''){
		jAlert("请输入手机号码","提示");
		return;
	}
	if(mobileNumber.length!=11){
		jAlert("手机号码格式错误，请重新输入","提示");
		return;
	}
	if(userchk == undefined || $.trim(userchk) == ''){
		jAlert("请先输入图片验证码","提示");
		return;
	}
	var data = {};
	data.mobile = mobileNumber;
	data.checkcode = $("#userchk").val();
	$.ajax({
		url: path + "/admin/login/checkCode",
		data: data,
		type: 'post',
		dataType: 'json',
		async:false,
		success: function(resp){
			if(resp.success){
				$.ajax({
					url: path + "/admin/login/smscode",
					type: 'post',
					dataType: 'json',
					data: {mobile: mobileNumber,checkcode: $("#userchk").val()},
					success: function(resp){
						if(resp.success){
							$("#clocker").text(seconds + "秒");
							$("#clocker").attr("name", seconds);
							$("#btn_smscode").attr("onclick", "").hide();
							$("#clocker").show();
							clockDo();
						}else{
							jAlert("短信发送失败，请稍后再试","提示");
							refreshCode(path);
						}
					},
					error: function(err){
						jAlert("短信发送失败，请稍后再试","提示");
						refreshCode(path);
					}
				});
			}else{
				refreshCode(path);
				$("#userchk").focus();
				jAlert(resp.message,"提示",function(){});
				return false;
			}
		},
		error: function(err){
			//refreshCode();
			return false;
		}
	});
}

function loginA(path){
	var userchk = $("#loginForm").find("input[name='captcha']").val();
	var smscode = $("#loginForm").find("input[name='smscode']").val();
	if(userchk == ""){
		jAlert("请输入图片验证码","提示");
		return false;
	}
	if(smscode == ""){
		jAlert("请输入手机动态码","提示");
		return false;
	}

	var username = $("#editForm").find("input[name='username']").val();
	var password = $("#editForm").find("input[name='password']").val();
	$("#loginForm").find("input[name='username']").val(username);
	$("#loginForm").find("input[name='password']").val(password);
	var data = common_ajax.ajaxFunc("/admin/login/loginA", $('#loginForm').serialize(), "json", null);
	if(data.success){
		//location.reload();
		location.href = path +"/admin/home";
	}else{
		refreshCode(path);
		jAlert(data.message,"提示");
	}
}

function getUrlParam(url,key){  
    url = url || location.search;  
    var arr = [],obj = {};  
    if(key){  
        url.replace(new RegExp("[&?]"+ key + "=([^&#]*)","ig"), function(a,b) {  
            arr.push(b);
        });
      return arr.join(",");
    }else{  
        url.replace(/[?&]([^&#]+)=([^&#]*)/g, function(a,b,c){  
            obj[b]=c;
        });
        return obj;
    }  
}

function refreshCode(path){
	$("#vcode").attr("src", path + "/admin/login/img" + "?r=" + (new Date()).getTime());
}