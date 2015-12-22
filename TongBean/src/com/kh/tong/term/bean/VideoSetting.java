package com.kh.tong.term.bean;

public class VideoSetting {
	long id;
	int resolutionX;// 分辨率 int类型
	int resolutionY;// 分辨率 int类型
	int interval;// 时长(秒) int类型
	int videoBitRate;// 码流(bps) int类型
	int audioBitRate;// 音频码流(bps) int 类型
	boolean enableAudio;// 打开音频 Bool类型
	String videoEncoder;// 视频编码器 String 类型
	String audioEncoder;// 音频编码器 String 类型

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getResolutionX() {
		return resolutionX;
	}

	public void setResolutionX(int resolutionX) {
		this.resolutionX = resolutionX;
	}

	public int getResolutionY() {
		return resolutionY;
	}

	public void setResolutionY(int resolutionY) {
		this.resolutionY = resolutionY;
	}

	public int getInterval() {
		return interval;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}

	public int getVideoBitRate() {
		return videoBitRate;
	}

	public void setVideoBitRate(int videoBitRate) {
		this.videoBitRate = videoBitRate;
	}

	public int getAudioBitRate() {
		return audioBitRate;
	}

	public void setAudioBitRate(int audioBitRate) {
		this.audioBitRate = audioBitRate;
	}

	public boolean isEnableAudio() {
		return enableAudio;
	}

	public void setEnableAudio(boolean enableAudio) {
		this.enableAudio = enableAudio;
	}

	public String getVideoEncoder() {
		return videoEncoder;
	}

	public void setVideoEncoder(String videoEncoder) {
		this.videoEncoder = videoEncoder;
	}

	public String getAudioEncoder() {
		return audioEncoder;
	}

	public void setAudioEncoder(String audioEncoder) {
		this.audioEncoder = audioEncoder;
	}

}
