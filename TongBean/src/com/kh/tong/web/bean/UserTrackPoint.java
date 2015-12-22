package com.kh.tong.web.bean;

import com.kh.tong.term.bean.TrackPoint;

public class UserTrackPoint {
	String userUUID;
	TrackPoint TrackPoint;

	public UserTrackPoint() {
		super();
	}

	public UserTrackPoint(String userUUID, TrackPoint trackPoint) {
		super();
		this.userUUID = userUUID;
		TrackPoint = trackPoint;
	}

	public String getUserUUID() {
		return userUUID;
	}

	public void setUserUUID(String userUUID) {
		this.userUUID = userUUID;
	}

	public TrackPoint getTrackPoint() {
		return TrackPoint;
	}

	public void setTrackPoint(TrackPoint trackPoint) {
		TrackPoint = trackPoint;
	}

}
