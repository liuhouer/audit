package com.auditing.work.controller;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.auditing.work.dal.dataobject.AuditingDetail;
import com.auditing.work.dal.dataobject.FirstCategory;
import com.auditing.work.dal.dataobject.FourthCategory;
import com.auditing.work.dal.dataobject.ReviewPoint;
import com.auditing.work.dal.dataobject.SecondCategory;
import com.auditing.work.dal.dataobject.ThirdCategory;
import com.auditing.work.enums.ResultCodeEnum;
import com.auditing.work.exception.WorkException;
import com.auditing.work.jpa.dao.AuditHistoryRepository;
import com.auditing.work.jpa.dao.DepartmentDao;
import com.auditing.work.jpa.po.AuditHistory;
import com.auditing.work.jpa.po.Department;
import com.auditing.work.modle.RowItem;
import com.auditing.work.modle.vo.TwoAuditHistory;
import com.auditing.work.result.BaseResult;
import com.auditing.work.service.AuditHistoryService;
import com.auditing.work.service.ReviewPointService;
import com.auditing.work.service.SysService;
import com.auditing.work.service.XlsTplService;
import com.auditing.work.service.XlsxFileService;
import com.google.common.collect.Maps;

import io.swagger.annotations.ApiOperation;
import net.sf.jxls.transformer.XLSTransformer;

