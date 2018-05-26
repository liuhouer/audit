package com.auditing.work.modle.vo;

import java.util.List;

public class UpdateFourthCategoryUserNameVo {
	private String userName;
	private List<Integer> ids;
	private String depName;
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public List<Integer> getIds() {
		return ids;
	}
	public void setIds(List<Integer> ids) {
		this.ids = ids;
	}
	public String getDepName() {
		return depName;
	}
	public void setDepName(String depName) {
		this.depName = depName;
	}
}
