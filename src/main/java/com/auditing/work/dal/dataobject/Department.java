package com.auditing.work.dal.dataobject;

/**
 * Created by innolab on 16-12-25.
 */
public class Department {
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private  Integer id;
    private  String name;

}
