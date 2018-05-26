package com.auditing.work.dal.dataobject;

/**
 * Created by innolab on 16-12-25.
 */
public class FirstCategory {

	public Integer levelIndex;
	
    public Integer getId() {
        return id;
    }

    @Override
	public String toString() {
		return "FirstCategory [id=" + id + ", level=" + level + ", name=" + name + ", remarks=" + remarks + "]";
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

    private  Integer id;
    private  String level ;
    private String name;
    private String remarks;

}
