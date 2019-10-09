$(function(){
		$("input.uploader").each(function(){
			var btnUpload=$(this);
			var noscale = btnUpload.attr("noscale");
			var shui = btnUpload.attr("shui");
			new AjaxUpload(btnUpload, {
				action: ctx+"/administration/console/file/uploadImage.do?noscale="+noscale+"&shui="+shui,
				name: "upfile",
				onSubmit: function(file, ext){
					 if (! (ext && /^(jpg|png|jpeg|gif)$/.test(ext))){ 
	                    // extension is not allowed 
						alert('只能上传 JPG, PNG ,GIF 文件');
						return false;
					}
					btnUpload.attr("disabled","disabled");
				},
				onComplete: function(file, response){
					btnUpload.removeAttr("disabled");
					var json = eval("("+response+")");
					if(json.state == "SUCCESS"){
						var files = json.url;
						if(files.length > 0){
							btnUpload.prev("img").eq(0).attr("src", imgDomain+files);
							btnUpload.next("input").eq(0).val(files);
							//alert("上传成功");
						}
					}else{
						alert(json.name);
					}
				}
			});	
		});
		
		$(".mulUploader").each(function(){
			var btnUpload=$(this);
			new AjaxUpload(btnUpload, {
				action: ctx+"/administration/console/file/uploadImage.do",
				name: "upfile",
				onSubmit: function(file, ext){
					 if (! (ext && /^(jpg|png|jpeg|gif)$/.test(ext))){ 
	                    // extension is not allowed 
						alert('只能上传 JPG, PNG ,GIF 文件');
						return false;
					}
					btnUpload.attr("disabled","disabled");
				},
				onComplete: function(file, response){
					btnUpload.removeAttr("disabled");
					var json = eval("("+response+")");
					if(json.state == "SUCCESS"){
						var html = "<img ondblclick='delthis(this)' id='"+json.url+"' src='"+ imgDomain+ "/"+ json.url+ "' style='max-width:600px;display:block;'/>";
						btnUpload.parent().find("div.mulContainer").eq(0).append(html);
						var $imageArray = btnUpload.parent().find("input[name='images']").eq(0);
						$imageArray.val($imageArray.val()+json.url+";");
					} else {
						alert(json.errorMsg);
					}
				}
			});	
		});
});


//上传文件
$(function(){
	$("input.uploaderfile").each(function(){
		var btnUpload=$(this);
		new AjaxUpload(btnUpload, {
			action: ctx+"/administration/console/file/uploadfile.do",
			name: "upfile",
			onSubmit: function(file, ext){
				if (!(ext && /^(doc|txt|xls|xlsx|rar)$/.test(ext))) {
					// extension is not allowed
					alert('只能上传 doc, txt ,xls ,rar 文件');
					return false;
				} 
				btnUpload.attr("disabled","disabled");
			},
			onComplete: function(file, response){
				btnUpload.removeAttr("disabled");
				var json = eval("("+response+")");
				if(json.state == "SUCCESS"){
					var files = json.url;
					var name = json.name;
					var original = json.original;
					var fileSize=json.size;
						
					if(files.length > 0){
						btnUpload.next("input").eq(0).val(fileSize);
						btnUpload.next("input").next("input").eq(0).val(original);
						btnUpload.next("input").next("input").next("input").eq(0).val(files);
						//alert("文件上传成功"); 
					}
				}else{
					alert(json.name);
				}
			}
		});	
	});
});
