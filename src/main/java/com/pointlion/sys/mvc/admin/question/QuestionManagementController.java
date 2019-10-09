package com.pointlion.sys.mvc.admin.question;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.druid.util.StringUtils;
import com.jfinal.aop.Before;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;
import com.pointlion.sys.interceptor.MainPageTitleInterceptor;
import com.pointlion.sys.mvc.common.base.BaseController;
import com.pointlion.sys.mvc.common.model.Answer;
import com.pointlion.sys.mvc.common.model.FileConfig;
import com.pointlion.sys.mvc.common.model.Question;
import com.pointlion.sys.mvc.common.model.Resource;
import com.pointlion.sys.mvc.common.model.SysUser;
import com.pointlion.sys.mvc.common.utils.UuidUtil;

@Before(MainPageTitleInterceptor.class)
public class QuestionManagementController extends BaseController {

	/*************************** 考核指标管理---开始 ***********************/

	/**
	 * 获得考核指标管理页面
	 */
	public void getListPage() {
		String topicid = getPara("topicid");
		setAttr("topicid", topicid);
		if (topicid != null && !"".equals(topicid)) {
			render("/WEB-INF/admin/question/list.html");
		} else {
			render("/WEB-INF/admin/question/toplist.html");
		}
	}

	/**
	 * 获得考核指标管理数据
	 * @throws UnsupportedEncodingException 
	 */
	public void listData() throws UnsupportedEncodingException {
		String topicid = getPara("topicid");
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
		Page<Question> page = Question.dao.getQuestionPage(currPage, Integer.valueOf(pageSize), topicid,Statustype,Searcht);
		renderPage(page.getList(), "", page.getTotalRow());
	}

	/**
	 * 获得编辑页面
	 */
	public void getEditPage() {
		String id = getPara("id");
		String topicid = getPara("topicid");
		if (StrKit.notBlank(id)) {
			Question templpate = Question.dao.findById(id);
			setAttr("t", templpate);
			if(StrKit.notBlank(templpate.getFId())){
				Resource resource = Resource.dao.findById(templpate.getFId());
				setAttr("resource", resource);
			}
			if(templpate.getIsstatis() == 1){
				List<FileConfig> fcs = FileConfig.dao.selectByQuestionid(templpate.getId());
				setAttr("f", fcs.get(0));
			}
		}
		setAttr("topicid", topicid);
		render("/WEB-INF/admin/question/edit.html");
	}

	/**
	 * 保存
	 */
	public void save() {
		Question question = getModel(Question.class);
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		HttpServletRequest req = getRequest();
		String username = getSessionAttr("username");
		String topicid = req.getParameter("topicid");
		SysUser user = SysUser.dao.findbyUserName(username);
		
		if(StrKit.isBlank(question.getFId())){
			renderError("请上传任务模板");
			return;
		}

		boolean result = false;
		if (StrKit.notBlank(question.getId())) {
			//判断附件是否更新
			boolean bol = false;
			Question q = Question.dao.findById(question.getId());
			if(q!=null && !question.getFId().equals(q.getFId())){
				bol = true;
			}
			result = question.update();
			if(result && bol){
				//删除原附件
				this.delFile(q.getFId());
			}
		} else {
			question.setTopicId(topicid);
			question.setId(UuidUtil.getUUID());
			question.setUserId(user.getId());
			question.setCreateTime(sf.format(new Date()));
			result = question.save();
		}
		
		if(result){
			//判断是否需要统计数据
			if(question.getIsstatis() == 1){
				FileConfig fc = getModel(FileConfig.class);
				fc.setTopicId(topicid);
				fc.setQuestionId(question.getId());
				if (StrKit.notBlank(fc.getId())) {
					fc.update();
				}else{
					fc.setId(UuidUtil.getUUID());
					fc.save();
				}
			}else{
				FileConfig.dao.deleteByQuestionid(question.getId());
			}
		}
		renderSuccess();
	}

	/***
	 * 删除
	 * 
	 * @throws Exception
	 */
	public void delete() throws Exception {
		Question.dao.deleteByIds(getPara("ids"));
		renderSuccess();
	}
	
	/***
	 * 删除服务器文件
	 * 
	 * @throws Exception
	 */
	public void delFile(String fidParam){
		if (StringUtils.isEmpty(fidParam)) {
			return;
		}
		Resource ob = Resource.dao.findById(fidParam);
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
		Resource.dao.deleteById(fidParam);// 删除附件上传记录
		renderSuccess(buff.toString());
	}
	
	/***
	 * 启用/禁用
	 * 
	 * @throws Exception
	 */
	public void status() throws Exception {
		String status = getPara("status");
		Question.dao.statusByIds(getPara("ids"), status);
		renderSuccess();
	}

	/************************ 数据库管理---结束 *************************************************/

	/**************************************************************************/
	private String pageTitle = "考核指标添加";// 模块页面标题
	private String breadHomeMethod = "getListPage";// 面包屑首页方法

	public Map<String, String> getPageTitleBread() {
		String topicid = getPara("topicid");
		if(topicid==null || "".equals(topicid)){
			pageTitle = "任务指标列表";// 模块页面标题
		}
		Map<String, String> pageTitleBread = new HashMap<String, String>();
		pageTitleBread.put("pageTitle", pageTitle);
		pageTitleBread.put("breadHomeMethod", breadHomeMethod);
		return pageTitleBread;
	}
}
