package com.pointlion.sys.mvc.common;

/**
 * 全局变量定义类
 * 
 * @author Administrator
 */
public class GlobalConstant {
	public static final String SMS_HOST = "app.cloopen.com";
	public static final String SMS_PORT = "8883";
	public static final String SMS_ACCOUNT_SID = "8a48b5514da42dc3014dacc1d50a042b";
	public static final String SMS_ACCOUNT_TOKEN = "82df0f86d5ef4feb93a550efb08a6c49";
	public static final String SMS_APP_ID = "8aaf0708685978ed01685a938e97015b";
	/**
	 * 短信模版-验证码
	 */
	public static final String SMS_TEMPLATE_CHECKCODE = "409136";
	/**
	 * 短信模版-订单成功 {1} 订单名 {2} 韵动券
	 */
	public static final String SMS_TEMPLATE_ORDER_SUCCESS = "408941";
	/**
	 * 短信模版-54466 积分兑换商品 {1} 订单名 {2} 韵动券 {3} 地址
	 */
	public static final String SMS_TEMPLATE_POINT_EXCH = "408922";

	/**
	 * 购票成功-短信通知
	 */
	public static final String SMS_TEMPLATE_TICKET_ORDER_SUCCESS = "408940";
	/**
	 * 游泳馆预定短信
	 */
	public static final String SMS_TEMPLATE_SWIMMING_VENUE_ORDER_SUCCESS = "408926";
	/**
	 * 活动变更
	 */
	public static final String SMS_TEMPLATE_ACTIVITY_NOTIFY_PARAMETER_2 = "408939";

	/**
	 * 活动通知短信
	 */
	public static final String SMS_TEMPLATE_ACTIVITY_NOTIFY_PARAMETER_1 = "408937";
}