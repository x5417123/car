package com.kh.tong.web.bean;

public class FriendUserInfo {
    Friend friend;
    UserInfo userInfo;

    public FriendUserInfo() {
        super();
    }

    public FriendUserInfo(Friend friend, UserInfo userInfo) {
        super();
        this.friend = friend;
        this.userInfo = userInfo;
    }

    public Friend getFriend() {
        return friend;
    }

    public void setFriend(Friend friend) {
        this.friend = friend;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

}
