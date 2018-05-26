package com.auditing.work.service;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class ExcelSheet {
	private Sheet sheet ;
	private  FormulaEvaluator evaluator;   
	public ExcelSheet(Workbook workbook,Sheet sheet ){
		this.sheet = sheet;
		this.evaluator = workbook.getCreationHelper().createFormulaEvaluator(); 
	}
	private  String dateFormat = "yyyy-MM-dd";
	private String decimal_format = "0";
	public String getCellValue(Integer rowIx,Integer cellIx){
		 Row row = sheet.getRow(rowIx);
        if (row == null)
            return "";
		 Cell cell = row.getCell(cellIx);

		 return  cellToString(cell);
	}
	
	public  String cellToString(Cell cell){
		  CellValue cellValue = evaluator.evaluate(cell);   
		  String cellString = "";
        if (cellValue == null) {       
            return cellString;   
        }   
        // 经过公式解析，最后只存在Boolean、Numeric和String三种数据类型，此外就是Error了   
        // 其余数据类型，根据官方文档，完全可以忽略http://poi.apache.org/spreadsheet/eval.html   
        switch (cellValue.getCellType()) {   
        case Cell.CELL_TYPE_BOOLEAN:   
      	  cellString = Boolean.toString(cellValue.getBooleanValue());
            break;   
        case Cell.CELL_TYPE_NUMERIC:   
            // 这里的日期类型会被转换为数字类型，需要判别后区分处理   
            if (DateUtil.isCellDateFormatted(cell)) {  
          	  	SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
          	    cellString = formatter.format(cell.getDateCellValue());               
            } else {  
          	  
				DecimalFormat df = new DecimalFormat(decimal_format);  
          	  
          	  cellString = df.format(cell.getNumericCellValue());  
          	//  cellString = Double.toString(  cellValue.getNumberValue());   
            }   
            break;   
        case Cell.CELL_TYPE_STRING:   
      	  cellString =  cellValue.getStringValue();   
            break;   
        case Cell.CELL_TYPE_FORMULA:   
            break;   
        case Cell.CELL_TYPE_BLANK:   
            break;   
        case Cell.CELL_TYPE_ERROR:   
            break;   
        default:   
            break;   
        }   
        
        return cellString;
	}

}
