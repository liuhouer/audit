package com.auditing.work.controller;

import java.io.File;

import org.junit.Test;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.auditing.work.jpa.po.ReviewPointDoc;
import com.auditing.work.result.BaseResult;

public class MyDebug  {
	RestTemplate restTemplate = new RestTemplate();

	


	  
	 @Test
//	 @Ignore
	 public void testNewDoc(){
		 String base_host = "http://localhost:8888/api/";
		 RestTemplate rest = new RestTemplate();  
		    String filePath = "G:\\work\\audit\\src\\test\\resources\\testFile.txt";
			FileSystemResource resource = new FileSystemResource(new File(filePath ));  
		    MultiValueMap<String, Object> param = new LinkedMultiValueMap<String, Object>();  
		    param.add("file", resource);  
		    param.add("fileName", "test.txt");  
		    ResponseEntity<ReviewPointDoc> res = 
		    		rest.postForEntity(base_host+"reviewPointDoc?reviewPointId=34&userId=8", param, ReviewPointDoc.class);  
		    System.out.println(res.getStatusCode());
		    System.out.println(res.getBody());
	 }
	 
	 @Test
	 public void testimportXlsxFile() {
		 //	 String base_host = "http://116.62.8.110:8088/com.auditing/api/";
		 String base_host = "http://localhost:8888/api/";
		 
		 RestTemplate rest = new RestTemplate();  
		    String filePath = "G:\\work\\audit\\doc\\肿瘤医院三甲标准2016全内容.xlsx";
			FileSystemResource resource = new FileSystemResource(new File(filePath ));  
		    MultiValueMap<String, Object> param = new LinkedMultiValueMap<String, Object>();  
		    param.add("file", resource);  
		    param.add("fileName", "final.xlsx");  
		    ResponseEntity<BaseResult> res = 
		    		rest.postForEntity(base_host+"file/v1/importXlsxFile", param, BaseResult.class);  
		    System.out.println(res.getStatusCode());
		    System.out.println(res.getBody());
	 }
	 
	 
	 @SuppressWarnings("rawtypes")
	@Test
	 public void test() {
		 String base_host ="http://localhost:8888/api/";
		 RestTemplate rest = new RestTemplate();  
		
		 LinkedMultiValueMap<String, Object> param = new LinkedMultiValueMap<String, Object>();  
		    param.add("userName", "superadmin");  
		    param.add("id", "34");  
		    param.add("passed", "true"); 
		    param.add("status", "1");  
	
		    
	
		    		HttpHeaders headers = new HttpHeaders();  
		    		headers.setContentType(MediaType.parseMediaType(MediaType.APPLICATION_FORM_URLENCODED_VALUE));  
		    		HttpEntity<LinkedMultiValueMap> req = new HttpEntity<LinkedMultiValueMap>(param,headers);  
		    
		    			    ResponseEntity<BaseResult> res = 
				    		rest.postForEntity(base_host+"reviewpoints/update",
				    				req, BaseResult.class);  
		    
		    System.out.println(res.getStatusCode());
		    System.out.println(res.getBody());
	 }

}
