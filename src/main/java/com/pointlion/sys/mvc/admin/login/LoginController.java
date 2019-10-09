/**
 * @author Lion
 * @date 2017年1月24日 下午12:02:35
 * @qq 439635374
 */
package com.pointlion.sys.mvc.admin.login;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.math.RandomUtils;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.shiro.authc.credential.PasswordService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.session.SessionException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;

import com.jfinal.aop.Before;
import com.jfinal.kit.StrKit;
import com.pointlion.sys.mvc.common.GlobalConstant;
import com.pointlion.sys.mvc.common.base.BaseController;
import com.pointlion.sys.mvc.common.dto.ZtreeNode;
import com.pointlion.sys.mvc.common.model.SysCustomSetting;
import com.pointlion.sys.mvc.common.model.SysOrg;
import com.pointlion.sys.mvc.common.model.SysRole;
import com.pointlion.sys.mvc.common.model.SysUser;
import com.pointlion.sys.mvc.common.utils.DateUtil;
import com.pointlion.sys.mvc.common.utils.SmsUtil;
import com.pointlion.sys.mvc.common.utils.UuidUtil;
import com.pointlion.sys.plugin.shiro.ShiroKit;
import com.pointlion.sys.plugin.shiro.ext.CaptchaFormAuthenticationInterceptor;
import com.pointlion.sys.plugin.shiro.ext.CaptchaRender;
import com.pointlion.sys.plugin.shiro.ext.CaptchaUsernamePasswordToken;
import com.pointlion.sys.plugin.shiro.ext.IncorrectCaptchaException;


public class LoginController extends BaseController {
    
    private static final Logger LOG = Logger.getLogger(LoginController.class);
    private static final int DEFAULT_CAPTCHA_LEN = 4;//验证码长度
    private static final String LOGIN_URL = "/admin/login";
    
    /***
     * 首页或者登陆
     */
    public void index(){
    	this.createToken("loginToken");
    	Subject subject = ThreadContext.getSubject();
    	if(subject.isAuthenticated()){//已经登陆成功
    		SessionUtil.setUsernameToSession(this.getRequest(), ShiroKit.getUsername());
    		
    		SysCustomSetting setting = SysCustomSetting.dao.getLoginUserCstSetting();
    		if(setting!=null&&setting.getNavColl().equals("0")){
    			if(setting.getOffType().equals("push")){
            		this.redirect("/admin/home?&offcanvas=push");
            	}else if(setting.getOffType().equals("slide")){
            		this.redirect("/admin/home?&offcanvas=slide");
            	}else if(setting.getOffType().equals("reveal")){
            		this.redirect("/admin/home?&offcanvas=reveal");        		
            	}else{
            		this.redirect("/admin/home");
            	}
    		}else{
    			this.redirect("/admin/home");
    		}
    	}else{
    		this.render("/login.html");
    	}
    }
    /**
     * 注册
     */
    public void register() {
		render("/register.html");
	}
    public void doRegister(){
    	HttpServletRequest request = this.getRequest();
    	HttpSession session = request.getSession();
    	SysUser o = getModel(SysUser.class, "user");
    	String topasswd = request.getParameter("topassword");
    	String captcha = request.getParameter("captcha");
    	if(StrKit.isBlank(o.getUsername())){
    		renderError("请输入用户名");
    		return;
    	}
    	if(StrKit.isBlank(o.getPassword())){
    		renderError("请输入密码");
    		return;
    	}
    	if(o.getPassword().length()<6){
    		renderError("密码至少输入6位数");
    		return;
    	}
    	if(StrKit.isBlank(topasswd)){
    		renderError("请输入确认密码");
    		return;
    	}
    	if(!o.getPassword().equals(topasswd)){
    		renderError("密码和确认密码不一致");
    		return;
    	}
    	if(StrKit.isBlank(o.getOrgid())){
    		renderError("请选择区级别组织机构");
    		return;
    	}
    	String md5Code = null;
        if(session != null){
            md5Code = (String)session.getAttribute(CaptchaRender.DEFAULT_CAPTCHA_MD5_CODE_KEY);
        }
    	boolean isRight = CaptchaRender.validate(md5Code, captcha);
        if (!isRight) {
            renderError("验证码错误!");
            return;
        }
    	SysUser user = SysUser.dao.getByUsername(o.getUsername());
    	if(user!=null && StrKit.notBlank(user.getId())){
    		renderError("该用户名已存在");
    		return;
    	}
    	SysOrg org = SysOrg.dao.getById(o.getOrgid());
    	if(org==null || !"2".equals(org.getType())){
    		renderError("请选择区级别组织机构");
    		return;
    	}
		o.setId(UuidUtil.getUUID());
		o.setAreaCode(org.getAreaCode());
		PasswordService svc = new DefaultPasswordService();
		o.setPassword(svc.encryptPassword(o.getPassword()));//加密新密码
		//添加权限
		List<SysRole> roles = SysRole.dao.getlist();
		for (SysRole sysRole : roles) {
			if("注册人员".equals(sysRole.getName())){
				o.setRoleid(sysRole.getId());
			}
		}
		if(o.save()){
			//授权
			SysUser.dao.giveUserRole(o.getId(),o.getRoleid());
		}
		renderSuccess();
    	
    }
    /***
     * 获取树
     */
    public void getOrgTree(){
    	List<SysOrg> menuList = SysOrg.dao.getChildrenAll("#root");
		List<ZtreeNode> nodelist = SysOrg.dao.toZTreeNode(menuList ,false,false);//数据库中的菜单
    	/*List<ZtreeNode> rootList = new ArrayList<ZtreeNode>();//页面展示的,带根节点
    	//声明根节点
    	ZtreeNode root = new ZtreeNode();
    	root.setId("#root");
    	root.setName("湖南省文化旅游厅");
    	root.setChildren(nodelist);
    	root.setOpen(true);
    	rootList.add(root);*/
    	renderJson(nodelist);
    }
    /***
     * 没有权限访问的
     */
    public void needPermission(){
    	render("/error/needPermission.html");
    }
    
