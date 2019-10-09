/**
 * @author Lion
 * @date 2017年1月24日 下午12:02:35
 * @qq 439635374
 */
package com.pointlion.sys.mvc.admin.changepassword;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.shiro.authc.credential.PasswordService;

import com.jfinal.aop.Before;
import com.pointlion.sys.interceptor.MainPageTitleInterceptor;
import com.pointlion.sys.mvc.common.base.BaseController;
import com.pointlion.sys.mvc.common.model.SysUser;
import com.pointlion.sys.mvc.common.utils.DateUtil;

/***
 * 用户管理控制器
 * 
 * @author Administrator
 *
 */
@Before(MainPageTitleInterceptor.class)
public class changepasswordController extends BaseController {

	/***
	 * 获得首页
	 */
	public void getDraftListPage() {
		String username = getSessionAttr("username");
		SysUser sysUser = SysUser.dao.getByUsername(username);
		setAttr("o", sysUser);
		System.out.println("{title:'绩效考核系统-密码管理',action:'com.pointlion.sys.mvc.admin.changepassword.getDraftListPage',type:'action',ip:'"+getRequest().getRemoteAddr()+"',username:'"+username+"',logintime:'"+DateUtil.dateToString(new Date(), 0)+"'}");
		render("/WEB-INF/admin/changepassword/edit.html");
	}

	/***
	 * 保存
	 */
	public HttpServletRequest re;

	public void save() {
		String id = getPara("sysUser.id");
		String psw = getPara("passwordo");
		String psw1 = getPara("sysUser.password");
		SysUser o = getModel(SysUser.class);
		SysUser u = SysUser.dao.findById(id);
		String password = u.getPassword();// 获取原始密码
		PasswordService svc = new DefaultPasswordService();
		if (svc.passwordsMatch(psw, password)) {
			u.setPassword(svc.encryptPassword(psw1));// 加密新密码
			u.update();
			System.out.println("{title:'绩效考核系统-修改密码',action:'com.pointlion.sys.mvc.admin.changepassword.save',type:'action',ip:'"+getRequest().getRemoteAddr()+"',username:'"+u.getUsername()+"',logintime:'"+DateUtil.dateToString(new Date(), 0)+"'}");
			renderSuccess();

		} else {
			renderValidFail();
		}

	}

	/**************************************************************************/
	private String pageTitle = "修改密码";// 模块页面标题
	private String breadHomeMethod = "getDraftListPage";// 面包屑首页方法

	public Map<String, String> getPageTitleBread() {
		Map<String, String> pageTitleBread = new HashMap<String, String>();
		pageTitleBread.put("pageTitle", pageTitle);
		pageTitleBread.put("breadHomeMethod", breadHomeMethod);
		return pageTitleBread;
	}
}
