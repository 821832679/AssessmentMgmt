/**
 * @author Lion
 * @date 2017年1月24日 下午12:02:35
 * @qq 439635374
 */
package com.pointlion.sys.mvc.admin.upload;

import java.util.HashMap;
import java.util.Map;

import com.jfinal.upload.UploadFile;
import com.pointlion.sys.mvc.common.base.BaseController;

/***
 * 通知公告控制器（web）
 * @author Administrator
 *
 */
public class UploadController extends BaseController {
	
	/***
	 * 合作伙伴图片上传
	 */
	public void uploadBrandPic(){
		UploadFile file = getFile("file","/brand");
		Map<String,String> data = new HashMap<String , String>();
		data.put("filename", file.getFileName());
		data.put("path", "/brand");
		renderSuccess(data,"上传成功");
	}
	
	/***
	 * 轮播图片上传
	 */
	public void uploadLoopPic(){
		UploadFile file = getFile("file","/loop");
		Map<String,String> data = new HashMap<String , String>();
		data.put("filename", file.getFileName());
		data.put("path", "/loop");
		renderSuccess(data,"上传成功");
	}
	
	/***
	 * 人物头像图片上传
	 */
	public void uploadArticlePic(){
		UploadFile file = getFile("file","/avatars");
		Map<String,String> data = new HashMap<String , String>();
		data.put("filename", file.getFileName());
		data.put("path", "/avatars");
		renderSuccess(data,"上传成功");
	}
	
	/***
	 * 文章图片上传
	 */
	/*public void uploadArticlePic(){
		UploadFile file = getFile("file","/article");
		Map<String,String> data = new HashMap<String , String>();
		data.put("filename", file.getFileName());
		data.put("path", "/article");
		renderSuccess(data,"上传成功");
	}*/
	/***
	 * 慈善企业图片上传
	 */
	public void uploadChaentPic(){
		UploadFile file = getFile("file","/chaent");
		Map<String,String> data = new HashMap<String , String>();
		data.put("filename", file.getFileName());
		data.put("path", "/chaent");
		renderSuccess(data,"上传成功");
	}
	/***
	 * 慈善人物图片上传
	 */
	public void uploadCharitorPic(){
		UploadFile file = getFile("file","/charitor");
		Map<String,String> data = new HashMap<String , String>();
		data.put("filename", file.getFileName());
		data.put("path", "/charitor");
		renderSuccess(data,"上传成功");
	}
	/***
	 * 项目图片上传
	 */
	public void uploadProjectPic(){
		UploadFile file = getFile("file","/project");
		Map<String,String> data = new HashMap<String , String>();
		data.put("filename", file.getFileName());
		data.put("path", "/project");
		renderSuccess(data,"上传成功");
	}
	/***
	 * 信息公开的报告图片上传
	 */
	public void uploadReportPic(){
		UploadFile file = getFile("file","/report");
		Map<String,String> data = new HashMap<String , String>();
		data.put("filename", file.getFileName());
		data.put("path", "/report");
		renderSuccess(data,"上传成功");
	}
	/***
	 * 信息公开的报告图片上传
	 */
	public void uploadRulesPic(){
		UploadFile file = getFile("file","/rules");
		Map<String,String> data = new HashMap<String , String>();
		data.put("filename", file.getFileName());
		data.put("path", "/rules");
		renderSuccess(data,"上传成功");
	}
	
}
