package com.auditing.work.dal.dataobject;

/**
 * Created by innolab on 16-12-25.
 */
public class SecondCategory {
	public Integer levelIndex;
    @Override
	public String toString() {
		return "SecondCategory [id=" + id + ", level=" + level + ", name=" + name + ", first_id=" + first_id + "]";
	}

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

    public Integer getFirst_id() {
        return first_id;
    }

    public void setFirst_id(Integer first_id) {
        this.first_id = first_id;
    }

    private  Integer id;
    private  String level ;
    private String name;
    private String remarks;
    private  Integer first_id;
}
