/**
 * @author Lion
 * @date 2017年1月24日 下午12:02:35
 * @qq 439635374
 */
package com.pointlion.sys.config;

import java.util.Properties;

import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.wall.WallFilter;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.ext.handler.UrlSkipHandler;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.dialect.MysqlDialect;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.template.Engine;
import com.pointlion.sys.handler.GlobalHandler;
import com.pointlion.sys.interceptor.ExceptionInterceptor;
import com.pointlion.sys.interceptor.IfLoginInterceptor;
import com.pointlion.sys.mvc.admin.admapplog.AdmAppLogController;
import com.pointlion.sys.mvc.admin.answer.AnswerManagementController;
import com.pointlion.sys.mvc.admin.answernoedit.AnswerNoEditManagementController;
import com.pointlion.sys.mvc.admin.applog.AppLogController;
import com.pointlion.sys.mvc.admin.bumph.BumphController;
import com.pointlion.sys.mvc.admin.changepassword.changepasswordController;
import com.pointlion.sys.mvc.admin.chat.ChatController;
import com.pointlion.sys.mvc.admin.cstmsetting.CustomSettingController;
import com.pointlion.sys.mvc.admin.db.BRdbController;
import com.pointlion.sys.mvc.admin.generator.GeneratorController;
import com.pointlion.sys.mvc.admin.help.HelpManagementController;
import com.pointlion.sys.mvc.admin.home.HomeController;
import com.pointlion.sys.mvc.admin.ic.InformationmCheckController;
import com.pointlion.sys.mvc.admin.im.InformationmManagementController;
import com.pointlion.sys.mvc.admin.into.FileController;
import com.pointlion.sys.mvc.admin.leave.OaLeaveController;
import com.pointlion.sys.mvc.admin.login.LoginController;
import com.pointlion.sys.mvc.admin.menu.MenuController;
import com.pointlion.sys.mvc.admin.notice.NoticeController;
import com.pointlion.sys.mvc.admin.org.OrgController;
import com.pointlion.sys.mvc.admin.pm.ProgramManagementController;
import com.pointlion.sys.mvc.admin.prolemanage.prolemanageController;
import com.pointlion.sys.mvc.admin.psourcemanage.psourcemanageController;
import com.pointlion.sys.mvc.admin.question.QuestionManagementController;
import com.pointlion.sys.mvc.admin.questionanswer.QuestionAnswerManagementController;
import com.pointlion.sys.mvc.admin.resget.OaResGetController;
import com.pointlion.sys.mvc.admin.resources.resourceController;
import com.pointlion.sys.mvc.admin.role.RoleController;
import com.pointlion.sys.mvc.admin.score.ScoreManagementController;
import com.pointlion.sys.mvc.admin.scoreadmin.ScoreAdminManagementController;
import com.pointlion.sys.mvc.admin.scorelook.ScoreLookManagementController;
import com.pointlion.sys.mvc.admin.sqlcontrol.SqlController;
import com.pointlion.sys.mvc.admin.topic.TopicManagementController;
import com.pointlion.sys.mvc.admin.topicdelay.TopicDelayManagementController;
import com.pointlion.sys.mvc.admin.topictype.TopicTypeManagementController;
import com.pointlion.sys.mvc.admin.uidemo.UIDemoController;
import com.pointlion.sys.mvc.admin.upload.UploadController;
import com.pointlion.sys.mvc.admin.usecar.OaUseCarController;
import com.pointlion.sys.mvc.admin.user.UserController;
import com.pointlion.sys.mvc.admin.usermanage.usermanageController;
import com.pointlion.sys.mvc.admin.workflow.WorkFlowController;
import com.pointlion.sys.mvc.admin.workflow.main.StencilsetRestResource;
import com.pointlion.sys.mvc.admin.workflow.model.ModelController;
import com.pointlion.sys.mvc.admin.workflow.model.ModelEditorJsonRestResource;
import com.pointlion.sys.mvc.admin.workflow.model.ModelSaveRestResource;
import com.pointlion.sys.mvc.admin.workflow.rest.ProcessDefinitionDiagramLayoutResource;
import com.pointlion.sys.mvc.admin.workflow.rest.ProcessInstanceDiagramLayoutResource;
import com.pointlion.sys.mvc.admin.workflow.rest.ProcessInstanceHighlightsResource;
import com.pointlion.sys.mvc.admin.zjscore.ZjScoreManagementController;
import com.pointlion.sys.mvc.common.model._MappingKit;
import com.pointlion.sys.mvc.mobile.bumph.MobileBumphController;
import com.pointlion.sys.mvc.mobile.common.MobileController;
import com.pointlion.sys.mvc.mobile.login.MobileLoginController;
import com.pointlion.sys.mvc.mobile.notice.MobileNoticeController;
import com.pointlion.sys.plugin.activiti.ActivitiPlugin;
import com.pointlion.sys.plugin.shiro.ShiroInterceptor;
import com.pointlion.sys.plugin.shiro.ShiroKit;
import com.pointlion.sys.plugin.shiro.ShiroPlugin;

