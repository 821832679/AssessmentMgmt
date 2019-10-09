package com.pointlion.sys.mvc.admin.topictype;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.aop.Before;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;
import com.pointlion.sys.interceptor.MainPageTitleInterceptor;
import com.pointlion.sys.mvc.common.base.BaseController;
import com.pointlion.sys.mvc.common.model.TopicType;
import com.pointlion.sys.mvc.common.utils.UuidUtil;

@Before(MainPageTitleInterceptor.class)
public class TopicTypeManagementController extends BaseController {


	/**
	 * 获得指标列表分类页面
	 */
	public void getListPage() {
		render("/WEB-INF/admin/topictype/list.html");
	}

	/**
	 * 获得指标列表分类数据
	 * @throws UnsupportedEncodingException 
	 */
	public void listData() throws UnsupportedEncodingException {
		String curr = getPara("pageNumber");
		Float currFloat = Float.valueOf(curr);
		Integer currPage = currFloat.intValue();
		String pageSize = getPara("pageSize");
		String status = getPara("StatusValue");

		Page<TopicType> page = TopicType.dao.getTopicByStatusPage(currPage, Integer.valueOf(pageSize),status);
		renderPage(page.getList(), "", page.getTotalRow());
	}
	
	
	public void listStatusData() throws UnsupportedEncodingException {
		String status = getPara("StatusValue");
		List<TopicType> list = TopicType.dao.getTopicByStatus(status);
		renderPage(list, "", 0);
	}

	/**
	 * 获得编辑页面
	 */
	public void getEditPage() {
		String id = getPara("id");
		if (StrKit.notBlank(id)) {
			TopicType templpate = TopicType.dao.findById(id);
			setAttr("t", templpate);
		}
		render("/WEB-INF/admin/topictype/edit.html");
	}

	/**
	 * 保存
	 */
	public void save() {
		TopicType topicType = getModel(TopicType.class);
		if (StrKit.notBlank(topicType.getId())) {
			topicType.update();
		} else {
			topicType.setId(UuidUtil.getUUID());
			topicType.save();
		}
		renderSuccess();
	}

	/***
	 * 删除
	 * 
	 * @throws Exception
	 */
	public void delete() throws Exception {
		TopicType.dao.deleteByIds(getPara("ids"));
		renderSuccess();
	}
	
	/***
	 * 启用/禁用
	 * 
	 * @throws Exception
	 */
	public void status() throws Exception {
		String status = getPara("status");
		TopicType.dao.statusByIds(getPara("ids"), status);
		renderSuccess();
	}

	/************************ 数据库管理---结束 *************************************************/

	/**************************************************************************/
	private String pageTitle = "指标分类列表";// 模块页面标题
	private String breadHomeMethod = "getListPage";// 面包屑首页方法

	public Map<String, String> getPageTitleBread() {
		Map<String, String> pageTitleBread = new HashMap<String, String>();
		pageTitleBread.put("pageTitle", pageTitle);
		pageTitleBread.put("breadHomeMethod", breadHomeMethod);
		return pageTitleBread;
	}
}