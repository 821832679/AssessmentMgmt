package com.pointlion.sys.mvc.admin.answernoedit;

import java.io.File;
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
import com.pointlion.sys.mvc.common.model.SysUser;
import com.pointlion.sys.mvc.common.model.Topic;
import com.pointlion.sys.mvc.common.model.TopicDelay;
import com.pointlion.sys.mvc.common.model.TopicType;
import com.pointlion.sys.mvc.common.utils.ArabicToChineseUtil;
import com.pointlion.sys.mvc.common.utils.DateUtils;
import com.pointlion.sys.mvc.common.utils.ImageResizeUtil;

@Before(MainPageTitleInterceptor.class)
public class AnswerNoEditManagementController extends BaseController {

	/*************************** 指标管理---开始 ***********************/
	
	/**
	 * 获得指标管理页面-不可编辑
	 */
	public void getNoEditListPage() {
		String topicid = getPara("topicid");
		if (topicid != null && !"".equals(topicid)) {
			Topic topic = Topic.dao.findById(topicid);
			Date begintime = DateUtils.convert2YMdhmsTime(topic.getBeginTime());
			Date endtime = DateUtils.convert2YMdhmsTime(topic.getEndTime());
			topic.setBeginTime(DateUtils.convert2YMdhms(begintime));
			topic.setEndTime(DateUtils.convert2YMdhms(endtime));
			boolean bol = DateUtils.isEffectiveDate(new Date(), begintime, endtime);
			setAttr("topicTimeStat", bol);
			setAttr("topic", topic);
		}
		String username = getSessionAttr("username");
		SysUser user = SysUser.dao.findbyUserName(username);
		//查询是否延期
		TopicDelay delay = TopicDelay.dao.find(topicid, user.getId());
		if(delay!=null && delay.getStat()==1){
			setAttr("topicTimeStat", true);
			setAttr("yqendtime", DateUtils.convert2YMdhms(DateUtils.convert2YMdhmsTime(delay.getYqendTime())));
		}
		setAttr("topicid", topicid);
		setAttr("name", user.getName());
		Map<String,String> map = getSjHtml(topicid);
		setAttr("itemhtml", map.get("itemhtml"));
		setAttr("sjhtml", map.get("content"));
		setAttr("total", map.get("total"));
		render("/WEB-INF/admin/questionanswer/noeditlist.html");
	}

	/************************ 数据库管理---结束 *************************************************/

	/**************************************************************************/
	private String pageTitle = "考核指标列表";// 模块页面标题
	private String breadHomeMethod = "getNoEditListPage";// 面包屑首页方法

	public Map<String, String> getPageTitleBread() {
		Map<String, String> pageTitleBread = new HashMap<String, String>();
		pageTitleBread.put("pageTitle", pageTitle);
		pageTitleBread.put("breadHomeMethod", breadHomeMethod);
		return pageTitleBread;
	}

