package com.pointlion.sys.mvc.admin.answer;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.CellType;

import com.alibaba.druid.util.StringUtils;
import com.jfinal.aop.Before;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;
import com.pointlion.sys.interceptor.MainPageTitleInterceptor;
import com.pointlion.sys.mvc.common.base.BaseController;
import com.pointlion.sys.mvc.common.model.Answer;
import com.pointlion.sys.mvc.common.model.AnswerExcel;
import com.pointlion.sys.mvc.common.model.FileConfig;
import com.pointlion.sys.mvc.common.model.Question;
import com.pointlion.sys.mvc.common.model.Resource;
import com.pointlion.sys.mvc.common.model.Score;
import com.pointlion.sys.mvc.common.model.SysUser;
import com.pointlion.sys.mvc.common.model.Topic;
import com.pointlion.sys.mvc.common.model.TopicDelay;
import com.pointlion.sys.mvc.common.utils.DateUtils;
import com.pointlion.sys.mvc.common.utils.UuidUtil;

@Before(MainPageTitleInterceptor.class)
public class AnswerManagementController extends BaseController {

	/*************************** 指标管理---开始 ***********************/

	/**
	 * 获得指标管理页面
	 */
	public void getListPage() {
		render("/WEB-INF/admin/answer/list.html");
	}

	/**
	 * 获得指标管理数据
	 */
	public void listData() {
		String curr = getPara("pageNumber");
		Float currFloat = Float.valueOf(curr);
		Integer currPage = currFloat.intValue();
		String pageSize = getPara("pageSize");
		Page<Answer> page = Answer.dao.getAnswerPage(currPage, Integer.valueOf(pageSize));
		renderPage(page.getList(), "", page.getTotalRow());
	}

	/**
	 * 获得编辑页面
	 */
	public void getEditPage() {
		String id = getPara("id");
		if (StrKit.notBlank(id)) {
			Answer templpate = Answer.dao.findById(id);
			setAttr("t", templpate);
		}
		render("/WEB-INF/admin/answer/edit.html");
	}
	/**
	 * 保存时附件id做不删除处理
	 * @title ridHandle
	 * @param a	原录入指标数据
	 * @param answer	新录入指标数据
	 */
	private void ridHandle(Answer a, Answer answer){
		if(a!=null && StrKit.notBlank(a.getRId())){
			String rid = a.getRId();
			if (!StringUtils.isEmpty(answer.getRId())) {
				String[] rids = answer.getRId().split(",");
				for (int j = 0; j < rids.length; j++) {
					if(rid.indexOf(rids[j]) < 0){
						rid += ","+rids[j];
					}
				}
			}
			answer.setRId(rid);
		}
	}
	
