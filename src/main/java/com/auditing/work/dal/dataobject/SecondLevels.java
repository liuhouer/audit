package com.auditing.work.dal.dataobject;

import java.util.List;

/**
 * Created by innolab on 16-12-17.
 */
public class SecondLevels {
    public List<ThirdLevels> getThirds() {
        return thirds;
    }

    public void setThirds(List<ThirdLevels> thirds) {
        this.thirds = thirds;
    }

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

    public String getSecondLevel() {
        return secondLevel;
    }

    public void setSecondLevel(String secondLevel) {
        this.secondLevel = secondLevel;
    }

    private String secondLevel;

    private String level;

    private String standards;

    private List<ThirdLevels> thirds;
}
