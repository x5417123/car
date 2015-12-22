package com.kh.tong.term.bean;

public class RecordingStatus {
	long id;
	int status;// 工作状态 int类型
	String recordingFile;// 正在写的文件 String类型

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getRecordingFile() {
		return recordingFile;
	}

	public void setRecordingFile(String recordingFile) {
		this.recordingFile = recordingFile;
	}

}
