package com.pointlion.sys.mvc.admin.topicdelay;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.aop.Before;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;
import com.pointlion.sys.interceptor.MainPageTitleInterceptor;
import com.pointlion.sys.mvc.common.base.BaseController;
import com.pointlion.sys.mvc.common.model.SysOrg;
import com.pointlion.sys.mvc.common.model.SysRole;
import com.pointlion.sys.mvc.common.model.SysUser;
import com.pointlion.sys.mvc.common.model.Topic;
import com.pointlion.sys.mvc.common.model.TopicDelay;
import com.pointlion.sys.mvc.common.utils.DateUtils;
import com.pointlion.sys.mvc.common.utils.UuidUtil;

@Before(MainPageTitleInterceptor.class)
public class TopicDelayManagementController extends BaseController {


	/**
	 * 获得指标列表分类页面
	 */
	public void getListPage() {
		List<SysRole> srList= SysRole.dao.getlist();
		setAttr("srList", srList);
		List<Topic> tlist = Topic.dao.getlist();
		setAttr("topicList", tlist);
		render("/WEB-INF/admin/topicdelay/list.html");
	}

	/**
	 * 获得指标列表分类数据
	 * @throws UnsupportedEncodingException 
	 */
	public void listData() throws UnsupportedEncodingException {
		String curr = getPara("pageNumber");
    	Float tempCurr = Float.valueOf(curr);
		Integer currPage =tempCurr.intValue();
    	String pageSize = getPara("pageSize");
    	String Roletype1 = getPara("RoleValue");
		String Searcht1 = getPara("SearchValue");
		String topicid = getPara("topicid");
		String orgName = getPara("orgName");
		String Roletype=Roletype1;
		String Searcht=Searcht1;
		if(Roletype1!="")
		{	 Roletype=URLDecoder.decode(Roletype1, "UTF-8");}
		if(Searcht1!="")
		{	 Searcht=URLDecoder.decode(Searcht1, "UTF-8");}
		if(orgName!="")
		{	 orgName=URLDecoder.decode(orgName, "UTF-8");}
		
		SysOrg org = SysOrg.dao.findByOrgName(orgName);
		String code ="";  //机构代码
		
		if(org!=null){
			//市级
			if(org.getType()!=null){
				if(org.getType().equals("1")){
					code = org.getAreaCode().toString().substring(0, 4);
				}
				//区级
				if(org.getType().equals("2")){
					code = org.getAreaCode().toString().substring(0, 6);
				}
				//镇，乡，街道级
				if(org.getType().equals("3")){
					code = org.getAreaCode().toString().substring(0, 9);
				}
				//村级
				if(org.getType().equals("4")){
					code = org.getAreaCode().toString();
				}
			}
		}
		Map<String, TopicDelay> delays = TopicDelay.dao.getDelayToMap(null);
    	Page<SysUser> page = SysUser.dao.findListPage(currPage,Integer.valueOf(pageSize),Roletype,Searcht,code);
    	List<SysUser> list = new ArrayList<>();
		if (page != null) {
			for (int i = 0; i < page.getList().size(); i++) {
				SysUser user = page.getList().get(i);
				user.setPassword("");
				user.put("yqendtime", "-");
				user.put("stat", "-");
				if(topicid!=null && delays.get(topicid+"-"+user.getId())!=null){
					TopicDelay delay = delays.get(topicid+"-"+user.getId());
					user.put("yqendtime", DateUtils.convert2YMdhms(DateUtils.convert2YMdhmsTime(delay.getYqendTime())));
					if(delay.getStat()==1){
						user.put("stat", "考核延期中");
					}
					if(delay.getStat()==2){
						user.put("stat", "已超过延期截止时间");
					}
					user.put("delayid", delay.getId());
				}
				list.add(user);
			}
		}
		renderPage(list, "", page.getTotalRow());
	}
	
	/***
	 * 给用户延期
	 */
	public void getGiveUserDelay(){
		String userids = getPara("userids");
		String delayid = getPara("delayid");
		setAttr("userids", userids);
		List<Topic> tlist = Topic.dao.getlist();
		setAttr("topicList", tlist);
		if(StrKit.notBlank(delayid)){
			TopicDelay delay = TopicDelay.dao.findById(delayid);
			setAttr("d", delay);
		}
		render("/WEB-INF/admin/topicdelay/giveuserdelay.html");
	}
	
	public void save(){
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String[] uids = getPara("userids").split(",");
		TopicDelay delay = getModel(TopicDelay.class);
		if(StrKit.isBlank(delay.getYqendTime())){
			renderError("请设置延期截止时间");
			return;
		}
		if (StrKit.notBlank(delay.getId())) {
			delay.update();
		} else {
			for (String uid : uids) {
				delay.setId(UuidUtil.getUUID());
				delay.setCreateTime(sf.format(new Date()));
				delay.setUserId(uid);
				delay.save();
			}
		}
		renderSuccess();
	}
	
	/************************ 数据库管理---结束 *************************************************/

	/**************************************************************************/
	private String pageTitle = "考核延期列表";// 模块页面标题
	private String breadHomeMethod = "getListPage";// 面包屑首页方法

	public Map<String, String> getPageTitleBread() {
		Map<String, String> pageTitleBread = new HashMap<String, String>();
		pageTitleBread.put("pageTitle", pageTitle);
		pageTitleBread.put("breadHomeMethod", breadHomeMethod);
		return pageTitleBread;
	}
}