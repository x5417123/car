package com.kh.tong.term.bean;

public class MusicFile {
	long id;
	String uuid; // UUID作为终端的音乐文件名（带后缀）
	String userUuid;
	String name; // 音乐文件名（带后缀）
	String path;
	long fileSize; // name+filesize做第一重判定
	String md5; // md5 做第二重判定（第一重判定完全相同）
	long length;// 单位秒

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

	public String getUserUuid() {
		return userUuid;
	}

	public void setUserUuid(String userUuid) {
		this.userUuid = userUuid;
	}

	public String getUserUUID() {
		return userUuid;
	}

	public void setUserUUID(String userUUID) {
		this.userUuid = userUUID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

	public long getLength() {
		return length;
	}

	public void setLength(long length) {
		this.length = length;
	}

}
