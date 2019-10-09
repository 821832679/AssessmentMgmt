/**
 * @author Lion
 * @date 2017年1月24日 下午12:02:35
 * @qq 439635374
 */
package com.pointlion.sys.mvc.admin.prolemanage;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.aop.Before;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;
import com.pointlion.sys.interceptor.MainPageTitleInterceptor;
import com.pointlion.sys.mvc.common.base.BaseController;
import com.pointlion.sys.mvc.common.model.SysRole;
import com.pointlion.sys.mvc.common.model.SysRoleAuth;
import com.pointlion.sys.mvc.common.model.SysUser;
import com.pointlion.sys.mvc.common.model.Tpsource;
import com.pointlion.sys.mvc.common.utils.UuidUtil;
import com.pointlion.sys.plugin.shiro.ShiroKit;

/***
 * 用户管理控制器
 * 
 * @author Administrator
 *
 */
@Before(MainPageTitleInterceptor.class)
public class prolemanageController extends BaseController {

	/***
	 * 获取首页
	 */
	public void getDraftListPage() {
		render("/WEB-INF/admin/prolemanage/list.html");
	}
	

	/***
     * 查询分页数据
	 * @throws UnsupportedEncodingException 
     */
    public void listData() throws UnsupportedEncodingException{
    	String curr = getPara("pageNumber");
    	Float tempCurr = Float.valueOf(curr);
		Integer currPage =tempCurr.intValue();
    	String pageSize = getPara("pageSize");
    	String Statustype = getPara("StatusValue");
		String Searcht1 = getPara("SearchValue");
		String Searcht=Searcht1;
		if(Searcht1!="")
		{	 Searcht=URLDecoder.decode(Searcht1, "UTF-8");}
    	Page<SysRole> page = SysRole.dao.getRolePage(currPage,Integer.valueOf(pageSize),Statustype,Searcht);
    	renderPage(page.getList(),"" ,page.getTotalRow());
    }

	   /***
  * 获取编辑页面
  */
 public void getEditPage(){
	 
	List<SysRole> roles  = SysRole.dao.getlist();
	setAttr("roleList", roles);
 	//添加和修改
 	String id = getPara("id");
 	if(StrKit.notBlank(id)){
 		SysRole role = SysRole.dao.getById(id);
 		setAttr("o", role);
 	}
 	render("/WEB-INF/admin/prolemanage/edit.html");
 }
 /***
  * 保存
  */
 public void save(){
 	SysRole role = getModel(SysRole.class);
 	if(StrKit.notBlank(role.getId())){
 		role.update();
 	}else{
 		role.setId(UuidUtil.getUUID());
 		role.save();
 	}
 	renderSuccess();
 }
 /***
  * 获取选择权限菜单的页面
  */
 public void getGiveAuthPage(){
 	setAttr("roleid", getPara("roleid"));
 	render("/WEB-INF/admin/prolemanage/giveAuth.html");
 }

 
 /***
  * 给角色赋权
  */
 public void changeRoleAuth(){
 	String data = getPara("data");
 	String roleid = getPara("roleid"); 
 	SysRole.dao.changeRoleAuth(roleid,data);
 	renderSuccess();
 }
 
 /***
  * 获取角色下所有的权限
  */
 public void getRoleAuthByRoleid(){
 	String roleid = getPara("roleid");
 	List<SysRoleAuth> list = SysRole.dao.getRoleAuthByRoleId(roleid);
 	renderJson(list);
 }
 /***
  * 获取所有的角色
  * 给用户赋值角色时候用
  */
 public void getAllRoleTreeNode(){
 	renderJson(SysRole.dao.getAllRoleTreeNode());
 }
 /***
  * 获取用户下所有角色
  */
 public void getAllRoleByUserid(){
 	renderJson(SysRole.dao.getAllRoleByUserid(getPara("userid")));
 }


	/***
	 * 删除
	 * 
	 * @throws Exception
	 */
 public void delete() throws Exception{
		SysRole.dao.deleteByIds(getPara("ids"));
   	renderSuccess();
   }
	/**************************************************************************/
	private String pageTitle = "角色管理";// 模块页面标题
	private String breadHomeMethod = "getDraftListPage";// 面包屑首页方法

	public Map<String, String> getPageTitleBread() {
		Map<String, String> pageTitleBread = new HashMap<String, String>();
		pageTitleBread.put("pageTitle", pageTitle);
		pageTitleBread.put("breadHomeMethod", breadHomeMethod);
		return pageTitleBread;
	}
}
