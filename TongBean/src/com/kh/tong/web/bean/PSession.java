package com.kh.tong.web.bean;

import java.util.Date;
import java.util.List;

public class PSession {
    long id;
    String UUID;
    String sessionUUID;
    String leaderUUID;
    Date createTime;
    int flag;
    Date closeTime;
    List<SessionUser> users;
    Session session;

    public PSession() {
        super();
    }

    public PSession(long id, String uUID, String sessionUUID,
            String leaderUUID, Date createTime, int flag, Date closeTime,
            List<SessionUser> users, Session session) {
        super();
        this.id = id;
        UUID = uUID;
        this.sessionUUID = sessionUUID;
        this.leaderUUID = leaderUUID;
        this.createTime = createTime;
        this.flag = flag;
        this.closeTime = closeTime;
        this.users = users;
        this.session = session;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
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

    public String getLeaderUUID() {
        return leaderUUID;
    }

    public void setLeaderUUID(String leaderUUID) {
        this.leaderUUID = leaderUUID;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public Date getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(Date closeTime) {
        this.closeTime = closeTime;
    }

}
