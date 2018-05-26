package com.auditing.work.jpa.po;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;


/**
 * The persistent class for the public_doc database table.
 * 
 */
@Entity
@Table(name="public_doc")
@NamedQuery(name="PublicDoc.findAll", query="SELECT p FROM PublicDoc p")
public class PublicDoc implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	  private Long id;

	@Column(name="upload_date")
	private Date uploadDate;

	@Lob
	private byte[] doc;

	@Column(name="doc_name")
	private String docName;

	@Column(name="user_id")
	private Long userId;
	
	  @Transient
	    private String userName;

	    public String getUserName() {
			return userName;
		}

		public void setUserName(String userName) {
			this.userName = userName;
		}

	public PublicDoc() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getUploadDate() {
		return uploadDate;
	}


	public void setUploadDate(Date uploadDate) {
		this.uploadDate = uploadDate;
	}

	public byte[] getDoc() {
		return this.doc;
	}

	public void setDoc(byte[] doc) {
		this.doc = doc;
	}

	public String getDocName() {
		return this.docName;
	}

	public void setDocName(String docName) {
		this.docName = docName;
	}

	public Long getUserId() {
		return this.userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

}