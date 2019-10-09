package com.pointlion.sys.mvc.admin.scorelook;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
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
import com.pointlion.sys.mvc.common.model.Resource;
import com.pointlion.sys.mvc.common.model.Score;
import com.pointlion.sys.mvc.common.model.SysOrg;
import com.pointlion.sys.mvc.common.model.SysUser;
import com.pointlion.sys.mvc.common.model.Topic;
import com.pointlion.sys.mvc.common.model.TopicType;
import com.pointlion.sys.mvc.common.utils.ArabicToChineseUtil;
import com.pointlion.sys.mvc.common.utils.DateUtils;
import com.pointlion.sys.mvc.common.utils.SysOrgUtil;

@Before(MainPageTitleInterceptor.class)
public class ScoreLookManagementController extends BaseController {

	/*************************** 评分管理---开始 ***********************/

	/**
	 * 获得评分管理页面
	 */
	public void getListPage() {
		render("/WEB-INF/admin/scorelook/list.html");
	}
	/**
	 * 获得专家评分管理页面
	 */
	public void getZjListPage() {
		render("/WEB-INF/admin/scorelook/zjlist.html");
	}

	/**
	 * 获得评分管理数据
	 * @throws UnsupportedEncodingException 
	 */
	public void listData() throws UnsupportedEncodingException {
		String username = getSessionAttr("username");
		SysUser currUser = SysUser.dao.findbyUserName(username);
		//String result = SysOrgUtil.getOrgParentById(currUser.getOrgid(), new StringBuilder()).toString();
		String ids = "";
		if (StringUtils.isEmpty(currUser.getOrgid())) {
			ids = "#root";
		} else {
			ids = currUser.getOrgid();
		}
		List<SysOrg> sysOrgList = SysOrg.dao.getChildrenAll(ids);
		String result = SysOrgUtil.getOrgListForString(sysOrgList, currUser.getOrgid());
		
		String curr = getPara("pageNumber");
		Float currFloat = Float.valueOf(curr);
		Integer currPage = currFloat.intValue();
		String pageSize = getPara("pageSize");
		String Searcht1 = getPara("SearchValue");
		String orgid = getPara("orgid");
		String Searcht = Searcht1;
		if (Searcht1 != "") {
			Searcht = URLDecoder.decode(Searcht1, "UTF-8");
		}
		Page<Score> page = Score.dao.getScoreNotNullPage(currPage, Integer.valueOf(pageSize),Searcht, orgid,result, currUser.getId());
		if("1".equals(currUser.getIsAdmin())){
			page = Score.dao.getScoreNotNullPage(currPage, Integer.valueOf(pageSize),Searcht, orgid, result);
		}
		List<Score> list = new ArrayList<Score>();
		for (int i = 0; i < page.getList().size(); i++) {
			Score score = page.getList().get(i);
			SysUser user = SysUser.dao.findById(score.getUserId());
			score.setUserName(user.getName());
			if (score.getPersonId() != null) {
				user = SysUser.dao.findById(score.getPersonId());
				score.setPerson(user.getName());
			}
			if (score.getZjId() != null) {
				user = SysUser.dao.findById(score.getZjId());
				score.setZjName(user.getName());
			}
			Topic topic = Topic.dao.findById(score.getTopicId());
			if (topic != null) {
				score.setTitle(topic.getName());
			}
			list.add(score);
		}
		renderPage(list, "", page.getTotalRow());
	}

