package com.kh.tong.web.bean;

import java.util.List;

public class PSessionLocation {
    String pSessionUUID;// 结伴出行的sessionUUID
    String sessionUUID;// 关联的会话sessionUUID
    List<UserTrackPoint> userTrackPoints;

    public PSessionLocation() {
        super();
    }

    public PSessionLocation(String pSessionUUID, String sessionUUID,
            List<UserTrackPoint> userTrackPoints) {
        super();
        this.pSessionUUID = pSessionUUID;
        this.sessionUUID = sessionUUID;
        this.userTrackPoints = userTrackPoints;
    }

    public String getpSessionUUID() {
        return pSessionUUID;
    }

    public void setpSessionUUID(String pSessionUUID) {
        this.pSessionUUID = pSessionUUID;
    }

    public String getSessionUUID() {
        return sessionUUID;
    }

    public void setSessionUUID(String sessionUUID) {
        this.sessionUUID = sessionUUID;
    }

    public List<UserTrackPoint> getUserTrackPoints() {
        return userTrackPoints;
    }

    public void setUserTrackPoints(List<UserTrackPoint> userTrackPoints) {
        this.userTrackPoints = userTrackPoints;
    }

}
