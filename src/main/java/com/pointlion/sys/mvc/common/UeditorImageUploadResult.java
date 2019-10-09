package com.pointlion.sys.mvc.common;

import java.io.Serializable;
/**
 * Ueditor上传图片的返回结果
 * @author John.liu
 * @date 2015-9-30
 * @description
 */
public class UeditorImageUploadResult implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String state = "SUCCESS";
	private String error;
	private String type;
	private String name;
	private String url;
	private String original;
	private String size;
	public UeditorImageUploadResult() {
	}
	
	public UeditorImageUploadResult(String state, String error) {
		this.state = state;
		this.error = error;
	}

	public UeditorImageUploadResult(String state, String type, String name,
			String url, String original, String size) {
		this.state = state;
		this.type = type;
		this.name = name;
		this.url = url;
		this.original = original;
		this.size = size;
	}
	public UeditorImageUploadResult(String type, String name,
			String url, String original, String size) {
		this.type = type;
		this.name = name;
		this.url = url;
		this.original = original;
		this.size = size;
	}
	
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getOriginal() {
		return original;
	}
	public void setOriginal(String original) {
		this.original = original;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
}
