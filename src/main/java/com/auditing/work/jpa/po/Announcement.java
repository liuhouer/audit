package com.auditing.work.jpa.po;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 公告
 */
@ApiModel(description = "公告")
@Entity
@Table(name = "announcement")
public class Announcement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

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

    /**
     * 创建时间，非必填，系统自动生成
     */
    @ApiModelProperty(value = "创建时间，非必填，系统自动生成")
    @Column(name = "creation_time")
    @JsonIgnore
    private LocalDate creationTime;

    
    
    
  

	@Transient
    private String userName;
    
    @Transient
    private String creationTimeView;

    public String getCreationTimeView() {
		return creationTimeView;
	}

	public void setCreationTimeView(String creationTimeView) {
		this.creationTimeView = creationTimeView;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public Announcement userId(Long userId) {
        this.userId = userId;
        return this;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public Announcement title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public Announcement content(String content) {
        this.content = content;
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDate getCreationTime() {
        return creationTime;
    }

    public Announcement creationTime(LocalDate creationTime) {
        this.creationTime = creationTime;
        return this;
    }

    public void setCreationTime(LocalDate creationTime) {
        this.creationTime = creationTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Announcement announcement = (Announcement) o;
        if (announcement.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, announcement.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Announcement{" +
            "id=" + id +
            ", userId='" + userId + "'" +
            ", title='" + title + "'" +
            ", content='" + content + "'" +
            ", creationTime='" + creationTime + "'" +
            '}';
    }
}
