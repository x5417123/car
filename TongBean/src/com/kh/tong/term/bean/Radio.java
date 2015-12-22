package com.kh.tong.term.bean;

public class Radio {
    int id;
    String uuid;
    String name;
    String parentUuid;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParent_uuid() {
        return parentUuid;
    }

    public void setParent_uuid(String parent_uuid) {
        this.parentUuid = parent_uuid;
    }

}
