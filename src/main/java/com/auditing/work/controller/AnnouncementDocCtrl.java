package com.auditing.work.controller;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.auditing.work.enums.ResultCodeEnum;
import com.auditing.work.jpa.dao.AnnouncementDocRepository;
import com.auditing.work.jpa.po.AnnouncementDoc;
import com.auditing.work.jpa.po.ReviewPointDoc;
import com.auditing.work.result.BaseResult;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/api")
@Api(description="公告附件 API")
public class AnnouncementDocCtrl {
	

	@Autowired 
	AnnouncementDocRepository announcementDocRepository;
	
	

	  
	
	  
    @RequestMapping(value = "/announcementDoc", method = RequestMethod.POST)
    @ApiOperation(value="创建公告附件",notes = "创建公告附件", httpMethod = "POST")
    @ApiResponses(value = {  	
    		@ApiResponse(code=400,message="创建失败,上传文件为空"),
 	    	@ApiResponse(code=200,message="创建成功",response=ReviewPointDoc.class)
 	 })
    @Transactional
	public ResponseEntity<BaseResult> add(
			@ApiParam(name="announcementId",value="表 announcement_doc 的 id",required=false) 
			@RequestParam(value="announcementId",required=false) Long  announcementId,
			@ApiParam(name="userId",value="表 users 的 id",required=false) 
			@RequestParam("userId") Long  userId,
			final HttpServletRequest request){
    	 BaseResult baseResult = new BaseResult();
    	 AnnouncementDoc announcementDoc = new AnnouncementDoc();
    	FileItem fileItem = findFile(request);
    	 if (fileItem==null||fileItem.getSize() == 0) {
    		  baseResult.setResultCode("400");
 	          baseResult.setSuccess(false);
 	          baseResult.setResultMessage("file Content is null");
        	  return ResponseEntity.ok(baseResult);
         }
    	 String fileName = fileItem.getName();
         fileName = fileName.toLowerCase();
         announcementDoc.setDocName(fileName);
         announcementDoc.setDoc(fileItem.get());
         if (announcementId != null) {
        	 announcementDoc.setAnnouncementId(announcementId);
		 }
        
         announcementDoc.setUploadDate(new Date());
         announcementDoc.setUserId(userId);
         AnnouncementDoc res = announcementDocRepository.save(announcementDoc);
    	 
    	
         baseResult.setData(res);
         baseResult.setResultCode(ResultCodeEnum.SUCCESS.getCode());
         baseResult.setSuccess(true);
         baseResult.setResultMessage(ResultCodeEnum.SUCCESS.getDesc());
         return ResponseEntity.ok(baseResult);
        
	} 
    
	 private FileItem findFile(HttpServletRequest request) {
	        MultipartResolver resolver = new CommonsMultipartResolver(request.getSession().getServletContext());
	        MultipartHttpServletRequest multipartRequest = resolver.resolveMultipart(request);

	        //DefaultMultipartHttpServletRequest request1 = (DefaultMultipartHttpServletRequest) request;
	        CommonsMultipartFile mf = (CommonsMultipartFile) multipartRequest.getFile("file");
	        if (mf != null && mf.getFileItem() != null && mf.getFileItem().getSize()>0) {
	            return mf.getFileItem();
	        } else {
	            return null;
	        }
	    }
	
    
    @RequestMapping(value = "/announcementDoc/{id}/doc", method = RequestMethod.GET)
    @ApiOperation(value="下载公告附件",notes = "下载公告附件", httpMethod = "GET")
    @Transactional(readOnly=true)
	public ResponseEntity<byte[]> download(@PathVariable("id") Long  id) throws IOException {
		
		AnnouncementDoc announcementDoc = announcementDocRepository.findOne(id);
		String dfileName = new String(announcementDoc.getDocName().getBytes("utf-8"), "iso8859-1");
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		headers.setContentDispositionFormData("attachment", dfileName);
		return new ResponseEntity<byte[]>(announcementDoc.getDoc(), headers, HttpStatus.CREATED);
	}

	
    @RequestMapping(value = "/announcementDoc/{id}", method = RequestMethod.DELETE)
    @ApiOperation(value="删除公告附件",notes = "删除公告附件", httpMethod = "DELETE")
    @Transactional()
	public ResponseEntity<Void> del(@PathVariable("id") Long  id) throws IOException {
		
    	announcementDocRepository.delete(id);
	
	
		return ResponseEntity.ok().build();
	}

	
	    @RequestMapping(value = "/announcementDoc/list", method = RequestMethod.GET)
	    @ApiOperation(value="公告附件列表",notes = "公告附件列表", httpMethod = "GET")
	    @Transactional(readOnly=true)
		public ResponseEntity<BaseResult> list(
				@ApiParam(name="announcementId",value="表 announcement_doc 的 id",required=true) 
				@RequestParam("announcementId") Long  announcementId ) throws IOException {
			
			List<AnnouncementDoc> list = announcementDocRepository.findByAnnouncementId(announcementId);
			list.forEach(data->{
				data.doc(null);
			});
		     BaseResult baseResult = new BaseResult();
	         baseResult.setData(list);
	         baseResult.setResultCode(ResultCodeEnum.SUCCESS.getCode());
	         baseResult.setSuccess(true);
	         baseResult.setResultMessage(ResultCodeEnum.SUCCESS.getDesc());
	         return ResponseEntity.ok(baseResult);
			
		}
	  
	
    


}
