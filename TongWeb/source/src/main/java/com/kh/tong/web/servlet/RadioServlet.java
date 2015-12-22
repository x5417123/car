package com.kh.tong.web.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.kh.json.GsonHelper;
import com.kh.tong.web.bean.User;
import com.kh.tong.web.db.DBManager;
import com.kh.tong.web.servlet.helper.Util;
import com.kh.webdata.WebResponse;
import com.kh.webutils.WebConstants;
import com.kh.webutils.WebUtil;
import com.ximalaya.sdk4j.Lives;
import com.ximalaya.sdk4j.model.Paging;
import com.ximalaya.sdk4j.model.XimalayaException;
import com.ximalaya.sdk4j.model.dto.live.Province;
import com.ximalaya.sdk4j.model.dto.live.Radio;
import com.ximalaya.sdk4j.model.dto.live.RadioList;

public class RadioServlet extends HttpServlet {

    private static final long serialVersionUID = 3506823452348304139L;
    static Logger log = Logger.getLogger(RadioServlet.class);
    public static final String ParamAction = "act";
    public static final String ParamPage = "page";
    public static final String ParamCount = "count";
    public static final String ParamProvinceCode = "province_code";
    public static final String ParamRadioId = "radio_id";
    public static final String ParamUserUUID = "user_uuid";

