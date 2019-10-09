package com.pointlion.sys.mvc.admin.db;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fasterxml.jackson.databind.deser.std.JdkDeserializers.UUIDDeserializer;
import com.jfinal.plugin.activerecord.Page;
import com.pointlion.sys.mvc.common.base.BaseController;
import com.pointlion.sys.mvc.common.model.OaBumph;
import com.pointlion.sys.mvc.common.model.TDbbackup;
import com.pointlion.sys.mvc.common.utils.DateUtil;
import com.pointlion.sys.mvc.common.utils.DbFH;
import com.pointlion.sys.mvc.common.utils.FileUtil;
import com.pointlion.sys.mvc.common.utils.PageData;
import com.pointlion.sys.mvc.common.utils.UuidUtil;

public class BRdbController  extends BaseController{

	/***************************数据库管理---开始***********************/
	/**
	 * 获得显示数据库表的页面
	 * 
	 */
	
	public void getAllTable(){
		setBread("数据库备份",this.getRequest().getServletPath(),"备份");
		
		Object[] arrOb = null;
		//String ip = "192.168.1.103";
		try {
			arrOb =	DbFH.getTables();
			List<String> tblist = (List<String>)arrOb[1];
			//System.out.println(arrOb);
			setAttr("varList", tblist); //所有表
			setAttr("dbtype",arrOb[2]); //数据库类型
			setAttr("databaseName", arrOb[0]); //数据库名
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		render("/WEB-INF/admin/db/bklist.html");
	}
	
	
	/**
	 * 数据库备份
	 */
	public void backupAll(){
		//校验权限....
		
		String kackupPath;
		//String ip = "192.168.1.103";
		TDbbackup backup;
		try
		{
			kackupPath =DbFH.getDbFH().backup("").toString();
			if(kackupPath!="errer"){
				backup = new TDbbackup();
				backup.setBkId(UuidUtil.getUUID());
				backup.setBkObject("整库");   
				backup.setBkTime(DateUtil.getTime());
				backup.setOperateUser("admin"); //暂时写死
				backup.setStorageLocation(kackupPath);
				backup.setDateSize(FileUtil.getFilesize(kackupPath).toString());
				backup.save();
				renderSuccess();
			}
			else
			{
				render("/WEB-INF/admin/db/fail.html");
			}
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 获得显示数据库还原信息的页面
	 * 
	 */
	
	public void getRecoverPage(){
		setBread("数据库还原",this.getRequest().getServletPath(),"还原");
		render("/WEB-INF/admin/db/rclist.html");
	}
	
	/***
	 * 数据还原
	 */
	public void recover(){
		String sqlPath = getPara("sqlPath");
		String tableName = "整库";  //整库
		//String ip ="192.168.1.103";
		String returnStr = null;
		try {
			returnStr = DbFH.getDbFH().recover(tableName, sqlPath).toString();
			System.out.println(returnStr);
			if(returnStr!="errer"){
				renderSuccess();
			}
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	
	/***
	 * 备份记录列表数据
	 */
    public void recoverListData(){
    	String curr = getPara("pageNumber");
    	String pageSize = getPara("pageSize");
    	Page<TDbbackup> page = TDbbackup.dao.getPage(Integer.valueOf(curr),Integer.valueOf(pageSize));
    	renderPage(page.getList(),"" ,page.getTotalRow());
    }
	
	
	
	/************************数据库管理---结束*************************************************/
    
    /**************************************************************************/
	public void setBread(String name,String url,String nowBread){
		Map<String,String> pageTitleBread = new HashMap<String,String>();
		pageTitleBread.put("pageTitle", name);
		pageTitleBread.put("url", url);
		pageTitleBread.put("nowBread", nowBread);
		this.setAttr("pageTitleBread", pageTitleBread);
	}
	
}
