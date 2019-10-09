package com.pointlion.sys.mvc.common.utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.UUID;

import com.jfinal.kit.PropKit;

public class FileUploadUtil{
	
	private static FileUploadUtil instance;
	
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	
	private static String imageRoot;
	
	private static String fileRoot;
	
	private FileUploadUtil(){
		
	}
	
	public static FileUploadUtil getInstance(){
		if(instance == null ){
			instance = new FileUploadUtil();
		}
		return instance;
	}
	
	static{
		imageRoot = PropKit.get("image.root.path");//GlobalProperties.getProperty("image.root.path");
		File file = new File(imageRoot);
		if(!file.exists()){
			file.mkdirs();
		}
		
		fileRoot = PropKit.get("file.root.path");//GlobalProperties.getProperty("file.root.path");
		file = new File(fileRoot);
		if(!file.exists()){
			file.mkdirs();
		}
	}
	
	public static String getLongFileName(String suffix){
		//return getDirectory() + File.separator + UUID.randomUUID().toString() + suffix;
		return getDirectory() + "/" + UUID.randomUUID().toString() + suffix;
	}
	
	public static String getFileName(String suffix){
		return UUID.randomUUID().toString() + suffix;
	}
	
	public static String getDirectory(){
		return sdf.format(DateUtils.getDate());
	}
	
	public static String getImageRoot(){
		return imageRoot;
	}
	
	public static String getFileRoot() {
		return fileRoot;
	}
}
