package com.auditing.work.modle.vo;

import com.auditing.work.modle.BaseRequest;

public class UserRequest extends BaseRequest {

    /** 序列号 */
    private static final long serialVersionUID = -1073504461591955753L;

    public UserRequest getNewUser() {
        return newUser;
    }

    public void setNewUser(UserRequest newUser) {
        this.newUser = newUser;
    }

    private UserRequest            newUser;

    /** 用户名称 */
    private String            userName;

    private String            password;

    private String            department;

    private String            role;

    /**
     * Getter method for property <tt>userName</tt>.
     *
     * @return property value of userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Setter method for property <tt>userName</tt>.
     *
     * @param userName value to be assigned to property userName
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Getter method for property <tt>password</tt>.
     *
     * @return property value of password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Setter method for property <tt>password</tt>.
     *
     * @param password value to be assigned to property password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Getter method for property <tt>department</tt>.
     *
     * @return property value of department
     */
    public String getDepartment() {
        return department;
    }

    /**
     * Setter method for property <tt>department</tt>.
     *
     * @param department value to be assigned to property department
     */
    public void setDepartment(String department) {
        this.department = department;
    }

    /**
     * Getter method for property <tt>role</tt>.
     *
     * @return property value of role
     */
    public String getRole() {
        return role;
    }

    /**
     * Setter method for property <tt>role</tt>.
     *
     * @param role value to be assigned to property role
     */
    public void setRole(String role) {
        this.role = role;
    }


}
