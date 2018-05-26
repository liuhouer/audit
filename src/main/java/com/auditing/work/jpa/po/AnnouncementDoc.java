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

import io.swagger.annotations.ApiModel;

/**
 * Entity name
 */
@ApiModel(description = "公告附件")
@Entity
@Table(name = "announcement_doc")
public class AnnouncementDoc implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "announcement_id")
    private Long announcementId;
    
    public Long getAnnouncementId() {
		return announcementId;
	}

	public void setAnnouncementId(Long announcementId) {
		this.announcementId = announcementId;
	}

	@Lob
    @Column(name = "doc")
    private byte[] doc;

    @Column(name = "doc_name")
    private String docName;
    
    
	
	@Column(name="upload_date")
	private Date uploadDate;

	@Column(name="user_id")
	private Long userId;



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

  
  

    public byte[] getDoc() {
        return doc;
    }

    public AnnouncementDoc doc(byte[] doc) {
        this.doc = doc;
        return this;
    }

    public void setDoc(byte[] doc) {
        this.doc = doc;
    }

    public String getDocName() {
        return docName;
    }

    public AnnouncementDoc docName(String docName) {
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
        AnnouncementDoc reviewPointDoc = (AnnouncementDoc) o;
        if (reviewPointDoc.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, reviewPointDoc.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ReviewPointDoc{" +
            "id=" + id +
            ", reviewPointId='" + announcementId + "'" +
            ", doc='" + doc + "'" +
            ", docName='" + docName + "'" +
            '}';
    }
}
