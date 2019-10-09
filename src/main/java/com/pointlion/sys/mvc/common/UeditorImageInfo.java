package com.pointlion.sys.mvc.common;

import java.io.Serializable;

public class UeditorImageInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String url;
	private long mtime;  //eg：1441784906
	
	public UeditorImageInfo() {
	}
	public UeditorImageInfo(String url, long mtime) {
		this.url = url;
		this.mtime = mtime;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public long getMtime() {
		return mtime;
	}
	public void setMtime(long mtime) {
		this.mtime = mtime;
	}
}
