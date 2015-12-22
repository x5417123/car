package com.kh.tong.term.bean;

public class BlueToothDevice {
    String name;// 蓝牙设备名称
    String address;// 蓝牙MAC地址
    int bondState;// 绑定状态 .1-绑定,2-正在绑定,3-没有绑定

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getBondState() {
        return bondState;
    }

    public void setBondState(int bondState) {
        this.bondState = bondState;
    }

}
