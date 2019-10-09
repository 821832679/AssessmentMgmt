package com.pointlion.sys.mvc.common.utils;

import java.util.HashMap;

import org.apache.log4j.Logger;

import com.cloopen.rest.sdk.CCPRestSDK;
import com.pointlion.sys.mvc.common.GlobalConstant;

public class SmsUtil {

	private static Logger logger = Logger.getLogger(SmsUtil.class);
	
	/**
	 * 发送 短信
	 * @param templateId  短信模版id
	 * @param mobile 	     手机 
	 * @param content     短信
	 * @param type		     短信作用类型  1: 注册    2:修改登录密码   3:忘记登录密码    4:修改支付密码   5:绑定其他手机    6:其他
	 * @return
	 */
	public static HashMap<String, Object> send(final String templateId, final String mobile, final String[] content){
		final CCPRestSDK restAPI = new CCPRestSDK();
		restAPI.init(GlobalConstant.SMS_HOST, GlobalConstant.SMS_PORT);
		restAPI.setAccount(GlobalConstant.SMS_ACCOUNT_SID, GlobalConstant.SMS_ACCOUNT_TOKEN);
		restAPI.setAppId(GlobalConstant.SMS_APP_ID);// 初始化应用ID
		
		HashMap<String, Object> result = restAPI.sendTemplateSMS(mobile,templateId ,content);
		
		if("000000".equals(result.get("statusCode"))){
			//正常返回输出data包体信息（map）
			logger.info("sms send success, templateId="+templateId+"");
		}else{
			//异常返回输出错误码和错误信息
			Object msgObj = result.get("statusMsg");
			logger.info("sms send failed, statusCode=" + result.get("statusCode") +",errorMsg= "+msgObj);
		}
		
		return result;
	}
	
}
