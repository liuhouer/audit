package com.auditing.work.jpa.po;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.springframework.context.annotation.Lazy;

import io.swagger.annotations.ApiModel;

/**
 * Entity name
 */
@ApiModel(description = "评审附件")
@Entity
@Table(name = "review_point_doc")
public class ReviewPointDoc implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "review_point_id")
    private Long reviewPointId;
    
//    @Lob
//    @Lazy
//    @Column(name = "doc")
//    private byte[] doc;

    @Column(name = "doc_name")
    private String docName;
    
    
	
	@Column(name="upload_date")
	private Date uploadDate;

	@Column(name="user_id")
	private Long userId;
	  @Column(name = "fourth_id")
	 private Integer fourthId;
	  
	  @Column(name="path")
	  private String path;

    public Integer getFourthId() {
		return fourthId;
	}

	public void setFourthId(Integer fourthId) {
		this.fourthId = fourthId;
	}
	public Long getUserId() {
		return userId;
	}

	public Date getUploadDate() {
		return uploadDate;
	}


	public void setUploadDate(Date uploadDate) {
		this.uploadDate = uploadDate;
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

    public ReviewPointDoc reviewPointId(Long reviewPointId) {
        this.reviewPointId = reviewPointId;
        return this;
    }

    public void setReviewPointId(Long reviewPointId) {
        this.reviewPointId = reviewPointId;
    }

//    public byte[] getDoc() {
//        return doc;
//    }
//
//    public ReviewPointDoc doc(byte[] doc) {
//        this.doc = doc;
//        return this;
//    }
//
//    public void setDoc(byte[] doc) {
//        this.doc = doc;
//    }

    public String getDocName() {
        return docName;
    }

    public ReviewPointDoc docName(String docName) {
        this.docName = docName;
        return this;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ReviewPointDoc reviewPointDoc = (ReviewPointDoc) o;
        if (reviewPointDoc.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, reviewPointDoc.id);
    }

    public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ReviewPointDoc{" +
            "id=" + id +
            ", reviewPointId='" + reviewPointId + "'" +
//            ", doc='" + doc + "'" +
            ", docName='" + docName + "'" +
            '}';
    }
}
