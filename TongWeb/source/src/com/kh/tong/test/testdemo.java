package com.kh.tong.test;

import java.text.ParseException;

import com.ximalaya.sdk4j.Banners;
import com.ximalaya.sdk4j.model.XimalayaException;

public class testdemo {
    public static void main(String[] args) throws ParseException {
        // DateFormat df = new SimpleDateFormat("yyyyMMddHH");
        // Date date = df.parse("2015120914");
        // Date date2 = df.parse("2015120917");
        // Calendar cal = Calendar.getInstance();
        // cal.setTime(date);
        // Calendar cal2 = Calendar.getInstance();
        // cal2.setTime(date2);
        // long timestamp = cal.getTimeInMillis();
        // long timestamp2 = cal2.getTimeInMillis();
        // System.out.println(timestamp);
        // System.out.println(timestamp2);
        //
        // for (long i = timestamp; i < timestamp2; i = i + 5000) {
        // TrackPoint point = new TrackPoint();
        // point.setLat(31.97175 + Math.random() / 5000.0);
        // point.setLon(118.759786 + Math.random() / 5000.0);
        // point.setSysTime(new Date(i));
        // point.setGpsTime(new Date(i));
        // DBManager.addTrackPoint(point);
        // }
        Banners banners = new Banners();
        try {
            System.out.println(banners.getRankBanners());
        } catch (XimalayaException e) {
            e.printStackTrace();
        }
    }

}
