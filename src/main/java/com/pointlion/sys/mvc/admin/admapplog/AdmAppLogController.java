/**
 * 
 */
package com.pointlion.sys.mvc.admin.admapplog;

import java.util.HashMap;
import java.util.Map;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Page;
import com.pointlion.sys.interceptor.MainPageTitleInterceptor;
import com.pointlion.sys.mvc.common.base.BaseController;
import com.pointlion.sys.mvc.common.model.AdminApplog;

/**
 * @author Lion
 * @date 2017年2月16日 下午4:04:25
 * @qq 439635374
 */
@Before(MainPageTitleInterceptor.class)
public class AdmAppLogController extends BaseController{
	/***
	 * 获得列表页
	 */
	public void getListPage(){
    	render("/WEB-INF/admin/admapplog/list.html");
    }
    /***
     * 获取编辑页面
     */
    /*public void getEditPage(){
    	//添加和修改
    	String id = getPara("id");
    	if(StrKit.notBlank(id)){
    		SysRole role = SysRole.dao.getById(id);
    		setAttr("o", role);
    	}
    	render("/WEB-INF/admin/role/edit.html");
    }
    
     * 查询分页数据
     */
    public void listData(){
    	String curr = getPara("pageNumber");
    	String pageSize = getPara("pageSize");
    	Page<AdminApplog> page = AdminApplog.dao.getAppLogPage(Integer.valueOf(curr),Integer.valueOf(pageSize));
    	renderPage(page.getList(),"" ,page.getTotalRow());
    }
    
    /***
     * 删除
     * @throws Exception
     */
    public void delete() throws Exception{
    	AdminApplog.dao.deleteByIds(getPara("ids"));
    	
    	renderSuccess();
    }
    

    

    /**************************************************************************/
	private String pageTitle = "应用日志管理";//模块页面标题
	private String breadHomeMethod = "getListPage";//面包屑首页方法
	
	public Map<String,String> getPageTitleBread() {
		Map<String,String> pageTitleBread = new HashMap<String,String>();
		pageTitleBread.put("pageTitle", pageTitle);
		pageTitleBread.put("breadHomeMethod", breadHomeMethod);
		return pageTitleBread;
	}
}
