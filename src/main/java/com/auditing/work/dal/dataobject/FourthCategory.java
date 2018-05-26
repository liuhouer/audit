package com.auditing.work.dal.dataobject;

import com.auditing.work.modle.vo.FourthcategoryReviewDep;

/**
 * Created by innolab on 16-12-25.
 */
public class FourthCategory {
	public Integer levelIndex;
    @Override
	public String toString() {
		return "FourthCategory [id=" + id + ", level=" + level + ", name=" + name + ", third_id=" + third_id + "]";
	}
    private String userName;
	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Integer getThird_id() {
        return third_id;
    }

    public void setThird_id(Integer third_id) {
        this.third_id = third_id;
    }

    public String getTotal_score() {
        return total_score;
    }

    public void setTotal_score(String total_score) {
        this.total_score = total_score;
    }

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	public FourthcategoryReviewDep getReviewDep() {
		return reviewDep;
	}

	public void setReviewDep(FourthcategoryReviewDep reviewDep) {
		this.reviewDep = reviewDep;
	}
	private  Integer id;
    private  String level ;
    private String name;
    private String remarks;
    private  Integer third_id;
    private String total_score;
    private FourthcategoryReviewDep reviewDep;
}