public class MainConfig extends JFinalConfig {
    Routes routes;
    
	/**
	 * 配置JFinal常量
	 */
	@Override
	public void configConstant(Constants me) {
		//读取数据库配置文件
		PropKit.use("config.properties");
		//设置当前是否为开发模式
		me.setDevMode(PropKit.getBoolean("devMode"));
		if (isOSLinux()) {
			// 设置默认上传文件保存路径 getFile等使用
			me.setBaseUploadPath(PropKit.get("image.root.path"));// for linux's setting
			// 设置默认下载文件路径 renderFile等使用
			me.setBaseDownloadPath(PropKit.get("image.root.path"));// for linux's setting
		} else {
			// 设置默认上传文件保存路径 getFile等使用
			me.setBaseUploadPath("resource/upload");// for window's setting
			// 设置默认下载文件路径 renderFile等使用
			me.setBaseDownloadPath("resource/upload"); // for window's setting
		}
		//设置上传最大限制尺寸
		me.setMaxPostSize(1024*1024*500);
		//获取beetl模版引擎
//		me.setRenderFactory(new BeetlRenderFactory());
//		me.setError404View("/error/404.html");
        // 获取GroupTemplate ,可以设置共享变量等操作
//        @SuppressWarnings("unused")
//		GroupTemplate groupTemplate = BeetlRenderFactory.groupTemplate ;
	}

	/**
	 * 配置JFinal插件
	 * 数据库连接池
	 * ORM
	 * 缓存等插件
	 * 自定义插件
	 */
	@Override
	public void configPlugin(Plugins me) {
		//配置数据库连接池插件
		DruidPlugin druidPlugin =new DruidPlugin(PropKit.get("jdbcUrl"), PropKit.get("user"), PropKit.get("password"));
		druidPlugin.addFilter(new StatFilter());
		 WallFilter wall=new WallFilter();
		 wall.setDbType("mysql");
		 druidPlugin.addFilter(wall);
		//orm映射 配置ActiveRecord插件
		ActiveRecordPlugin arp=new ActiveRecordPlugin(druidPlugin);
		arp.setShowSql(PropKit.getBoolean("devMode"));
		arp.setDialect(new MysqlDialect());
		_MappingKit.mapping(arp);
		ActivitiPlugin acitivitiPlugin = new ActivitiPlugin();
		ShiroPlugin shiroPlugin = new ShiroPlugin(this.routes);
	    shiroPlugin.setLoginUrl("/admin/login");//登陆url：未验证成功跳转
	    shiroPlugin.setSuccessUrl("/admin/index");//登陆成功url：验证成功自动跳转
	    shiroPlugin.setUnauthorizedUrl("/admin/login/needPermission");//授权url：未授权成功自动跳转
	    //添加到插件列表中
	    me.add(druidPlugin);
	    me.add(arp);
	    me.add(acitivitiPlugin);
	    me.add(shiroPlugin);
	}
	
	/**
	 * 配置全局拦截器
	 */
	@Override
	public void configInterceptor(Interceptors me) {
		 me.add(new ShiroInterceptor());//shiro拦截器
		 me.add(new IfLoginInterceptor());//判断是否登录拦截器
		 me.add(new ExceptionInterceptor());//通用异常处理拦截器
	}
	
