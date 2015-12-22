package com.kh.tong.web.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import com.kh.webdata.WebResponse;
import com.kh.webutils.WebConstants;
import com.kh.webutils.WebUtil;

public class ConfigServlet extends HttpServlet {
    private static final long serialVersionUID = 5954042766113409571L;
    private static final String projectName = "/html5app/?";
    private static final String urlUserUUID = "user_uuid=";
    private static final String urlIp = "&porturl=";
    private static final String urlPassword = "&password=";

    private static final String ParamAction = "act";
    private static final String ParamIp = "ip";
    private static final String ParamUserUUID = "user_uuid";
    private static final String ParamPassword = "password";

    private static final String ActionGetHtmlAppUrl = "get_app_url";

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doWork(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doWork(request, response);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void doWork(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String callback = request.getParameter(WebConstants.RequestParamJsonpCallback);
        String action = request.getParameter(ParamAction);
        String ip = request.getParameter(ParamIp);
        String userUUID = request.getParameter(ParamUserUUID);
        String password = request.getParameter(ParamPassword);
        WebResponse result = new WebResponse();
        String res = null;
        int errorCode = 0;

        if (action == null || ip == null || userUUID == null || password == null) {
            errorCode = WebConstants.WebResponseCodeInvalidRequest;
        }
        if (errorCode == 0) {
            if (action.equals(ActionGetHtmlAppUrl)) {
                res = "http:\\" + ip + projectName + urlUserUUID + userUUID + urlIp + ip + urlPassword + password;
                errorCode = 100;
                result.setData(res);
            }
        }
        result.setResponseCode(errorCode);

        JSONObject json = JSONObject.fromObject(result, WebConstants.NGJsonConfig);
        response.setContentType(WebConstants.ContentTypeText);
        PrintWriter out = response.getWriter();
        if (callback == null) {
            out.print(json);
        } else {
            WebUtil.outputJSONP(out, callback, json);
        }
        out.flush();
        out.close();
    }

}
