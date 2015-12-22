package com.kh.tong.web.bean;

public class PhoneMatchFriendInfo {
    String phone;
    UserInfo userInfo;
    int flag;

    public PhoneMatchFriendInfo() {
        super();
    }

    public PhoneMatchFriendInfo(String phone, UserInfo userInfo, int flag) {
        super();
        this.phone = phone;
        this.userInfo = userInfo;
        this.flag = flag;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

}