@RequestMapping("/api/file")
@Controller
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FileController extends BaseController{
	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd") ;
    private static final String  CODE = "utf-8";

    @Autowired
    SysService sysService;
    
    @Autowired
    ReviewPointService reviewPointService;
    
    @Autowired
    XlsxFileService xlsxFileService;
    
    @Autowired
    XlsTplService xlsTplService;
    
	@Autowired
	AuditHistoryRepository auditHistoryRepository;
	 @RequestMapping(value = "/xls/auditHistory", method = RequestMethod.GET)
	    @ApiOperation(value="下载历史审核",notes = "下载历史审核")
	 public ResponseEntity<Void> auditHistory(
	    		@RequestParam(value="auditHistoryId")  List<Long> auditHistoryIds,				
				  HttpServletResponse response) throws Exception  {
		 
		 
		 List<AuditHistory> list = auditHistoryIds.stream().map(id->{
			 AuditHistory auditHistory = auditHistoryRepository.findOne(id);
//			 System.out.println(auditHistory);
		    	return auditHistory;
		 }).collect(Collectors.toList());
//		 System.out.println(list);
		 
			Resource resource =new  ClassPathResource("xls/auditHistoryList.xls");
	    	
	    	 InputStream is = resource.getInputStream();
	         XLSTransformer transformer = new XLSTransformer();
	         Map<String, Object> beans = Maps.newHashMap();
	         beans.put("list", list);
	         Workbook workbook = transformer.transformXLS(is, beans);
	        
	    	String dfileName = new String("历史审核数据".getBytes("utf-8"), "iso8859-1");

	
			 response.setContentType("application/octet-stream");// 设置强制下载不打开
			                  response.addHeader("Content-Disposition",
			                          "attachment;fileName=" + dfileName+".xls");// 设置文件名
	    
		    OutputStream out = response.getOutputStream();
		    workbook.write(out);
		 
			                 
			return ResponseEntity.ok().build();
	 }
	  @RequestMapping(value = "/xls/twoAuditHistory", method = RequestMethod.GET)
	    @ApiOperation(value="下载比较历史审核查询结果",notes = "下载比较历史审核查询结果")
	    public ResponseEntity<Void> TwoAuditHistory(
	    		@RequestParam(value="auditHistoryIdA")  Long auditHistoryIdA ,
				 @RequestParam(value="auditHistoryIdB")  Long auditHistoryIdB,
				  HttpServletResponse response) throws Exception  {
	      
		  
		  
	    	Resource resource =new  ClassPathResource("xls/twoAuditHistory.xls");
	    	
	    	 InputStream is = resource.getInputStream();
	         XLSTransformer transformer = new XLSTransformer();
	         Map<String, Object> beans = Maps.newHashMap();
	         TwoAuditHistory  twoAuditHistory= xlsTplService.getXlsTwoAuditHistoryViewList(auditHistoryIdA,auditHistoryIdB);
	         beans.put("list",twoAuditHistory.auditHistoryDetails );
	         beans.put("auditHistoryA",twoAuditHistory.auditHistoryA );
	         beans.put("auditHistoryB",twoAuditHistory.auditHistoryB );
	         Workbook workbook = transformer.transformXLS(is, beans);
	         String name = twoAuditHistory.auditHistoryA.getName()+"与"+twoAuditHistory.auditHistoryB.getName()+"的比较历史审核";
	    	String dfileName = new String(name.getBytes("utf-8"), "iso8859-1");

			 response.setContentType("application/octet-stream");// 设置强制下载不打开
			                  response.addHeader("Content-Disposition",
			                          "attachment;fileName=" + dfileName+".xls");// 设置文件名
	    
		    OutputStream out = response.getOutputStream();
		    workbook.write(out);
		 
			                 
			return ResponseEntity.ok().build();
	   
	    }
	  
    
    @RequestMapping(value = "/xls/reviewPoint", method = RequestMethod.GET)
    @ApiOperation(value="下载修改部门的模板信息",notes = "下载修改部门的模板信息")
    public ResponseEntity<Void> getReviewPointXls(HttpServletResponse response) throws Exception  {
      
    	Resource resource =new  ClassPathResource("xls/tpl.xls");
    	
    	 InputStream is = resource.getInputStream();
         XLSTransformer transformer = new XLSTransformer();
         Map<String, Object> beans = Maps.newHashMap();
         beans.put("list", reviewPointService.getXlsCellVoList());
         Workbook workbook = transformer.transformXLS(is, beans);
        
    	String dfileName = new String("审核要点数据".getBytes("utf-8"), "iso8859-1");

		 response.setContentType("application/octet-stream");// 设置强制下载不打开
		                  response.addHeader("Content-Disposition",
		                          "attachment;fileName=" + dfileName+".xls");// 设置文件名
    
	    OutputStream out = response.getOutputStream();
	    workbook.write(out);
	 
		                 
		return ResponseEntity.ok().build();
   
    }
    
    @RequestMapping(value = "/xls/fourthcategoryView", method = RequestMethod.GET)
    @ApiOperation(value="下载任务概览查询结果",notes = "下载任务概览查询结果")
    public ResponseEntity<Void> fourthcategoryView(HttpServletResponse response) throws Exception  {
      
    	Resource resource =new  ClassPathResource("xls/fourthcategoryView.xls");
    	
    	 InputStream is = resource.getInputStream();
         XLSTransformer transformer = new XLSTransformer();
         Map<String, Object> beans = Maps.newHashMap();
         beans.put("list", xlsTplService.getXlsFourthcategoryViewList());
         Workbook workbook = transformer.transformXLS(is, beans);
        
    	String dfileName = new String("任务概览数据".getBytes("utf-8"), "iso8859-1");

		 response.setContentType("application/octet-stream");// 设置强制下载不打开
		                  response.addHeader("Content-Disposition",
		                          "attachment;fileName=" + dfileName+".xls");// 设置文件名
    
	    OutputStream out = response.getOutputStream();
	    workbook.write(out);
	 
		                 
		return ResponseEntity.ok().build();
   
    }
    
    @RequestMapping(value = "/xls/reviewPoint", method = RequestMethod.POST)
    @ApiOperation(value="导入xls更新部门信息",notes = "导入xls更新部门信息", httpMethod = "POST")
    public ResponseEntity<BaseResult> updateDep(final HttpServletRequest request) {
    	 final BaseResult baseResult = new BaseResult();
	        baseResult.setResultCode(ResultCodeEnum.SUCCESS.getCode());
	        baseResult.setData("解析成功");
	        baseResult.setSuccess(true);
	        baseResult.setResultMessage(ResultCodeEnum.SUCCESS.getDesc());
         FileItem fileItem = findFile(request);
         if (fileItem==null||fileItem.getSize() == 0) {
             throw new WorkException(ResultCodeEnum.SYSTEM_ERROR, "文档为空!");
         }else{
             String fileName = fileItem.getName();
             fileName = fileName.toLowerCase();
             if(fileName.endsWith(".xls")){
                 try {
                	 	HSSFWorkbook w = new HSSFWorkbook(fileItem.getInputStream());	                    
                     	xlsxFileService.updateDep(w);
						} catch (Exception e) {
							e.printStackTrace();
	                        baseResult.setSuccess(false);
	                        baseResult.setResultCode("500");
	                        baseResult.setResultMessage(e.getMessage());
						}
             }else{
                 baseResult.setSuccess(false);
                 baseResult.setResultCode("400");
                 baseResult.setResultMessage("仅支持 xls 文件");
             }
         }
        
         return ResponseEntity.ok(baseResult);
    }
    
//    @RequestMapping(value = "/test", method = RequestMethod.GET)
//    @ResponseBody
//    public String test(){
//    	return xlsxFileService.toString();
//    }
    
    @RequestMapping(value = "/v1/importXlsxFile", method = RequestMethod.POST)
    @ApiOperation(value="超级管理员文件导入接口(v1)",notes = "超级管理员文件导入接口(v1)", httpMethod = "POST")
    public ResponseEntity<BaseResult> importXlsxFileV1(final HttpServletRequest request) {
          final BaseResult baseResult = new BaseResult();
		        baseResult.setResultCode(ResultCodeEnum.SUCCESS.getCode());
		        baseResult.setData("解析成功");
		        baseResult.setSuccess(true);
		        baseResult.setResultMessage(ResultCodeEnum.SUCCESS.getDesc());
                FileItem fileItem = findFile(request);
                if (fileItem==null||fileItem.getSize() == 0) {
                    throw new WorkException(ResultCodeEnum.SYSTEM_ERROR, "文档为空!");
                }else{
                    String fileName = fileItem.getName();
                    fileName = fileName.toLowerCase();
                    if(fileName.endsWith(".xlsx")){
	                    try {
	                        XSSFWorkbook w = new XSSFWorkbook(fileItem.getInputStream());	
	                        xlsxFileService.read(w);
							} catch (Exception e) {
								e.printStackTrace();
		                        baseResult.setSuccess(false);
		                        baseResult.setResultCode("500");
		                        baseResult.setResultMessage(e.getMessage());
							}
                    }else{
                        baseResult.setSuccess(false);
                        baseResult.setResultCode("400");
                        baseResult.setResultMessage("仅支持 xlsx 文件");
                    }
                }
               
                return ResponseEntity.ok(baseResult);
    }

    @RequestMapping(value = "/importXlsxFile", method = RequestMethod.POST)
    @ApiOperation(value="超级管理员文件导入接口",notes = "超级管理员文件导入接口", httpMethod = "POST")
    public ResponseEntity<BaseResult> importXlsxFile(final HttpServletRequest request) {
          final BaseResult baseResult = new BaseResult();
		        baseResult.setResultCode(ResultCodeEnum.SUCCESS.getCode());
		        baseResult.setData("解析成功");
		        baseResult.setSuccess(true);
		        baseResult.setResultMessage(ResultCodeEnum.SUCCESS.getDesc());
                FileItem fileItem = findFile(request);
                if (fileItem==null||fileItem.getSize() == 0) {
                    throw new WorkException(ResultCodeEnum.SYSTEM_ERROR, "文档为空!");
                }else{
                    String fileName = fileItem.getName();
                    fileName = fileName.toLowerCase();
                    if(fileName.endsWith(".xlsx")){
	                    try {
	                        XSSFWorkbook w = new XSSFWorkbook(fileItem.getInputStream());	
	                        XSSFSheet sheet = w.getSheetAt(0);	
	                        sysService.delAll();
								assemblydata2(sheet);
							} catch (Exception e) {
								e.printStackTrace();
		                        baseResult.setSuccess(false);
		                        baseResult.setResultCode("500");
		                        baseResult.setResultMessage(e.getMessage());
							}
                    }else{
                        baseResult.setSuccess(false);
                        baseResult.setResultCode("400");
                        baseResult.setResultMessage("仅支持 xlsx 文件");
                    }
                }
               
                return ResponseEntity.ok(baseResult);
    }

    
    
    /**
     * 获取姓名
     * @return 字符串
     */
//    @RequestMapping(value = "/importFile", method = RequestMethod.POST)
//    @ApiOperation(value="超级管理员文件导入接口",notes = "超级管理员文件导入接口", httpMethod = "POST")
//    public DeferredResult<String> importFile(final HttpServletRequest request) {
//        final DeferredResult<String> result = new DeferredResult<String>();
//        final BaseResult baseResult = new BaseResult();
//        processWithQuery(new BaseRequest(), baseResult, result, new BusinessProcess() {
//            public void doProcess() throws Exception {
//                FileItem fileItem = findFile(request);
//                if (fileItem==null||fileItem.getSize() == 0) {
//                    throw new WorkException(ResultCodeEnum.SYSTEM_ERROR, "文档为空!");
//                }else{
//                    Map<String, Object> resultMap = new HashMap<String, Object>();
//                    String fileName = fileItem.getName();
//                    fileName = fileName.toLowerCase();
//
//                    // 判断Excel类型
//                    if(fileName.endsWith(".xls")){
//
//                        HSSFWorkbook w = new HSSFWorkbook(fileItem.getInputStream());
//
//                        HSSFSheet sheet = w.getSheetAt(0);
//
//                        // 导入Excel校验
//                        importExcelFile(sheet, resultMap);
//
//                        baseResult.setSuccess(true);
//                        baseResult.setResultCode(ResultCodeEnum.SUCCESS.getCode());
//                        baseResult.setResultMessage(ResultCodeEnum.SUCCESS.getDesc());
//                        result.setResult(JSONObject.toJSONString(baseResult));
//                    }else if(fileName.endsWith(".xlsx")){
//
//                        XSSFWorkbook w = new XSSFWorkbook(fileItem.getInputStream());
//
//                        XSSFSheet sheet = w.getSheetAt(0);
//
//                        assemblydata2(sheet);
//                    }
//                }
//            }
//        });
//
//        return result;
//    }

    @Autowired DepartmentDao departmentDao;
    
    public void assemblydata2(XSSFSheet sheet) throws Exception{
        String type = null;
        String publicLevel = null;
//        System.out.println(sheet.getLastRowNum());
        for (int rows = 1; rows < sheet.getLastRowNum(); rows++) {//有多少行
            XSSFRow row = sheet.getRow(rows);//取得某一行   对象

            RowItem rowItem = buildRowItem(row);

            if (StringUtils.isNotBlank(rowItem.getLevel())){
                if (rowItem.getLevel().split("\\.").length == 1) {
                    FirstCategory first = firstCategoryMapper.selectByLevel(rowItem.getLevel());
                    if (null != first ){
                        continue;
                    }
                    FirstCategory firstCategory = new FirstCategory();
                    firstCategory.setLevel(rowItem.getLevel());
                    firstCategory.setName(rowItem.getStandards());
                    firstCategory.setRemarks("");
                    firstCategoryMapper.insert(firstCategory);

                } else if (rowItem.getLevel().split("\\.").length == 2) {
                    SecondCategory second = secondCategoryMapper.selectByLevel(rowItem.getLevel());
                    if (null != second) {
                        continue;
                    }
                    int index = rowItem.getLevel().lastIndexOf(".");
                    String firstLevel = rowItem.getLevel().substring(0, index);
                    FirstCategory first = firstCategoryMapper.selectByLevel(firstLevel);
                    if (null == first) {
                        throw new Exception("它没有一级类目");
                    }
                    SecondCategory secondCategory = new SecondCategory();
                    secondCategory.setLevel(rowItem.getLevel());
                    secondCategory.setName(rowItem.getStandards());
                    secondCategory.setFirst_id(first.getId());
                    secondCategory.setRemarks("");
                    secondCategoryMapper.insert(secondCategory);
                } else if (rowItem.getLevel().split("\\.").length == 3) {
                    ThirdCategory third = thirdCategoryMapper.selectByLevel(rowItem.getLevel());
                    if (null != third) {
                        continue;
                    }
                    int index = rowItem.getLevel().lastIndexOf(".");
                    String secondLevel = rowItem.getLevel().substring(0, index);
                    SecondCategory second = secondCategoryMapper.selectByLevel(secondLevel);
                    if (null == second) {
                        throw new Exception("它没有二级目录");
                    }
                    ThirdCategory thirdCategory = new ThirdCategory();
                    thirdCategory.setLevel(rowItem.getLevel());
                    thirdCategory.setName(rowItem.getStandards());
                    thirdCategory.setSecond_id(second.getId());
                    thirdCategory.setRemarks("");
                    thirdCategoryMapper.insert(thirdCategory);

                } else if (rowItem.getLevel().split("\\.").length == 4) {
                    publicLevel = rowItem.getLevel();
                    FourthCategory fourth = fourthCategoryMapper.selectByLevel(rowItem.getLevel());
                    if (null != fourth) {
                        continue;
                    }


                    int index = rowItem.getLevel().lastIndexOf(".");
                    String thirdLevel = rowItem.getLevel().substring(0, index);
                    ThirdCategory third = thirdCategoryMapper.selectByLevel(thirdLevel);
                    if (null == third) {
                        throw new Exception("它没有三级目录");
                    }



                    FourthCategory fourthCategory = new FourthCategory();
                    fourthCategory.setLevel(rowItem.getLevel());
                    fourthCategory.setName(rowItem.getStandards());
                    fourthCategory.setThird_id(third.getId());
                    fourthCategory.setRemarks("");
                    fourthCategory.setTotal_score("");
                    fourthCategoryMapper.insert(fourthCategory);
                }
            } else {
                FourthCategory fourth = fourthCategoryMapper.selectByLevel(publicLevel);
                if (null == fourth) {
                    throw new Exception("它没有四级目录");
                }


                if (type != null) {
                    if (type.equals("B")) {
                        rowItem.setTitle("符合'C',并");
                    } else if (type.equals("A")) {
                        rowItem.setTitle("符合'B',并");
                    }
                }
                if (rowItem.getDetail().contains("【A】")) {
                    type = "A";
                    continue;
                }
                if (rowItem.getDetail().contains("【B】")) {
                    type = "B";
                    continue;
                }
                if (rowItem.getDetail().contains("【C】")) {
                    type = "C";
                    continue;
                }

                rowItem.setType(type);


                ReviewPoint reviewPoint = new ReviewPoint();
                reviewPoint.setStatus(0);
                reviewPoint.setFourth_id(fourth.getId());
                reviewPoint.setScore(rowItem.getType());
                reviewPoint.setTitle("");
                reviewPoint.setDetail("");
                reviewPoint.setRemarks("");
                reviewPoint.setAttachment("");
                reviewPoint.setPassed(null);
                reviewPoint.setDepartment_id(rowItem.getDepartment());
                reviewPoint.setIsEdit(true);
                if (null != rowItem.getTitle()) {
                    reviewPoint.setTitle(rowItem.getTitle());
                }
                reviewPoint.setDetail(rowItem.getDetail());
//                if (StringUtils.isBlank(reviewPoint.getDetail())) {
//                	continue ;
//				}
//                System.out.println(reviewPoint.getDetail());
                for (String depName : StringUtils.split(rowItem.getDepartment(), ",")) {
					Department department = this.departmentDao.findByName(depName);
					if (department == null) {
						department = new Department();
						department.setName(depName);
						this.departmentDao.save(department);
					}
				}
              
                
                reviewPointMapper.insert(reviewPoint);
                
                
                
                
            }

        }
    }


    public FileItem findFile(HttpServletRequest request) {
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

    /**
     * 校验导入Excel文件
     * @param sheet
     * @return
     */
    @SuppressWarnings("unused")
	private boolean importExcelFile(HSSFSheet sheet, Map<String, Object> resultMap) throws Exception{

        // 1. 初始化导入统计
        List<RowItem> importRowList = new ArrayList<RowItem>();
        resultMap.put("result", "true");
        resultMap.put("importRowList", importRowList);
        resultMap.put("importNum", sheet.getLastRowNum());
        resultMap.put("checkFailedNum", 0);
        resultMap.put("importSuccessNum", 0);
        resultMap.put("importFailedNum", 0);

        // 2. 校验文件是否为空
        if(sheet.getLastRowNum() < 1){
            resultMap.put("result", "false");
            resultMap.put("resultMsg", "导入的Excel文件为空，请确认要导入的数据在第一个表单中，请确认有无隐藏表单！");
            return false;
        }

        // 3. 校验是否都为空行并且插入
        int blankNum = 0;
        int checkFailedNum = 0;
        List<String> ids = new ArrayList<String>();
        StringBuilder importCheckResult = new StringBuilder();
        String type = null;
        String publicLevel = null;
        String publicStandards = null;
        for(int i = 1; i <= sheet.getLastRowNum(); i++){
            HSSFRow row = sheet.getRow(i);
            if(isBlankRow(row)){
                blankNum++;
                continue;
            }

            RowItem rowItem =buildRowItem(row);

            if (StringUtils.isNotBlank(rowItem.getLevel())){
                type = null;
                publicLevel = null;
                publicStandards = null;
                if (rowItem.getLevel().split("\\.").length == 4) {
                    publicLevel = rowItem.getLevel();
                    publicStandards = rowItem.getStandards();
                    continue;
                }
            }
            if(type!=null) {
                rowItem.setType("符合'" + type + "',并");
            }
            if (rowItem.getDetail().contains("【A】")){
                type = "A";
                continue;
            }
            if (rowItem.getDetail().contains("【B】")){
                type = "C";
                continue;
            }
            if (rowItem.getDetail().contains("【C】")){
                type = "C";
                continue;
            }
            rowItem.setLevel(publicLevel);
            rowItem.setStandards(publicStandards);

            importRowList.add(rowItem);
            AuditingDetail auditingDetail = buildAudtingDetail(rowItem);
            auditingDetailMapper.insert(auditingDetail);
        }
        if(blankNum == sheet.getLastRowNum()){
            resultMap.put("result", "false");
            resultMap.put("resultMsg", "导入的Excel文件为空！");
            return false;
        }else{
            resultMap.put("importNum", sheet.getLastRowNum() - blankNum);
        }

        return true;
    }

    /**
     * 行非空校验
     * @param row
     * @return
     */
    private boolean isBlankRow(HSSFRow row) {
        if (row == null)
            return true;
        for (int i = row.getFirstCellNum(); i < row.getLastCellNum(); i++) {
            HSSFCell hcell = row.getCell(i);
            if (!isBlankCell(hcell))
                return false;
        }
        return true;
    }

    /**
     * Cell非空校验
     * @param hcell
     * @return
     */
    private boolean isBlankCell(HSSFCell hcell) {
        if(null == hcell || hcell.getCellType() == HSSFCell.CELL_TYPE_BLANK){
            return true;
        }else{
            return false;
        }
    }

    private RowItem buildRowItem(HSSFRow row) throws Exception{
        RowItem rowItem = new RowItem();
        if(null == row){
            return rowItem;
        }
        rowItem.setLevel((null == row.getCell(0)) ? null : new String(row.getCell(0).toString().getBytes(CODE),"utf-8"));
        rowItem.setStandards((null == row.getCell(1)) ? null : new String(row.getCell(1).toString().getBytes(CODE),"utf-8"));
        rowItem.setDetail((null == row.getCell(2)) ? null : new String(row.getCell(2).toString().getBytes(CODE),"utf-8"));
        rowItem.setDepartment((null == row.getCell(3)) ? null : new String(row.getCell(3).toString().getBytes(CODE),"utf-8"));
        rowItem.setResult((null == row.getCell(4)) ? null : new String(row.getCell(4).toString().getBytes(CODE),"utf-8"));

        return rowItem;
    }

    private RowItem buildRowItem(XSSFRow row) throws Exception{
        RowItem rowItem = new RowItem();
        if(null == row){
            return rowItem;
        }
        String level = (null == row.getCell(0)) ? null : new String(row.getCell(0).toString().getBytes(CODE),"utf-8");
        if (StringUtils.isNotBlank(level)){
            String[] strs = level.split("\\.");
            if (strs.length==2){
                if (strs[1].equals("0")){
                    level = strs[0];
                }
            }
        }
        rowItem.setLevel(level);
        rowItem.setStandards((null == row.getCell(1)) ? null : new String(row.getCell(1).toString().getBytes(CODE),"utf-8"));
//        System.out.println(new String(row.getCell(1).toString().getBytes("gbk"),"utf-8"));
//        System.out.println(new String(row.getCell(1).toString().getBytes("gb2312"),"utf-8"));
//        System.out.println(new String(row.getCell(1).toString().getBytes("ISO8859_1"),"utf-8"));
//        System.out.println(new String(row.getCell(1).toString().getBytes("utf-8"),"utf-8"));
//        System.out.println(new String(row.getCell(1).toString().getBytes(CODE),"utf-8"));
        rowItem.setDetail((null == row.getCell(2)) ? null : new String(row.getCell(2).toString().getBytes(CODE),"utf-8"));
        rowItem.setDepartment((null == row.getCell(3)) ? null : new String(row.getCell(3).toString().getBytes(CODE),"utf-8"));
        rowItem.setResult((null == row.getCell(4)) ? null : new String(row.getCell(4).toString().getBytes(CODE),"utf-8"));

        return rowItem;
    }
}
