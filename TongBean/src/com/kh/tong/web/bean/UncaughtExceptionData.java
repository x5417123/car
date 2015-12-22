package com.kh.tong.web.bean;

import java.util.Date;

public class UncaughtExceptionData {
    String deviceId; // 设备唯一ID(基于IMEI或者Android ID等)
    String language; // 设备语言
    String version; // 应用版本号
    String systemVersion;// 系统版本号
    int type;// 设备类型，1-酷走app, 2-酷走终端
    Date time;// 崩溃的时间
    String data;// 异常数据

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getSystemVersion() {
        return systemVersion;
    }

    public void setSystemVersion(String systemVersion) {
        this.systemVersion = systemVersion;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
