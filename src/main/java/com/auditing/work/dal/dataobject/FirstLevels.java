package com.auditing.work.dal.dataobject;

import java.util.List;
/**
 * Created by innolab on 16-12-17.
 */
public class FirstLevels {
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getStandards() {
        return standards;
    }

    public void setStandards(String standards) {
        this.standards = standards;
    }

    private Long id;

    private String level;

    public String getThirdLevel() {
        return thirdLevel;
    }

    public void setThirdLevel(String thirdLevel) {
        this.thirdLevel = thirdLevel;
    }

    private String thirdLevel;
    private String secondLevel;

    private String standards;

    public List<SecondLevels> getSeconds() {
        return seconds;
    }

    public void setSeconds(List<SecondLevels> seconds) {
        this.seconds = seconds;
    }

    private List<SecondLevels> seconds;
}
