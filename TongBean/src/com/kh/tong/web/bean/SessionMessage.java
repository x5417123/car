package com.kh.tong.web.bean;

import java.util.Date;

public class SessionMessage {
    long id;
    String UUID;
    String sessionUUID;
    int type;
    String content;
    String senderUUID;
    Date createTime;
    int fromDeviceType;
    String fromDeviceId;
    UserInfo userInfo;

    public SessionMessage() {
        super();
    }

    public SessionMessage(long id, String uUID, String sessionUUID, int type,
            String content, String senderUUID, Date createTime,
            int fromDeviceType, String fromDeviceId, UserInfo userInfo) {
        super();
        this.id = id;
        UUID = uUID;
        this.sessionUUID = sessionUUID;
        this.type = type;
        this.content = content;
        this.senderUUID = senderUUID;
        this.createTime = createTime;
        this.fromDeviceType = fromDeviceType;
        this.fromDeviceId = fromDeviceId;
        this.userInfo = userInfo;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
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
        return sessionUUID;
    }

    public void setSessionUUID(String sessionUUID) {
        this.sessionUUID = sessionUUID;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSenderUUID() {
        return senderUUID;
    }

    public void setSenderUUID(String senderUUID) {
        this.senderUUID = senderUUID;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getFromDeviceType() {
        return fromDeviceType;
    }

    public void setFromDeviceType(int fromDeviceType) {
        this.fromDeviceType = fromDeviceType;
    }

    public String getFromDeviceId() {
        return fromDeviceId;
    }

    public void setFromDeviceId(String fromDeviceId) {
        this.fromDeviceId = fromDeviceId;
    }

}
