package com.kh.tong.web.bean;

import java.util.Date;
import java.util.List;

public class Session {
    long id;
    String uuid;
    String name;
    int sessionType;
    String initUserUUID;
    Date createTime;
    Date msgUpdateTime;
    List<SessionUser> users;

    public Session() {
        super();
    }

    public Session(long id, String uuid, String name, int sessionType,
            String initUserUUID, Date create_time, Date msg_update_time,
            List<SessionUser> users) {
        super();
        this.id = id;
        this.uuid = uuid;
        this.name = name;
        this.sessionType = sessionType;
        this.initUserUUID = initUserUUID;
        this.createTime = create_time;
        this.msgUpdateTime = msg_update_time;
        this.users = users;
    }

    public List<SessionUser> getUsers() {
        return users;
    }

    public void setUsers(List<SessionUser> users) {
        this.users = users;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSessionType() {
        return sessionType;
    }

    public void setSessionType(int _sessionType) {
        sessionType = _sessionType;
    }

    public String getInitUserUUID() {
        return initUserUUID;
    }

    public void setInitUserUUID(String initUserUUID) {
        this.initUserUUID = initUserUUID;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date create_time) {
        this.createTime = create_time;
    }

    public Date getMsgUpdateTime() {
        return msgUpdateTime;
    }

    public void setMsgUpdateTime(Date msg_update_time) {
        this.msgUpdateTime = msg_update_time;
    }

}
