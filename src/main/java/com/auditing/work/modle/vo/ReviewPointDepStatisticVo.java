package com.auditing.work.modle.vo;

public class ReviewPointDepStatisticVo {
	public String  depName;
	public Integer unreviewedNumber = 0;
	public Integer reviewedNumber = 0;
	public Integer rejectNumber = 0;
	public Integer rejectToComfirmedNumber = 0;
	public String percentageComplete;
	@Override
	public String toString() {
		return "ReviewPointDepStatisticVo [depName=" + depName + ", unreviewedNumber=" + unreviewedNumber
				+ ", reviewedNumber=" + reviewedNumber + ", rejectNumber=" + rejectNumber + ", rejectToComfirmedNumber="
				+ rejectToComfirmedNumber + "]";
	}


}
