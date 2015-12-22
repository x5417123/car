package com.kh.tong.term.bean;

public class TrackPlayStatus {
	String trackId;// 点播UUID
	int trackStatus;// 1-播放 ,2-停止
	long progress;// Track播放到多少毫秒

	public String getTrackId() {
		return trackId;
	}

	public void setTrackId(String trackId) {
		this.trackId = trackId;
	}

	public int getTrackStatus() {
		return trackStatus;
	}

	public void setTrackStatus(int trackStatus) {
		this.trackStatus = trackStatus;
	}

	public long getProgress() {
		return progress;
	}

	public void setProgress(long progress) {
		this.progress = progress;
	}

}