	/**
	 * 保存
	 */
	public void save() {
		Answer answer = getModel(Answer.class);
		String topicid = answer.getTopicId();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String username = getSessionAttr("username");
		SysUser user = SysUser.dao.findbyUserName(username);

		if (StrKit.notBlank(answer.getId())) {
			answer.update();
		} else {
			Topic topic = Topic.dao.findById(topicid);
			boolean bol = DateUtils.isEffectiveDate(new Date(), DateUtils.convert2YMdhmsTime(topic.getBeginTime()), DateUtils.convert2YMdhmsTime(topic.getEndTime()));
			if(!bol){
				//判断是否延期考核
				TopicDelay delay = TopicDelay.dao.find(topicid, user.getId());
				if(delay==null || delay.getStat()!=1){
					renderError("考核未开始或已结束");
					return;
				}
			}
			//删除原有评价和回答指标
			List<Score> scores = Score.dao.getScoreList(topicid, user.getId());
			for (Score s : scores) {
				s.delete();
			}
			List<Answer> answers = Answer.dao.getAnswerList(topicid, user.getId());
			for (Answer a : answers) {
				a.delete();
				AnswerExcel.dao.deleteByAnswerid(a.getId());
			}
			//保存指标
			Page<Question> page = Question.dao.getQuestionPage(1, 100, topicid,null,null);
			for (int i = 0; i < page.getList().size(); i++) {
				Question question = page.getList().get(i);
				answer = getModel(Answer.class);
				answer.setQuestionId(question.getId());
				answer.setDescrible(getPara(question.getId()));
				answer.setTopicId(topicid);
				answer.setId(UuidUtil.getUUID());
				answer.setPersonId(user.getId());
				String rid = getPara("fileRid" + question.getId());
				if (!StringUtils.isEmpty(rid) && !"null".equals(rid)) {
					answer.setRId(rid);
				} else {
					answer.setRId(null);
				}
				for (Answer a : answers) {
					if(a.getTopicId().equals(topicid) && a.getQuestionId().equals(question.getId()) && a.getPersonId().equals(user.getId())){
						this.ridHandle(a, answer);//特殊处理，保留其他人上传附件
					}
				}
				String fid = getPara("fileFid" + question.getId());
				if (!StringUtils.isEmpty(fid) && !"null".equals(fid)) {
					answer.setFId(fid);
				} else {
					answer.setFId(null);
				}
				answer.setCreateTime(sf.format(new Date()));
				bol = answer.save();
				//保存成功后续操作
				if(bol){
					if (!StringUtils.isEmpty(answer.getFId()) && question.getIsstatis() == 1) {
						//根据配置保存excle中的数据
						FileConfig config = FileConfig.dao.selectByQuestionid(question.getId()).get(0);
						Resource resource = Resource.dao.findById(fid);
						if(resource!=null){
							File file = new File(resource.getUploadPath()+"/"+resource.getRname());
							AnswerExcel answerExcel = new AnswerExcel();
							answerExcel.setAnswerId(answer.getId());
							answerExcel.setFileId(answer.getFId());
							answerExcel.setQuestionId(question.getId());
							answerExcel.setPersonId(user.getId());
							answerExcel.setCreateTime(answer.getCreateTime());
							answerExcel.setTarget(resource.getRname().substring(0, resource.getRname().indexOf(".")));
							try {
								this.read(file, answerExcel, config);
							} catch (Exception e) {
								System.out.println("excel文件保存失败，原因："+e.getMessage());
								Question q = Question.dao.findById(question.getId());
								if(q!=null){
									renderError("任务"+q.getSortValue()+"上传模板存在格式错误或更改，保存失败！");
								}else{
									renderError("有任务上传模板存在格式错误或更改，保存失败！");
								}
								return;
							}
						}
					}
				}
			}
			Score score = getModel(Score.class);
			score.setId(UuidUtil.getUUID());
			score.setUserId(user.getId());
			score.setTopicId(topicid);
			score.setCreateTime(sf.format(new Date()));
			score.save();
		}
		renderSuccess();
	}
	
	/***
	 * 删除
	 * 
	 * @throws Exception
	 */
	public void delete() throws Exception {
		Answer.dao.deleteByIds(getPara("ids"));
		renderSuccess();
	}
	
