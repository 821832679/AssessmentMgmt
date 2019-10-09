function jbsxBoxAjaxDone(json) {
	DWZ.ajaxDone(json);
	if (json.statusCode == DWZ.statusCode.ok) {
		if (json.navTabId) {
			//navTab.reloadFlag(json.navTabId);
//			$("form[rel='"+json.navTabId+"']").submit();
			divSearch($("form[rel='"+json.navTabId+"']").get(0), json.navTabId);
		} else { // 重新载入当前navTab页面
			navTabPageBreak();
		}
		if ("closeCurrent" == json.callbackType) {
			setTimeout(function() {
				$.pdialog.closeCurrent();
			}, 100);
		} else if ("forward" == json.callbackType) {
			navTab.reload(json.forwardUrl);
		}
	}
}