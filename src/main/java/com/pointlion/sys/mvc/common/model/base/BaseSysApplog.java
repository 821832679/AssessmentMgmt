package com.pointlion.sys.mvc.common.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseSysApplog<M extends BaseSysApplog<M>> extends Model<M> implements IBean {

	public M setApplogId(java.lang.String id) {
		set("id", id);
		return (M)this;
	}
	
	public java.lang.String getApplogId() {
		return getStr("id");
	}

	public M setUSERNAME(java.lang.String USERNAME) {
		set("USERNAME", USERNAME);
		return (M)this;
	}
	
	public java.lang.String getUSERNAME() {
		return getStr("USERNAME");
	}

	public M setCONTENT(java.lang.String CONTENT) {
		set("CONTENT", CONTENT);
		return (M)this;
	}
	
	public java.lang.String getCONTENT() {
		return getStr("CONTENT");
	}

	public M setSourceIp(java.lang.String sourceIp) {
		set("Source_IP", sourceIp);
		return (M)this;
	}
	
	public java.lang.String getSourceIp() {
		return getStr("Source_IP");
	}

	public M setDestIp(java.lang.String destIp) {
		set("Dest_IP", destIp);
		return (M)this;
	}
	
	public java.lang.String getDestIp() {
		return getStr("Dest_IP");
	}

	public M setCTIME(java.lang.String CTIME) {
		set("CTIME", CTIME);
		return (M)this;
	}
	
	public java.lang.String getCTIME() {
		return getStr("CTIME");
	}

}
