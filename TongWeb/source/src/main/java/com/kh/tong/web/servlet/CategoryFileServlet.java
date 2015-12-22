package com.kh.tong.web.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.kh.tong.web.bean.ResourceFile;
import com.kh.tong.web.bean.User;
import com.kh.tong.web.db.UserDBManger;
import com.kh.tong.web.servlet.helper.SessionHelp;
import com.kh.tong.web.servlet.helper.Util;
import com.kh.webdata.WebResponse;
import com.kh.webutils.MimeUtil;
import com.kh.webutils.WebConstants;
import com.kh.webutils.WebUtil;

public class CategoryFileServlet extends HttpServlet {
    static Logger log = Logger.getLogger(CategoryFileServlet.class);
    private static final long serialVersionUID = 3159940025658111978L;
    public static final String ParamAction = "act";
    public static final String ParamUUID = "uuid";
    public static final String ParamType = "type";
    public static final String ParamUserUuid = "user_uuid";

    public static final String ActionUpload = "upload_resource";
    public static final String ActionDownload = "get_resource";
    public static final String ActionuploadResourceNosession = "upload_resource_nosession";

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
        WebResponse result = new WebResponse();
        int errorCode = 0;
        User userSession = null;

        if (action == null) {
            errorCode = WebConstants.WebResponseCodeInvalidRequest;
        }
        if (action.equals(ActionDownload)) {
            String uuid = request.getParameter(ParamUUID);
            if (uuid != null) {
                ResourceFile resourceFile = UserDBManger.getFilePathByUUID(uuid);
                String filepath = Util.getUserRootDir() + File.separator + resourceFile.getFilePath();
                if (Util.isExistDir(filepath)) {
                    result.setResponseCode(WebConstants.WebResponseCodeFail);
                    result.setMessage("目录无法下载");
                } else if (!Util.isExistFile(filepath)) {
                    result.setResponseCode(WebConstants.WebResponseCodeFail);
                    result.setMessage("源文件不存在");
                } else {
                    String defaultType = MimeUtil.lookupMime(resourceFile.getFileExt());
                    Util.download(filepath, defaultType, request, response);
                    result.setResponseCode(WebConstants.WebResponseCodeOk);
                    return;
                }
            }
        } else {
            if (errorCode == 0) {
                HttpSession session = request.getSession();
                if (session != null) {
                    userSession = (User) session.getAttribute(WebConstants.SessionAttributeUser);
                }
                if (userSession == null && !action.equals(ActionuploadResourceNosession)) {
                    errorCode = WebConstants.WebResponseCodeSessionExpired;
                }
            }
            if (errorCode == 0) {
                if (action.equals(ActionUpload)) {
                    Date now = Calendar.getInstance().getTime();
                    String dataUUID = Util.generateUUID(); // TODO: data UUID
                                                           // 从之前的申请接口获取
                    String fileDirAbsolutePath = Util.getDataDirAbsolutePath(now, dataUUID);
                    String filePath = Util.getDataPath(now, dataUUID);
                    String fileAbsolutePath = Util.getDataAbsolutePath(now, dataUUID);

                    boolean createDirOk = Util.createFolder(fileDirAbsolutePath);
                    if (createDirOk) {
                        boolean uploadresult = SessionHelp.upload(fileAbsolutePath, request, dataUUID,
                                userSession.getUuid(), filePath);
                        if (uploadresult) {
                            result.setData(dataUUID);
                            result.setResponseCode(WebConstants.WebResponseCodeOk);
                        } else {
                            result.setResponseCode(WebConstants.WebResponseCodeFail);
                            result.setMessage("服务器错误!上传图片失败！");
                        }
                    } else {
                        result.setResponseCode(WebConstants.WebResponseCodeFail);
                        result.setMessage("服务器错误!创建目录文件夹失败！");
                    }
                } else if (action.equals(ActionuploadResourceNosession)) {
                    String userUuid = request.getParameter(ParamUserUuid);
                    if (userUuid != null) {
                        Date now = Calendar.getInstance().getTime();
                        String dataUUID = Util.generateUUID(); // TODO: data
                                                               // UUID
                                                               // 从之前的申请接口获取
                        String fileDirAbsolutePath = Util.getDataDirAbsolutePath(now, dataUUID);
                        String filePath = Util.getDataPath(now, dataUUID);
                        String fileAbsolutePath = Util.getDataAbsolutePath(now, dataUUID);

                        boolean createDirOk = Util.createFolder(fileDirAbsolutePath);
                        if (createDirOk) {
                            boolean uploadresult = SessionHelp.upload(fileAbsolutePath, request, dataUUID, userUuid,
                                    filePath);
                            if (uploadresult) {
                                result.setData(dataUUID);
                                result.setResponseCode(WebConstants.WebResponseCodeOk);
                            } else {
                                result.setResponseCode(WebConstants.WebResponseCodeFail);
                                result.setMessage("服务器错误!上传图片失败！");
                            }
                        } else {
                            result.setResponseCode(WebConstants.WebResponseCodeFail);
                            result.setMessage("服务器错误!创建目录文件夹失败！");
                        }
                    }
                }
            } else {
                result.setResponseCode(errorCode);
            }
        }

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