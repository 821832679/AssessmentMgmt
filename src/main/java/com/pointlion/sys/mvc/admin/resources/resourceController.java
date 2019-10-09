package com.pointlion.sys.mvc.admin.resources;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.druid.util.StringUtils;
import com.jfinal.aop.Before;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.render.JsonRender;
import com.jfinal.upload.UploadFile;
import com.pointlion.sys.interceptor.MainPageTitleInterceptor;
import com.pointlion.sys.mvc.common.base.BaseController;
import com.pointlion.sys.mvc.common.model.Resource;
import com.pointlion.sys.mvc.common.model.Suffix;
import com.pointlion.sys.mvc.common.model.SysOrg;
import com.pointlion.sys.mvc.common.model.SysUser;
import com.pointlion.sys.mvc.common.utils.DateUtil;
import com.pointlion.sys.mvc.common.utils.ImageResizeUtil;
import com.pointlion.sys.mvc.common.utils.SysOrgUtil;
import com.pointlion.sys.mvc.common.utils.UuidUtil;

/**
 * 业务资源管理
 */
@Before(MainPageTitleInterceptor.class)
public class resourceController extends BaseController {

	/*************************** 资源管理---开始 ***********************/
	public void getListPage() {
		String username = getSessionAttr("username");
		SysUser user = SysUser.dao.findbyUserName(username);
		String orgId = user.getOrgid();
		SysOrg org = SysOrg.dao.findById(orgId);

		String code = ""; // 机构代码

		if (org != null) {
			// 市级
			if (org.getType() != null) {
				if (org.getType().equals("1")) {
					code = org.getAreaCode().toString().substring(0, 4);
				}
				// 区级
				if (org.getType().equals("2")) {
					code = org.getAreaCode().toString().substring(0, 6);
				}
				// 镇，乡，街道级
				if (org.getType().equals("3")) {
					code = org.getAreaCode().toString().substring(0, 9);
				}
				// 村级
				if (org.getType().equals("4")) {
					code = org.getAreaCode().toString();
				}
			}
		}

		// 查询该机构用户下的所属用户
		List<SysUser> users = SysUser.dao.findbyAreaCode(code);
		// 查询该机构下的所属机构
		List<SysOrg> orgs = SysOrg.dao.findByParentId(user.getOrgid());
		// 查询已有文件格式
		List<Suffix> suffixs = Suffix.dao.findAll();
		setAttr("users", users);
		setAttr("orgs", orgs);
		setAttr("suffixs", suffixs);
		setAttr("code", code);

		render("/WEB-INF/admin/resources/list.html");
	}

