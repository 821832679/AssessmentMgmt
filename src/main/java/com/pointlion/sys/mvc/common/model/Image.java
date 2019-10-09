package com.pointlion.sys.mvc.common.model;

import com.jfinal.plugin.activerecord.Page;
import com.pointlion.sys.mvc.common.model.base.BaseImage;

@SuppressWarnings("serial")
public class Image extends BaseImage<Image> {
	public final static Image dao = new Image();

	public Page<Image> getImagePage(String md5str) {
		return Image.dao.paginate(1, 10, "select * ", " from t_image where md5str = '" + md5str + "'");
	}
}
