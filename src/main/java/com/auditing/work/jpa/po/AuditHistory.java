package com.auditing.work.jpa.po;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 历史审核
 */
@ApiModel(description = "历史审核")
@Entity
@Table(name = "audit_history")
public class AuditHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    @Column(name = "name")
    private String name;

    /**
     * 开始时间
     */
  
    @Column(name = "start_time")
    @JsonIgnore
    private LocalDate startTime;

    @javax.persistence.Transient
    @ApiModelProperty(value = "开始时间")
    private String startTimeView;

    public void setStartTimeView(String startTimeView){
    	this.startTimeView = startTimeView;
    }
    
    public String getStartTimeView() {
		return startTimeView;
    
	}


    
    /**
     * 结束时间
     */
 
    @Column(name = "end_time")
    @JsonIgnore
    private LocalDate endTime;

    @javax.persistence.Transient
    @ApiModelProperty(value = "结束时间")
    private String endTimeView;

    public void setEndTimeView(String endTimeView){
    	this.endTimeView = endTimeView;
    }
    
    public String getEndTimeView() {
		return endTimeView;
    	
	}


    
    /**
     * A级达标条款数量
     */
    @ApiModelProperty(value = "A级达标条款数量")
    @Column(name = "a_number")
    private Integer aNumber;

    /**
     * B级达标条款数量
     */
    @ApiModelProperty(value = "B级达标条款数量")
    @Column(name = "b_number")
    private Integer bNumber;

    /**
     * C级达标条款数量
     */
    @ApiModelProperty(value = "C级达标条款数量")
    @Column(name = "c_number")
    private Integer cNumber;

    /**
     * D级达标条款数量
     */
    @ApiModelProperty(value = "D级达标条款数量")
    @Column(name = "d_number")
    private Integer dNumber;

    /**
     * E级达标条款数量
     */
    @ApiModelProperty(value = "E级达标条款数量")
    @Column(name = "e_number")
    private Integer eNumber;

    /**
     * 总达标条款数量
     */
    @ApiModelProperty(value = "总达标条款数量")
    @Column(name = "total_number")
    private Integer totalNumber;

    /**
     * A级达标条款数量
     */
    @ApiModelProperty(value = "A级达标条款数量")
    @Column(name = "a_percentage_complete")
    private String aPercentageComplete;

    /**
     * B级达标条款数量
     */
    @ApiModelProperty(value = "B级达标条款数量")
    @Column(name = "b_percentage_complete")
    private String bPercentageComplete;

    /**
     * C级达标条款数量
     */
    @ApiModelProperty(value = "C级达标条款数量")
    @Column(name = "c_percentage_complete")
    private String cPercentageComplete;

    /**
     * D级达标条款数量
     */
    @ApiModelProperty(value = "D级达标条款数量")
    @Column(name = "d_percentage_complete")
    private String dPercentageComplete;

    /**
     * E级达标条款数量
     */
    @ApiModelProperty(value = "E级达标条款数量")
    @Column(name = "e_percentage_complete")
    private String ePercentageComplete;

    /**
     * A级达标条款数量(关键)
     */
    @ApiModelProperty(value = "A级达标条款数量(关键)")
    @Column(name = "key_a_number")
    private Integer keyANumber;

    /**
     * B级达标条款数量(关键)
     */
    @ApiModelProperty(value = "B级达标条款数量(关键)")
    @Column(name = "key_b_number")
    private Integer keyBNumber;

    /**
     * C级达标条款数量(关键)
     */
    @ApiModelProperty(value = "C级达标条款数量(关键)")
    @Column(name = "key_c_number")
    private Integer keyCNumber;

    /**
     * D级达标条款数量(关键)
     */
    @ApiModelProperty(value = "D级达标条款数量(关键)")
    @Column(name = "key_d_number")
    private Integer keyDNumber;

    /**
     * E级达标条款数量(关键)
     */
    @ApiModelProperty(value = "E级达标条款数量(关键)")
    @Column(name = "key_e_number")
    private Integer keyENumber;

    /**
     * 总达标条款数量(关键)
     */
    @ApiModelProperty(value = "总达标条款数量(关键)")
    @Column(name = "key_total_number")
    private Integer keyTotalNumber;

    /**
     * A级达标条款数量(关键)
     */
    @ApiModelProperty(value = "A级达标条款数量(关键)")
    @Column(name = "key_a_percentage_complete")
    private String keyAPercentageComplete;

    /**
     * B级达标条款数量(关键)
     */
    @ApiModelProperty(value = "B级达标条款数量(关键)")
    @Column(name = "key_b_percentage_complete")
    private String keyBPercentageComplete;

    /**
     * C级达标条款数量(关键)
     */
    @ApiModelProperty(value = "C级达标条款数量(关键)")
    @Column(name = "key_c_percentage_complete")
    private String keyCPercentageComplete;

    /**
     * D级达标条款数量(关键)
     */
    @ApiModelProperty(value = "D级达标条款数量(关键)")
    @Column(name = "key_d_percentage_complete")
    private String keyDPercentageComplete;

    /**
     * E级达标条款数量(关键)
     */
    @ApiModelProperty(value = "E级达标条款数量(关键)")
    @Column(name = "key_e_percentage_complete")
    private String keyEPercentageComplete;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public AuditHistory name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getStartTime() {
        return startTime;
    }

    public AuditHistory startTime(LocalDate startTime) {
        this.startTime = startTime;
        return this;
    }

    public void setStartTime(LocalDate startTime) {
        this.startTime = startTime;
    }

    public LocalDate getEndTime() {
        return endTime;
    }

    public AuditHistory endTime(LocalDate endTime) {
        this.endTime = endTime;
        return this;
    }

    public void setEndTime(LocalDate endTime) {
        this.endTime = endTime;
    }

    public Integer getaNumber() {
        return aNumber;
    }

    public AuditHistory aNumber(Integer aNumber) {
        this.aNumber = aNumber;
        return this;
    }

    public void setaNumber(Integer aNumber) {
        this.aNumber = aNumber;
    }

    public Integer getbNumber() {
        return bNumber;
    }

    public AuditHistory bNumber(Integer bNumber) {
        this.bNumber = bNumber;
        return this;
    }

    public void setbNumber(Integer bNumber) {
        this.bNumber = bNumber;
    }

    public Integer getcNumber() {
        return cNumber;
    }

    public AuditHistory cNumber(Integer cNumber) {
        this.cNumber = cNumber;
        return this;
    }

    public void setcNumber(Integer cNumber) {
        this.cNumber = cNumber;
    }

    public Integer getdNumber() {
        return dNumber;
    }

    public AuditHistory dNumber(Integer dNumber) {
        this.dNumber = dNumber;
        return this;
    }

    public void setdNumber(Integer dNumber) {
        this.dNumber = dNumber;
    }

    public Integer geteNumber() {
        return eNumber;
    }

    public AuditHistory eNumber(Integer eNumber) {
        this.eNumber = eNumber;
        return this;
    }

    public void seteNumber(Integer eNumber) {
        this.eNumber = eNumber;
    }

    public Integer getTotalNumber() {
        return totalNumber;
    }

    public AuditHistory totalNumber(Integer totalNumber) {
        this.totalNumber = totalNumber;
        return this;
    }

    public void setTotalNumber(Integer totalNumber) {
        this.totalNumber = totalNumber;
    }

    public String getaPercentageComplete() {
        return aPercentageComplete;
    }

    public AuditHistory aPercentageComplete(String aPercentageComplete) {
        this.aPercentageComplete = aPercentageComplete;
        return this;
    }

    public void setaPercentageComplete(String aPercentageComplete) {
        this.aPercentageComplete = aPercentageComplete;
    }

    public String getbPercentageComplete() {
        return bPercentageComplete;
    }

    public AuditHistory bPercentageComplete(String bPercentageComplete) {
        this.bPercentageComplete = bPercentageComplete;
        return this;
    }

    public void setbPercentageComplete(String bPercentageComplete) {
        this.bPercentageComplete = bPercentageComplete;
    }

    public String getcPercentageComplete() {
        return cPercentageComplete;
    }

    public AuditHistory cPercentageComplete(String cPercentageComplete) {
        this.cPercentageComplete = cPercentageComplete;
        return this;
    }

    public void setcPercentageComplete(String cPercentageComplete) {
        this.cPercentageComplete = cPercentageComplete;
    }

    public String getdPercentageComplete() {
        return dPercentageComplete;
    }

    public AuditHistory dPercentageComplete(String dPercentageComplete) {
        this.dPercentageComplete = dPercentageComplete;
        return this;
    }

    public void setdPercentageComplete(String dPercentageComplete) {
        this.dPercentageComplete = dPercentageComplete;
    }

    public String getePercentageComplete() {
        return ePercentageComplete;
    }

    public AuditHistory ePercentageComplete(String ePercentageComplete) {
        this.ePercentageComplete = ePercentageComplete;
        return this;
    }

    public void setePercentageComplete(String ePercentageComplete) {
        this.ePercentageComplete = ePercentageComplete;
    }

    public Integer getKeyANumber() {
        return keyANumber;
    }

    public AuditHistory keyANumber(Integer keyANumber) {
        this.keyANumber = keyANumber;
        return this;
    }

    public void setKeyANumber(Integer keyANumber) {
        this.keyANumber = keyANumber;
    }

    public Integer getKeyBNumber() {
        return keyBNumber;
    }

    public AuditHistory keyBNumber(Integer keyBNumber) {
        this.keyBNumber = keyBNumber;
        return this;
    }

    public void setKeyBNumber(Integer keyBNumber) {
        this.keyBNumber = keyBNumber;
    }

    public Integer getKeyCNumber() {
        return keyCNumber;
    }

    public AuditHistory keyCNumber(Integer keyCNumber) {
        this.keyCNumber = keyCNumber;
        return this;
    }

    public void setKeyCNumber(Integer keyCNumber) {
        this.keyCNumber = keyCNumber;
    }

    public Integer getKeyDNumber() {
        return keyDNumber;
    }

    public AuditHistory keyDNumber(Integer keyDNumber) {
        this.keyDNumber = keyDNumber;
        return this;
    }

    public void setKeyDNumber(Integer keyDNumber) {
        this.keyDNumber = keyDNumber;
    }

    public Integer getKeyENumber() {
        return keyENumber;
    }

    public AuditHistory keyENumber(Integer keyENumber) {
        this.keyENumber = keyENumber;
        return this;
    }

    public void setKeyENumber(Integer keyENumber) {
        this.keyENumber = keyENumber;
    }

    public Integer getKeyTotalNumber() {
        return keyTotalNumber;
    }

    public AuditHistory keyTotalNumber(Integer keyTotalNumber) {
        this.keyTotalNumber = keyTotalNumber;
        return this;
    }

    public void setKeyTotalNumber(Integer keyTotalNumber) {
        this.keyTotalNumber = keyTotalNumber;
    }

    public String getKeyAPercentageComplete() {
        return keyAPercentageComplete;
    }

    public AuditHistory keyAPercentageComplete(String keyAPercentageComplete) {
        this.keyAPercentageComplete = keyAPercentageComplete;
        return this;
    }

    public void setKeyAPercentageComplete(String keyAPercentageComplete) {
        this.keyAPercentageComplete = keyAPercentageComplete;
    }

    public String getKeyBPercentageComplete() {
        return keyBPercentageComplete;
    }

    public AuditHistory keyBPercentageComplete(String keyBPercentageComplete) {
        this.keyBPercentageComplete = keyBPercentageComplete;
        return this;
    }

    public void setKeyBPercentageComplete(String keyBPercentageComplete) {
        this.keyBPercentageComplete = keyBPercentageComplete;
    }

    public String getKeyCPercentageComplete() {
        return keyCPercentageComplete;
    }

    public AuditHistory keyCPercentageComplete(String keyCPercentageComplete) {
        this.keyCPercentageComplete = keyCPercentageComplete;
        return this;
    }

    public void setKeyCPercentageComplete(String keyCPercentageComplete) {
        this.keyCPercentageComplete = keyCPercentageComplete;
    }

    public String getKeyDPercentageComplete() {
        return keyDPercentageComplete;
    }

    public AuditHistory keyDPercentageComplete(String keyDPercentageComplete) {
        this.keyDPercentageComplete = keyDPercentageComplete;
        return this;
    }

    public void setKeyDPercentageComplete(String keyDPercentageComplete) {
        this.keyDPercentageComplete = keyDPercentageComplete;
    }

    public String getKeyEPercentageComplete() {
        return keyEPercentageComplete;
    }

    public AuditHistory keyEPercentageComplete(String keyEPercentageComplete) {
        this.keyEPercentageComplete = keyEPercentageComplete;
        return this;
    }

    public void setKeyEPercentageComplete(String keyEPercentageComplete) {
        this.keyEPercentageComplete = keyEPercentageComplete;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AuditHistory auditHistory = (AuditHistory) o;
        if (auditHistory.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), auditHistory.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AuditHistory{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", startTime='" + getStartTime() + "'" +
            ", endTime='" + getEndTime() + "'" +
            ", aNumber='" + getaNumber() + "'" +
            ", bNumber='" + getbNumber() + "'" +
            ", cNumber='" + getcNumber() + "'" +
            ", dNumber='" + getdNumber() + "'" +
            ", eNumber='" + geteNumber() + "'" +
            ", totalNumber='" + getTotalNumber() + "'" +
            ", aPercentageComplete='" + getaPercentageComplete() + "'" +
            ", bPercentageComplete='" + getbPercentageComplete() + "'" +
            ", cPercentageComplete='" + getcPercentageComplete() + "'" +
            ", dPercentageComplete='" + getdPercentageComplete() + "'" +
            ", ePercentageComplete='" + getePercentageComplete() + "'" +
            ", keyANumber='" + getKeyANumber() + "'" +
            ", keyBNumber='" + getKeyBNumber() + "'" +
            ", keyCNumber='" + getKeyCNumber() + "'" +
            ", keyDNumber='" + getKeyDNumber() + "'" +
            ", keyENumber='" + getKeyENumber() + "'" +
            ", keyTotalNumber='" + getKeyTotalNumber() + "'" +
            ", keyAPercentageComplete='" + getKeyAPercentageComplete() + "'" +
            ", keyBPercentageComplete='" + getKeyBPercentageComplete() + "'" +
            ", keyCPercentageComplete='" + getKeyCPercentageComplete() + "'" +
            ", keyDPercentageComplete='" + getKeyDPercentageComplete() + "'" +
            ", keyEPercentageComplete='" + getKeyEPercentageComplete() + "'" +
            "}";
    }
}