	/**
	 * 获得指标管理页面
	 */
	public void getListByIDPage() {
		String topicid = getPara("topicid");
		String userid = getPara("userid");
		if (topicid != null && !"".equals(topicid)) {
			Topic topic = Topic.dao.findById(topicid);
			if (topic != null) {
				boolean bol = DateUtils.isEffectiveDate(new Date(), DateUtils.convert2YMdhmsTime(topic.getBeginTime()), DateUtils.convert2YMdhmsTime(topic.getEndTime()));
				setAttr("topicTimeStat", bol);topic.setBeginTime(DateUtils.convert2YMdhms(DateUtils.convert2YMdhmsTime(topic.getBeginTime())));
				topic.setEndTime(DateUtils.convert2YMdhms(DateUtils.convert2YMdhmsTime(topic.getEndTime())));
				setAttr("topic", topic);
			}
		}
		String username = getSessionAttr("username");
		SysUser user = SysUser.dao.findbyUserName(username);
		setAttr("topicid", topicid);
		setAttr("name", user.getName());
		Map<String,String> map = getSjHtml(topicid,userid);
		setAttr("itemhtml", map.get("itemhtml"));
		setAttr("sjhtml", map.get("content"));
		setAttr("total", map.get("total"));
		
		String id = getPara("id");
		if (StrKit.notBlank(id)) {
			Score templpate = Score.dao.findById(id);
			if(templpate.getMarkTime()==null){
				templpate.setScore("");
			}
			setAttr("t", templpate);
		}
		render("/WEB-INF/admin/scorelook/edit.html");
	}
	
	/**
	 * 获得专家指标管理页面
	 */
	public void getZjListByIDPage() {
		String topicid = getPara("topicid");
		String userid = getPara("userid");
		if (topicid != null && !"".equals(topicid)) {
			Topic topic = Topic.dao.findById(topicid);
			if (topic != null) {
				boolean bol = DateUtils.isEffectiveDate(new Date(), DateUtils.convert2YMdhmsTime(topic.getBeginTime()), DateUtils.convert2YMdhmsTime(topic.getEndTime()));
				setAttr("topicTimeStat", bol);topic.setBeginTime(DateUtils.convert2YMdhms(DateUtils.convert2YMdhmsTime(topic.getBeginTime())));
				topic.setEndTime(DateUtils.convert2YMdhms(DateUtils.convert2YMdhmsTime(topic.getEndTime())));
				setAttr("topic", topic);
			}
		}
		String username = getSessionAttr("username");
		SysUser user = SysUser.dao.findbyUserName(username);
		setAttr("topicid", topicid);
		setAttr("name", user.getName());
		Map<String,String> map = getSjHtml(topicid,userid);
		setAttr("itemhtml", map.get("itemhtml"));
		setAttr("sjhtml", map.get("content"));
		setAttr("total", map.get("total"));
		
		String id = getPara("id");
		if (StrKit.notBlank(id)) {
			Score templpate = Score.dao.findById(id);
			if(templpate.getMarkTime()==null){
				templpate.setScore("");
			}
			setAttr("t", templpate);
		}
		render("/WEB-INF/admin/scorelook/zjedit.html");
	}

