var pointLion = function(){
		/***
		 * 选择一个组织结构方法
		 */
		var selectOneOrgNode={};//机构数据
		var selectOneOrg = function(callback){
			layer.open({
				  type: 2,
				  title: false, //不显示标题栏
				  area: ['300px', '550px'],
				  shade: 0.8,
				  id: 'selectOneOrg', //设定一个id，防止重复弹出
				  resize: false,
				  closeBtn: false,
				  isOutAnim : false , 
				  btn: ['确定', '取消'], 
				  btnAlign: 'c',
				  content: ctx+'/admin/org/getSelectOneOrgPage',
				  success: function(layero){
					  
				  },
				  yes: function(){
					  if( callback != null ){
						  callback(selectOneOrgNode);
					  }
					  layer.closeAll();
				  }
			});
			
		};
		//获取选择好的机构数据---提供给弹出层调用
		var  setOneOrgNode = function(treeNode){
			selectOneOrgNode = treeNode;
		};
		
		/***
		 * 选择一个栏目结构方法
		 */
		var selectOneNavcolNode={};//栏目数据
		var selectOneNavcol = function(callback){
			layer.open({
				  type: 2,
				  title: false, //不显示标题栏
				  area: ['300px', '550px'],
				  shade: 0.8,
				  id: 'selectOneNavcol', //设定一个id，防止重复弹出
				  resize: false,
				  closeBtn: false,
				  isOutAnim : false , 
				  btn: ['确定', '取消'], 
				  btnAlign: 'c',
				  content: ctx+'/admin/navcol/getSelectOneNavcolPage',
				 
				  success: function(layero){
					  
				  },
				  yes: function(){
					  if( callback != null ){
						  callback(selectOneNavcolNode);
					  }
					  layer.closeAll();
				  }
			});
			
		};
		//获取选择好的栏目数据---提供给弹出层调用
		var  setOneNavcolNode = function(treeNode){
			selectOneNavcolNode = treeNode;
		};
		/***
		 * 选择多个组织结构方法
		 */
		var selectManyOrgNode;//机构数据
		var selectManyOrg = function(callback){
			layer.open({
			  type: 2,
			  title: false, //不显示标题栏
			  area: ['300px', '550px'],
			  shade: 0.8,
			  id: 'selectOneOrg', //设定一个id，防止重复弹出
			  resize: false,
			  closeBtn: false,
			  isOutAnim : false , 
			  btn: ['确定', '取消'], 
			  btnAlign: 'c',
			  content: ctx+'/admin/org/getSelectManyOrgPage',
			  success: function(layero){
				  
			  },
			  yes: function(){
				  if( callback != null ){
					  callback(selectManyOrgNode);
				  }
				  layer.closeAll();
			  }
			});
		};
		//获取选择好的机构数据---提供给弹出层调用
		var  setManyOrgNode = function(treeNode){
			 selectManyOrgNode = treeNode;
		};
		/***
		 * 公用弹出提醒
		 */
		var alertMsg = function(msg,type,size,callback){
			var t = "mint";//默认颜色
			var s = "small";
			if(type){
				t = type;
			}
			if(size){
				s = size;
			}
			bootbox.dialog({  
	            buttons: {  
	            	ok: {  
	                    label: '确定',
	                    className: "btn-"+t,
	                    callback : function(){
	    	            	if(callback!=null){
	    	    				callback();
	    	    			}
	    	            },
	                }  
	            },  
	            message: msg,  
	            title : '提示信息',
	            size: s,
	            animateIn: 'flipInX',
	            animateOut : 'flipOutX' 
	        });  
		};
		/***
		 * 公用弹出提醒
		 */
		var confimMsg = function(msg,size,callback){
			var s = "small";
			if(size){
				s = size;
			}
			bootbox.dialog({  
	            buttons: {
	            	cancel : {
	                	label: '取消',
	                    className: "btn-default",
	                    callback : function(){
	    	            	
	    	            }
	                },
	            	ok: {  
	                    label: '确定',
	                    className: "btn-mint",
	                    callback : function(){
	    	            	if(callback!=null){
	    	    				callback();
	    	    			}
	    	            }
	                }
	                
	            },  
	            message: msg,  
	            title : '向您确认',
	            size: s,
	            animateIn: 'swing',
	            animateOut : 'hinge'
	        });  
		};
		/***
		 * 弹出即时聊天页面
		 */
		var chatId;
		var openChat = function(uid){
			chatId = layer.open({
				  type: 2,
				  title: false, //不显示标题栏
				  area: ['500px', '552px'],
				  shade: 0.8,
				  id: 'openChat', //设定一个id，防止重复弹出
				  resize: false,
				  closeBtn: false,
				  isOutAnim : false , 
				  btnAlign: 'c',
				  content: ctx+'/admin/chat/getChatPage?id='+uid,
				  success: function(layero){
					  
				  }
				});
		};
		var closeChat = function(){
			layer.close(chatId);
		};
		
		/***
		 * 初始化图片上传
		 */
		var initUploader  = function(domid){
			var uploader = WebUploader.create({
			    // 选完文件后，是否自动上传。
			    auto: true,
			    // swf文件路径
			    swf: ctx+'/common/plugins/webuploader/Uploader.swf',
			    // 文件接收服务端。
			    server: ctx+'/admin/upload/upload',
			    // 内部根据当前运行是创建，可能是input元素，也可能是flash.
			    pick: '#filePicker',
			    // 只允许选择图片文件。
			    accept: {
			        title: 'Images',
			        extensions: 'gif,jpg,jpeg,bmp,png',
			        mimeTypes: 'image/*'
			    }
			});
			//上传成功，添加缩略图，和添加路径参数
			uploader.on( 'uploadSuccess', function( file ,data) {
			    $("#fileList").html("<img style='width:200px;height:120px;margin-left: 20px;' src='"+ctx+"/upload"+data.data.path+"/"+data.data.filename+"'/>");
			    //添加路径
			    $("#"+domid).val("/upload"+data.data.path+"/"+data.data.filename);
			});
			// 文件上传失败，显示上传出错。
			uploader.on( 'uploadError', function( file ) {
			    alert("上传出错");
			});

			// 完成上传完了，成功或者失败，先删除进度条。
			uploader.on( 'uploadComplete', function( file ) {
				
			});
		};
		
		/***
		 * 合作伙伴图片上传
		 */
		var initBrandPicUploader  = function(domid){
			var uploader = WebUploader.create({
			    // 选完文件后，是否自动上传。
			    auto: true,
			    // swf文件路径
			    swf: ctx+'/common/plugins/webuploader/Uploader.swf',
			    // 文件接收服务端。
			    server: ctx+'/admin/upload/uploadBrandPic',
			    // 内部根据当前运行是创建，可能是input元素，也可能是flash.
			    pick: '#filePicker',
			    // 只允许选择图片文件。
			    accept: {
			        title: 'Images',
			        extensions: 'gif,jpg,jpeg,bmp,png',
			        mimeTypes: 'image/*'
			    }
			});
			//上传成功，添加缩略图，和添加路径参数
			uploader.on( 'uploadSuccess', function( file ,data) {
			    $("#fileList").html("<img style='width:200px;height:120px;margin-left: 20px;' src='"+"http://www.hnwcdf.net/images/upload"+data.data.path+"/"+data.data.filename+"'/>");
			    //添加路径
			    $("#"+domid).val("/upload"+data.data.path+"/"+data.data.filename);
			});
			// 文件上传失败，显示上传出错。
			uploader.on( 'uploadError', function( file ) {
			    alert("上传出错");
			});

			// 完成上传完了，成功或者失败，先删除进度条。
			uploader.on( 'uploadComplete', function( file ) {
				
			});
		};
		
		/***
		 * 轮播图片上传
		 */
		var initLoopPicUploader = function(domid){
			var uploader = WebUploader.create({
			    // 选完文件后，是否自动上传。
			    auto: true,
			    // swf文件路径
			    swf: ctx+'/common/plugins/webuploader/Uploader.swf',
			    // 文件接收服务端。
			    server: ctx+'/admin/upload/uploadLoopPic',
			    // 内部根据当前运行是创建，可能是input元素，也可能是flash.
			    pick: '#filePicker',
			    // 只允许选择图片文件。
			    accept: {
			        title: 'Images',
			        extensions: 'gif,jpg,jpeg,bmp,png',
			        mimeTypes: 'image/*'
			    }
			});
			//上传成功，添加缩略图，和添加路径参数
			uploader.on( 'uploadSuccess', function( file ,data) {
			    $("#fileList").html("<img style='width:200px;height:120px;margin-left: 20px;' src='"+"http://www.hnwcdf.net/images/upload"+data.data.path+"/"+data.data.filename+"'/>");
			    //添加路径
			    $("#"+domid).val("/upload"+data.data.path+"/"+data.data.filename);
			});
			// 文件上传失败，显示上传出错。
			uploader.on( 'uploadError', function( file ) {
			    alert("上传出错");
			});

			// 完成上传完了，成功或者失败，先删除进度条。
			uploader.on( 'uploadComplete', function( file ) {
				
			});
		};
		
		/***
		 * 文章图片上传
		 */
		var initArticlePicUploader = function(domid){
			var uploader = WebUploader.create({
			    // 选完文件后，是否自动上传。
			    auto: true,
			    // swf文件路径
			    swf: ctx+'/common/plugins/webuploader/Uploader.swf',
			    // 文件接收服务端。
			    server: ctx+'/admin/upload/uploadArticlePic',
			    // 内部根据当前运行是创建，可能是input元素，也可能是flash.
			    pick: '#filePicker',
			    // 只允许选择图片文件。
			    accept: {
			        title: 'Images',
			        extensions: 'gif,jpg,jpeg,bmp,png',
			        mimeTypes: 'image/*'
			    }
			});
			//上传成功，添加缩略图，和添加路径参数
			uploader.on( 'uploadSuccess', function( file ,data) {
				
				//http://www.hnwcdf.net/images/upload/article/Koala.jpg
				//使用自己的图片服务器
				//console.log(data.data.path+"/"+data.data.filename);
			    $("#fileList").html("<img style='width:200px;height:120px;margin-left: 20px;' src='"+"http://210.43.241.80:8080/YiDaoMgmt/common/img/temp"+data.data.path+"/"+data.data.filename+"'/>");
			    //添加路径
			    $("#"+domid).val("http://210.43.241.80:8080/YiDaoMgmt/common/img/temp"+data.data.path+"/"+data.data.filename);
			    //console.log("/upload"+data.data.path+"/"+data.data.filename);
			});
			// 文件上传失败，显示上传出错。
			uploader.on( 'uploadError', function( file ) {
			    alert("上传出错");
			});

			// 完成上传完了，成功或者失败，先删除进度条。
			uploader.on( 'uploadComplete', function( file ) {
				
			});
		};
		
		/***
		 * 慈善企业图片上传
		 */
		var initChaentPicUploader = function(domid){
			var uploader = WebUploader.create({
			    // 选完文件后，是否自动上传。
			    auto: true,
			    // swf文件路径
			    swf: ctx+'/common/plugins/webuploader/Uploader.swf',
			    // 文件接收服务端。
			    server: ctx+'/admin/upload/uploadChaentPic',
			    // 内部根据当前运行是创建，可能是input元素，也可能是flash.
			    pick: '#filePicker',
			    // 只允许选择图片文件。
			    accept: {
			        title: 'Images',
			        extensions: 'gif,jpg,jpeg,bmp,png',
			        mimeTypes: 'image/*'
			    }
			});
			//上传成功，添加缩略图，和添加路径参数
			uploader.on( 'uploadSuccess', function( file ,data) {
			    $("#fileList").html("<img style='width:200px;height:120px;margin-left: 20px;' src='"+"http://www.hnwcdf.net/images/upload"+data.data.path+"/"+data.data.filename+"'/>");
			    //添加路径
			    $("#"+domid).val("/upload"+data.data.path+"/"+data.data.filename);
			});
			// 文件上传失败，显示上传出错。
			uploader.on( 'uploadError', function( file ) {
			    alert("上传出错");
			});

			// 完成上传完了，成功或者失败，先删除进度条。
			uploader.on( 'uploadComplete', function( file ) {
				
			});
		};
		
		/***
		 * 慈善人物图片上传
		 */
		var initCharitorPicUploader = function(domid){
			var uploader = WebUploader.create({
			    // 选完文件后，是否自动上传。
			    auto: true,
			    // swf文件路径
			    swf: ctx+'/common/plugins/webuploader/Uploader.swf',
			    // 文件接收服务端。
			    server: ctx+'/admin/upload/uploadCharitorPic',
			    // 内部根据当前运行是创建，可能是input元素，也可能是flash.
			    pick: '#filePicker',
			    // 只允许选择图片文件。
			    accept: {
			        title: 'Images',
			        extensions: 'gif,jpg,jpeg,bmp,png',
			        mimeTypes: 'image/*'
			    }
			});
			//上传成功，添加缩略图，和添加路径参数
			uploader.on( 'uploadSuccess', function( file ,data) {
			    $("#fileList").html("<img style='width:200px;height:120px;margin-left: 20px;' src='"+"http://www.hnwcdf.net/images/upload"+data.data.path+"/"+data.data.filename+"'/>");
			    //添加路径
			    $("#"+domid).val("/upload"+data.data.path+"/"+data.data.filename);
			});
			// 文件上传失败，显示上传出错。
			uploader.on( 'uploadError', function( file ) {
			    alert("上传出错");
			});

			// 完成上传完了，成功或者失败，先删除进度条。
			uploader.on( 'uploadComplete', function( file ) {
				
			});
		};
		
		/***
		 * 项目封面图片上传
		 */
		var initProjectPicUploader = function(domid){
			var uploader = WebUploader.create({
			    // 选完文件后，是否自动上传。
			    auto: true,
			    // swf文件路径
			    swf: ctx+'/common/plugins/webuploader/Uploader.swf',
			    // 文件接收服务端。
			    server: ctx+'/admin/upload/uploadProjectPic',
			    // 内部根据当前运行是创建，可能是input元素，也可能是flash.
			    pick: '#filePicker',
			    // 只允许选择图片文件。
			    accept: {
			        title: 'Images',
			        extensions: 'gif,jpg,jpeg,bmp,png',
			        mimeTypes: 'image/*'
			    }
			});
			//上传成功，添加缩略图，和添加路径参数
			uploader.on( 'uploadSuccess', function( file ,data) {
			    $("#fileList").html("<img style='width:200px;height:120px;margin-left: 20px;' src='"+"http://www.hnwcdf.net/images/upload"+data.data.path+"/"+data.data.filename+"'/>");
			    //添加路径
			    $("#"+domid).val("/upload"+data.data.path+"/"+data.data.filename);
			});
			// 文件上传失败，显示上传出错。
			uploader.on( 'uploadError', function( file ) {
			    alert("上传出错");
			});

			// 完成上传完了，成功或者失败，先删除进度条。
			uploader.on( 'uploadComplete', function( file ) {
				
			});
		};
		
		/***
		 * 信息公开报告图片上传
		 */
		var initReportPicUploader = function(domid){
			var uploader = WebUploader.create({
			    // 选完文件后，是否自动上传。
			    auto: true,
			    // swf文件路径
			    swf: ctx+'/common/plugins/webuploader/Uploader.swf',
			    // 文件接收服务端。
			    server: ctx+'/admin/upload/uploadReportPic',
			    // 内部根据当前运行是创建，可能是input元素，也可能是flash.
			    pick: '#filePicker',
			    // 只允许选择图片文件。
			    accept: {
			        title: 'Images',
			        extensions: 'gif,jpg,jpeg,bmp,png',
			        mimeTypes: 'image/*'
			    }
			});
			//上传成功，添加缩略图，和添加路径参数
			uploader.on( 'uploadSuccess', function( file ,data) {
			    $("#fileList").html("<img style='width:200px;height:120px;margin-left: 20px;' src='"+"http://www.hnwcdf.net/images/upload"+data.data.path+"/"+data.data.filename+"'/>");
			    //添加路径
			    $("#"+domid).val("/upload"+data.data.path+"/"+data.data.filename);
			});
			// 文件上传失败，显示上传出错。
			uploader.on( 'uploadError', function( file ) {
			    alert("上传出错");
			});

			// 完成上传完了，成功或者失败，先删除进度条。
			uploader.on( 'uploadComplete', function( file ) {
				
			});
		};
		
		/***
		 * Rules图片上传
		 */
		var initRulesPicUploader = function(domid){
			var uploader = WebUploader.create({
			    // 选完文件后，是否自动上传。
			    auto: true,
			    // swf文件路径
			    swf: ctx+'/common/plugins/webuploader/Uploader.swf',
			    // 文件接收服务端。
			    server: ctx+'/admin/upload/uploadRulesPic',
			    // 内部根据当前运行是创建，可能是input元素，也可能是flash.
			    pick: '#filePicker',
			    // 只允许选择图片文件。
			    accept: {
			        title: 'Images',
			        extensions: 'gif,jpg,jpeg,bmp,png',
			        mimeTypes: 'image/*'
			    }
			});
			//上传成功，添加缩略图，和添加路径参数
			uploader.on( 'uploadSuccess', function( file ,data) {
			    $("#fileList").html("<img style='width:200px;height:120px;margin-left: 20px;' src='"+"http://www.hnwcdf.net/images/upload"+data.data.path+"/"+data.data.filename+"'/>");
			    //添加路径
			    $("#"+domid).val("/upload"+data.data.path+"/"+data.data.filename);
			});
			// 文件上传失败，显示上传出错。
			uploader.on( 'uploadError', function( file ) {
			    alert("上传出错");
			});

			// 完成上传完了，成功或者失败，先删除进度条。
			uploader.on( 'uploadComplete', function( file ) {
				
			});
		};
		
		/***
		 * 打开公共流程历史任务列表页面
		 */
		var openTaskHisListPage = function(insid){
			layer.open({
				  type: 2,
				  title: false, //不显示标题栏
				  area: ['750px', '500px'],
				  shade: 0.8,
				  id: 'taskHisListPage', //设定一个id，防止重复弹出
				  resize: false,
				  closeBtn: false,
				  isOutAnim : false , 
				  btnAlign: 'c',
				  content: ctx+'/admin/workflow/getWorkFlowHis?insid='+insid,
				  success: function(layero){
					  
				  }
				});
		}
		return {
			selectOneOrg : selectOneOrg,
			setOneOrgNode : setOneOrgNode,
			selectOneNavcol : selectOneNavcol,
			setOneNavcolNode : setOneNavcolNode,
			alertMsg : alertMsg ,
			confimMsg : confimMsg,
			openChat : openChat,
			closeChat : closeChat,
			selectManyOrg : selectManyOrg,
			setManyOrgNode : setManyOrgNode,
			initUploader : initUploader,
			initBrandPicUploader : initBrandPicUploader,
			initLoopPicUploader : initLoopPicUploader,
			initArticlePicUploader : initArticlePicUploader,
			initChaentPicUploader : initChaentPicUploader,
			initCharitorPicUploader : initCharitorPicUploader,
			initProjectPicUploader : initProjectPicUploader,
			initReportPicUploader : initReportPicUploader,
			initRulesPicUploader : initRulesPicUploader,
			openTaskHisListPage : openTaskHisListPage
		};
}();		