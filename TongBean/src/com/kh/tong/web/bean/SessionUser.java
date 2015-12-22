package com.kh.tong.web.bean;

import java.util.Date;

public class SessionUser {
    long id;
    String UUID;
    String SessionUUID;
    String userUUID;
    String remarkName;
    Date createTime;
    UserInfo userInfo;

    public SessionUser() {
        super();
    }

    public SessionUser(long id, String uUID, String sessionUUID,
            String userUUID, String remarkName, Date createTime,
            UserInfo userInfo) {
        super();
        this.id = id;
        UUID = uUID;
        SessionUUID = sessionUUID;
        this.userUUID = userUUID;
        this.remarkName = remarkName;
        this.createTime = createTime;
        this.userInfo = userInfo;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String uUID) {
        UUID = uUID;
    }

    public String getSessionUUID() {
        return SessionUUID;
    }

    public void setSessionUUID(String sessionUUID) {
        SessionUUID = sessionUUID;
    }

    public String getUserUUID() {
        return userUUID;
    }

    public void setUserUUID(String userUUID) {
        this.userUUID = userUUID;
    }

    public String getRemarkName() {
        return remarkName;
    }

    public void setRemarkName(String remarkName) {
        this.remarkName = remarkName;
    }

}
