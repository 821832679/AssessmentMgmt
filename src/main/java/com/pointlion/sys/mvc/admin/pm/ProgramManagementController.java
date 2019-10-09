package com.pointlion.sys.mvc.admin.pm;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.jfinal.aop.Before;
import com.jfinal.json.Json;
import com.jfinal.kit.JsonKit;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;
import com.pointlion.sys.interceptor.MainPageTitleInterceptor;
import com.pointlion.sys.mvc.common.base.BaseController;
import com.pointlion.sys.mvc.common.model.CustomJson;
import com.pointlion.sys.mvc.common.model.SysRole;
import com.pointlion.sys.mvc.common.model.TCustomFieldTemplate;
import com.pointlion.sys.mvc.common.model.TDbbackup;
import com.pointlion.sys.mvc.common.utils.UuidUtil;
@Before(MainPageTitleInterceptor.class)
public class ProgramManagementController extends BaseController {

	/***************************栏目管理---开始***********************/
	
	/**
	 * 获得栏目管理页面
	 */
	public void getPmList(){
		
		render("/WEB-INF/admin/pm/list.html");
	}
	
	/**
	 * 获得栏目管理数据
	 */
	public void listData(){
		
		String curr = getPara("pageNumber");
		Float currFloat = Float.valueOf(curr);
		Integer currPage = currFloat.intValue();
    	String pageSize = getPara("pageSize");
    	Page<TCustomFieldTemplate> page = TCustomFieldTemplate.dao.getPmPage(currPage,Integer.valueOf(pageSize));
    	renderPage(page.getList(),"" ,page.getTotalRow());
		
	}
	
	/**
	 * 获得编辑页面
	 */
	public void getEditPage(){
		
		String id = getPara("id");
    	if(StrKit.notBlank(id)){
    		TCustomFieldTemplate templpate = TCustomFieldTemplate.dao.findById(id);
    		setAttr("t", templpate);
    	}
    	render("/WEB-INF/admin/pm/edit.html");
	}
	
	/**
	 * 保存
	 */
	public void save(){
		TCustomFieldTemplate template = getModel(TCustomFieldTemplate.class);
		//json字符串的处理
		CustomJson json = new CustomJson();
		json.setLabel(template.getName());
		json.setFieldName(template.getFieldName());
		json.setReadOnly("true");
		json.setType("textfield");
		json.setDefaultValue("");
		String context = JsonKit.toJson(json);
		template.setContext(context);
    	if(StrKit.notBlank(template.getId())){
    		template.update();
    	}else{
    		template.setId(UuidUtil.getUUID());
    		template.save();
    	}
    	renderSuccess();
	}
	
    /***
     * 删除
     * @throws Exception
     */
    public void delete() throws Exception{
    	TCustomFieldTemplate.dao.deleteByIds(getPara("ids"));
    	renderSuccess();
    }
	
	/************************数据库管理---结束*************************************************/
    
    /**************************************************************************/
    private String pageTitle = "栏目添加";//模块页面标题
	private String breadHomeMethod = "getPmList";//面包屑首页方法
	public Map<String,String> getPageTitleBread() {
		Map<String,String> pageTitleBread = new HashMap<String,String>();
		pageTitleBread.put("pageTitle", pageTitle);
		pageTitleBread.put("breadHomeMethod", breadHomeMethod);
		return pageTitleBread;
	}
}
