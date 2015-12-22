package com.kh.tong.web.bean;

import java.util.Date;

public class PhoneReg {
    int id;
    String uuid;
    String phone;
    String verifyCode;
    Date validTime;
    Date createTime;

    public PhoneReg() {
        super();
    }

    public PhoneReg(int id, String uuid, String phone, String verifyCode,
            Date validTime, Date createTime) {
        super();
        this.id = id;
        this.uuid = uuid;
        this.phone = phone;
        this.verifyCode = verifyCode;
        this.validTime = validTime;
        this.createTime = createTime;
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

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    public Date getValidTime() {
        return validTime;
    }

    public void setValidTime(Date validTime) {
        this.validTime = validTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

}
