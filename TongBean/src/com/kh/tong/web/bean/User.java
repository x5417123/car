package com.kh.tong.web.bean;

import java.util.Date;

public class User {
    int id;
    String uuid;
    String phone;
    String name;
    String nickname;
    String email;
    String phoneLoginCode;
    Date phoneLoginCodeValidtime;
    String emailVerifyCode;
    Date emailVerifyCodeValidtime;
    int emailVerifyStatus;
    String iconUUID;
    String password;
    Date regTime;
    Date lastLoginTime;

    public User() {

    }

    public User(int id, String uuid, String phone, String name,
            String nickname, String email, String phoneLoginCode,
            Date phoneLoginCodeValidtime, String emailVerifyCode,
            Date emailVerifyCodeValidtime, int emailVerifyStatus,
            String iconUUID, String password, Date regTime, Date lastLoginTime) {
        super();
        this.id = id;
        this.uuid = uuid;
        this.phone = phone;
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.phoneLoginCode = phoneLoginCode;
        this.phoneLoginCodeValidtime = phoneLoginCodeValidtime;
        this.emailVerifyCode = emailVerifyCode;
        this.emailVerifyCodeValidtime = emailVerifyCodeValidtime;
        this.emailVerifyStatus = emailVerifyStatus;
        this.iconUUID = iconUUID;
        this.password = password;
        this.regTime = regTime;
        this.lastLoginTime = lastLoginTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneLoginCode() {
        return phoneLoginCode;
    }

    public void setPhoneLoginCode(String phoneLoginCode) {
        this.phoneLoginCode = phoneLoginCode;
    }

    public Date getPhoneLoginCodeValidtime() {
        return phoneLoginCodeValidtime;
    }

    public void setPhoneLoginCodeValidtime(Date phoneLoginCodeValidtime) {
        this.phoneLoginCodeValidtime = phoneLoginCodeValidtime;
    }

    public String getEmailVerifyCode() {
        return emailVerifyCode;
    }

    public void setEmailVerifyCode(String emailVerifyCode) {
        this.emailVerifyCode = emailVerifyCode;
    }

    public Date getEmailVerifyCodeValidtime() {
        return emailVerifyCodeValidtime;
    }

    public void setEmailVerifyCodeValidtime(Date emailVerifyCodeValidtime) {
        this.emailVerifyCodeValidtime = emailVerifyCodeValidtime;
    }

    public int getEmailVerifyStatus() {
        return emailVerifyStatus;
    }

    public void setEmailVerifyStatus(int emailVerifyStatus) {
        this.emailVerifyStatus = emailVerifyStatus;
    }

    public String getIconUUID() {
        return iconUUID;
    }

    public void setIconUUID(String iconUUID) {
        this.iconUUID = iconUUID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getRegTime() {
        return regTime;
    }

    public void setRegTime(Date regTime) {
        this.regTime = regTime;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

}
