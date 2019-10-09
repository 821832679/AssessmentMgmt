package com.pointlion.sys.mvc.admin.file;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.druid.util.Base64;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Page;
import com.pointlion.sys.interceptor.MainPageTitleInterceptor;
import com.pointlion.sys.mvc.common.UeditorImageInfo;
import com.pointlion.sys.mvc.common.UeditorImageResult;
import com.pointlion.sys.mvc.common.UeditorImageUploadResult;
import com.pointlion.sys.mvc.common.base.BaseController;
import com.pointlion.sys.mvc.common.model.Image;
import com.pointlion.sys.mvc.common.utils.FileUploadUtil;
import com.pointlion.sys.mvc.common.utils.ImageResizeUtil;
import com.pointlion.sys.mvc.common.utils.MD5Util;

import sun.misc.BASE64Decoder;

@Before(MainPageTitleInterceptor.class)
public class FileController extends BaseController {
	private static Logger logger = Logger.getLogger(FileController.class);
	
	@RequestMapping("uploadedScrawl.html")
	public void uploadedScrawl(@RequestParam String upfile, HttpServletRequest request,
			HttpServletResponse response) {
		BASE64Decoder decoder = new BASE64Decoder();
		try {
			if (upfile == null || "".equals(upfile)) {
				throw new Exception();
			}
			if (upfile.startsWith("data:image/jpeg;base64,")) {
				upfile = upfile.replace("data:image/jpeg;base64,", "");
			}
			// Base64解码
			// 涂鸦图片以base64字符串提交到这里
			byte[] bytes = decoder.decodeBuffer(upfile);
			for (int i = 0; i < bytes.length; ++i) {
				if (bytes[i] < 0) {// 调整异常数据
					bytes[i] += 256;
				}
			}
			if (bytes.length > 3145728) {
				UeditorImageUploadResult result = new UeditorImageUploadResult("ERROR", "", "图片大小超过3Mb限制", "", "", "0");
				renderSuccess(JSONObject.toJSONString(result));
				return;
			}
			String imageMd5Str = MD5Util.MD5(Base64.byteArrayToBase64(bytes));
			// 查询图片是否已经存在，如果存在则不再次上传，直接引用之前的图片
			Image image = getImageByMd5Str(imageMd5Str);
			String suffix = ".jpg";
			if (image != null) {
				File imgfile = new File(FileUploadUtil.getImageRoot(), image.getImageUrl());
				if (imgfile.exists()) {
					UeditorImageUploadResult result = new UeditorImageUploadResult("SUCCESS", suffix,
							image.getImageUrl(), image.getImageUrl(), "", "" + bytes.length);
					renderSuccess(JSONObject.toJSONString(result));
					return;
				}
			}
			int width = 600;
			int height = 2000;
			String from = request.getParameter("from");
			String waterImage = null;
			if ("editor".equals(from)) {
				waterImage = request.getSession().getServletContext().getRealPath("/res/images/image_water.png");
				width = 800;
			}

			// 生成jpeg图片
			File saveDirectory = new File(FileUploadUtil.getImageRoot(), FileUploadUtil.getDirectory());
			if (!saveDirectory.exists()) {
				saveDirectory.mkdirs();
			}
			String fileName = FileUploadUtil.getLongFileName(suffix);
			ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
			File destFile = new File(FileUploadUtil.getImageRoot(), fileName);
			if (waterImage != null) {
				ImageResizeUtil.resizeWithIcon(bis, destFile.getAbsolutePath(), width, waterImage);
			} else {
				ImageResizeUtil.resize(bis, destFile.getAbsolutePath(), width);
			}
			if (destFile.exists()) {
				saveImageInfo(fileName, fileName, imageMd5Str);
				renderSuccess(JSONObject
						.toJSONString(new UeditorImageUploadResult(suffix, fileName, fileName, "", "" + bytes.length)));
			}
		} catch (Exception e) {
			logger.error("===Scrawl image upload error!===");
			e.printStackTrace();
			renderSuccess(JSONObject.toJSONString(new UeditorImageUploadResult("ERROR", "图片上传失败")));
		}
	}

	@RequestMapping("selectImages.html")
	public void selectImages(HttpServletRequest request, HttpServletResponse response) {
		File imageRoot = new File(FileUploadUtil.getImageRoot());
		if (imageRoot.exists()) {
			File[] subDirectories = imageRoot.listFiles(new FileFilter() {
				@Override
				public boolean accept(File file) {
					if (file.isDirectory()) {
						return true;
					}
					return false;
				}
			});
			final List<UeditorImageInfo> list = new ArrayList<UeditorImageInfo>();
			// 遍历子目录的图片
			for (File dir : subDirectories) {
				final String dirname = dir.getName();
				File[] images = dir.listFiles(new FileFilter() {
					@Override
					public boolean accept(File file) {
						String filename = file.getName().toLowerCase();
						if (filename.endsWith(".jpg") || filename.endsWith(".jpeg") || filename.endsWith(".png")
								|| filename.endsWith(".gif") || filename.endsWith(".bmp")) {
							list.add(new UeditorImageInfo(dirname + "/" + filename, file.lastModified()));
							return true;
						}
						return false;
					}
				});
			}
			renderSuccess(JSONObject.toJSONString(new UeditorImageResult(list.size(), list)));
		}
	}

