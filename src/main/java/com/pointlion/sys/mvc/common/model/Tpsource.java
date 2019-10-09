package com.pointlion.sys.mvc.common.model;



import com.jfinal.aop.Before;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.pointlion.sys.mvc.common.model.base.BaseTpsource;

/**
 * Generated by JFinal.
 */
@SuppressWarnings("serial")
public class Tpsource extends BaseTpsource<Tpsource> {
	
	public static final Tpsource dao = new Tpsource();
	/***
	 * 根据主键查询
	 */
	public Tpsource getById(String id){
		return Tpsource.dao.findById(id);
	}
	/***
	 * 获取分页
	 */
	public Page<Tpsource> getPage(Integer curr , Integer pagesize ,String Educationaltype,String Professional,String Searcht,String firsttype,String secondtype,String thirdtype){
		if(Searcht==""){	
		if(Educationaltype==""&&Professional==""&&firsttype=="")
		{return Tpsource.dao.paginate(curr, pagesize, "select u.*,o.Value Value1,p.Value Value2", " from t_tpsource u LEFT JOIN t_tpclassify o on  u.TPC_Code=o.TPC_Code LEFT JOIN t_tpqualifications p on  u.TPQ_Code=p.TPQ_Code  ");};
		if(Educationaltype==""&&Professional!=""&&firsttype=="")
		{return Tpsource.dao.paginate(curr, pagesize, "select u.*,o.Value Value1,p.Value Value2", " from t_tpsource u LEFT JOIN t_tpclassify o on  u.TPC_Code=o.TPC_Code LEFT JOIN t_tpqualifications p on  u.TPQ_Code=p.TPQ_Code   where  u.TPC_Code='"+Professional+"'");};
		if(Educationaltype==""&&Professional==""&&firsttype!="")
		{   
			if(secondtype=="")
			{return Tpsource.dao.paginate(curr, pagesize, "select u.*,o.Value Value1,p.Value Value2", " from t_tpsource u LEFT JOIN t_tpclassify o on  u.TPC_Code=o.TPC_Code LEFT JOIN t_tpqualifications p on  u.TPQ_Code=p.TPQ_Code");}
		    else{
		    	if(thirdtype=="")
		    	{return Tpsource.dao.paginate(curr, pagesize, "select u.*,o.Value Value1,p.Value Value2", " from t_tpsource u LEFT JOIN t_tpclassify o on  u.TPC_Code=o.TPC_Code LEFT JOIN t_tpqualifications p on  u.TPQ_Code=p.TPQ_Code where u.municipallevel='"+secondtype+"'");}
		    	else
		    	{return Tpsource.dao.paginate(curr, pagesize, "select u.*,o.Value Value1,p.Value Value2", " from t_tpsource u LEFT JOIN t_tpclassify o on  u.TPC_Code=o.TPC_Code LEFT JOIN t_tpqualifications p on  u.TPQ_Code=p.TPQ_Code where u.municipallevel='"+secondtype+"' and  u.countylevel='"+thirdtype+"'");}	
		    }
		}
		if(Educationaltype==""&&Professional!=""&&firsttype!="")
		{   
			if(secondtype=="")
			{return Tpsource.dao.paginate(curr, pagesize, "select u.*,o.Value Value1,p.Value Value2", " from t_tpsource u LEFT JOIN t_tpclassify o on  u.TPC_Code=o.TPC_Code LEFT JOIN t_tpqualifications p on  u.TPQ_Code=p.TPQ_Code where u.TPC_Code='"+Professional+"'");}
		    else{
		    	if(thirdtype=="")
		    	{return Tpsource.dao.paginate(curr, pagesize, "select u.*,o.Value Value1,p.Value Value2", " from t_tpsource u LEFT JOIN t_tpclassify o on  u.TPC_Code=o.TPC_Code LEFT JOIN t_tpqualifications p on  u.TPQ_Code=p.TPQ_Code where u.municipallevel='"+secondtype+"' and u.TPC_Code='"+Professional+"'");}
		    	else
		    	{return Tpsource.dao.paginate(curr, pagesize, "select u.*,o.Value Value1,p.Value Value2", " from t_tpsource u LEFT JOIN t_tpclassify o on  u.TPC_Code=o.TPC_Code LEFT JOIN t_tpqualifications p on  u.TPQ_Code=p.TPQ_Code where u.municipallevel='"+secondtype+"' and  u.countylevel='"+thirdtype+"' and u.TPC_Code='"+Professional+"'");}	
		    }
		}
		if(Educationaltype!=""&&Professional!=""&&firsttype!="")
		{   
			if(secondtype=="")
			{return Tpsource.dao.paginate(curr, pagesize, "select u.*,o.Value Value1,p.Value Value2", " from t_tpsource u LEFT JOIN t_tpclassify o on  u.TPC_Code=o.TPC_Code LEFT JOIN t_tpqualifications p on  u.TPQ_Code=p.TPQ_Code where u.TPC_Code='"+Professional+"' and u.TPQ_Code='"+Educationaltype+"'");}
		    else{
		    	if(thirdtype=="")
		    	{return Tpsource.dao.paginate(curr, pagesize, "select u.*,o.Value Value1,p.Value Value2", " from t_tpsource u LEFT JOIN t_tpclassify o on  u.TPC_Code=o.TPC_Code LEFT JOIN t_tpqualifications p on  u.TPQ_Code=p.TPQ_Code where u.municipallevel='"+secondtype+"' and u.TPC_Code='"+Professional+"' and u.TPQ_Code='"+Educationaltype+"'");}
		    	else
		    	{return Tpsource.dao.paginate(curr, pagesize, "select u.*,o.Value Value1,p.Value Value2", " from t_tpsource u LEFT JOIN t_tpclassify o on  u.TPC_Code=o.TPC_Code LEFT JOIN t_tpqualifications p on  u.TPQ_Code=p.TPQ_Code where u.municipallevel='"+secondtype+"' and  u.countylevel='"+thirdtype+"' and u.TPC_Code='"+Professional+"' and u.TPQ_Code='"+Educationaltype+"'");}	
		        }
		}
		if(Educationaltype!=""&&Professional==""&&firsttype!="")
		{   
			if(secondtype=="")
			{return Tpsource.dao.paginate(curr, pagesize, "select u.*,o.Value Value1,p.Value Value2", " from t_tpsource u LEFT JOIN t_tpclassify o on  u.TPC_Code=o.TPC_Code LEFT JOIN t_tpqualifications p on  u.TPQ_Code=p.TPQ_Code where u.TPQ_Code='"+Educationaltype+"'");}
		    else{
		    	if(thirdtype=="")
		    	{return Tpsource.dao.paginate(curr, pagesize, "select u.*,o.Value Value1,p.Value Value2", " from t_tpsource u LEFT JOIN t_tpclassify o on  u.TPC_Code=o.TPC_Code LEFT JOIN t_tpqualifications p on  u.TPQ_Code=p.TPQ_Code where u.municipallevel='"+secondtype+"'  and u.TPQ_Code='"+Educationaltype+"'");}
		    	else
		    	{return Tpsource.dao.paginate(curr, pagesize, "select u.*,o.Value Value1,p.Value Value2", " from t_tpsource u LEFT JOIN t_tpclassify o on  u.TPC_Code=o.TPC_Code LEFT JOIN t_tpqualifications p on  u.TPQ_Code=p.TPQ_Code where u.municipallevel='"+secondtype+"' and  u.countylevel='"+thirdtype+"' and u.TPQ_Code='"+Educationaltype+"'");}	
		    }
		}
		if(Educationaltype!=""&&Professional!=""&&firsttype=="")
		{     return Tpsource.dao.paginate(curr, pagesize, "select u.*,o.Value Value1,p.Value Value2", " from t_tpsource u LEFT JOIN t_tpclassify o on  u.TPC_Code=o.TPC_Code LEFT JOIN t_tpqualifications p on  u.TPQ_Code=p.TPQ_Code   where u.TPQ_Code='"+Educationaltype+"'and u.TPC_Code='"+Professional+"'");}
		if(Educationaltype!=""&&Professional==""&&firsttype=="")
		{return Tpsource.dao.paginate(curr, pagesize, "select u.*,o.Value Value1,p.Value Value2", " from t_tpsource u LEFT JOIN t_tpclassify o on  u.TPC_Code=o.TPC_Code LEFT JOIN t_tpqualifications p on  u.TPQ_Code=p.TPQ_Code  where u.TPQ_Code='"+Educationaltype+"'");};
	}		
	  else{	return Tpsource.dao.paginate(curr, pagesize, "select u.*,o.Value Value1,p.Value Value2", " from t_tpsource u LEFT JOIN t_tpclassify o on  u.TPC_Code=o.TPC_Code LEFT JOIN t_tpqualifications p on  u.TPQ_Code=p.TPQ_Code  where CONCAT(u.TPQ_Code,u.TPC_Code,TP_Id,TPName,TPP_Code,Tel,Sex,Age,TP_Add) like concat('%', '"+Searcht+"','%') "); }
       return Tpsource.dao.paginate(curr, pagesize, "select u.*,o.Value Value1,p.Value Value2", " from t_tpsource u LEFT JOIN t_tpclassify o on  u.TPC_Code=o.TPC_Code LEFT JOIN t_tpqualifications p on  u.TPQ_Code=p.TPQ_Code   where u.TPQ_Code='"+Educationaltype+"' and u.TPC_Code='"+Professional+"'");
	}
	
	/**
	 * 查询人才记录
	 * @return
	 */
	public Integer count() {
		Integer count = Db.queryInt("select count(*) from t_tpsource");
		return count;
	}

	/**
	 * 查询是否已存在
	 * @param tpId
	 * @return
	 */
	public boolean isExit(String tpId) {
		Integer i = Db.queryInt("select count(*) from t_tpsource where TP_Id = ?", tpId);
		if(i!=0){
			return true;
		}
		else{
			return false;
		}
		
	}
	
}