	/**
	 * 配置全局处理器
	 */
	@Override
	public void configHandler(Handlers handler) {
		//log.info("configHandler 全局配置处理器，设置跳过哪些URL不处理");
		handler.add(new UrlSkipHandler("/|/admin/friendchat/.*|/ca/.*|/se/.*|.*.htm|.*.html|.*.js|.*.css|.*.json|.*.png|.*.gif|.*.jpg|.*.jpeg|.*.bmp|.*.ico|.*.exe|.*.txt|.*.zip|.*.rar|.*.7z|.*.tff|.*.woff|.*.ttf|.*.map|.*.jsp|.*.do", false));
		handler.add(new GlobalHandler());
	}
	

	
	/**
	 * 配置JFinal路由映射
	 */
	@Override
	public void configRoute(Routes me) {
		this.routes = me;//shiro使用
		//前端DEMO
		me.add("/admin/uidemo", UIDemoController.class);//UIdemo
		//系统管理
		me.add("/admin/login", LoginController.class);//登陆
		me.add("/admin/home", HomeController.class);//首页
		me.add("/admin/org", OrgController.class);//用户
		me.add("/admin/chat",ChatController.class);//即时聊天
	
		
		//me.add("/admin/upload",FileUploadController.class);//新的文件上传
		
		
		me.add("/admin/user", UserController.class);//用户
		me.add("/admin/menu", MenuController.class);//菜单
		me.add("/admin/role",RoleController.class);//执行对象-功能
		me.add("/admin/applog",AppLogController.class);//应用日志
		me.add("/admin/customsetting",CustomSettingController.class);//执行对象-功能
		me.add("/admin/model",ModelController.class);//工作流-模型
		me.add("/admin/workflow",WorkFlowController.class);//工作流
		//代码生成器
		me.add("/admin/generator",GeneratorController.class);//工作流
		//文件上传
		me.add("/admin/upload",UploadController.class);//文件上传
		//在线办公
		me.add("/admin/bumph",BumphController.class);//公文管理---内部发文，收文转发
		me.add("/admin/notice",NoticeController.class);//通知公告
		me.add("/admin/resget",OaResGetController.class);//物品领用
		me.add("/admin/usecar",OaUseCarController.class);//车辆派送
		me.add("/admin/leave",OaLeaveController.class);//请销假
		//流程在线编辑器和流程跟踪所用路由
		me.add("/admin/process-instance/highlights",ProcessInstanceHighlightsResource.class);//modeler
		me.add("/admin/process-instance/diagram-layout",ProcessInstanceDiagramLayoutResource.class);//modeler
		me.add("/admin/process-definition/diagram-layout",ProcessDefinitionDiagramLayoutResource.class);//modeler
		me.add("/admin/modelEditor/save",ModelSaveRestResource.class);
		me.add("/admin/editor/stencilset",StencilsetRestResource.class);
		me.add("/admin/modelEditor/json",ModelEditorJsonRestResource.class);
		//手机端接口
		me.add("/mobile/common",MobileController.class);
		me.add("/mobile/notice",MobileNoticeController.class);
		me.add("/mobile/login",MobileLoginController.class);
		me.add("/mobile/bumph",MobileBumphController.class);
		
		//数据库管理
		me.add("/admin/db",BRdbController.class);
		
		me.add("/admin/admapplog",AdmAppLogController.class);//应用操作记录
		me.add("/admin/sqlcontrol",SqlController.class);//SQl监控；
		me.add("/admin/psourcemanage",psourcemanageController.class);//人才资源管理；
		me.add("/admin/changepassword",changepasswordController.class);//修改密码；
		me.add("/admin/usermanage",usermanageController.class);//用户管理；
	    me.add("/admin/prolemanage",prolemanageController.class);//角色管理；
	    me.add("/admin/into",FileController.class);
	    
	    //业务资源管理
	    me.add("/admin/pm",ProgramManagementController.class);
	  	me.add("/admin/im",InformationmManagementController.class);
	  	me.add("/admin/ic",InformationmCheckController.class);
	  	me.add("/admin/resources",resourceController.class);
	  	
	    //绩效考核管理
	    me.add("/admin/topic",TopicManagementController.class);
	    me.add("/admin/topictype",TopicTypeManagementController.class);
	  	me.add("/admin/answer",AnswerManagementController.class);
	  	me.add("/admin/question",QuestionManagementController.class);
	  	me.add("/admin/questionanswer",QuestionAnswerManagementController.class);
	  	me.add("/admin/answernoedit",AnswerNoEditManagementController.class);
	  	me.add("/admin/score",ScoreManagementController.class);
	  	me.add("/admin/scorelook",ScoreLookManagementController.class);
	  	me.add("/admin/file",FileController.class);
	  	me.add("/admin/zjscore",ZjScoreManagementController.class);//专家评分
	  	me.add("/admin/help",HelpManagementController.class);//专家评分
	  	me.add("/admin/scoreadmin",ScoreAdminManagementController.class);//管理员指标维护
	  	me.add("/admin/topicdelay",TopicDelayManagementController.class);//管理员考核延期维护
	}
	
	@Override
	public void configEngine(Engine me) {
		me.addSharedObject("shiro",new ShiroKit());
	}
	
	/**
	 * 操作系统
	 * @return
	 */
	public static boolean isOSLinux() {
		Properties prop = System.getProperties();

		String os = prop.getProperty("os.name");
		if (os != null && os.toLowerCase().indexOf("linux") > -1) {
			return true;
		} else {
			return false;
		}
	}
}