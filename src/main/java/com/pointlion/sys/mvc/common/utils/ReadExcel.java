package com.pointlion.sys.mvc.common.utils;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.map.LinkedMap;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.jfinal.plugin.activerecord.Db;
import com.pointlion.sys.mvc.common.base.BaseController;
import com.pointlion.sys.mvc.common.model.SysMenu;
import com.pointlion.sys.mvc.common.model.Tpsource;



public class ReadExcel extends BaseController{
	 public void read(File file,String[] key) {
	         //读excele 到数据库
	         if (!file.exists()) System.out.println("文件不存在");
	         Map<String, Object> map = new LinkedMap<String, Object>();
	         Tpsource tps = new Tpsource(); //修改表
	        try {
	            POIFSFileSystem poifsFileSystem = new POIFSFileSystem(new FileInputStream(file));
	            HSSFWorkbook hssfWorkbook = new HSSFWorkbook(poifsFileSystem);
	            HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(0); //数据表
	            int rowLength = hssfSheet.getLastRowNum()+1;//行数
	            HSSFRow hssfRow = hssfSheet.getRow(0);//行
	            int colLength = hssfRow.getLastCellNum();//列长度
	            HSSFCell hssfCell = hssfRow.getCell(0); //；单元格
	            CellStyle cellStyle = hssfCell.getCellStyle();  //单元格类型
	 
	            for (int i = 0; i < rowLength; i++) {
	                HSSFRow hssfRow1 = hssfSheet.getRow(i);  //循环获取所有行
	                for (int j = 0; j < colLength; j++) {
	                    HSSFCell hssfCell1 = hssfRow1.getCell(j);  //循环获取每行的单元格
	                    if (hssfCell1 != null) {
	                        hssfCell1.setCellType(CellType.STRING);
	                    }
	                    try {
							map.put(key[j], hssfCell1.getStringCellValue());
						} catch (Exception e) {
							map.put(key[j], null);
						}
	                    
	                }
	                tps._setAttrs(map);
	              
	                tps.save();
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	 public void write1(File file) {
		  //读数据库 到excele
		  Tpsource tps = new Tpsource();
		  List<Tpsource> find = tps.find("select * from  t_tpsource");
		    int rowLength =find.size();
		    Object[] sqlrow = find.get(1)._getAttrValues(); //获取一行数据
		    int celllength = sqlrow.length;
		     String string = sqlrow[0].toString(); //一列中第0个列名
		     
		     String[] _getAttrNames = find.get(0)._getAttrNames();//获取一行的列名
		     String string2 = _getAttrNames[0];//获取第0个列名
		     System.out.println(string2);//列名
		     
		     
		     
		     
		     
	        Workbook workbook = new HSSFWorkbook();
	        Sheet sheet = workbook.createSheet("0");
	        
	        Row row = sheet.createRow(0);
	        
	        for (int i = 0; i < celllength; i++) {
	        	
	        	row.createCell(i).setCellValue( _getAttrNames[i]);

	        }
	        Row row1;
	        Object[] sqlrow2;
	        for (int j = 1; j < rowLength; j++) {
	        	 row1 = sheet.createRow(j);
	        	  sqlrow2 = find.get(j)._getAttrValues();
		        for (int i = 0; i < celllength-1; i++) {
		        	try {
						row1.createCell(i).setCellValue( sqlrow2[i].toString());
					} catch (Exception e) {
						continue;
					}
		        	

		        }
	        }
	        
//	        CellStyle cellStyle = workbook.createCellStyle();
//	        // 设置这些样式
//	        cellStyle.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
//	        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
////	        cellStyle.setBorderBottom(CellStyle.BORDER_THIN);
////	        cellStyle.setBorderLeft(CellStyle.BORDER_THIN);
////	        cellStyle.setBorderRight(CellStyle.BORDER_THIN);
////	        cellStyle.setBorderTop(CellStyle.BORDER_THIN);
////	        cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
	 
//	        row.createCell(0).setCellStyle(cellStyle);
	        //row.createCell(0).setCellValue("姓名");
	 
//	        row.createCell(1).setCellStyle(cellStyle);
	       // row.createCell(1).setCellValue("年龄");
	        
	        
	        
	        
	        
	 
	        //workbook.setSheetName(0, "信息");
	        try {
	        	//File file = new File("././POI/POI3.xls");
	           
	            FileOutputStream fileoutputStream = new FileOutputStream(file);
	            workbook.write(fileoutputStream);
	            fileoutputStream.close();
	          
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        try {
				workbook.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	 
	
	
}
