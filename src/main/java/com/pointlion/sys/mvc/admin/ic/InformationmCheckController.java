package com.pointlion.sys.mvc.admin.ic;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Before;
import com.jfinal.kit.JsonKit;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;
import com.pointlion.sys.interceptor.MainPageTitleInterceptor;
import com.pointlion.sys.mvc.common.base.BaseController;
import com.pointlion.sys.mvc.common.model.CustomJson;
import com.pointlion.sys.mvc.common.model.CustomValue;
import com.pointlion.sys.mvc.common.model.SysOrg;
import com.pointlion.sys.mvc.common.model.SysUser;
import com.pointlion.sys.mvc.common.model.TCustomFieldTemplate;
import com.pointlion.sys.mvc.common.model.TPublicService;
import com.pointlion.sys.mvc.common.utils.DateUtil;
import com.pointlion.sys.mvc.common.utils.UuidUtil;
@Before(MainPageTitleInterceptor.class)
public class InformationmCheckController extends BaseController {

	/***************************信息管理---开始***********************/
	
	/**
	 * 获得信息审核数据
	 */
	public void listAuditData(){
		
		String username = getSessionAttr("username");
		SysUser currUser = SysUser.dao.findbyUserName(username);
		String orgId  = currUser.getOrgid();
		SysOrg org = SysOrg.dao.findById(orgId);
		String code = "";  //机构代码
		Integer leavl = 0;  //机构等级
		if(org!=null){
			leavl = Integer.valueOf(org.getType())+1;
			//市级
			if(org.getType()!=null){
				if(org.getType().equals("1")){
					code = org.getAreaCode().toString().substring(0, 4);
				}
				//区级
				if(org.getType().equals("2")){
					code = org.getAreaCode().toString().substring(0, 6);
				}
				//镇，乡，街道级
				if(org.getType().equals("3")){
					code = org.getAreaCode().toString().substring(0, 9);
				}
				//村级
				if(org.getType().equals("4")){
					code = org.getAreaCode().toString();
				}
			}
		}
		String state = getPara("state");
		String createTime = getPara("createTime");
		String keyWord = getPara("keyWord");
		try {
			keyWord = URLDecoder.decode(keyWord, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}  
		System.out.println(state+" "+createTime+" "+keyWord);
		String curr = getPara("pageNumber");
		Float currFloat = Float.valueOf(curr);
		Integer currPage = currFloat.intValue();
    	String pageSize = getPara("pageSize");
    	Page<TPublicService> page = TPublicService.dao.getImPage(currPage,Integer.valueOf(pageSize),state,createTime,keyWord,code,leavl);
    	renderPage(page.getList(),"" ,page.getTotalRow());
		
	}
	
	
	/**
	 * 获得信息上传数据
	 */
	public void listData(){
		
		String username = getSessionAttr("username");
		SysUser currUser = SysUser.dao.findbyUserName(username);
		String orgId  = currUser.getOrgid();
		SysOrg org = SysOrg.dao.findById(orgId);
		
		String code = org.getAreaCode().toString();
		
		String state = getPara("state");
		String createTime = getPara("createTime");
		String keyWord = getPara("keyWord");
		try {
			keyWord = URLDecoder.decode(keyWord, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}  
		System.out.println(state+" "+createTime+" "+keyWord);
		String curr = getPara("pageNumber");
		Float currFloat = Float.valueOf(curr);
		Integer currPage = currFloat.intValue();
    	String pageSize = getPara("pageSize");
    	Page<TPublicService> page = TPublicService.dao.getImPage(currPage,Integer.valueOf(pageSize),state,createTime,keyWord,code);
    	renderPage(page.getList(),"" ,page.getTotalRow());
		
	}
	
	
	
	/**
	 * 获得编辑页面
	 */
	public void getEditPage(){
		String id = getPara("id");
		List<CustomJson> formList = TCustomFieldTemplate.dao.findAllContext();
    	if(StrKit.notBlank(id)){
    		List<CustomValue> values = new ArrayList<CustomValue>();
    		List<CustomJson> formList1 = TCustomFieldTemplate.dao.findAllContext();
    		TPublicService service = TPublicService.dao.findById(id);
    		//Map<String, Object> valueMap = new HashMap<String,Object>();
    		String jsons = service.getTemplateData();
    		JSONObject jsonObj = JSONObject.parseObject(jsons);
    		for(int i=0;i<formList1.size();i++){
    			CustomValue value = new CustomValue();
    			value.setName(formList1.get(i).getFieldName());
    			//如果json数据不为空
    			if(jsonObj.getString(value.getName())!=null){
    			
    				value.setValue(jsonObj.getString(value.getName()));
    			}
    			else{
    				value.setValue("");
    			}
    			values.add(value);
    		}
    		//valueMap = JSONObject.parseObject(jsons);
    		setAttr("values", values);
    		setAttr("s", service);
    	}
    	setAttr("formList",formList);
    	render("/WEB-INF/admin/im/edit.html");
	}
	
	public void getInfoPage(){
		String id = getPara("id");
		List<CustomJson> formList = TCustomFieldTemplate.dao.findAllContext();
    	if(StrKit.notBlank(id)){
    		List<CustomValue> values = new ArrayList<CustomValue>();
    		List<CustomJson> formList1 = TCustomFieldTemplate.dao.findAllContext();
    		TPublicService service = TPublicService.dao.findById(id);
    		//Map<String, Object> valueMap = new HashMap<String,Object>();
    		String jsons = service.getTemplateData();
    		JSONObject jsonObj = JSONObject.parseObject(jsons);
    		for(int i=0;i<formList1.size();i++){
    			CustomValue value = new CustomValue();
    			value.setName(formList1.get(i).getFieldName());
    			//如果json数据不为空
    			if(jsonObj.getString(value.getName())!=null){
    			
    				value.setValue(jsonObj.getString(value.getName()));
    			}
    			else{
    				value.setValue("");
    			}
    			values.add(value);
    		}
    		//valueMap = JSONObject.parseObject(jsons);
    		setAttr("values", values);
    		setAttr("s", service);
    	}
    	setAttr("formList",formList);
    	render("/WEB-INF/admin/im/info.html");
	}
	
	/**
	 * 获得表单采集项
	 */
	public void getFormList(){
		List<CustomJson> formList = TCustomFieldTemplate.dao.findAllContext();
    	renderJson(formList);
	}
	
	/**
	 * 获得信息审核列表页面
	 */
	public void getImAuditList(){
		render("/WEB-INF/admin/im/audit.html");
	}
	
	/**
	 * 获得审核查看页面
	 */
	public void getAuditPage(){
		String id = getPara("id");
		List<CustomJson> formList = TCustomFieldTemplate.dao.findAllContext();
    	if(StrKit.notBlank(id)){
    		List<CustomValue> values = new ArrayList<CustomValue>();
    		List<CustomJson> formList1 = TCustomFieldTemplate.dao.findAllContext();
    		TPublicService service = TPublicService.dao.findById(id);
    		//Map<String, Object> valueMap = new HashMap<String,Object>();
    		String jsons = service.getTemplateData();
    		JSONObject jsonObj = JSONObject.parseObject(jsons);
    		for(int i=0;i<formList1.size();i++){
    			CustomValue value = new CustomValue();
    			value.setName(formList1.get(i).getFieldName());
    			//如果json数据不为空
    			if(jsonObj.getString(value.getName())!=null){
    			
    				value.setValue(jsonObj.getString(value.getName()));
    			}
    			else{
    				value.setValue("");
    			}
    			values.add(value);
    		}
    		//valueMap = JSONObject.parseObject(jsons);
    		setAttr("values", values);
    		setAttr("s", service);
    	}
    	setAttr("formList",formList);
    	render("/WEB-INF/admin/im/aduitEdit.html");
	}
	
	/**
	 * 审核判断
	 */
	public void audit(){
		String tid = getPara("tid");
		String pass = getPara("pass");
		String rejected = getPara("rejected");
		
		TPublicService tpService = TPublicService.dao.findById(tid);
		
		if(tpService!=null){
			if(pass!=null){
				tpService.setIsPass(pass);
				tpService.update();
			}
			if(rejected!=null){
				tpService.setIsPass(rejected);
				tpService.update();
			}
		}
		renderSuccess();
	}
	
	
	/**
	 * 保存
	 */
	public void save(){
		
		String username = getSessionAttr("username");
		SysUser currUser = SysUser.dao.findbyUserName(username);
		String orgId  = currUser.getOrgid();
		SysOrg org = SysOrg.dao.findById(orgId);
		
		TPublicService publicService = getModel(TPublicService.class);
		List<CustomJson> formList = TCustomFieldTemplate.dao.findAllContext();
		Map<String,String> templateDatas = new HashMap<String,String>();
		for (CustomJson customJson : formList) {
			templateDatas.put(customJson.getFieldName(), getPara(customJson.getFieldName()));
		}
		String templateData = JsonKit.toJson(templateDatas);
		publicService.setTemplateData(templateData);
		
    	if(StrKit.notBlank(publicService.getId())){
    		publicService.update();
    	}else{
    		publicService.setId(UuidUtil.getUUID());
    		publicService.setCreateTimet(DateUtil.getDay());
    		publicService.setIsPass("noPass");
    		publicService.setAreaCode(org.getAreaCode());
    		publicService.setLeavl(org.getType());
    		publicService.save();
    	}
    	renderSuccess();
	}
	
    /***
     * 删除
     * @throws Exception
     */
    public void delete() throws Exception{
    	TPublicService.dao.deleteByIds(getPara("ids"));
    	renderSuccess();
    }
	
	/************************信息管理---结束*************************************************/
    
    /**************************************************************************/
    private String pageTitle = "信息审核";//模块页面标题
	private String breadHomeMethod = "getImAuditList";//面包屑首页方法
	public Map<String,String> getPageTitleBread() {
		Map<String,String> pageTitleBread = new HashMap<String,String>();
		pageTitleBread.put("pageTitle", pageTitle);
		pageTitleBread.put("breadHomeMethod", breadHomeMethod);
		return pageTitleBread;
	}
}
