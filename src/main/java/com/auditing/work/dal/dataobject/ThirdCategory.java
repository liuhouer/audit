package com.auditing.work.dal.dataobject;

/**
 * Created by innolab on 16-12-25.
 */
public class ThirdCategory {
	public Integer levelIndex;
    @Override
	public String toString() {
		return "ThirdCategory [id=" + id + ", level=" + level + ", name=" + name + ", second_id=" + second_id + "]";
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

    public Integer getSecond_id() {
        return second_id;
    }

    public void setSecond_id(Integer second_id) {
        this.second_id = second_id;
    }

    private  Integer id;
    private  String level ;
    private String name;
    private String remarks;
    private  Integer second_id;
}
