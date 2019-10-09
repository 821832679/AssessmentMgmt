package com.pointlion.sys.mvc.common.model.base;

import com.jfinal.plugin.activerecord.IBean;
import com.jfinal.plugin.activerecord.Model;

@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseFileConfig<M extends BaseFileConfig<M>> extends Model<M> implements IBean {

	public java.lang.String getId() {
		return getStr("id");
	}
	
	public M setId(java.lang.String id) {
		set("id", id);
		return (M) this;
	}
	
	public java.lang.String getTopicId() {
		return getStr("topicid");
	}
	
	public M setTopicId(java.lang.String topicid) {
		set("topicid", topicid);
		return (M) this;
	}
	
	public java.lang.String getQuestionId() {
		return getStr("questionid");
	}
	
	public M setQuestionId(java.lang.String questionid) {
		set("questionid", questionid);
		return (M) this;
	}
	
	public java.lang.String getIstype() {
		return getStr("istype");
	}
	
	public M setIstype(java.lang.String istype) {
		set("istype", istype);
		return (M) this;
	}
	
	public java.lang.Integer getDirection() {
		return getInt("direction");
	}
	
	public M setDirection(java.lang.Integer direction) {
		set("direction", direction);
		return (M) this;
	}
	
	public java.lang.Integer getBegin() {
		return getInt("begin");
	}
	
	public M setBegin(java.lang.Integer begin) {
		set("begin", begin);
		return (M) this;
	}
	
	public java.lang.Integer getRownum() {
		return getInt("rownum");
	}
	
	public M setRownum(java.lang.Integer rownum) {
		set("rownum", rownum);
		return (M) this;
	}
	
	public java.lang.Integer getEnd() {
		return getInt("end");
	}
	
	public M setEnd(java.lang.Integer end) {
		set("end", end);
		return (M) this;
	}
	
}