    public static final String ActionGetNationalRadioList = "get_national_radio_list";
    public static final String ActionGetProvinceList = "get_province_list";
    public static final String ActionGetProvinceRadioList = "get_province_radio_list";
    public static final String ActionGetInternetRadioList = "get_internet_radio_list";
    public static final String ActionGetCollectRadioList = "get_collect_radio_list";
    public static final String ActionGetRadioListById = "get_radio_list_by_id";
    public static final String ActionAddCollectRadioIdList = "add_collect_radio_id_list";
    public static final String ActionUpdateCollectRadioIdList = "update_collect_radio_id_list";
    public static final String ActionDeleteCollectRadioIdList = "delete_collect_radio_id_list";
    public static final String ActionSaveRadioPlayStatus = "save_radio_play_status";
    public static final String ActionGetRadioPlayStatus = "get_radio_play_status";

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            doWork(request, response);
        } catch (XimalayaException e) {
            log.error("XimalayaException:" + e);
            e.printStackTrace();
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            doWork(request, response);
        } catch (XimalayaException e) {
            log.error("XimalayaException:" + e);
            e.printStackTrace();
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void doWork(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException,
            XimalayaException {
        String callback = request.getParameter(WebConstants.RequestParamJsonpCallback);
        WebResponse result = new WebResponse();
        String action = request.getParameter(ParamAction);
        Lives lives = new Lives();
        User userSession = null;
        if (request.getSession() != null) {
            userSession = (User) request.getSession().getAttribute(WebConstants.SessionAttributeUser);
        }
        String currentUserUUID = null;
        if (userSession != null) {
            currentUserUUID = userSession.getUuid();
        }
        if (ActionGetNationalRadioList.equals(action)) {
            int radioType = 1;
            int page = WebUtil.parseInt(request.getParameter(ParamPage), 1);
            int count = WebUtil.parseInt(request.getParameter(ParamCount), 20);
            Paging paging = new Paging(page, count);
            RadioList radioList = lives.getRadioList(radioType, null, paging);
            // 是否在用户收藏列表中?改变isCollected属性为true
            List<Long> collectedRadioIDList = DBManager.getCollectRadioIdList(currentUserUUID);
            List<Radio> list = radioList.getRadios();
            for (Radio radio : list) {
                if (collectedRadioIDList.contains(radio.getId())) {
                    radio.setCollected(true);
                }
            }
            result.setResponseCode(WebConstants.WebResponseCodeOk);
            result.setData(radioList);
        } else if (ActionGetProvinceList.equals(action)) {
            List<Province> provinces = lives.getProvinces();
            result.setResponseCode(WebConstants.WebResponseCodeOk);
            result.setData(provinces);
        } else if (ActionGetProvinceRadioList.equals(action)) {
            int radioType = 2;
            int page = WebUtil.parseInt(request.getParameter(ParamPage), 1);
            int count = WebUtil.parseInt(request.getParameter(ParamCount), 20);
            String provinceCode = request.getParameter(ParamProvinceCode);
            if (provinceCode == null || provinceCode.length() == 0) {
                result.setResponseCode(WebConstants.WebResponseCodeFail);
                result.setMessage("provinceCode为空");
            } else {
                Paging paging = new Paging(page, count);
                RadioList radioList = lives.getRadioList(radioType, provinceCode, paging);
                // 是否在用户收藏列表中?改变isCollected属性为true
                List<Long> collectedRadioIDList = DBManager.getCollectRadioIdList(currentUserUUID);
                List<Radio> list = radioList.getRadios();
                for (Radio radio : list) {
                    if (collectedRadioIDList.contains(radio.getId())) {
                        radio.setCollected(true);
                    }
                }
                result.setResponseCode(WebConstants.WebResponseCodeOk);
                result.setData(radioList);
            }
        } else if (ActionGetInternetRadioList.equals(action)) {
            int radioType = 3;
            int page = WebUtil.parseInt(request.getParameter(ParamPage), 1);
            int count = WebUtil.parseInt(request.getParameter(ParamCount), 20);
            Paging paging = new Paging(page, count);
            RadioList radioList = lives.getRadioList(radioType, null, paging);
            // 是否在用户收藏列表中?改变isCollected属性为true
            List<Long> collectedRadioIDList = DBManager.getCollectRadioIdList(currentUserUUID);
            List<Radio> list = radioList.getRadios();
            for (Radio radio : list) {
                if (collectedRadioIDList.contains(radio.getId())) {
                    radio.setCollected(true);
                }
            }
            result.setResponseCode(WebConstants.WebResponseCodeOk);
            result.setData(radioList);
        } else if (ActionGetRadioListById.equals(action)) {
            String data = request.getParameter(ParamRadioId);
            List<Long> radioIdList = Util.parseListLong(data);
            if (radioIdList == null) {
                result.setResponseCode(WebConstants.WebResponseCodeFail);
                result.setMessage("radio_id为空");
            } else {
                long[] radiosIdArray = new long[radioIdList.size()];
                for (int i = 0; i < radioIdList.size(); i++) {
                    radiosIdArray[i] = radioIdList.get(i);
                }
                List<Radio> radios = lives.batchGetRadios(radiosIdArray);
                // 是否在用户收藏列表中?改变isCollected属性为true
                List<Long> collectedRadioIDList = DBManager.getCollectRadioIdList(currentUserUUID);
                for (Radio radio : radios) {
                    if (collectedRadioIDList.contains(radio.getId())) {
                        radio.setCollected(true);
                    }
                }
                result.setResponseCode(WebConstants.WebResponseCodeOk);
                result.setData(radios);
            }
        } else if (ActionGetCollectRadioList.equals(action)) {
            String userUUID = request.getParameter(ParamUserUUID);
            String dbUUID = "";
            if (userUUID != null) {
                dbUUID = userUUID;
            } else {
                dbUUID = currentUserUUID;
            }
            List<Long> radioIdList = DBManager.getCollectRadioIdList(dbUUID);
            long[] radiosIdArray = new long[radioIdList.size()];
            for (int i = 0; i < radioIdList.size(); i++) {
                radiosIdArray[i] = radioIdList.get(i);
            }
            List<Radio> radios = lives.batchGetRadios(radiosIdArray);
            // isCollected属性为true
            for (Radio radio : radios) {
                radio.setCollected(true);
            }
            result.setResponseCode(WebConstants.WebResponseCodeOk);
            result.setData(radios);
        } else if (ActionUpdateCollectRadioIdList.equals(action)) {
            String data = request.getParameter(ParamRadioId);
            List<Long> radioIdList = Util.parseListLong(data);
            if (radioIdList == null) {
                result.setResponseCode(WebConstants.WebResponseCodeFail);
                result.setMessage("radio_id为空");
            } else {
                boolean ret = DBManager.updateCollectRadioList(radioIdList, currentUserUUID);
                if (!ret) {
                    result.setResponseCode(WebConstants.WebResponseCodeFail);
                    result.setMessage("服务器错误！");
                } else {
                    result.setResponseCode(WebConstants.WebResponseCodeOk);
                }
            }
        } else if (ActionAddCollectRadioIdList.equals(action)) {
            String data = request.getParameter(ParamRadioId);
            List<Long> radioIdList = Util.parseListLong(data);
            if (radioIdList == null) {
                result.setResponseCode(WebConstants.WebResponseCodeFail);
                result.setMessage("radio_id为空");
            } else {
                boolean ret = DBManager.insertCollectRadioList(radioIdList, currentUserUUID);
                if (!ret) {
                    result.setResponseCode(WebConstants.WebResponseCodeFail);
                    result.setMessage("服务器错误！");
                } else {
                    result.setResponseCode(WebConstants.WebResponseCodeOk);
                }
            }
        } else if (ActionDeleteCollectRadioIdList.equals(action)) {
            String data = request.getParameter(ParamRadioId);
            List<Long> radioIdList = Util.parseListLong(data);
            if (radioIdList == null) {
                result.setResponseCode(WebConstants.WebResponseCodeFail);
                result.setMessage("radio_id为空");
            } else {
                boolean ret = DBManager.deleteCollectRadioList(radioIdList, currentUserUUID);
                if (!ret) {
                    result.setResponseCode(WebConstants.WebResponseCodeFail);
                    result.setMessage("服务器错误！");
                } else {
                    result.setResponseCode(WebConstants.WebResponseCodeOk);
                }
            }
        } else if (ActionSaveRadioPlayStatus.equals(action)) {
            Long radioId = WebUtil.parseLong(request.getParameter(ParamRadioId), -1);
            if (radioId < 0) {
                result.setResponseCode(WebConstants.WebResponseCodeFail);
                result.setMessage("radioId参数错误");
            } else {
                boolean ret = DBManager.insertLastRadioId(radioId, currentUserUUID);
                if (!ret) {
                    result.setResponseCode(WebConstants.WebResponseCodeFail);
                    result.setMessage("服务器错误！");
                } else {
                    result.setResponseCode(WebConstants.WebResponseCodeOk);
                }
            }
        } else if (ActionGetRadioPlayStatus.equals(action)) {
            Long radioId = DBManager.getLastRadioId(currentUserUUID);
            result.setResponseCode(WebConstants.WebResponseCodeOk);
            result.setData(radioId);
        } else {
            log.error("Unsupported action : " + action);
            result.setResponseCode(WebConstants.WebResponseCodeInvalidRequest);
        }

        Gson gson = GsonHelper.getGson();
        response.setContentType(WebConstants.ContentTypeText);
        PrintWriter out = response.getWriter();
        if (callback == null) {
            out.print(gson.toJson(result));
        } else {
            WebUtil.outputJSONP(out, callback, result, gson);
        }
        out.flush();
        out.close();
    }
}
