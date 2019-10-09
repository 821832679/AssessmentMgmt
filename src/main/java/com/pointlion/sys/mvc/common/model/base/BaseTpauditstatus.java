package com.pointlion.sys.mvc.common.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseTpauditstatus<M extends BaseTpauditstatus<M>> extends Model<M> implements IBean {

	public M setStatusCode(java.lang.Integer statusCode) {
		set("Status_Code", statusCode);
		return (M)this;
	}
	
	public java.lang.Integer getStatusCode() {
		return getInt("Status_Code");
	}

	public M setStatausName(java.lang.String statausName) {
		set("Stataus_Name", statausName);
		return (M)this;
	}
	
	public java.lang.String getStatausName() {
		return getStr("Stataus_Name");
	}

	public M setParentCode(java.lang.String parentCode) {
		set("Parent_Code", parentCode);
		return (M)this;
	}
	
	public java.lang.String getParentCode() {
		return getStr("Parent_Code");
	}

	public M setPY(java.lang.String PY) {
		set("PY", PY);
		return (M)this;
	}
	
	public java.lang.String getPY() {
		return getStr("PY");
	}

	public M setRemark(java.lang.String Remark) {
		set("Remark", Remark);
		return (M)this;
	}
	
	public java.lang.String getRemark() {
		return getStr("Remark");
	}

	public M setOrder(java.lang.String Order) {
		set("Order", Order);
		return (M)this;
	}
	
	public java.lang.String getOrder() {
		return getStr("Order");
	}

	public M setMenderId(java.lang.String menderId) {
		set("Mender_Id", menderId);
		return (M)this;
	}
	
	public java.lang.String getMenderId() {
		return getStr("Mender_Id");
	}

	public M setModiftTime(java.util.Date ModiftTime) {
		set("ModiftTime", ModiftTime);
		return (M)this;
	}
	
	public java.util.Date getModiftTime() {
		return get("ModiftTime");
	}

	public M setCreatorId(java.lang.String creatorId) {
		set("Creator_Id", creatorId);
		return (M)this;
	}
	
	public java.lang.String getCreatorId() {
		return getStr("Creator_Id");
	}

	public M setCreateTime(java.util.Date CreateTime) {
		set("CreateTime", CreateTime);
		return (M)this;
	}
	
	public java.util.Date getCreateTime() {
		return get("CreateTime");
	}

}