/**
 * @author Lion
 * @date 2017年1月24日 下午12:02:35
 * @qq 439635374
 */
package com.pointlion.sys.mvc.admin.sqlcontrol;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import com.jfinal.aop.Before;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;
import com.pointlion.sys.interceptor.MainPageTitleInterceptor;
import com.pointlion.sys.mvc.common.base.BaseController;
import com.pointlion.sys.mvc.common.model.SysUser;
import com.pointlion.sys.mvc.common.utils.UuidUtil;
import com.pointlion.sys.plugin.shiro.ShiroKit;

/***
 * 用户管理控制器
 * 
 * @author Administrator
 *
 */
@Before(MainPageTitleInterceptor.class)
public class SqlController extends BaseController {

	/***
	 * 获取问卷管理首页
	 */
	public void getDraftListPage() {
		render("/WEB-INF/admin/sqlcontrol/draft/list.html");
	}
	
	
	
	private String pageTitle = "性能监控";// 模块页面标题
	private String breadHomeMethod = "getDraftListPage";// 面包屑首页方法

	public Map<String, String> getPageTitleBread() {
		Map<String, String> pageTitleBread = new HashMap<String, String>();
		pageTitleBread.put("pageTitle", pageTitle);
		pageTitleBread.put("breadHomeMethod", breadHomeMethod);
		return pageTitleBread;
	}
	
}
