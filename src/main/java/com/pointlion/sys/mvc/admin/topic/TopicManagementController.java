package com.pointlion.sys.mvc.admin.topic;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.druid.util.StringUtils;
import com.jfinal.aop.Before;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;
import com.pointlion.sys.interceptor.MainPageTitleInterceptor;
import com.pointlion.sys.mvc.common.base.BaseController;
import com.pointlion.sys.mvc.common.model.Answer;
import com.pointlion.sys.mvc.common.model.Question;
import com.pointlion.sys.mvc.common.model.SysOrg;
import com.pointlion.sys.mvc.common.model.SysUser;
import com.pointlion.sys.mvc.common.model.Topic;
import com.pointlion.sys.mvc.common.model.TopicDelay;
import com.pointlion.sys.mvc.common.utils.DateUtils;
import com.pointlion.sys.mvc.common.utils.SysOrgUtil;
import com.pointlion.sys.mvc.common.utils.UuidUtil;

@Before(MainPageTitleInterceptor.class)
public class TopicManagementController extends BaseController {

	/*************************** 指标列表管理---开始 ***********************/

	/**
	 * 获得指标列表管理页面
	 */
	public void getListPage() {
		render("/WEB-INF/admin/topic/list.html");
	}

	/**
	 * 获得指标列表管理数据
	 * @throws UnsupportedEncodingException 
	 */
	public void listData() throws UnsupportedEncodingException {
		String username = getSessionAttr("username");
		SysUser user = SysUser.dao.findbyUserName(username);
		String ids = "";
		if (StringUtils.isEmpty(user.getOrgid())) {
			ids = "#root";
		} else {
			ids = user.getOrgid();
		}
		List<SysOrg> sysOrgList = SysOrg.dao.getChildrenAll(ids);
		String result = SysOrgUtil.getOrgListForString(sysOrgList, user.getOrgid());
		
		String curr = getPara("pageNumber");
		Float currFloat = Float.valueOf(curr);
		Integer currPage = currFloat.intValue();
		String pageSize = getPara("pageSize");
		String Statustype = getPara("StatusValue");
		String Searcht1 = getPara("SearchValue");
		String Searcht = Searcht1;
		if (Searcht1 != "") {
			Searcht = URLDecoder.decode(Searcht1, "UTF-8");
		}
		Page<Topic> page = Topic.dao.getTopicPage(currPage, Integer.valueOf(pageSize),Statustype,Searcht,result);
		renderPage(page.getList(), "", page.getTotalRow());
	}

	/**
	 * 判断该指标列表是否已经提交过
	 * @throws UnsupportedEncodingException 
	 */
	public void listDataForSubmit() throws UnsupportedEncodingException {
		String username = getSessionAttr("username");
		SysUser currUser = SysUser.dao.findbyUserName(username);
		String result = SysOrgUtil.getOrgParentById(currUser.getOrgid(), new StringBuilder()).toString();
		
		String curr = getPara("pageNumber");
		Float currFloat = Float.valueOf(curr);
		Integer currPage = currFloat.intValue();
		String pageSize = getPara("pageSize");
		String Searcht1 = getPara("SearchValue");
		String Searcht = Searcht1;
		if (Searcht1 != "") {
			Searcht = URLDecoder.decode(Searcht1, "UTF-8");
		}
		Page<Topic> page = Topic.dao.getTopicByStatusPage(currPage, Integer.valueOf(pageSize), Searcht, result);
		List<Topic> list = new ArrayList<Topic>();
		for (int i = 0; i < page.getList().size(); i++) {
			Topic topic = page.getList().get(i);
			Page<Question> page2 = Question.dao.getQuestionBySstatusPage(currPage, Integer.valueOf(pageSize), topic.getId());
			if(page2==null || page2.getList().size()<=0){
				continue;
			}
				
			Page<Answer> page3 = Answer.dao.getAnswerPageByUser(currPage, Integer.valueOf(pageSize), topic.getId(), currUser.getId());
			if (page3 != null && page3.getList().size() > 0) {
				topic.setSubmit(0);
			} else {
				topic.setSubmit(1);
			}
			
			//判断是否延期考核
			TopicDelay delay = TopicDelay.dao.find(topic.getId(), currUser.getId());
			if(delay!=null && delay.getStat()==1){
				topic.put("yqendtime", DateUtils.convert2YMdhms(DateUtils.convert2YMdhmsTime(delay.getYqendTime())));
			}
			list.add(topic);
		}
		renderPage(list, "", page.getTotalRow());
	}

	/**
	 * 获得编辑页面
	 */
	public void getEditPage() {
		String id = getPara("id");
		if (StrKit.notBlank(id)) {
			Topic templpate = Topic.dao.findById(id);
			templpate.setBeginTime(DateUtils.convert2YMdhms(DateUtils.convert2YMdhmsTime(templpate.getBeginTime())));
			templpate.setEndTime(DateUtils.convert2YMdhms(DateUtils.convert2YMdhmsTime(templpate.getEndTime())));
			templpate.setQxBeginTime(DateUtils.convert2YMdhms(DateUtils.convert2YMdhmsTime(templpate.getQxBeginTime())));
			templpate.setQxEndTime(DateUtils.convert2YMdhms(DateUtils.convert2YMdhmsTime(templpate.getQxEndTime())));
			templpate.setZjBeginTime(DateUtils.convert2YMdhms(DateUtils.convert2YMdhmsTime(templpate.getZjBeginTime())));
			templpate.setZjEndTime(DateUtils.convert2YMdhms(DateUtils.convert2YMdhmsTime(templpate.getZjEndTime())));
			setAttr("t", templpate);
		}
		render("/WEB-INF/admin/topic/edit.html");
	}

	/**
	 * 保存
	 */
	public void save() {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Topic topic = getModel(Topic.class);
		String username = getSessionAttr("username");
		SysUser user = SysUser.dao.findbyUserName(username);
		if(StrKit.isBlank(topic.getBeginTime()) || StrKit.isBlank(topic.getEndTime())){
			renderError("请设置考核时间");
			return;
		}
		if(StrKit.isBlank(topic.getQxBeginTime()) || StrKit.isBlank(topic.getQxEndTime())){
			renderError("请设置区县评分时间");
			return;
		}
		if(StrKit.isBlank(topic.getZjBeginTime()) || StrKit.isBlank(topic.getZjEndTime())){
			renderError("请设置专家评分时间");
			return;
		}
		
		if (StrKit.notBlank(topic.getId())) {
			topic.update();
		} else {
			topic.setId(UuidUtil.getUUID());
			topic.setUserId(user.getId());
			topic.setCreateTime(sf.format(new Date()));
			topic.save();
		}
		renderSuccess();
	}

	/***
	 * 删除
	 * 
	 * @throws Exception
	 */
	public void delete() throws Exception {
		Topic.dao.deleteByIds(getPara("ids"));
		renderSuccess();
	}
	
	/***
	 * 启用/禁用
	 * 
	 * @throws Exception
	 */
	public void status() throws Exception {
		String status = getPara("status");
		Topic.dao.statusByIds(getPara("ids"), status);
		renderSuccess();
	}

	/************************ 数据库管理---结束 *************************************************/

	/**************************************************************************/
	private String pageTitle = "任务指标管理";// 模块页面标题
	private String breadHomeMethod = "getListPage";// 面包屑首页方法

	public Map<String, String> getPageTitleBread() {
		Map<String, String> pageTitleBread = new HashMap<String, String>();
		pageTitleBread.put("pageTitle", pageTitle);
		pageTitleBread.put("breadHomeMethod", breadHomeMethod);
		return pageTitleBread;
	}
}