    /**
     * @Title: img  
     * @Description: 图形验证码   
     * @since V1.0.0
     */
    public void img(){
        CaptchaRender img = new CaptchaRender(DEFAULT_CAPTCHA_LEN);
        this.setSessionAttr(CaptchaRender.DEFAULT_CAPTCHA_MD5_CODE_KEY, img.getMd5RandonCode());
        this.setSessionAttr("randonCode",img.getRandonCode());
        render(img);
    }
    
    @Before({LoginValidator.class, CaptchaFormAuthenticationInterceptor.class})
    public void doLogin(){
        try {
            CaptchaUsernamePasswordToken token = this.getAttr("shiroToken");
            Subject subject = SecurityUtils.getSubject();
            
            setSessionAttr("username", token.getUsername());
            
            ThreadContext.bind(subject);
            //进行用用户名和密码验证
            if(subject.isAuthenticated()){
            	subject.logout();
            }else{  
            	subject.login(token);
            	
            	
            }
          //写操作记录到应用日志中
        	/*String username = SessionUtil.getUsernameFromSession(this.getRequest());
        	SysApplog sysapplog = getModel(SysApplog.class);
        	sysapplog.setApplogId(UuidUtil.getUUID());
        	sysapplog.setUSERNAME(username);
        	sysapplog.setCONTENT("登录操作");
        	sysapplog.setCTIME("2018-05-13");
        	sysapplog.save();*/
            System.out.println("{title:'绩效考核系统-登录操作',action:'com.pointlion.sys.mvc.admin.login.getDraftListPage',type:'action',ip:'"+getRequest().getRemoteAddr()+"',username:'"+token.getUsername()+"',logintime:'"+DateUtil.dateToString(new Date(), 0)+"'}");
            //调转到主页面
            this.redirect("/admin/home");
          
        }catch (IncorrectCaptchaException e) {
        	LOG.error(e.getMessage());
        	setAttr("error", e.getMessage());
        	this.render("/login.html");
        } catch (LockedAccountException e) {
        	LOG.error(e.getMessage());
        	setAttr("error", "账号已被锁定!");
        	this.render("/login.html");
        } catch (AuthenticationException e) {
        	LOG.error(e.getMessage());
        	setAttr("error", e.getMessage());
        	this.render("/login.html");
        } catch (Exception e) {
            LOG.error(e.getMessage());
            setAttr("error", "系统异常!");
        	this.render("/login.html");
        }
    }
    @RequiresAuthentication
    public void logout() {
        Subject currentUser = SecurityUtils.getSubject();
        try {
            currentUser.logout();
            this.removeSessionAttr("user");
            this.redirect(LOGIN_URL);
        } catch (SessionException ise) {
            LOG.debug("Encountered session exception during logout.  This can generally safely be ignored.", ise);
        } catch (Exception e) {
            LOG.debug("登出发生错误", e);
        }
    }
    
