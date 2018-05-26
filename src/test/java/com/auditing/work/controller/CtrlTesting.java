package com.auditing.work.controller;

import java.util.Map;

import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.auditing.work.result.BaseResult;
import com.google.common.collect.Maps;


public class CtrlTesting {
	 protected RestTemplate template = new RestTemplate();
	 @Test
	 public void testLogin() {
		 String base_host = "http://116.62.8.110:8088/com.auditing/api/";
//		 String base_host = "http://localhost:8888/api/";
		 RestTemplate rest = new RestTemplate();  
		 Map<String,Object> data = Maps.newHashMap();
		 data.put("userName", "admin");
		 data.put("password", "123456");
		    ResponseEntity<BaseResult> res = 
		    		rest.postForEntity(base_host+"user/login", data, BaseResult.class);  
		    System.out.println(res.getStatusCode());
		    System.out.println(res.getBody());
		}

}