	@RequestMapping("uploadImage.html")
	public void uploadImage(@RequestParam MultipartFile upfile, HttpServletRequest request,
			HttpServletResponse response) {
		UeditorImageUploadResult result = null;
		String imageMd5Str = null;
		if (!upfile.isEmpty()) {
			long fileSize = upfile.getSize();
			if (fileSize > 5242880) {
				// 限制5Mb以内
				result = new UeditorImageUploadResult("图片大小超过5Mb限制", "", "图片大小超过5Mb限制", "", "", "0");
				renderSuccess(JSONObject.toJSONString(result));
				return;
			}
			// 文件名称 带后缀
			String orgFilename = upfile.getOriginalFilename();
			// 文件后缀
			String suffix = orgFilename.substring(orgFilename.lastIndexOf("."));
			// 查询图片是否已经存在，如果存在则不再次上传，直接引用之前的图片
			try {
				imageMd5Str = MD5Util.MD5(Base64.byteArrayToBase64(upfile.getBytes()));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			Image image = getImageByMd5Str(imageMd5Str);
			if (image != null) {
				File imgfile = new File(FileUploadUtil.getImageRoot(), image.getImageUrl());
				if (imgfile.exists()) {
					result = new UeditorImageUploadResult("SUCCESS", suffix, image.getImageUrl(), image.getImageUrl(),
							orgFilename, "" + fileSize);
					renderSuccess(JSONObject.toJSONString(result));
					return;
				}
			}
			int width = 600;
			// int height = 5000;
			String from = request.getParameter("from");
			String waterImage = null;

			if ("editor".equals(from)) {
				waterImage = request.getSession().getServletContext().getRealPath("/res/images/image_water.png");
				width = 800;
			}

			String maxWidthTmp = request.getParameter("maxWidth");
			if (maxWidthTmp != null) {
				width = Integer.parseInt(maxWidthTmp);
			}
			// String noscaleTmp = request.getParameter("noscale");

			// 图片保存目录
			String fileName = FileUploadUtil.getLongFileName(suffix);
			File saveDirectory = new File(FileUploadUtil.getImageRoot(), FileUploadUtil.getDirectory());
			File destFile = new File(FileUploadUtil.getImageRoot(), fileName);
			if (!saveDirectory.exists()) {
				saveDirectory.mkdirs();
			}
			try {
				if (suffix != null && suffix.equalsIgnoreCase(".gif")) {
					if (fileSize > 2097152) {
						// gif动图限定2mb，原图上传
						result = new UeditorImageUploadResult("GIF图片大小超过2Mb限制", "", "GIF图片大小超过2Mb限制", "", "", "0");
						renderSuccess(JSONObject.toJSONString(result));
						return;
					} else {
						FileUtils.writeByteArrayToFile(destFile, upfile.getBytes());
					}
				} else {
					// 判断图片压缩 compress 为空则不压缩
					String compress = request.getParameter("compress");
					// 判断图片水印
					String shui = request.getParameter("shui");
					Enumeration enu = request.getParameterNames();
					while (enu.hasMoreElements()) {
						String paraName = (String) enu.nextElement();
						System.out.println(paraName + ": " + request.getParameter(paraName));
					}
					if (shui != null) {
						waterImage = null;
					}
					if (compress == null) {
						// 判断图片尺寸
						/*
						 * if(waterImage != null){
						 * ImageResizeUtil.resizeWithIcon(upfile.getInputStream(
						 * ), destFile.getAbsolutePath(), width, waterImage);
						 * }else{
						 * ImageResizeUtil.resize(upfile.getInputStream(),
						 * destFile.getAbsolutePath(), width); }
						 */
						ImageResizeUtil.resize(upfile.getInputStream(), destFile.getAbsolutePath(), width);
					} else {
						byte[] data = readInputStream(upfile.getInputStream());
						// 创建输出流
						FileOutputStream outStream = new FileOutputStream(destFile);
						// 写入数据
						outStream.write(data);
						// 关闭输出流
						outStream.close();
					}
				}

				// 保存图片信息
				if (destFile.exists()) {
					saveImageInfo(orgFilename, fileName, imageMd5Str);
					result = new UeditorImageUploadResult("SUCCESS", suffix, fileName, fileName, orgFilename,
							"" + fileSize);
				} else {
					result = new UeditorImageUploadResult("ERROR", "", "图片上传失败", "", "", "0");
				}
			} catch (IOException e) {
				result = new UeditorImageUploadResult("ERROR", "", "图片上传失败", "", "", "0");
			} catch (Exception e) {
				e.printStackTrace();
				result = new UeditorImageUploadResult("ERROR", "", "图片上传失败", "", "", "0");
			} finally {

			}
			renderSuccess(JSONObject.toJSONString(result));
		} else {

		}
	}

	public byte[] readInputStream(InputStream inStream) throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		// 创建一个Buffer字符串
		byte[] buffer = new byte[1024];
		// 每次读取的字符串长度，如果为-1，代表全部读取完毕
		int len = 0;
		// 使用一个输入流从buffer里把数据读取出来
		while ((len = inStream.read(buffer)) != -1) {
			// 用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
			outStream.write(buffer, 0, len);
		}
		// 关闭输入流
		inStream.close();
		// 把outStream里的数据写入内存
		return outStream.toByteArray();
	}
	
