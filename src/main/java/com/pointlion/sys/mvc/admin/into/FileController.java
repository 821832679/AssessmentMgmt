package com.pointlion.sys.mvc.admin.into;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.ObjectUtils.Null;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.upload.UploadFile;
import com.pointlion.sys.interceptor.MainPageTitleInterceptor;
import com.pointlion.sys.mvc.common.base.BaseController;
import com.pointlion.sys.mvc.common.model.Tpclassify;
import com.pointlion.sys.mvc.common.model.Tpqualifications;
import com.pointlion.sys.mvc.common.utils.ReadExcel;
@Before(MainPageTitleInterceptor.class)
public class FileController extends BaseController {
    String excel=null;
    String classpath =this.getClass().getResource("/").getPath();

	public void downfile() {
		//excle导出
//		try{
//			String classpath2 =classpath;
//			classpath2 = classpath2.replaceAll("/", "\\\\\\\\");  
//			classpath2 = classpath2.substring(2, classpath2.length());
//			System.out.println("输出成功"+classpath2);
//			//Db.find("select * into outfile 'D:\\ifno.xls' from t_tpsource");
//			Db.find("select * into outfile 'E:\\mer_no.xls' CHARACTER SET gbk from t_tpsource;");
//			System.out.println("输出成功"+classpath2);
//		}catch(Exception ee)
//		{
//			System.out.println(ee);
//		}
		//windows
		 //File file = new File("E:/1234.xls");//缓存在服务器的excle
		 //linux
		 File file = new File("/usr/local/document/foundation-pc/images/upload/1234.xls");
			try {
				ReadExcel readExcel = new ReadExcel();
				readExcel.write1(file);
			} catch (Exception e) {
				System.out.println(e);
				e.printStackTrace();
			}
		

		if (file.exists()) {
			renderFile(file);//传回
		} else {
			renderJson();
		}
		//render("/WEB-INF/admin/menu/list.html");
	}

	public void uploadFile() {
	 //excele导入
		UploadFile uploadFile = this.getFile();
		String fileName = uploadFile.getOriginalFileName();
		File file = uploadFile.getFile();
		FileService fs = new FileService();
		File t = new File( classpath+"\\"+ UUID.randomUUID().toString()
				+ file.getName().substring(file.getName().lastIndexOf(".")));
		/*debug */String str=classpath;
		try {
			t.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		fs.fileChannelCopy(file, t);
		file.delete();
		this.renderHtml("success,<a href=\"./\">back</a>");
		System.out.println("输出成功"+t.getPath());
		String[] key = {"TP_Id",	"TPName",	"TPC_Code",	"TPQ_Code",	"TPP_Code",	"Org_Id",	"Status_Code",	"Sex",	"Age",	"Identy_Id",	"Tel",	"TP_Add",	"Email",	"type",	"Icon_Url",	"Position",	"GZjf",	"Work_Age",	"BZlb",	"TeacherIntroduction",	"Opus",	"Vilage",	"Vil_Peo",	"Town",	"Tow_Peo",	"Country",	"Cou_Peo",	"City",	"Cit_Peo",	"Save",	"Sav_Peo",	"Mender_Id",	"ModifyTime",	"Creator_Id",	"municipallevel",	"countylevel",	"CreateTime",};
		try {
			new ReadExcel().read(t,key);
		} catch (Exception e) {
			//renderError();
		}
		
		redirect("http://210.43.241.80:8080/YiDaoMgmt/admin/home");

		//renderSuccess();
		
		
//		List<Tpqualifications> tpqList  = Tpqualifications.dao.getlist();
//		setAttr("tpqList", tpqList);
//		List<Tpclassify> tpcList= Tpclassify.dao.getlist();
//		setAttr("tpcList", tpcList);
//		renderTemplate("/WEB-INF/admin/psourcemanage/list.html");
		
//		this.renderHtml("success,<a href=\"./\">back</a>"
//				+ "<script type=”text/javascript”>"
//				//+ "window.onload=function (){"
//				+ "function a(){"
//				+ "alert(\"我是回调函数c\");  "
//			//	+ "pointLion.alertMsg(\"保存成功!\" , \"success\" , \"small\" , function(){"
//			//	+ "window.history.back();"
//				+ "}"
//				+ "</script>");
		
		
		
	}
//	public void refesh(){
//		//render("/WEB-INF/admin/menu/list.html");
//	}
//	
//	private String pageTitle = "人才管理";// 模块页面标题
//	private String breadHomeMethod = "getListPage";// 面包屑首页方法
//	public Map<String, String> getPageTitleBread() {
//		Map<String, String> pageTitleBread = new HashMap<String, String>();
//		pageTitleBread.put("pageTitle", pageTitle);
//		pageTitleBread.put("breadHomeMethod", breadHomeMethod);
//		return pageTitleBread;
//	}
	
	//存在问题
	//1.导出的excle  字段顺序与数据库不同 调整顺序后才可以直接导入
	//2.导入数据时，id作为主键不能与数据库中的相同，否则不能导入 且不能多和少字段
	//3.文件必须是.xls
	//4数据库中的数据类型应为varchar或int
	
	
}
