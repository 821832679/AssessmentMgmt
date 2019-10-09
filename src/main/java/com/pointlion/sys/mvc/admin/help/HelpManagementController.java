/**
 * @author Lion
 * @date 2017年1月24日 下午12:02:35
 * @qq 439635374
 */
package com.pointlion.sys.mvc.admin.help;

import java.util.HashMap;
import java.util.Map;

import com.jfinal.aop.Before;
import com.pointlion.sys.interceptor.MainPageTitleInterceptor;
import com.pointlion.sys.mvc.common.base.BaseController;

@Before(MainPageTitleInterceptor.class)
public class HelpManagementController extends BaseController {

	public void getListPage() {
		render("/WEB-INF/admin/help/list.html");
	}


	/************************ 数据库管理---结束 *************************************************/

	/**************************************************************************/
	private String pageTitle = "帮助文档";// 模块页面标题
	private String breadHomeMethod = "getListPage";// 面包屑首页方法

	public Map<String, String> getPageTitleBread() {
		String topicid = getPara("topicid");
		if(topicid==null || "".equals(topicid)){
			pageTitle = "帮助文档";// 模块页面标题
		}
		Map<String, String> pageTitleBread = new HashMap<String, String>();
		pageTitleBread.put("pageTitle", pageTitle);
		pageTitleBread.put("breadHomeMethod", breadHomeMethod);
		return pageTitleBread;
	}
}
