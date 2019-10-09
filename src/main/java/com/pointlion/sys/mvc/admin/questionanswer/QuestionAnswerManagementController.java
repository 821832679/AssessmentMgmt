package com.pointlion.sys.mvc.admin.questionanswer;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.aop.Before;
import com.jfinal.kit.PropKit;
import com.jfinal.kit.Ret;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.upload.UploadFile;
import com.pointlion.sys.interceptor.MainPageTitleInterceptor;
import com.pointlion.sys.mvc.common.base.BaseController;
import com.pointlion.sys.mvc.common.model.Question;
import com.pointlion.sys.mvc.common.model.SysUser;
import com.pointlion.sys.mvc.common.model.Topic;
import com.pointlion.sys.mvc.common.model.TopicDelay;
import com.pointlion.sys.mvc.common.model.TopicType;
import com.pointlion.sys.mvc.common.utils.ArabicToChineseUtil;
import com.pointlion.sys.mvc.common.utils.DateUtils;
import com.pointlion.sys.mvc.common.utils.ReadExcel;

@Before(MainPageTitleInterceptor.class)
public class QuestionAnswerManagementController extends BaseController {
	/*************************** 指标管理---开始 ***********************/

	/**
	 * 获得指标管理页面
	 */
	public void getListPage() {
		String topicid = getPara("topicid");
		if (topicid != null && !"".equals(topicid)) {
			Topic topic = Topic.dao.findById(topicid);
			boolean bol = DateUtils.isEffectiveDate(new Date(), DateUtils.convert2YMdhmsTime(topic.getBeginTime()), DateUtils.convert2YMdhmsTime(topic.getEndTime()));
			topic.setBeginTime(DateUtils.convert2YMdhms(DateUtils.convert2YMdhmsTime(topic.getBeginTime())));
			topic.setEndTime(DateUtils.convert2YMdhms(DateUtils.convert2YMdhmsTime(topic.getEndTime())));
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
		setAttr("sjhtml", map.get("content"));
		setAttr("itemhtml", map.get("itemhtml"));
		setAttr("total", map.get("total"));
		setAttr("imgDomain", PropKit.get("image.domain"));
		render("/WEB-INF/admin/questionanswer/list.html");
	}

	/************************ 数据库管理---结束 *************************************************/

	/**************************************************************************/
	private String pageTitle = "考核指标列表";// 模块页面标题
	private String breadHomeMethod = "getListPage";// 面包屑首页方法

	public Map<String, String> getPageTitleBread() {
		Map<String, String> pageTitleBread = new HashMap<String, String>();
		pageTitleBread.put("pageTitle", pageTitle);
		pageTitleBread.put("breadHomeMethod", breadHomeMethod);
		return pageTitleBread;
	}

	public Map<String,String> getSjHtml(String topicid) {
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
					itemsb.append("<li><a href='#item_"+(i + 1)+"'>"+(i + 1)+"</a></li>");
					sb.append("<p><a name='item_"+(i + 1)+"' ></a>");
					sb.append(question.getDescrible());
					if(StrKit.notBlank(question.getFId())){
						sb.append("<p>");
						sb.append("<p id='uploadAnswerFile"+(i + 1)+"'></p>");
						sb.append("<input type='hidden' id='fileFid"+question.getId()+"' name='fileFid"+question.getId()+"' value=''>");
						sb.append("<input type='file' accept='.xls' class='btn btn-info data-toolbar' id='fileAnswerText"+(i + 1)+"' name='fileAnswerText"+(i + 1)+"' placeholder='请选择文件' style='display: inline;'/>");
						sb.append("<a class='btn btn-info data-toolbar' href='javascript:void(0)' onclick=uploadAnswerFile('"+question.getId()+"','"+(i + 1)+"','"+id+"')>上传任务"+(i+1)+"</a>");
						sb.append("<a class='btn btn-info data-toolbar' href='"+this.getRequest().getContextPath()+"/admin/resources/downFile?rid="+question.getFId()+"'>下载任务"+(i+1)+"模板</a>");
						sb.append("</p>");
					}
					sb.append("<textarea  class='form-control' style='width:1550px;height:300px;' id='content"+(i + 1)+"' name='"+question.getId()+"' questionid='"+question.getId()+"'></textarea>");
					sb.append("<small class='help-block' data-bv-validator='notEmpty' data-bv-for='content"+(i + 1)+"' data-bv-result='NOT_VALIDATED' style='display: none;color: red;' id='valid-"+(i + 1)+"'>*考核"+(i + 1)+"指标不能为空</small>");
					sb.append("</p>");
					sb.append("<p>");
					sb.append("<p id='uploadFile"+(i + 1)+"'></p>");
					sb.append("<input type='hidden' id='fileRid"+question.getId()+"' name='fileRid"+question.getId()+"' value=''>");
					sb.append("<input type='file' class='btn btn-info data-toolbar' id='fileText"+(i + 1)+"' name='fileText"+(i + 1)+"' placeholder='请选择文件' style='display: inline;'/>");
					sb.append("<a class='btn btn-info data-toolbar' href='javascript:void(0)' onclick=uploadFile('"+question.getId()+"','"+(i + 1)+"','"+id+"')>上传</a>");
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
	
    /**
     * 上传文章的图片
     * (百度富文本编辑器统一调用控制器 )
     * @Clear 清除当前方法以及上一级路径的拦截，确保前端页面在没有登录 的情况下也能进行请求
     */
	public void uploadImage() {
		if ("config".equals(getPara("action"))) {
			render("/ueditor/jsp/config.json");
			return;
		}
		UploadFile file = getFile("upfile"); // 获取文件
		String fileName = file.getFileName();
		String suffix = fileName.substring(fileName.lastIndexOf("."));

		String newFileName = System.currentTimeMillis() + suffix;
		file.getFile().renameTo((new File(PropKit.get("image.root.path") + newFileName)));
		String[] typeArr = fileName.split("\\.");
		String orig = file.getOriginalFileName();
		long size = file.getFile().length();
		// 获取相对地址
		String urls = PropKit.get("image.domain") + "/" + newFileName;
		Ret ret = Ret.create("state", "SUCCESS") // 下面这几个都是必须返回给ueditor的数据
				.set("url", urls) // 文件上传后的路径
				.set("title", newFileName) // 文件名称
				.set("original", orig).set("type", "." + typeArr[1]).set("size", size);
		renderJson(ret);
	}
}