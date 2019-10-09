/**
 * @date 2017年1月24日 下午12:02:35
 * @qq 439635374
 */
package com.pointlion.sys.mvc.admin.psourcemanage;


import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Page;
import com.pointlion.sys.interceptor.MainPageTitleInterceptor;
import com.pointlion.sys.mvc.common.base.BaseController;
import com.pointlion.sys.mvc.common.dto.ZtreeNode;
import com.pointlion.sys.mvc.common.model.SysMenu;
import com.pointlion.sys.mvc.common.model.SysOrg;
import com.pointlion.sys.mvc.common.model.Tpclassify;
import com.pointlion.sys.mvc.common.model.Tplocalization;
import com.pointlion.sys.mvc.common.model.Tporganization;
import com.pointlion.sys.mvc.common.model.Tppolitical;
import com.pointlion.sys.mvc.common.model.Tpqualifications;
import com.pointlion.sys.mvc.common.model.Tpsource;
import com.pointlion.sys.mvc.common.utils.DateUtil;

import java.util.List;

/**
 * 人才资源管理
 */
@Before(MainPageTitleInterceptor.class)
public class psourcemanageController extends BaseController {
	
	/***************************人才管理---开始***********************/
	
	/***
	 * 获取人才资源管理页面
	 */
	public void getListPage() {
		List<Tpqualifications> tpqList  = Tpqualifications.dao.getlist();
		setAttr("tpqList", tpqList);
		List<Tpclassify> tpcList= Tpclassify.dao.getlist();
		setAttr("tpcList", tpcList);
		List<Tplocalization> tplList = Tplocalization.dao.findTop();
		setAttr("tplList", tplList);
		render("/WEB-INF/admin/psourcemanage/list.html");
	}
	
	/***
	 * 获取分页数据
	 * @throws UnsupportedEncodingException 
	 **/
	public void ListData() throws UnsupportedEncodingException {
		
		String curr = getPara("pageNumber");
		Float tempCurr = Float.valueOf(curr);
		Integer currPage =tempCurr.intValue();
		String pageSize = getPara("pageSize");
		String Educationaltype1 = getPara("EducationalValue");
		String Professional1 = getPara("ProfessionalValue");
		String Searcht1 = getPara("SearchValue");
		String firsttype1 = getPara("firstValue");
		String secondtype1 = getPara("secondValue");
		String thirdtype1 = getPara("thirdValue");
		String Educationaltype=Educationaltype1;
		String Professional=Professional1;
		String Searcht=Searcht1;
		String firsttype=firsttype1;
		String secondtype=secondtype1;
		String thirdtype=thirdtype1;
		if(Educationaltype1!="")  //解决字符乱码问题。
		{	 Educationaltype=URLDecoder.decode(Educationaltype1, "UTF-8");}
		if(Professional1!="")
		{	 Professional=URLDecoder.decode(Professional1, "UTF-8");}
		if(Searcht1!="")
		{	 Searcht=URLDecoder.decode(Searcht1, "UTF-8");}
		if(firsttype1!="")
		{	 firsttype=URLDecoder.decode(firsttype1, "UTF-8");}
		if(secondtype1!="")
		{	 secondtype=URLDecoder.decode(secondtype1, "UTF-8");}
		if(thirdtype1!="")
		{	 thirdtype=URLDecoder.decode(thirdtype1, "UTF-8");}
		Page<Tpsource> page = Tpsource.dao.getPage(currPage, Integer.valueOf(pageSize),Educationaltype,Professional,Searcht,firsttype,secondtype,thirdtype);
		renderPage(page.getList(),"" ,page.getTotalRow());
	}
	
	
	/**
	 * 新增人才信息页面
	 */
    
