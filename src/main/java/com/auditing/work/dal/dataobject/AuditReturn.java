package com.auditing.work.dal.dataobject;
import java.util.List;
/**
 * Created by innolab on 16-12-17.
 */
public class AuditReturn {

    public List<AuditingDetail> getAudits() {
        return audits;
    }

    public void setAudits(List<AuditingDetail> audits) {
        this.audits = audits;
    }

    public String getStandards1() {
        return standards1;
    }

    public void setStandards1(String standards1) {
        this.standards1 = standards1;
    }

    public String getStandards2() {
        return standards2;
    }

    public void setStandards2(String standards2) {
        this.standards2 = standards2;
    }

    public String getStandards3() {
        return standards3;
    }

    public void setStandards3(String standards3) {
        this.standards3 = standards3;
    }

    private String standards1;

    public Long getId1() {
        return id1;
    }

    public void setId1(Long id1) {
        this.id1 = id1;
    }

    public Long getId2() {
        return id2;
    }

    public void setId2(Long id2) {
        this.id2 = id2;
    }

    public Long getId3() {
        return id3;
    }

    public void setId3(Long id3) {
        this.id3 = id3;
    }

    private Long id1;
    private String standards2;
    private Long id2;
    private String standards3;
    private Long id3;

    List<AuditingDetail> audits;
}
