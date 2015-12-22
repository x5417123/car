package com.kh.tong.web.bean;

public class Friend {
    int id;
    String uuid;
    String userUuid;
    String friendUserUuid;
    int flag;
    String requestRemark;
    String remarkName;

    public Friend() {
        super();
    }

    public Friend(int id, String uuid, String userUuid, String friendUserUuid,
            int flag, String requestRemark, String remarkName) {
        super();
        this.id = id;
        this.uuid = uuid;
        this.userUuid = userUuid;
        this.friendUserUuid = friendUserUuid;
        this.flag = flag;
        this.requestRemark = requestRemark;
        this.remarkName = remarkName;
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

    public String getUserUuid() {
        return userUuid;
    }

    public void setUserUuid(String userUuid) {
        this.userUuid = userUuid;
    }

    public String getFriendUserUuid() {
        return friendUserUuid;
    }

    public void setFriendUserUuid(String friendUserUuid) {
        this.friendUserUuid = friendUserUuid;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getRequestRemark() {
        return requestRemark;
    }

    public void setRequestRemark(String requestRemark) {
        this.requestRemark = requestRemark;
    }

    public String getRemarkName() {
        return remarkName;
    }

    public void setRemarkName(String remarkName) {
        this.remarkName = remarkName;
    }

}
