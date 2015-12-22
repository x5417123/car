package com.kh.tong.term.bean;

public class TrackProgress {
    long trackId; // 上次播放的声音的Id
    long albumId; // 声音所属专辑
    long progress; // 声音进度，播放到的毫秒数

    public long getTrackId() {
        return trackId;
    }

    public void setTrackId(long trackId) {
        this.trackId = trackId;
    }

    public long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(long albumId) {
        this.albumId = albumId;
    }

    public long getProgress() {
        return progress;
    }

    public void setProgress(long progress) {
        this.progress = progress;
    }

}