	/**
	 * 获得资料信息列表
	 */
	public void listData() {
		String curr = getPara("pageNumber");
		Float tempCurr = Float.valueOf(curr);
		Integer currPage = tempCurr.intValue();
		String pageSize = getPara("pageSize");
		// 获得组合信息
		String suffix = getPara("suffix");
		String area = getPara("area");
		String uploader = getPara("uploader");
		String date = getPara("date");
		String keyWord = getPara("keyWord");

		String username = getSessionAttr("username");
		SysUser user = SysUser.dao.findbyUserName(username);
		String ids = "";
		if (StringUtils.isEmpty(user.getOrgid())) {
			ids = "#root";
		} else {
			ids = user.getOrgid();
		}
		List<SysOrg> sysOrgList = SysOrg.dao.getChildrenAll(ids);
		String result = SysOrgUtil.getOrgListForString(sysOrgList, user.getOrgid());

		try {
			if (suffix != "") // 解决字符乱码问题。
			{
				suffix = URLDecoder.decode(suffix, "UTF-8");
			}
			if (area != "") {
				area = URLDecoder.decode(area, "UTF-8");
			}
			if (uploader != "") {
				uploader = URLDecoder.decode(uploader, "UTF-8");
			}
			if (date != "") {
				date = URLDecoder.decode(date, "UTF-8");
			}
			if (keyWord != "") {
				keyWord = URLDecoder.decode(keyWord, "UTF-8");
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		System.out.println(suffix + " " + area + " " + uploader + " " + date + " " + keyWord);

		Page<Resource> page = Resource.dao.getPage(currPage, Integer.valueOf(pageSize), date, suffix, area, uploader,
				keyWord, result);
		renderPage(page.getList(), "", page.getTotalRow());
	}
	
	private File getUploadFile(String uploadPath, String fileName , String newFilename, Integer index){
		File file = new File(uploadPath + File.separator + newFilename);
		if(file.exists()){
			index++;
			String suffix = fileName.substring(fileName.lastIndexOf("."));
			newFilename = fileName.substring(0, fileName.lastIndexOf(".")) +  index + suffix;
			return getUploadFile( uploadPath, fileName, newFilename, index);
		}else{
			return file;
		}
	}
	
	/**
	 * 导入资源
	 */
	public void uploadFile() {
		UploadFile file = this.getFile();
		String params = null;
		if (file != null) {
			// 1.封装resource
			Resource resource = new Resource();
			String uuid = UuidUtil.getUUID();
			resource.setRid(uuid);
			String fileName = file.getFileName();
			String uploadPath = file.getUploadPath() +"/"+ DateUtil.getDays();  //日期文件夹   
			//自定义路径
			File newfile = new File(uploadPath);
			//生成日期文件夹    
			if(!newfile.exists()) {
				newfile.mkdir();
			}
			File f = getUploadFile(uploadPath, fileName, fileName, 0);
			file.getFile().renameTo(f);	//复制到新文件夹
			file.getFile().delete();	//删除原文件
			resource.setRname(f.getName());
			resource.setUploadPath(uploadPath);
			String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
			resource.setSuffix(suffix);
			String username = getSessionAttr("username");
			SysUser user = SysUser.dao.findbyUserName(username);
			String date = DateUtil.getDay();
			resource.setCreatorId(user.getId());
			resource.setCreateTime(date);
			resource.setMenderId(user.getId());
			resource.setModifyTime(date);
			resource.save();
			
			// 1.1封装suffix
			boolean isExit = Suffix.dao.isExit(suffix);
			if (isExit) {
				// 如果不存在
				Suffix suf = new Suffix();
				suf.setSid(UuidUtil.getUUID());
				suf.setSuffix(suffix);
				suf.setCreateTime(date);
				suf.save();
			}
			params = "{\"ifSuccess\":\"success\",\"fileName\":\""+fileName+"\",\"id\":\""+uuid+"\"}";
		} else {
			params = "{\"ifSuccess\":\"fail\"}";
		}
		render(new JsonRender(params).forIE());
	}
	
	/**
	 * 下载资源
	 */
	public void downFile() {
		String rid = getPara("rid");
		Resource resource = Resource.dao.findById(rid);
		if (resource != null) {
			String path = resource.getUploadPath();
			String fileName = path.substring(path.lastIndexOf("/"), path.length())+"/"+resource.getRname();
			if(fileName.indexOf("resource")>0 || fileName.indexOf("upload")>0 ){
				fileName = resource.getRname();
			}
			//判断是否为图片
			if(ImageResizeUtil.isImgSuffix(resource.getSuffix())){
				setAttr("imgurl", PropKit.get("image.domain")+fileName);
				render("/WEB-INF/admin/resources/img.html");
			}else
				renderFile(fileName);
		}
	}

	/***************************** 资格管理----结束 *********************************************/
	private String pageTitle = "资源检索";// 模块页面标题
	private String breadHomeMethod = "getListPage";// 面包屑首页方法

	public Map<String, String> getPageTitleBread() {
		Map<String, String> pageTitleBread = new HashMap<String, String>();
		pageTitleBread.put("pageTitle", pageTitle);
		pageTitleBread.put("breadHomeMethod", breadHomeMethod);
		return pageTitleBread;
	}
}