	public static void main(String[] args) {
		String[] str = new String[5];
		str[0] = "株洲市";
		str[1] = "芦淞区";
		str[2] = " ";
		str[3] = " ";
		str[4] = " ";
		System.out.println(org.apache.commons.lang.StringUtils.join(str,","));
	}
	
	
	@SuppressWarnings("resource")
	public void read(File file, AnswerExcel answerExcel, FileConfig config) throws Exception {
		// 读excele 到数据库
		if (!file.exists())
			System.out.println("文件不存在");
		POIFSFileSystem poifsFileSystem = new POIFSFileSystem(new FileInputStream(file));
		HSSFWorkbook hssfWorkbook = new HSSFWorkbook(poifsFileSystem);
		HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(0); // 数据表
		int rowLength = hssfSheet.getLastRowNum() + 1;// 行数
		//判断是横向还是纵向
		if(config.getDirection() == 1){
			int sortvalue = 1;
			//横向
			for (int i = config.getBegin(); i < (rowLength-config.getEnd()); i++) {
				HSSFRow hssfRow = hssfSheet.getRow(i); // 循环获取所有行
				int colLength = hssfRow.getLastCellNum();// 列长度
				StringBuilder sb = new StringBuilder();
				sb.append("[");
				
				for (int j = 0; j < colLength; j++) {	//循环每一列
					HSSFCell hssfCell = hssfRow.getCell(j); // 获取结果
					if (hssfCell != null) {
						if(hssfCell.getCellTypeEnum() == CellType.STRING){
							sb.append(j==0?"'"+hssfCell.getStringCellValue()+"'":","+"'"+hssfCell.getStringCellValue()+"'");
						}else if (hssfCell.getCellTypeEnum() == CellType.NUMERIC) {
							// 判断参数类型
					        if(HSSFDateUtil.isCellDateFormatted(hssfCell)){
					            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
					            String date = sdf.format(HSSFDateUtil.getJavaDate(hssfCell.getNumericCellValue()));
					            sb.append(j==0?"'"+date+"'":","+"'"+date+"'");
					        }else {
					        	hssfCell.setCellType(CellType.STRING);
					        	sb.append(j==0?"'"+hssfCell.getStringCellValue()+"'":","+"'"+hssfCell.getStringCellValue()+"'");
					        }
						}
					}else{
						sb.append(j==0?"''":",''");
					}
				}
				sb.append("]");
				answerExcel.setId(UuidUtil.getUUID());
				answerExcel.setTitle(answerExcel.getTarget());
				answerExcel.setAnswervaljson(sb.toString());
				answerExcel.setSortvalue(sortvalue);
				answerExcel.save();
				sortvalue++;
			}
		}else if(config.getDirection() == 0){
			//纵向
			//从第一行循环起
			for (int i = 1; i < rowLength; i++) {
				HSSFRow hssfRow = hssfSheet.getRow(i); // 循环获取所有行
				HSSFCell hssfCell0 = hssfRow.getCell(0); // 获取标题
				HSSFCell hssfCell1 = hssfRow.getCell(1); // 获取结果
				if (hssfCell0 != null) {
					hssfCell0.setCellType(CellType.STRING);
				} 
				if (hssfCell1 != null) {
					hssfCell1.setCellType(CellType.STRING);
				}
				answerExcel.setId(UuidUtil.getUUID());
				answerExcel.setTitle(hssfCell0.getStringCellValue());
				answerExcel.setAnswerval(hssfCell1.getStringCellValue());
				answerExcel.setSortvalue(i);
				answerExcel.save();
			}
		}
	}
	
	/***
	 * 删除服务器文件
	 * 
	 * @throws Exception
	 */
	public void delFile() throws Exception {
		String idParam = getPara("id");
		String fidParam = getPara("fid");
		if (StringUtils.isEmpty(idParam) || StringUtils.isEmpty(fidParam)) {
			return;
		}
		Resource ob = Resource.dao.findById(fidParam);
		if (ob != null) {
			String fileName = ob.getRname();
			String filePath = ob.getUploadPath();
			String path = filePath + "/" + fileName;
			File file = new File(path);
			if (file.exists()) {
				//file.delete();// 删除附件
			}
		}
		StringBuffer buff = new StringBuffer();
		try {
			Answer ab = Answer.dao.findById(idParam);
			if (ab != null) {
				ab.setFId(null);
				ab.update();// 更新答题附件
			}
			//Resource.dao.deleteById(fidParam);// 删除附件上传记录
			AnswerExcel.dao.deleteByFileid(fidParam);//删除附件统计记录
		} catch (Exception e) {
			e.printStackTrace();
			renderError();
		}
		renderSuccess(buff.toString());
	}
	
	

	/************************ 数据库管理---结束 *************************************************/

	/**************************************************************************/
	private String pageTitle = "得分查询";// 模块页面标题
	private String breadHomeMethod = "getListPage";// 面包屑首页方法

	public Map<String, String> getPageTitleBread() {
		Map<String, String> pageTitleBread = new HashMap<String, String>();
		pageTitleBread.put("pageTitle", pageTitle);
		pageTitleBread.put("breadHomeMethod", breadHomeMethod);
		return pageTitleBread;
	}
}
