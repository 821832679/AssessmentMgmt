package com.pointlion.sys.mvc.common.utils;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.druid.util.StringUtils;
import com.pointlion.sys.mvc.common.model.SysOrg;

/*作者：Tan
       日期：2018年11月17日
       时间：下午12:15:37
**/
public class SysOrgUtil {
	public static String getOrgListForString(List<SysOrg> sysOrgList, String orgid) {
		List<String> orgList = ergodicList(sysOrgList, new ArrayList<String>());
		StringBuffer buff = new StringBuffer();
		if (!StringUtils.isEmpty(orgid)) {
			buff.append("'" + orgid + "',");
		}
		String result = null;
		for (int i = 0; i < orgList.size(); i++) {
			buff.append("'");
			buff.append(orgList.get(i));
			buff.append("'");
			buff.append(",");
		}
		if (buff != null && buff.length() > 0) {
			result = buff.toString().substring(0, buff.length() - 1);
		}
		return result;
	}
	
	/**
	 * 作用:遍历组织机构查询所有下级id
	 * @param sysOrg
	 * @param result
	 * @return
	 */
	private static List<String> ergodicList(List<SysOrg> sysOrg, List<String> result) {
		for (int i = 0; i < sysOrg.size(); i++) {
			// 查询某节点的子节点（获取的是list）
			result.add(sysOrg.get(i).getId());// 前序遍历
			if (null != sysOrg.get(i).getChildren()) {
				List<SysOrg> children = sysOrg.get(i).getChildren();
				ergodicList(children, result);
			}
			// result.add(root.get(i).getId());//后序遍历
		}
		return result;
	}
	
	/**
     * 根据子级部门ID查询父级部门id(递归查询父级部门)
     *
     * @param
     * @return
     */
	public static StringBuilder getOrgParentById(String childId, StringBuilder stringBuilder) {
		if (StringUtil.isNotEmpty(childId)) {
			SysOrg sysOrg = SysOrg.dao.getById(childId);
			if (null != sysOrg && StringUtil.isNotEmpty(sysOrg.getParentId())) {
				String faterId = sysOrg.getParentId();// 获取父级id
				if (StringUtil.isNotEmpty(faterId) && !"#root".equals(faterId)) {// ID为#root，代表顶级部门
					if (stringBuilder != null) {
						if (stringBuilder.length() > 0) {
							stringBuilder.append(",").append("'").append(faterId).append("'");
							getOrgParentById(faterId, stringBuilder);
						} else {
							stringBuilder.append("'").append(childId).append("'").append(",");
							stringBuilder.append("'").append(faterId).append("'");
							getOrgParentById(faterId, stringBuilder);
						}
					}
				} else {
					if (stringBuilder.length() <= 0) {
						stringBuilder.append("'").append(sysOrg.getId()).append("'");
					}
				}
			}
		}
		return stringBuilder;
	}
}