	public void logout2() {
		try {
			Subject subject = ThreadContext.getSubject();
			if (!subject.isAuthenticated()) {
				renderSuccess();
			} else {
				renderError();
			}
		} catch (Exception e) {
			LOG.debug("登出发生错误", e);
			renderError();
		}
	}
	
    public void doReg() {  
            PasswordService svc = new DefaultPasswordService();  
            String encrypted = svc.encryptPassword("liyang");  
            SysUser user = new SysUser();
            user.set("username", "li");
            user.set("password", encrypted);
            user.save();
    }
    
    /**
     * 用户名、密码验证弹出手机短信码验证
     */
	@Before({ LoginValidator.class, CaptchaFormAuthenticationInterceptor.class })
	public void login() {
		String username = "";
		Subject subject = null;
		try {
			CaptchaUsernamePasswordToken token = this.getAttr("shiroToken");
			subject = SecurityUtils.getSubject();
			setSessionAttr("username", token.getUsername());
			username = token.getUsername();

			ThreadContext.bind(subject);
			// 进行用用户名和密码验证
			if (subject.isAuthenticated()) {
				subject.logout();
			} else {
				subject.login(token);
			}
		} catch (IncorrectCaptchaException e) {
			LOG.error(e.getMessage());
			renderError(e.getMessage());
			return;
		} catch (LockedAccountException e) {
			LOG.error(e.getMessage());
			renderError("账号已被锁定!");
			return;
		} catch (AuthenticationException e) {
			LOG.error(e.getMessage());
			renderError(e.getMessage());
			return;
		} catch (Exception e) {
			LOG.error(e.getMessage());
			renderError("系统异常!");
			return;
		}
		SysUser user = SysUser.dao.getByUsername(username);
		String mobile = null;
		if (user != null) {
			if (StrKit.notBlank(user.getMobile())) {
				mobile = user.getMobile();
			}
		}
		subject.logout();
		renderSuccess(mobile);
	}
	
