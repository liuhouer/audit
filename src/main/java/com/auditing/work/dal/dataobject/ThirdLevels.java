package com.auditing.work.dal.dataobject;

import java.util.List;

/**
 * Created by innolab on 16-12-17.
 */
public class ThirdLevels {
    private Long id;

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


    private String level;

    public String getThirdLevel() {
        return thirdLevel;
    }

    public void setThirdLevel(String thirdLevel) {
        this.thirdLevel = thirdLevel;
    }

    private String thirdLevel;

    private String standards;

    public List<AuditingDetail> getAudits() {
        return audits;
    }

    public void setAudits(List<AuditingDetail> audits) {
        this.audits = audits;
    }

    private List<AuditingDetail> audits;
}