    public void getTmEditPage(){
		
		//查询分类表
		List<Tpclassify> tpcList =  Tpclassify.dao.getlist();
		//查询学历表
		List<Tpqualifications> tqpList = Tpqualifications.dao.getlist();
		//查询政治面貌表
		List<Tppolitical> tppList = Tppolitical.dao.getList();
		//查询父机构
		List<Tporganization> tpoList = Tporganization.dao.findTop();
		List<Tplocalization> tplList = Tplocalization.dao.findTop();
		setAttr("tpcList", tpcList);
		setAttr("tqpList", tqpList);
		setAttr("tppList", tppList);
		setAttr("tpoList", tpoList);
		setAttr("tplList", tplList);
		render("/WEB-INF/admin/psourcemanage/edit.html");
    }
	
    
    /**
     *编辑人才信息
     */
    public void edit(){
    	//获得编号id
    	String id = getPara("id");
    	Tpsource t =  Tpsource.dao.findById(id);
		// 查询分类表
		List<Tpclassify> tpcList = Tpclassify.dao.getlist();
		// 查询学历表
		List<Tpqualifications> tqpList = Tpqualifications.dao.getlist();
		// 查询政治面貌表
		List<Tppolitical> tppList = Tppolitical.dao.getList();
		//查询父机构
		List<Tporganization> tpoList = Tporganization.dao.findTop();
		List<Tplocalization> tplList = Tplocalization.dao.findTop();
		setAttr("tpcList", tpcList);
		setAttr("tqpList", tqpList);
		setAttr("tppList", tppList);
		setAttr("tpoList", tpoList);
		setAttr("tplList", tplList);
    	setAttr("t", t);
    	render("/WEB-INF/admin/psourcemanage/edit.html");
    }
    
    /**
     * 获得机构树页面
     */
    
    public void getGiveOrgPage(){
    	render("/WEB-INF/admin/psourcemanage/giveOrg.html");
    }
    
    /**
     * 获得机构树数据
     */
    
    public void getAllTopOrgTree(){
    	List<Tporganization> topOrgList = Tporganization.dao.getAllTopOrg();
    	List<ZtreeNode> nodelist = Tporganization.dao.toZTreeNode(topOrgList,true);//数据库中的菜单
    	List<ZtreeNode> rootList = new ArrayList<ZtreeNode>();//页面展示的,带根节点
    	//声明根节点
    	ZtreeNode root = new ZtreeNode();
    	root.setId("#root");
    	root.setName("湖南省");
    	root.setChildren(nodelist);
    	root.setOpen(true);
    	rootList.add(root);
    	renderJson(rootList);
    }
    
    /**
     * 获得选择的机构树的数据
     */
    public void getChosen(){
    	String nodeName = getPara("nodes");
    	SysOrg org = SysOrg.dao.findByOrgName(nodeName);
    	renderJson(org);
    }
    
    
    /**
     * 获得子机构项
     */
    public void getChildTppolitical(){
    	
    	String parentId = getPara("parentId");
    	if(parentId!="value"){
    	List<Tporganization> tpoChildList = Tporganization.dao.findChild(Integer.parseInt(parentId));
    	renderJson(tpoChildList);
    	}
    	
    }
    
    /**
     * 获得市，县项
     */
    public void getChildTplocalization(){
    	
    	String parentId = getPara("parentId");
    	if(parentId!="value"){
    	List<Tplocalization> tplChildList = Tplocalization.dao.findChild(Integer.parseInt(parentId));
    	renderJson(tplChildList);
    	}
    	
    }
    
    /**
     * 保存人才信息
     */

    public void save(){
    	Tpsource t = getModel(Tpsource.class);
    	//查询人才信息表的记录
    	//添加或修改
    
    	/**
    	 * 省级写死，若动态添加，需要数据库中添加省编号，然后这里getName查询省
    	 */
    	if(t.isExit(t.getTpId())){
    		if(t.getOrgId()!=null){
    		String address ="湖南省" +Tplocalization.dao.getName(t.getMunicipalLevel())+Tplocalization.dao.getName(t.getCountyLevel())+t.getTpAdd();
            t.setTpAdd(address);
    		t.setModifyTime(DateUtil.getTime().toString());
    		t.update();
    		}
    		else{
    			renderError();
    		}
    	}else{
    		//设置id
    		Integer count = t.count()+1;
        	t.setTpId(count.toString());
        	String address ="湖南省" +Tplocalization.dao.getName(t.getMunicipalLevel())+Tplocalization.dao.getName(t.getCountyLevel());
        	t.setTpAdd(address);
    		t.setCreateTime(DateUtil.getTime().toString());
    		t.setModifyTime(DateUtil.getTime().toString());
    		t.save();
    	}
    	renderSuccess();
    }

	/**************************************************************************/
	private String pageTitle = "人才管理";// 模块页面标题
	private String breadHomeMethod = "getListPage";// 面包屑首页方法

	public Map<String, String> getPageTitleBread() {
		Map<String, String> pageTitleBread = new HashMap<String, String>();
		pageTitleBread.put("pageTitle", pageTitle);
		pageTitleBread.put("breadHomeMethod", breadHomeMethod);
		return pageTitleBread;
	}
}