	/**
	 * 手机验证码登录验证
	 */
	@Before({ LoginValidator.class, CaptchaFormAuthenticationInterceptor.class })
	public void loginA() {
		HttpServletRequest request = this.getRequest();
		HttpSession session = request.getSession();
		String phone = request.getParameter("phone");
		String checkcode = request.getParameter("captcha");
		String smscode = request.getParameter("smscode");
		String username = request.getParameter("username");
		try {
			if (!checkVcode(session, checkcode)) {
				return;
			}
			SysUser user = SysUser.dao.getByUsername(username);
			if (user != null) {
				if (StrKit.notBlank(user.getMobile())) {
					if (!phone.equals(user.getMobile())) {
						renderError("手机与登录用户不匹配!");
						return;
					}
				}
			}
			if (!checkSmscode(session, smscode, phone)) {
				return;
			}
			CaptchaUsernamePasswordToken token = this.getAttr("shiroToken");
			Subject subject = SecurityUtils.getSubject();

			setSessionAttr("username", token.getUsername());

			ThreadContext.bind(subject);
			// 进行用用户名和密码验证
			if (subject.isAuthenticated()) {
				//subject.logout();
			} else {
				subject.login(token);
			}
			
			user = SysUser.dao.getByUsername(token.getUsername());
			if (user != null) {
				//如果手机号码为空，则更新绑定手机号
				if (StrKit.isBlank(user.getMobile())) {
					SysUser sysUser = new SysUser();
					sysUser.setId(user.getId());
					sysUser.setMobile(phone);
					sysUser.update();
				}
			}
			this.redirect("/admin/home");
		} catch (IncorrectCaptchaException e) {
			LOG.error(e.getMessage());
			renderError(e.getMessage());
			return;
		} catch (LockedAccountException e) {
			LOG.error(e.getMessage());
			renderError("账号已被锁定!");
			return;
		} catch (AuthenticationException e) {
			LOG.error(e.getMessage());
			renderError(e.getMessage());
			return;
		} catch (Exception e) {
			LOG.error(e.getMessage());
			renderError("系统异常!");
			return;
		}
		renderSuccess();
	}
	
	/**
	 * 发送短信
	 */
	public void smscode() {
		HttpServletRequest request = this.getRequest();
    	HttpSession session = request.getSession();
    	String mobile = request.getParameter("mobile");
		String smscode = String.format("%06d", RandomUtils.nextInt(1000000));
		String[] code = { smscode };
		boolean sendStatus = false;
		
		HashMap<String, Object> result = SmsUtil.send(GlobalConstant.SMS_TEMPLATE_CHECKCODE, mobile, code);
		if (result == null)
			sendStatus = false;
		String statusCode = String.valueOf(result.get("statusCode"));
		sendStatus = "000000".equals(statusCode);
		
		if (sendStatus) {
			session.setAttribute("regSmstime", System.currentTimeMillis());
			session.setAttribute("regSmscode", smscode);
			session.setAttribute("regMobile", mobile);
		} else {
			renderError("短信验证码发送失败");
		}
		renderSuccess();
	}
	
	public void smscode2() {
		HttpServletRequest request = this.getRequest();
    	HttpSession session = request.getSession();
    	String mobile = request.getParameter("mobile");
		String smscode = String.format("%06d", RandomUtils.nextInt(1000000));
		session.setAttribute("regSmstime", System.currentTimeMillis());
		session.setAttribute("regSmscode", smscode);
		session.setAttribute("regMobile", mobile);
		renderSuccess(smscode);
	}
	
	/**
	 * 先验证验证码是否正确
	 */
	public void checkCode() {
		HttpServletRequest request = this.getRequest();
    	HttpSession session = request.getSession();
    	String checkcode = request.getParameter("checkcode");
		if (!checkVcode(session, checkcode)) {
			return;
		} else {
			renderSuccess("验证码通过");
			return;
		}
	}
	
	/**
	 * 检查图片验证码是否正确
	 * @return
	 */
	private boolean checkVcode(HttpSession session, String checkcode) {
    	String md5Code = null;
        if(session != null){
            md5Code = (String)session.getAttribute(CaptchaRender.DEFAULT_CAPTCHA_MD5_CODE_KEY);
        }
    	boolean isRight = CaptchaRender.validate(md5Code, checkcode);
        if (!isRight) {
            renderError("图片验证码错误");
            return false;
        }
		return true;
	}
	
	/**
	 * 检查手机验证码是否正确
	 * @return
	 */
	private boolean checkSmscode(HttpSession session, String smscode, String mobile) {
		Object tmp1 = session.getAttribute("regSmscode");
		Object tmp2 = session.getAttribute("regMobile");
		if (tmp1 == null || !String.valueOf(tmp1).equals(smscode)) {
			renderError("手机短信码错误");
			return false;
		}
		if (tmp2 == null || !String.valueOf(tmp2).equals(mobile)) {
			renderError("手机号与发送验证码手机号不相符");
			return false;
		}
		return true;
	}
}