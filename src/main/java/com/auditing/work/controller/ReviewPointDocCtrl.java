package com.auditing.work.controller;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.auditing.work.dal.daointerface.ReviewPointMapper;
import com.auditing.work.dal.dataobject.ReviewPoint;
import com.auditing.work.enums.ResultCodeEnum;
import com.auditing.work.jpa.dao.ReviewPointDocRepository;
import com.auditing.work.jpa.po.ReviewPointDoc;
import com.auditing.work.modle.jf.DepartmentModel;
import com.auditing.work.modle.jf.ReviewPointDocModel;
import com.auditing.work.modle.jf.UserModel;
import com.auditing.work.result.BaseResult;
import com.auditing.work.util.CacheHelper;
import com.google.common.collect.Lists;
import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import jodd.io.FileUtil;

@RestController
@RequestMapping("/api")
@Api(description="评审附件 API")
public class ReviewPointDocCtrl extends BaseController {
	

	@Autowired 
	ReviewPointDocRepository reviewPointDocRepository;
	  @Autowired
	    protected ReviewPointMapper reviewPointMapper;

    @RequestMapping(value = "/reviewPointDoc", method = RequestMethod.POST)
    @ApiOperation(value="创建评审附件",notes = "创建评审附件", httpMethod = "POST")
    @ApiResponses(value = {  	
    		@ApiResponse(code=400,message="创建失败,上传文件为空"),
 	    	@ApiResponse(code=200,message="创建成功",response=ReviewPointDoc.class)
 	 })
	public ResponseEntity<BaseResult> add(
			@ApiParam(name="reviewPointId",value="表 review_point 的 id",required=true) 
			@RequestParam("reviewPointId") Long  reviewPointId,
			@ApiParam(name="userId",value="表 users 的 id",required=true) 
			@RequestParam("userId") Long  userId,
			@RequestParam(required=false, value="force", defaultValue="0") Integer force, 
			final HttpServletRequest request) throws IOException{
    	
    	try {
			
		
    	 BaseResult baseResult = new BaseResult();
//    	ReviewPointDoc reviewPointDoc = new ReviewPointDoc();
    	FileItem fileItem = findFile(request);
    	 if (fileItem==null||fileItem.getSize() == 0) {
    		  baseResult.setResultCode("400");
 	          baseResult.setSuccess(false);
 	          baseResult.setResultMessage("file Content is null");
        	  return ResponseEntity.ok(baseResult);
         }
    	 
    	 String fileName = fileItem.getName();
         fileName = fileName.toLowerCase();
         
         if(force==0) {
        	 String sql = "select * from review_point_doc where review_point_id = ? and doc_name = ?";
        	 List<ReviewPointDocModel> list = ReviewPointDocModel.dao.find(sql, reviewPointId.intValue(), fileName);
        	 if(CollectionUtils.isNotEmpty(list)) {
        		 baseResult.setResultCode(ResultCodeEnum.REVIEWPOINT_DOC_NAME_EXISTS.getCode());
                 baseResult.setSuccess(true);
                 baseResult.setResultMessage(ResultCodeEnum.REVIEWPOINT_DOC_NAME_EXISTS.getDesc());
                 return ResponseEntity.ok(baseResult);
        	 }
         } else if(force==1) {
        	 Db.update("delete from review_point_doc where review_point_id = ? and doc_name = ?", reviewPointId.intValue(), fileName);
         }
    	 
    	 ReviewPoint reviewPoint = reviewPointMapper.selectReviewPointById(reviewPointId.intValue());
    	 
//         reviewPointDoc.setDocName(fileName);
//         reviewPointDoc.setDoc(fileItem.get());
//         reviewPointDoc.setReviewPointId(reviewPointId);
//         reviewPointDoc.setUploadDate(new Date());
//         reviewPointDoc.setUserId(userId);
//         reviewPointDoc.setFourthId(reviewPoint.getFourth_id());
//    	 ReviewPointDoc res = reviewPointDocRepository.save(reviewPointDoc);
         
         byte[] bytes = fileItem.get();
         String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
         String path = getProp("doc.path") + "/" + UUID.randomUUID().toString() + "." + suffix;
         System.out.println(path);
         File file = new File(path);
         if(!file.exists()) {
        	file.createNewFile();
         }
         FileUtil.writeBytes(file, bytes);
         
         ReviewPointDocModel model = new ReviewPointDocModel();
         model.set("doc_name", fileName);
         model.set("review_point_id", reviewPointId);
         model.set("user_id", userId);
         model.set("fourth_id", reviewPoint.getFourth_id());
         model.set("upload_date", new Date());
    	 model.set("path", path);
    	 model.save();
    	
    	 System.out.println(model.getLong("id"));
    	 
         baseResult.setData(reviewPointDocRepository.findOne(model.getLong("id")));
         baseResult.setResultCode(ResultCodeEnum.SUCCESS.getCode());
         baseResult.setSuccess(true);
         baseResult.setResultMessage(ResultCodeEnum.SUCCESS.getDesc());
         return ResponseEntity.ok(baseResult);
    	} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
        
	} 
    
