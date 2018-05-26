package com.auditing.work.jpa.po;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;

import com.google.common.collect.Lists;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 公告
 */
@ApiModel(description = "新增公告")
public class AnnouncementCreateVo implements Serializable {

    private static final long serialVersionUID = 1L;

   

    /**
     * 用户Id
     */
    @ApiModelProperty(value = "用户Id", required = true)
    @Column(name = "user_id", nullable = false)
    private Long userId;

    /**
     * 标题
     */
    @ApiModelProperty(value = "标题", required = true)
    @Column(name = "title", nullable = false)
    private String title;

    /**
     * 内容
     */
    @ApiModelProperty(value = "内容", required = true)
    @Column(name = "content", length = 2000, nullable = false)
    private String content;



    
  
    @ApiModelProperty(value = "doc id list ")
    private List<Long> docList = Lists.newArrayList();
    
    public List<Long> getDocList() {
		return docList;
	}

	public void setDocList(List<Long> docList) {
		this.docList = docList;
	}



   
    public Long getUserId() {
        return userId;
    }

    public AnnouncementCreateVo userId(Long userId) {
        this.userId = userId;
        return this;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public AnnouncementCreateVo title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public AnnouncementCreateVo content(String content) {
        this.content = content;
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

  
   
}
