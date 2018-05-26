package com.auditing.work.service;

import java.text.NumberFormat;

public class NumberUtil {

	public static String getBaiFengBi(int diliverNum, int  queryMailNum){
//		int diliverNum=3;//举例子的变量
//		int queryMailNum=9;//举例子的变量
		if (queryMailNum == 0) {
			return "0";
		}
		// 创建一个数值格式化对象   
	    	         NumberFormat numberFormat = NumberFormat.getInstance();   
	    	          // 设置精确到小数点后2位   
	     	         numberFormat.setMaximumFractionDigits(2);   
	    	         String result = numberFormat.format((float)diliverNum/(float)queryMailNum*100);
	    	         return result;
	}
}