	public Map<String,String> getSjHtml(String topicid,String userid) {
		Map<String,String> map = new HashMap<>();
		StringBuffer buff = new StringBuffer();
		StringBuffer itemsb = new StringBuffer();
		Page<Question> page = Question.dao.getQuestionPage(1, 100, topicid,null,null);
		List<TopicType> tlist = TopicType.dao.getTopicByStatus("1");
		int index = 1;
		for (TopicType type : tlist) {
			StringBuffer sb = new StringBuffer();
			boolean bol = false;
			sb.append("<div class='panel panel-default space'>");
			sb.append("<div class='panel-heading'>");
			sb.append("<h3 class='panel-title'>"+ArabicToChineseUtil.foematInteger(index)+"."+type.getName()+"</h3>");
			sb.append("</div>");
			sb.append("<div class='panel-body'>");
			for (int i = 0; i < page.getList().size(); i++) {
				Question question = page.getList().get(i);
				if(type.getId().equals(question.getTypeid())){
					Page<Answer> page2 = Answer.dao.getAnswerPage(1, 100, question.getId(), userid);
					String content = "";
					String evaluate = "";
					String answerid = "";
					String rids = "";
					String fid = "";
					if(page2!=null && page2.getList().size()>0){
						Answer ob = page2.getList().get(0);
						content = ob.getDescrible();
						evaluate = ob.getEvaluate();
						answerid = ob.getId();
						rids = ob.getRId();
						rids = ob.getRId();
						fid = ob.getFId();
					}
					itemsb.append("<li><a href='#item_"+(i + 1)+"'>"+(i + 1)+"</a></li>");
					sb.append("<p><a name='item_"+(i + 1)+"' ></a>");
					sb.append(question.getDescrible());
					if(StrKit.notBlank(question.getFId())){
						sb.append("<p id='uploadAnswerFile"+(i + 1)+"'>");
						if (!StringUtils.isEmpty(fid)) {
							Resource resource = Resource.dao.findById(fid);
							if (resource != null) {
								String fileName = resource.getRname();
								String href = this.getRequest().getContextPath() + "/admin/resources/downFile?rid=" + fid;
								sb.append("<div id='file"+fid+"'><a class='answer-file' href='" + href + "'>" + fileName + "</a></div>");
							}
						}
						sb.append("</p>");
					}
					sb.append("<textarea  class='form-control' style='width:1550px;height:300px;' id='content"+(i + 1)+"' name='"+question.getId()+"' questionid='"+question.getId()+"'>"+content+"</textarea>");
					
					sb.append("<p>");
					sb.append("<p id='uploadFile"+(i + 1)+"'>");
					if (!StringUtils.isEmpty(rids)) {
						String rid[] = rids.split(",");
						int count = 0;
						for (int j = 0; j < rid.length; j++) {
							count++;
							Resource resource = Resource.dao.findById(rid[j]);
							if (resource != null) {
								String fileName = resource.getRname();
								String href = this.getRequest().getContextPath() + "/admin/resources/downFile?rid=" + rid[j];
								if (count > 1) {
									sb.append("<br><div><a href='" + href + "'>" + fileName + "</a></div>");
								} else {
									sb.append("<div><a href='" + href + "'>" + fileName + "</a></div>");
								}
							}
						}
					}
					sb.append("</p>");
					sb.append("<div style='width:100%;float: left;'>");
					sb.append("<lable style='float:left;'>评价：</lable><div class='radio-check'><input type='radio' name='"+answerid+"' id='"+answerid+"1' value='优秀' disabled "+("优秀".equals(evaluate)?"checked=true":"")+"/><label for='"+answerid+"1'>优秀</label></div>");
					sb.append("<div class='radio-check'><input type='radio' name='"+answerid+"' id='"+answerid+"2' value='合格' disabled "+("合格".equals(evaluate)?"checked=true":"")+"/><label for='"+answerid+"2'>合格</label></div>");
					sb.append("<div class='radio-check'><input type='radio' name='"+answerid+"' id='"+answerid+"3' value='不合格' disabled "+("不合格".equals(evaluate)?"checked=true":"")+"/><label for='"+answerid+"3'>不合格</label></div>");
					/*sb.append("评价：<label style='margin-left:10px;height: 25px;'><input name='"+answerid+"' type='radio' disabled value='优秀' "+("优秀".equals(evaluate)?"checked=true":"")+"/>优秀</label>");
					sb.append("<label style='margin-left:10px;height: 25px;'><input name='"+answerid+"' type='radio' disabled value='合格' "+("合格".equals(evaluate)?"checked=true":"")+"/>合格</label>");
					sb.append("<label style='margin-left:10px;height: 25px;'><input name='"+answerid+"' type='radio' disabled value='不合格' "+("不合格".equals(evaluate)?"checked=true":"")+"/>不合格</label>");*/
					sb.append("</div>");
					sb.append("</p>");
					bol = true;
				}
			}
			sb.append("</div>");
			sb.append("</div>");
			if(bol){
				buff.append(sb.toString());
				index++;
			}
		}
		map.put("itemhtml", itemsb.toString());
		map.put("content", buff.toString());
		map.put("total", page.getList().size()+"");
		return map;
	}

	/************************ 数据库管理---结束 *************************************************/

	/**************************************************************************/
	private String pageTitle = "评分查询";// 模块页面标题
	private String breadHomeMethod = "getListPage";// 面包屑首页方法

	public Map<String, String> getPageTitleBread() {
		Map<String, String> pageTitleBread = new HashMap<String, String>();
		pageTitleBread.put("pageTitle", pageTitle);
		pageTitleBread.put("breadHomeMethod", breadHomeMethod);
		return pageTitleBread;
	}
}