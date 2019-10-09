package com.pointlion.sys.mvc.common.model;
/**
 * 自定义的json对象
 * @author Snake
 *
 */
public class CustomJson {

	private String label;  //显示名称
	private String fieldName;  //字段名
	private String type;	//类型
	private String defaultValue; //默认值
	private String readOnly; //是否可读
	//private String value; //前端指令项
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	public String getReadOnly() {
		return readOnly;
	}
	public void setReadOnly(String readOnly) {
		this.readOnly = readOnly;
	}
	/*public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}*/
	
	
	
}