    public static void main(String[] args) {
    	String fileName = "2222.docx";
    	String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
		System.out.println(suffix);
	}
    
    
    @RequestMapping(value = "/reviewPointDoc/{id}/doc", method = RequestMethod.GET)
    @ApiOperation(value="下载评审附件",notes = "下载评审附件", httpMethod = "GET")
	public ResponseEntity<byte[]> download(@PathVariable("id") Long  id) throws IOException {
		
		ReviewPointDoc reviewPointDoc = reviewPointDocRepository.findOne(id);
		String dfileName = new String(reviewPointDoc.getDocName().getBytes("utf-8"), "iso8859-1");
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		headers.setContentDispositionFormData("attachment", dfileName);
		
		String path = reviewPointDoc.getPath();
		File file = new File(path);
		byte[] bytes = FileUtil.readBytes(file);
		
		return new ResponseEntity<byte[]>(bytes, headers, HttpStatus.CREATED);
	}

	@Autowired
	private CacheHelper cacheHelper;
	
	    @RequestMapping(value = "/reviewPointDoc/list", method = RequestMethod.GET, produces="application/json;charset=UTF-8")
	    @ApiOperation(value="评审附件列表",notes = "评审附件列表", httpMethod = "GET")
		public String list(
			@ApiParam(name="reviewPointId",value="表 review_point 的 id",required=true) 
			@RequestParam("reviewPointId") Long  reviewPointId ) throws IOException {
			
//			List<ReviewPointDoc> list = reviewPointDocRepository.findByReviewPointId(reviewPointId);
			String sql = "select * from review_point_doc where review_point_id = ?";
			List<ReviewPointDocModel> list = ReviewPointDocModel.dao.find(sql, reviewPointId);
			for(ReviewPointDocModel doc : list) {
				long userid = doc.getLong("user_id");
				UserModel user = cacheHelper.getCacheUserById(userid);
				doc.put("user_name", user.getStr("user_name"));
				Integer depid = user.getInt("department_id");
				if(depid!=null) {
					DepartmentModel dep = cacheHelper.getCacheDepartmentById(depid.longValue());
					doc.put("dep_name", dep.getStr("name"));
				}
			}
//			reviewPointDocList.forEach(data->{
//				data.doc(null);
//			});
		     BaseResult baseResult = new BaseResult();
	         baseResult.setData(list);
	         baseResult.setResultCode(ResultCodeEnum.SUCCESS.getCode());
	         baseResult.setSuccess(true);
	         baseResult.setResultMessage(ResultCodeEnum.SUCCESS.getDesc());
	         return JsonKit.toJson(baseResult);
			
		}
	    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); 
	    @RequestMapping(value = "/reviewPointDoc/query", method = RequestMethod.GET)
	    @ApiOperation(value="评审附件查询",notes = "评审附件查询", httpMethod = "GET")
	    public List<ReviewPointDoc> query(
	    		@RequestParam(value="uploadDateStart",required=false) String  uploadDateStart,
	    		@RequestParam(value="uploadDateEnd",required=false) String  uploadDateEnd,
	    		@RequestParam(value="fourthId",required=false) Integer  fourthId,
	    		@RequestParam(value="userId",required=false) Long  userId,
				@RequestParam(value="fileName",required=false) String  fileName){
	    	Specification<ReviewPointDoc> spec = new Specification<ReviewPointDoc>() {
				
				@Override
				public Predicate toPredicate(Root<ReviewPointDoc> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
					List<Predicate> predicates = Lists.newArrayList();

					if (fourthId != null) {
						predicates.add(cb.equal(root.get("fourthId").as(Integer.class), fourthId));
					}
					if (userId != null) {
						predicates.add(cb.equal(root.get("userId").as(Long.class), userId));
					}
					if (StringUtils.isNotBlank(fileName)) {
						predicates.add(cb.like(root.get("docName").as(String.class), "%"+fileName+"%"));
					}
					if (StringUtils.isNotBlank(uploadDateStart) ) {
						try {
							predicates.add(cb.greaterThanOrEqualTo(root.get("uploadDate").as(Date.class), formatter.parse(uploadDateStart)));
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
					if (StringUtils.isNotBlank(uploadDateEnd) ) {
						try {
							predicates.add(cb.lessThanOrEqualTo(root.get("uploadDate").as(Date.class), formatter.parse(uploadDateEnd)));
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
					if (predicates.isEmpty()) {
						return cb.conjunction();
					}
					return cb.and(predicates.toArray(new Predicate[predicates.size()]));
				}
			};
			List<ReviewPointDoc> list = reviewPointDocRepository.findAll(spec);
//			list.forEach(data->{
//				data.doc(null);
//			});
			return list;
	    }
	
	    @RequestMapping(value = "/reviewPointDoc/del", method = RequestMethod.GET, produces="application/json;charset=UTF-8")
	    @ApiOperation(value="删除评审附件",notes = "删除评审附件", httpMethod = "GET")
		public String del(
			@ApiParam(name="ids",value="文档id串（逗号分隔）",required=true) 
			@RequestParam(value="ids", required=true) String ids ) throws IOException {
	    	
	    	Db.tx(new IAtom() {
				@Override
				public boolean run() throws SQLException {
					String[] idz = ids.split(",");
			    	for(String id : idz) {
			    		ReviewPointDocModel model = ReviewPointDocModel.dao.findById(id);
			    		String path = model.getStr("path");
			    		try {
							FileUtil.delete(path);
						} catch (IOException e) {
							e.printStackTrace();
							throw new SQLException();
						}
			    		model.delete();
			    	}
					return true;
				}
			});
	    	return success();
	    }

}
