package com.kh.tong.term.bean;

public class Network3GUsage {
    long uplinkData;// 上行数据量
    long downlinkData;// 下行数据量 （加起来就是used）
    long currentMonthLimit; // 当月网络额度（剩余流量=当月额度-上行-下行）

    public long getUplinkData() {
        return uplinkData;
    }

    public void setUplinkData(long uplinkData) {
        this.uplinkData = uplinkData;
    }

    public long getDownlinkData() {
        return downlinkData;
    }

    public void setDownlinkData(long downlinkData) {
        this.downlinkData = downlinkData;
    }

    public long getCurrentMonthLimit() {
        return currentMonthLimit;
    }

    public void setCurrentMonthLimit(long currentMonthLimit) {
        this.currentMonthLimit = currentMonthLimit;
    }

}
