package com.kh.tong.term.bean;

import java.util.Date;

public class POI {
    Long id;
    Date createTime;
    double lon;
    double lat;
    String location;
    int type;

    public Long getID() {
        return id;
    }

    public void setID(Long iD) {
        id = iD;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

}
