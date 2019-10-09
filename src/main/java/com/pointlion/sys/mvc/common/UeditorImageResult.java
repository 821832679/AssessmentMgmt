package com.pointlion.sys.mvc.common;

import java.util.List;
/**
 * Ueditor选择在线照片的返回结果
 * @author John.liu
 * @date 2015-9-30
 * @description
 */
public class UeditorImageResult {
	private String state = "SUCCESS";
	private long start = 0;
	private long total = 0;
	private List<UeditorImageInfo> list;
	
	public UeditorImageResult() {
	}
	public UeditorImageResult(String state, long total,
			List<UeditorImageInfo> list) {
		this.state = state;
		this.total = total;
		this.list = list;
	}
	public UeditorImageResult(long total,
			List<UeditorImageInfo> list) {
		this.total = total;
		this.list = list;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public long getStart() {
		return start;
	}
	public void setStart(long start) {
		this.start = start;
	}
	public long getTotal() {
		return total;
	}
	public void setTotal(long total) {
		this.total = total;
	}
	public List<UeditorImageInfo> getList() {
		return list;
	}
	public void setList(List<UeditorImageInfo> list) {
		this.list = list;
	}
}
