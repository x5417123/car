package com.kh.tong.web.bean;

import java.util.Date;

public class ResourceFile {
    Long id;
    String uuid;
    String userUUID;
    String resourceType;
    String fileExt;
    String resouceFileMd5;
    String filePath;
    Date createTime;
    long requestFileSize;
    long uploadDataLength;
    String uploadDataMd5;

    public ResourceFile() {
        super();
    }

    public ResourceFile(Long id, String uuid, String userUUID,
            String resourceType, String fileExt, String resouceFileMd5,
            String filePath, Date createTime, long requestFileSize,
            long uploadDataLength, String uploadDataMd5) {
        super();
        this.id = id;
        this.uuid = uuid;
        this.userUUID = userUUID;
        this.resourceType = resourceType;
        this.fileExt = fileExt;
        this.resouceFileMd5 = resouceFileMd5;
        this.filePath = filePath;
        this.createTime = createTime;
        this.requestFileSize = requestFileSize;
        this.uploadDataLength = uploadDataLength;
        this.uploadDataMd5 = uploadDataMd5;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUserUUID() {
        return userUUID;
    }

    public void setUserUUID(String userUUID) {
        this.userUUID = userUUID;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getFileExt() {
        return fileExt;
    }

    public void setFileExt(String fileExt) {
        this.fileExt = fileExt;
    }

    public String getResouceFileMd5() {
        return resouceFileMd5;
    }

    public void setResouceFileMd5(String resouceFileMd5) {
        this.resouceFileMd5 = resouceFileMd5;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public long getRequestFileSize() {
        return requestFileSize;
    }

    public void setRequestFileSize(long requestFileSize) {
        this.requestFileSize = requestFileSize;
    }

    public long getUploadDataLength() {
        return uploadDataLength;
    }

    public void setUploadDataLength(long uploadDataLength) {
        this.uploadDataLength = uploadDataLength;
    }

    public String getUploadDataMd5() {
        return uploadDataMd5;
    }

    public void setUploadDataMd5(String uploadDataMd5) {
        this.uploadDataMd5 = uploadDataMd5;
    }

}