	public Map<String,String> getSjHtml(String topicid) {
		String username = getSessionAttr("username");
		SysUser user = SysUser.dao.findbyUserName(username);
		Map<String,String> map = new HashMap<>();
		StringBuffer buff = new StringBuffer();
		StringBuffer itemsb = new StringBuffer();
		Page<Question> page = Question.dao.getQuestionBySstatusPage(1, 100, topicid);
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
					Page<Answer> page2 = Answer.dao.getAnswerPage(1, 100, question.getId(), user.getId());
					String content = "";
					String rids = "";
					String fid = "";
					String id = "";
					if(page2!=null && page2.getList().size()>0){
						Answer ob = page2.getList().get(0);
						id = ob.getId();
						content = ob.getDescrible();
						rids = ob.getRId();
						fid = ob.getFId();
					}
					itemsb.append("<li><a href='#item_"+(i + 1)+"'>"+(i + 1)+"</a></li>");
					sb.append("<p><a name='item_"+(i + 1)+"' ></a>");
					sb.append(question.getDescrible());
					if(StrKit.notBlank(question.getFId())){
						sb.append("<p>");
						sb.append("<p id='uploadAnswerFile"+(i + 1)+"'>");
						if (!StringUtils.isEmpty(fid)) {
							Resource resource = Resource.dao.findById(fid);
							if (resource != null) {
								String fileName = resource.getRname();
								String href = this.getRequest().getContextPath() + "/admin/resources/downFile?rid=" + fid;
								sb.append("<div id='answerfile"+fid+"'><a class='answer-file' href='" + href + "'>" + fileName + "</a><span><a class='label label-default' href='javascript:void(0)' onclick=delAnswerFile('"+question.getId()+"','"+id+"','"+fid+"') style='margin-left: 20px;padding: .2em .6em 0.2em;'>删除</a></span></div>");
							}
						}
						sb.append("</p>");
						
						if (StringUtils.isEmpty(fid)) {
							sb.append("<input type='hidden' id='fileFid"+question.getId()+"' name='fileFid"+question.getId()+"' value=''>");
						} else {
							sb.append("<input type='hidden' id='fileFid" + question.getId() + "' name='fileFid"+ question.getId()+"' value='" + fid + "'>");
						}
						sb.append("<input type='file' accept='.xls' class='btn btn-info data-toolbar' id='fileAnswerText"+(i + 1)+"' name='fileAnswerText"+(i + 1)+"' placeholder='请选择文件' style='display: inline;'/>");
						sb.append("<a class='btn btn-info data-toolbar' href='javascript:void(0)' onclick=uploadAnswerFile('"+question.getId()+"','"+(i + 1)+"','"+id+"')>上传任务"+(i+1)+"</a>");
						sb.append("<a class='btn btn-info data-toolbar' href='"+this.getRequest().getContextPath()+"/admin/resources/downFile?rid="+question.getFId()+"'>下载任务"+(i+1)+"模板</a>");
						sb.append("</p>");
					}
					sb.append("<textarea  class='form-control' style='width:1550px;height:300px;' id='content"+(i + 1)+"' name='"+question.getId()+"' questionid='"+question.getId()+"'>"+content+"</textarea>");
					sb.append("<small class='help-block' data-bv-validator='notEmpty' data-bv-for='content"+(i + 1)+"' data-bv-result='NOT_VALIDATED' style='display: none;color: red;' id='valid-"+(i + 1)+"'>*考核"+(i + 1)+"指标不能为空</small>");
					sb.append("</p>");
					sb.append("<div>");
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
								String ahref = "<a href='" + href + "'>";
								if(ImageResizeUtil.isImgSuffix(resource.getSuffix())){
									ahref = "<a href='" + href + "' target='_blank'>";
								}
								if (count > 1) {
									//sb.append("<br><div id='file"+rid[j]+"'><a href='" + href + "'>" + fileName + "</a><span><a class='label label-default' href='javascript:void(0)' onclick=delFile('"+question.getId()+"','"+id+"','"+rid[j]+"') style='margin-left: 20px;padding: .2em .6em 0.2em;'>删除</a></span></div>");
									sb.append("<br><div id='file"+rid[j]+"'>"+ ahref + fileName + "</a></div>");
								} else {
									//sb.append("<div id='file"+rid[j]+"'><a href='" + href + "'>" + fileName + "</a><span><a class='label label-default' href='javascript:void(0)' onclick=delFile('"+question.getId()+"','"+id+"','"+rid[j]+"') style='margin-left: 20px;padding: .2em .6em 0.2em;'>删除</a></span></div>");
									sb.append("<div id='file"+rid[j]+"'>"+ ahref + fileName + "</a></div>");
								}
							}
						}
					}
					sb.append("</p>");
					if (StringUtils.isEmpty(rids)) {
						sb.append("<input type='hidden' id='fileRid" + question.getId() + "' name='fileRid"+ question.getId() + "' value=''>");
					} else {
						sb.append("<input type='hidden' id='fileRid" + question.getId() + "' name='fileRid"+ question.getId() + "' value='" + rids + "'>");
					}
					sb.append("<input type='file' class='btn btn-info data-toolbar' id='fileText"+(i + 1)+"' name='fileText"+(i + 1)+"' placeholder='请选择文件' style='display: inline;'/>");
					sb.append("<a class='btn btn-info data-toolbar' href='javascript:void(0)' onclick=uploadFile('"+question.getId()+"','"+(i + 1)+"','"+id+"') >上传</a>");
					sb.append("</div>");
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
	
	/***
	 * 删除服务器文件
	 * 
	 * @throws Exception
	 */
	public void delFile() throws Exception {
		String idParam = getPara("id");
		String ridParam = getPara("rid");
		if (StringUtils.isEmpty(idParam) || StringUtils.isEmpty(ridParam)) {
			return;
		}
		Resource ob = Resource.dao.findById(ridParam);
		if (ob != null) {
			String fileName = ob.getRname();
			String filePath = ob.getUploadPath();
			String path = filePath + "/" + fileName;
			File file = new File(path);
			if (file.exists()) {
				file.delete();// 删除附件
			}
		}
		StringBuffer buff = new StringBuffer();
		try {
			Answer ab = Answer.dao.findById(idParam);
			if (ab != null) {
				String rids = ab.getRId();
				if (!StringUtils.isEmpty(rids)) {
					if (!StringUtils.isEmpty(rids)) {
						String rid[] = rids.split(",");
						for (int i = 0; i < rid.length; i++) {
							if (rid[i].equals(ridParam)) {
								continue;
							} else {
								if (buff.length() > 0) {
									buff.append(",").append(rid[i]);
								} else {
									buff.append(rid[i]);
								}
							}
						}
					}
					if (buff.length() > 0) {
						ab.setRId(buff.toString());
					} else {
						ab.setRId(null);
					}
					ab.update();// 更新答题附件
				}
			}
			Resource.dao.deleteById(ridParam);// 删除附件上传记录
		} catch (Exception e) {
			e.printStackTrace();
			renderError();
		}
		renderSuccess(buff.toString());
	}
}