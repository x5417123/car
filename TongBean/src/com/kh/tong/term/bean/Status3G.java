package com.kh.tong.term.bean;

public class Status3G {
    String mnc; // mnc运营商代码
    String type; // 网络连接类型2G/3G/4G
    int intensity; // 信号强度
    boolean dialUpSuccess; // 是否拨号成功

    public String getMnc() {
        return mnc;
    }

    public void setMnc(String mnc) {
        this.mnc = mnc;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getIntensity() {
        return intensity;
    }

    public void setIntensity(int intensity) {
        this.intensity = intensity;
    }

    public boolean isDialUpSuccess() {
        return dialUpSuccess;
    }

    public void setDialUpSuccess(boolean dialUpSuccess) {
        this.dialUpSuccess = dialUpSuccess;
    }

}
