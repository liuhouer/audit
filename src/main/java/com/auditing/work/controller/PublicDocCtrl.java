package com.auditing.work.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
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

import com.auditing.work.dal.daointerface.UsersMapper;
import com.auditing.work.dal.dataobject.Users;
import com.auditing.work.enums.ResultCodeEnum;
import com.auditing.work.jpa.dao.PublicDocRepository;
import com.auditing.work.jpa.po.PublicDoc;
import com.auditing.work.jpa.po.ReviewPointDoc;
import com.auditing.work.result.BaseResult;
import com.google.common.collect.Lists;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/api")
@Api(description="公开文档 API")
public class PublicDocCtrl {
	

	@Autowired 
	PublicDocRepository publicDocRepository;
	 
	  @Autowired
	    protected UsersMapper usersMapper;
	  
	
	  
    @RequestMapping(value = "/publicDoc", method = RequestMethod.POST)
    @ApiOperation(value="创建公开文档",notes = "创建公开文档", httpMethod = "POST")
    @ApiResponses(value = {  	
    		@ApiResponse(code=400,message="创建失败,上传文件为空"),
 	    	@ApiResponse(code=200,message="创建成功",response=ReviewPointDoc.class)
 	 })
    @Transactional
	public ResponseEntity<BaseResult> add(
			@ApiParam(name="userId",value="表 users 的 id",required=true) 
			@RequestParam("userId") Long  userId,
			final HttpServletRequest request){
    	 BaseResult baseResult = new BaseResult();
    	 PublicDoc publicDoc = new PublicDoc();
    	FileItem fileItem = findFile(request);
    	 if (fileItem==null||fileItem.getSize() == 0) {
    		  baseResult.setResultCode("400");
 	          baseResult.setSuccess(false);
 	          baseResult.setResultMessage("file Content is null");
        	  return ResponseEntity.ok(baseResult);
         }
    	 String fileName = fileItem.getName();
         fileName = fileName.toLowerCase();
         publicDoc.setDocName(fileName);
         publicDoc.setDoc(fileItem.get());
         publicDoc.setUploadDate(new Date());
         publicDoc.setUserId(userId);
         PublicDoc res = publicDocRepository.save(publicDoc);
    	 
    	
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
	
    
    @RequestMapping(value = "/publicDoc/{id}/doc", method = RequestMethod.GET)
    @ApiOperation(value="下载公开文档",notes = "下载公开文档", httpMethod = "GET")
    @Transactional(readOnly=true)
	public ResponseEntity<byte[]> download(@PathVariable("id") Long  id) throws IOException {
		
		PublicDoc publicDoc = publicDocRepository.findOne(id);
		String dfileName = new String(publicDoc.getDocName().getBytes("utf-8"), "iso8859-1");
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		headers.setContentDispositionFormData("attachment", dfileName);
		return new ResponseEntity<byte[]>(publicDoc.getDoc(), headers, HttpStatus.CREATED);
	}

	    @RequestMapping(value = "/publicDoc/{id}", method = RequestMethod.DELETE)
	    @ApiOperation(value="删除公开文档",notes = "删除公开文档", httpMethod = "DELETE")
	    @Transactional()
		public ResponseEntity<Void> del(@PathVariable("id") Long  id) throws IOException {
			
			publicDocRepository.delete(id);
		
		
			return ResponseEntity.ok().build();
		}
	

	    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); 
	    @RequestMapping(value = "/publicDoc/list", method = RequestMethod.GET)
	    @ApiOperation(value="公共文件查询",notes = "公共文件查询", httpMethod = "GET")
	    @Transactional(readOnly=true)
	    public List<PublicDoc> query(
				@RequestParam(value="fileName",required=false) String  fileName){
	    	Specification<PublicDoc> spec = new Specification<PublicDoc>() {
				
				@Override
				public Predicate toPredicate(Root<PublicDoc> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
					List<Predicate> predicates = Lists.newArrayList();

					
					if (StringUtils.isNotBlank(fileName)) {
						predicates.add(cb.like(root.get("docName").as(String.class), "%"+fileName+"%"));
					}
				
					if (predicates.isEmpty()) {
						return cb.conjunction();
					}
					return cb.and(predicates.toArray(new Predicate[predicates.size()]));
				}
			};
			
			List<PublicDoc> list = publicDocRepository.findAll(spec);
			list.forEach(doc->{
				doc.setDoc(null);
				Users user = usersMapper.selectByPrimaryKey(doc.getUserId());
				if (user != null) {
					doc.setUserName(user.getUserName()  );
				
				}
			});	
			return list;
	    }
	
    


}
