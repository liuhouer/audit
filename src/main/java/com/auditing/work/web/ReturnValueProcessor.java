package com.auditing.work.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class ReturnValueProcessor implements HandlerMethodReturnValueHandler, InitializingBean {
	public void afterPropertiesSet() throws Exception {
		
	}

	public boolean supportsReturnType(MethodParameter returnType) {
		 Class<?> returnClass = returnType.getMethod().getClass();
		 /*
		  * 只有定义的方法有返回值便一律使用ReturnValueProcessor转换处理
		  * 程序如果需要自定义返回值则可以设置void类型的返回值，自己写入response
		  */
		 if(returnClass==void.class){
			 return false;
		 }
		 return true;
	}

	public void handleReturnValue(Object returnValue,
                                  MethodParameter returnType, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest) throws Exception {
	   String result = null;
	   HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);
	   mavContainer.setRequestHandled(true);
	   response.setHeader("Access-Control-Allow-Origin", "*");
       response.setHeader("Content-Type", "application/json;charset=UTF-8");
       if(returnValue ==null){
    	   Map<String, Object> message = new HashMap<String, Object>();
           message.put("success", true);
           message.put("msg", "接口返回空");
           result = JSONObject.toJSONString(message);
       }else if(returnValue instanceof String){
    	   String stringValue = (String)returnValue;
    	   if(stringValue.length()<1){
    		   Map<String, Object> message = new HashMap<String, Object>();
               message.put("success", true);
               message.put("msg", "接口返回空");
               result = JSONObject.toJSONString(message);
    	   }else{
    		   result = stringValue;
    	   }
       }else{
           result = JSONObject.toJSONString(returnValue, SerializerFeature.DisableCircularReferenceDetect);
       }
       response.setCharacterEncoding("utf-8");
       response.getWriter().print(result);
      
	}
	
	public static void main(String[] args) {
		boolean b = Object.class.isAssignableFrom(ReturnValueProcessor.class);
		System.out.println(b);
	}
	
}
