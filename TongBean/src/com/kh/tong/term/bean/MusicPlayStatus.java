package com.kh.tong.term.bean;

public class MusicPlayStatus {
    long musicUuid;// md5
    int musicStatus;// 1-播放 ,2-暂停
    long musicProgress;// 音乐为播放到多少毫秒

    public long getMusicUuid() {
        return musicUuid;
    }

    public void setMusicUuid(long musicUuid) {
        this.musicUuid = musicUuid;
    }

    public int getMusicStatus() {
        return musicStatus;
    }

    public void setMusicStatus(int musicStatus) {
        this.musicStatus = musicStatus;
    }

    public long getMusicProgress() {
        return musicProgress;
    }

    public void setMusicProgress(long musicProgress) {
        this.musicProgress = musicProgress;
    }

}
