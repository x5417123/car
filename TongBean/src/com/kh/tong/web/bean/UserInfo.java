package com.kh.tong.web.bean;

import java.util.Date;

public class UserInfo {
    String UUID;// 用户 UUID
    String name;// 用户名
    String nickName;// 名称
    String phone;// 电话号码
    String iconUUID;// 用户图标
    Date lastLoginTime;// 上次登陆时间

    public UserInfo() {
        super();
    }

    public UserInfo(String uUID, String name, String nickName, String phone,
            String iconUUID, Date lastLoginTime) {
        super();
        UUID = uUID;
        this.name = name;
        this.nickName = nickName;
        this.phone = phone;
        this.iconUUID = iconUUID;
        this.lastLoginTime = lastLoginTime;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String uUID) {
        UUID = uUID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIconUUID() {
        return iconUUID;
    }

    public void setIconUUID(String iconUUID) {
        this.iconUUID = iconUUID;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

}
