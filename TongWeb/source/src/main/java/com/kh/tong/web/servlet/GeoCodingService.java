package com.kh.tong.web.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.kh.json.GsonHelper;
import com.kh.tong.web.bean.UncaughtExceptionData;
import com.kh.tong.web.db.DBManager;
import com.kh.tong.web.servlet.helper.ParserTool;
import com.kh.tong.web.servlet.helper.Util;
import com.kh.webdata.WebResponse;
import com.kh.webutils.WebConstants;
import com.kh.webutils.WebUtil;

public class GeoCodingService extends HttpServlet {

    private static final long serialVersionUID = -8438196667339146715L;

    static Logger log = Logger.getLogger(GeoCodingService.class);

    private static final String baiduAk = "EjpC9eCpv334ZFNtFgTZ9k7t";
    private static final String baiduGC1 = "http://api.map.baidu.com/geocoder/v2/?ak=";
    private static final String baiduGC2 = "&location=";
    private static final String baiduGC3 = "&output=json";
    private static final String googleGC = "http://maps.googleapis.com/maps/api/geocode/json?language=zh-CN&latlng=";

    private static final String ParamAction = "act";
    private static final String ParamLat = "lat";
    private static final String ParamLng = "lng";
    private static final String ParamException = "exception";
    private static final String ParamDeviceId = "deviceid";
    private static final String ParamStartTime = "starttime";
    private static final String ParamEndTime = "endtime";
    private static final String ParamOffset = "offset";
    private static final String ParamLen = "len";

    private static final String ActionBaiduGeoCodin = "baidu";
    private static final String ActionGoogleGeoCodin = "google";
    private static final String ActionReportException = "report_exception";
    private static final String ActionQueryException = "query_exception";

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        dowork(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        dowork(request, response);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void dowork(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String callback = request.getParameter(WebConstants.RequestParamJsonpCallback);
        WebResponse result = new WebResponse();
        String action = request.getParameter(ParamAction);
        if (ActionBaiduGeoCodin.equals(action)) {
            String lat = request.getParameter(ParamLat);
            String lng = request.getParameter(ParamLng);
            if (lat != null && lng != null) {
                String url = baiduGC1 + baiduAk + baiduGC2 + lat + "," + lng + baiduGC3;
                result.setData(ParserTool.do_get(url));
                result.setMessage("成功！");
                result.setResponseCode(WebConstants.WebResponseCodeOk);
            } else {
                result.setMessage("缺少参数！");
                result.setResponseCode(WebConstants.WebResponseCodeInvalidRequest);
            }
        } else if (ActionGoogleGeoCodin.equals(action)) {
            String lat = request.getParameter(ParamLat);
            String lng = request.getParameter(ParamLng);
            if (lat != null && lng != null) {
                String url = googleGC + lat + "," + lng + "&sensor=true";
                result.setData(ParserTool.do_get(url).trim());
                result.setMessage("成功！");
                result.setResponseCode(WebConstants.WebResponseCodeOk);
            } else {
                result.setMessage("缺少参数！");
                result.setResponseCode(WebConstants.WebResponseCodeInvalidRequest);
            }
        } else if (ActionReportException.equals(action)) {
            String data = request.getParameter(ParamException);
            UncaughtExceptionData exception = Util.parseUncaughtExceptionData(data);
            if (exception == null) {
                result.setResponseCode(WebConstants.WebResponseCodeFail);
                result.setMessage("exception错误");
            } else {
                boolean ret = DBManager.insertUncaughtExceptionData(exception);
                if (!ret) {
                    result.setResponseCode(WebConstants.WebResponseCodeFail);
                    result.setMessage("服务器错误！");
                } else {
                    result.setResponseCode(WebConstants.WebResponseCodeOk);
                }
            }
        } else if (ActionQueryException.equals(action)) {
            List<String> deviceId = Util.parseListString(request.getParameter(ParamDeviceId));
            Date startTime = WebUtil.parseDate(request.getParameter(ParamStartTime), null);
            Date endTime = WebUtil.parseDate(request.getParameter(ParamEndTime), new Date());
            int offset = WebUtil.parseInt(request.getParameter(ParamOffset), 0);
            int len = WebUtil.parseInt(request.getParameter(ParamLen), 100);
            List<UncaughtExceptionData> exceptions = DBManager.getUncaughtException(deviceId, startTime, endTime,
                    offset, len);
            result.setResponseCode(WebConstants.WebResponseCodeOk);
            result.setData(exceptions);
        } else {
            result.setMessage("act参数不合法！");
            result.setResponseCode(WebConstants.WebResponseCodeInvalidRequest);
        }
        Gson gson = GsonHelper.getGson();
        response.setContentType(WebConstants.ContentTypeText);
        PrintWriter out = response.getWriter();
        if (callback == null) {
            out.print(gson.toJson(result));
        } else {
            WebUtil.outputJSONP(out, callback, result);
        }
        out.flush();
        out.close();
    }
}
