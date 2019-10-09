/**
 * @author Lion
 * @date 2017年1月24日 下午12:02:35
 * @qq 439635374
 */
package com.pointlion.sys.mvc.admin.usermanage;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.shiro.authc.credential.PasswordService;

import com.jfinal.aop.Before;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;
import com.pointlion.sys.interceptor.MainPageTitleInterceptor;
import com.pointlion.sys.mvc.common.base.BaseController;
import com.pointlion.sys.mvc.common.model.SysOrg;
import com.pointlion.sys.mvc.common.model.SysRole;
import com.pointlion.sys.mvc.common.model.SysUser;
import com.pointlion.sys.mvc.common.model.SysUserRole;
import com.pointlion.sys.mvc.common.utils.UuidUtil;

/***
 * 用户管理控制器
 * 
 * @author Administrator
 *
 */
@Before(MainPageTitleInterceptor.class)
public class usermanageController extends BaseController {

	/***
	 * 获取管理首页
	 */
	public void getDraftListPage() {
		List<SysRole> srList= SysRole.dao.getlist();
		setAttr("srList", srList);
		render("/WEB-INF/admin/usermanage/list.html");
	}
	

	/***
     * 获取分页数据
	 * @throws UnsupportedEncodingException 
     **/
    public void listData() throws UnsupportedEncodingException{
    	String curr = getPara("pageNumber");
    	Float tempCurr = Float.valueOf(curr);
		Integer currPage =tempCurr.intValue();
    	String pageSize = getPara("pageSize");
    	String Roletype1 = getPara("RoleValue");
		String Searcht1 = getPara("SearchValue");
		String orgName = getPara("orgName");
		String Roletype=Roletype1;
		String Searcht=Searcht1;
		if(Roletype1!="")
		{	 Roletype=URLDecoder.decode(Roletype1, "UTF-8");}
		if(Searcht1!="")
		{	 Searcht=URLDecoder.decode(Searcht1, "UTF-8");}
		if(orgName!="")
		{	 orgName=URLDecoder.decode(orgName, "UTF-8");}
		
		SysOrg org = SysOrg.dao.findByOrgName(orgName);
		String code ="";  //机构代码
		
		if(org!=null){
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
		//System.out.println(code);
		
    	Page<SysUser> page = SysUser.dao.findListPage(currPage,Integer.valueOf(pageSize),Roletype,Searcht,code);
    	List<SysUser> list = new ArrayList<>();
		if (page != null) {
			for (int i = 0; i < page.getList().size(); i++) {
				SysUser user = page.getList().get(i);
				user.setPassword("");
				list.add(user);
			}
		}
		renderPage(list, "", page.getTotalRow());
    }

/***
 * 保存
 */
    public void save(){
    	SysUser o = getModel(SysUser.class);
    	if(StrKit.notBlank(o.getId())){
    		String password = o.getPassword();
    		if(StrKit.isBlank(password)){//如果没传递密码
    			SysUser u = SysUser.dao.findById(o.getId());
    			password = u.getPassword();//获取原始密码
    			o.setPassword(password);//设置为原始密码
    		}else{//传递了密码 , 设置新密码
    			PasswordService svc = new DefaultPasswordService();
    			o.setPassword(svc.encryptPassword(password));//加密新密码
    		}
    		SysUserRole sysUserRole = SysUserRole.dao.findByUserId(o.getId());
    		//String roleId = SysRole.dao.findByrRoleId(o.getRoleid());
    		String roleId = o.getRoleid();
    		sysUserRole.setRoleId(roleId);
    		sysUserRole.update();
    		o.update();
    	}else{
    		o.setId(UuidUtil.getUUID());
    		PasswordService svc = new DefaultPasswordService();
    		o.setPassword(svc.encryptPassword(o.getPassword()));//加密新密码
    		o.save();
    		SysUserRole sysUserRole = new SysUserRole();
    		sysUserRole.setId(UuidUtil.getUUID());
    		//String roleId = SysRole.dao.findByrRoleId(o.getRoleid());
    		String roleId =  o.getRoleid();
    		sysUserRole.setRoleId(roleId);
    		sysUserRole.setUserId(o.getId());
    		sysUserRole.save();
    	}
    	renderSuccess();
    }
/***
 * 获取编辑页面
 */
public void getEditPage(){
	
		//获得角色信息
		List<SysRole> roleList = SysRole.dao.getlist();
		setAttr("roleList", roleList);
		//添加和修改
		String id = getPara("id");
		if(StrKit.notBlank(id)){//修改
			SysUser o = SysUser.dao.getById(id);
			String orgid = o.getOrgid();
			SysOrg org = SysOrg.dao.getById(orgid);
			if(org!=null){
			setAttr("org", org);
			}
			setAttr("o", o);
		}
	
	render("/WEB-INF/admin/usermanage/edit.html");
}
/***
 * 验证用户 , 是否被注册
 */
public void validUsername(){
	SysUser user = getModel(SysUser.class);
	if(user!=null){
		String username = user.getUsername();
		if(StrKit.notBlank(username)){
			SysUser o =  SysUser.dao.getByUsername(username);
			if(o==null){//用户不存在 
				renderValidSuccess();
			}else{
				renderValidFail();
			}
		}else{
			renderValidSuccess();
		}
	}else{
		renderValidSuccess();
	}
}

/***
 * 给用户赋值角色
 */
public void getGiveUserRolePage(){
	String userid = getPara("userid");
	setAttr("userid", userid);
	render("/WEB-INF/admin/usermanage/giveUserRole.html");
}
/***
 * 给用户赋值角色
 */
public void giveUserRole(){
	String userid = getPara("userid");
	String data = getPara("data");
	SysUser.dao.giveUserRole(userid,data);
	renderSuccess();
}
/***
 * 删除
 * @throws Exception
 */
public void delete() throws Exception{
		String ids = getPara("ids");
 	//执行删除
		SysUser.dao.deleteByIds(ids);
 	renderSuccess("删除成功!");
 }
 

	/**************************************************************************/
	private String pageTitle = "用户管理";// 模块页面标题
	private String breadHomeMethod = "getDraftListPage";// 面包屑首页方法

	public Map<String, String> getPageTitleBread() {
		Map<String, String> pageTitleBread = new HashMap<String, String>();
		pageTitleBread.put("pageTitle", pageTitle);
		pageTitleBread.put("breadHomeMethod", breadHomeMethod);
		return pageTitleBread;
	}
}