	/**
	 * 上传文件
	 * 
	 * @param upfile
	 * @param request
	 * @param response
	 */
	@RequestMapping("uploadfile.html")
	public void uploadfile(@RequestParam MultipartFile upfile, HttpServletRequest request,
			HttpServletResponse response) {
		UeditorImageUploadResult result = null;
		String imageMd5Str = null;

		// 文件名称 带后缀
		String orgFilename = upfile.getOriginalFilename();

		// 文件大小
		long fileSize = upfile.getSize();

		// 文件后缀
		String suffix = orgFilename.substring(orgFilename.lastIndexOf("."));

		// 文件是否已经存在，如果存在则不再次上传，直接引用之前的文件
		try {
			imageMd5Str = MD5Util.MD5(Base64.byteArrayToBase64(upfile.getBytes()));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		Image image = getImageByMd5Str(imageMd5Str);
		if (image != null) {
			File imgfile = new File(FileUploadUtil.getImageRoot(), image.getImageUrl());
			if (imgfile.exists()) {
				result = new UeditorImageUploadResult("SUCCESS", suffix, image.getImageUrl(), image.getImageUrl(),
						orgFilename, "" + fileSize);
				renderSuccess(JSONObject.toJSONString(result));
				return;
			}
		}

		// 文件保存
		String fileName = FileUploadUtil.getLongFileName(suffix);
		File saveDirectory = new File(FileUploadUtil.getFileRoot(), FileUploadUtil.getDirectory());
		File destFile = new File(FileUploadUtil.getFileRoot(), fileName);
		if (!saveDirectory.exists()) {
			saveDirectory.mkdirs();
		}
		// 文件保存目录

		InputStream in = null;
		FileOutputStream out = null;
		try {
			if (fileSize > 5242880) {
				// 限制300Mb以内
				result = new UeditorImageUploadResult("ERROR", "", "文件大小超过5Mb限制", "", "", "0");
			} else {
				in = upfile.getInputStream();

				out = new FileOutputStream(destFile);
				byte[] buffer = new byte[4096];
				int bytes_read;
				while ((bytes_read = in.read(buffer)) != -1) {
					out.write(buffer, 0, bytes_read);
				}

				saveImageInfo(orgFilename, fileName, imageMd5Str);
				result = new UeditorImageUploadResult("SUCCESS", suffix, orgFilename, fileName, orgFilename,
						"" + fileSize);
			}
			renderSuccess(JSONObject.toJSONString(result));
		} catch (IOException e1) {
			e1.printStackTrace();
			result = new UeditorImageUploadResult("ERROR", suffix, "文件上传失败", fileName, "", "");
			renderSuccess(JSONObject.toJSONString(result));
		} finally {
			if (in != null)
				try {
					in.close();
				} catch (IOException e) {
					;
				}
			if (out != null)
				try {
					out.close();
				} catch (IOException e) {
					;
				}
		}
	}

	private void saveImageInfo(String originalName, String newName, String md5str) {
		Image image = getImageByMd5Str(md5str);
		if (image == null) {
			image = new Image();
			image.setName(originalName);
			image.setImageUrl(newName);
			image.setMd5str(md5str);
			image.save();
		} else {
			image.setName(originalName);
			image.setImageUrl(newName);
			image.update();
		}
	}

	private Image getImageByMd5Str(String imageMd5Str) {
		Page<Image> image = Image.dao.getImagePage(imageMd5Str);
		if (image.getList().size() > 0) {
			return image.getList().get(0);
		}
		return null;
	}
	
	/**************************************************************************/
	private String pageTitle = "文件上传";// 模块页面标题
	private String breadHomeMethod = "getListPage";// 面包屑首页方法

	public Map<String, String> getPageTitleBread() {
		Map<String, String> pageTitleBread = new HashMap<String, String>();
		pageTitleBread.put("pageTitle", pageTitle);
		pageTitleBread.put("breadHomeMethod", breadHomeMethod);
		return pageTitleBread;
	}
}