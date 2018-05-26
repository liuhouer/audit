package com.auditing.work.dal.dataobject;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.auditing.work.jpa.po.DepRevierPointStauts;
import com.google.common.collect.Lists;

import io.swagger.annotations.ApiModelProperty;

/**
 * Created by innolab on 16-12-25.
 */
public class ReviewPoint {
    @Override
	public String toString() {
		return "ReviewPoint [id=" + id + ", fourth_id=" + fourth_id + ", detail=" + detail + ", score=" + score
				+ ", department_id=" + department_id + "]";
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFourth_id() {
        return fourth_id;
    }

    public void setFourth_id(Integer fourth_id) {
        this.fourth_id = fourth_id;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public Boolean getPassed() {
        return passed;
    }

    public void setPassed(Boolean passed) {
        this.passed = passed;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDepartment_id() {
        return department_id;
    }

    public void setDepartment_id(String department_id) {
        this.department_id = department_id;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLevel1Name() {
		return level1Name;
	}

	public void setLevel1Name(String level1Name) {
		this.level1Name = level1Name;
	}

	public String getLevel2Name() {
		return level2Name;
	}

	public void setLevel2Name(String level2Name) {
		this.level2Name = level2Name;
	}

	public String getLevel3Name() {
		return level3Name;
	}

	public void setLevel3Name(String level3Name) {
		this.level3Name = level3Name;
	}

	public String getLevel4Name() {
		return level4Name;
	}

	public void setLevel4Name(String level4Name) {
		this.level4Name = level4Name;
	}

	public Boolean getHasDoc() {
		return hasDoc;
	}

	public void setHasDoc(Boolean hasDoc) {
		this.hasDoc = hasDoc;
	}

	public Boolean getIsEdit() {
		if (isEdit == null) {
			return true;
		}
		return isEdit;
	}

	public void setIsEdit(Boolean isEdit) {
		this.isEdit = isEdit;
	}

	public List<String> getDepartmentIdList() {
		if (StringUtils.isNotBlank(department_id)) {
      	  	this.departmentIdList = Lists.newArrayList(StringUtils.split(department_id, ","));
		}
		return departmentIdList;
	}

	public void setDepartmentIdList(List<String> departmentIdList) {
		this.departmentIdList = departmentIdList;
	}

	public List<DepRevierPointStauts> getDepRevierPointStautsList() {
		return depRevierPointStautsList;
	}

	public void setDepRevierPointStautsList(List<DepRevierPointStauts> depRevierPointStautsList) {
		this.depRevierPointStautsList = depRevierPointStautsList;
	}

	private String title;
    private Integer id;
    private Integer fourth_id;
    private String detail;
    private String score;
    private Boolean passed;
    private  Integer status;
    private  String department_id;
    private  String attachment;
    private  String remarks;
    private  String level1Name;
    private  String level2Name;
    private  String level3Name;
    private  String level4Name;
    private  Boolean hasDoc;
    private  Boolean isEdit;
	@ApiModelProperty(value = "要点评审部门名称 list")
    private  List<String> departmentIdList = Lists.newArrayList();
    private List<DepRevierPointStauts> depRevierPointStautsList = Lists.newArrayList();
    private List<Department> departmentList;
	public List<Department> getDepartmentList() {
		return departmentList;
	}

	public void setDepartmentList(List<Department> departmentList) {
		this.departmentList = departmentList;
	}
	private String userName;
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
}
