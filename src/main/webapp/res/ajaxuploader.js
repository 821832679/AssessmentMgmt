function showUploader(options) {
	var $btnUpload = options.el;
	var callback = options.callback;
	var maxWidth = options.maxWidth;
	var compress = options.compress;
	//var maxHeight = options.maxHeight;
	var actionUrl = ctx+'/administration/console/file/uploadImage.do';
	if(maxWidth != undefined){
		actionUrl += '?maxWidth='+maxWidth;
	}
	if(compress!= undefined){
		actionUrl += '&compress='+compress;
	}
	new AjaxUpload($btnUpload, {
		action : actionUrl,
		name : "upfile",
		onSubmit : function(file, ext) {
			if (!(ext && /^(jpg|png|jpeg|gif)$/.test(ext))) {
				// extension is not allowed
				alert('只能上传 JPG, PNG ,GIF 文件');
				return false;
			}
			$btnUpload.removeAttr("onclick");
		},
		onComplete : function(file, response) {
			var json = eval("(" + response + ")");
			$btnUpload.attr("onclick", "showUploader()");
			
			if (json.state == "SUCCESS") {
				$btnUpload.parent().find("img.uploaderImage").attr("src", options.imgDomain + "" + json.url).show();
				$btnUpload.parent().find("input.uploaderHidden").val(json.url);
				if(typeof(callback)=="function"){
					callback();
				}
			} else {
				alert(json.errorMsg);
			}
		}
	});
}