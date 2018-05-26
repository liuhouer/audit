package com.auditing.work.jpa.po;

import java.io.Serializable;
import java.time.ZonedDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Entity ReviewPoint
 */

@Entity
@Table(name = "review_point_comment")
public class ReviewPointComment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "review_point_id")
    private Long reviewPointId;

    @Column(name = "content")
    private String content;

    @Column(name = "time")
    private ZonedDateTime time;

    @Column(name = "user_id")
    private Long userId;
    
    public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getReviewPointId() {
        return reviewPointId;
    }

    public ReviewPointComment reviewPointId(Long reviewPointId) {
        this.reviewPointId = reviewPointId;
        return this;
    }

    public void setReviewPointId(Long reviewPointId) {
        this.reviewPointId = reviewPointId;
    }

    public String getContent() {
        return content;
    }

    public ReviewPointComment content(String content) {
        this.content = content;
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ZonedDateTime getTime() {
        return time;
    }

    public ReviewPointComment time(ZonedDateTime time) {
        this.time = time;
        return this;
    }

    public void setTime(ZonedDateTime time) {
        this.time = time;
    }

  
  
}
