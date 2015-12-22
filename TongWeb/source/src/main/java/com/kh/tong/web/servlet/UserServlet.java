package com.kh.tong.web.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.kh.tong.web.bean.PhoneReg;
import com.kh.tong.web.bean.ResourceFile;
import com.kh.tong.web.bean.User;
import com.kh.tong.web.bean.UserInfo;
import com.kh.tong.web.db.UserDBManger;
import com.kh.tong.web.servlet.helper.Constants;
import com.kh.tong.web.servlet.helper.SMSImpl;
import com.kh.tong.web.servlet.helper.SessionHelp;
import com.kh.tong.web.servlet.helper.Util;
import com.kh.webdata.WebResponse;
import com.kh.webutils.MimeUtil;
import com.kh.webutils.WebConstants;
import com.kh.webutils.WebUtil;

public class UserServlet extends HttpServlet {
    private static final long serialVersionUID = 729802787437454871L;

    static Logger log = Logger.getLogger(UserServlet.class);

    public static final String EnvParamDevMode = "KH_DEV_MODE";

    private static boolean devMode = false;
    static {
        Map<String, String> env = System.getenv();
        log.info("env.get(EnvParamDevMode)?" + env.get(EnvParamDevMode));
        if ("1".equals(env.get(EnvParamDevMode))) {
            devMode = true;
        } else {
            devMode = false;
        }
        log.info("Dev mode : " + devMode);
    }

    private static final String MessageStart = "您当前操作的验证码为:";
    private static final String MessageEnd = ",有效时间为5分钟，请及时输入！";
    private static final String UserPassword = "123456";
    private static final String UserName = "kuzou_user";
    private static final String IconRealPath = "publc/person.jpg";

    private static final String ParamAction = "act";
    private static final String ParamPhone = "phone";
    private static final String ParamUsername = "username";
    private static final String ParamVerifyCode = "verifycode";
    private static final String ParamUser = "user";
    private static final String ParamLoginCode = "logincode";
    private static final String ParamOldPassword = "oldPassword";
    private static final String ParamNewPassword = "newPassword";
    private static final String ParamUUID = "uuid";
    private static final String ParamUserSearch = "search_user";
    private static final String ParamUserUuid = "user_uuid";

    private static final String ActionUpdateProfile = "update_profile";
    private static final String ActionRequestSMSRegCode = "request_sms_regcode";
    private static final String ActionRequestSMSLoginCode = "request_sms_logincode";
    private static final String ActionSendEmailVerifyCode = "send_email_verifycode";
    private static final String ActionSubmitSMSVerifyCode = "submit_sms_verifycode";
    private static final String ActionSubmitSMSLoginCode = "submit_sms_logincode";
    private static final String ActionSubmitEmailVerifyCode = "submit_email_verifycode";
    private static final String ActionUserLogin = "user_login";
    private static final String ActionResetPassword = "reset_password";
    private static final String ActionQueryInfo = "query_info";
    private static final String ActionQueryUserInfo = "query_userinfo";
    private static final String ActionUploadUserIcon = "up_user_icon";
    private static final String ActionDownloadUserIcon = "down_user_icon";
    private static final String ActionUploadUserIconNosession = "up_user_icon_nosession";

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
        User userSession = (User) request.getSession().getAttribute(WebConstants.SessionAttributeUser);
        if (ActionUpdateProfile.equals(action)) {
            // 更新用户个人资料
            if (userSession != null) {
                String userData = request.getParameter(ParamUser);
                if (userData != null) {
                    User user = Util.parseUser(userData);
                    boolean updateOK = false;
                    if (user != null) {
                        if (user.getUuid() == null) {
                            user.setUuid(userSession.getUuid());
                        }
                        if (user.getNickname() != null) {
                            log.info(user.getNickname());
                            updateOK = UserDBManger.updateUserNickName(user);
                        }
                        if (user.getPassword() != null) {
                            updateOK = UserDBManger.updateUserPassword(user);
                        }
                        if (user.getEmail() != null) {
                            updateOK = UserDBManger.updateUserEmail(user);
                        }
                        if (updateOK) {
                            result.setMessage("更新成功！");
                            result.setResponseCode(WebConstants.WebResponseCodeOk);
                        } else {
                            result.setMessage("更新失败！");
                            result.setResponseCode(WebConstants.WebResponseCodeFail);
                        }
                    } else {
                        result.setMessage("user参数错误！！");
                        result.setResponseCode(WebConstants.WebResponseCodeFail);
                    }
                }
            } else {
                result.setMessage("请先登录！");
                result.setResponseCode(WebConstants.WebResponseCodeOk);
            }
        } else if (ActionRequestSMSRegCode.equals(action)) {
            // 服务器发送短信注册码到手机
            String phone = request.getParameter(ParamPhone);
            if (phone != null && !"".equals(phone)) {
                if (Util.isMobileNO(phone)) {
                    if (UserDBManger.getUserByPhone(phone).size() > 0) {
                        result.setData(Constants.REQUEST_SMS_REG_CODE_PHONE_REPEAT);
                        result.setResponseCode(WebConstants.WebResponseCodeOk);
                    } else {
                        List<PhoneReg> phoneList = UserDBManger.getPhoneRegByPhone(phone);
                        String code = String.valueOf((int) ((Math.random() * 9 + 1) * 100000));
                        boolean phoneRegOK = false;
                        if (phoneList.size() > 0) {
                            PhoneReg phoneReg = phoneList.get(0);
                            long validTime = phoneReg.getValidTime().getTime() + 15 * 60 * 1000;
                            long nowTime = new Date().getTime();
                            if (validTime > nowTime) {
                                code = phoneReg.getVerifyCode();
                            }
                            phoneRegOK = UserDBManger.updatePhoneRegVerifyInfoByUUID(phoneReg.getUuid(), code);
                        } else {
                            PhoneReg phoneReg = new PhoneReg();
                            phoneReg.setPhone(phone);
                            phoneReg.setCreateTime(new Date());
                            phoneReg.setUuid(UUID.randomUUID().toString().replaceAll("-", ""));
                            phoneReg.setValidTime(new Date());
                            phoneReg.setVerifyCode(code);
                            phoneRegOK = UserDBManger.addPhoneReg(phoneReg);
                        }
                        if (phoneRegOK) {
                            String content = MessageStart + code + MessageEnd;
                            boolean messageOk = SMSImpl.sendOrdinarySMSMessages(content, phone, null);
                            if (messageOk) {
                                result.setData(Constants.REQUEST_SMS_REG_CODE_SUCCESS);
                                result.setResponseCode(WebConstants.WebResponseCodeOk);
                            } else {
                                result.setData(Constants.REQUEST_SMS_REG_CODE_FAIL);
                                result.setResponseCode(WebConstants.WebResponseCodeOk);
                            }
                        } else {
                            result.setMessage("服务器错误！");
                            result.setResponseCode(WebConstants.WebResponseCodeFail);
                        }
                    }
                } else {
                    result.setData(Constants.REQUEST_SMS_REG_CODE_PHONE_FORMAT_NOT_CORRECT);
                    result.setMessage("手机格式不正确！");
                    result.setResponseCode(WebConstants.WebResponseCodeOk);
                }
            } else {
                result.setMessage("缺少手机号参数！");
                result.setResponseCode(WebConstants.WebResponseCodeInvalidRequest);
            }
        } else if (ActionRequestSMSLoginCode.equals(action)) {
            // 服务器发送短信登录码到手机
            String phone = request.getParameter(ParamPhone);
            if (phone != null && !"".equals(phone)) {
                if (Util.isMobileNO(phone)) {
                    boolean userOK = UserDBManger.isUserPhoneExist(phone);
                    if (userOK) {
                        String code = String.valueOf((int) ((Math.random() * 9 + 1) * 100000));
                        boolean updateOK = UserDBManger.updateUserLoginCode(code, phone);
                        if (updateOK) {
                            String content = MessageStart + code + MessageEnd;
                            boolean messageOk = SMSImpl.sendOrdinarySMSMessages(content, phone, null);
                            if (messageOk) {
                                result.setData(Constants.REQUEST_SMS_REG_CODE_SUCCESS);
                                result.setResponseCode(WebConstants.WebResponseCodeOk);
                            } else {
                                result.setData(Constants.REQUEST_SMS_REG_CODE_FAIL);
                                result.setResponseCode(WebConstants.WebResponseCodeOk);
                            }
                        } else {
                            result.setResponseCode(WebConstants.WebResponseCodeFail);
                        }
                    } else {
                        result.setData(Constants.REQUEST_SMS_REG_CODE_PHONE_REPEAT);
                        result.setResponseCode(WebConstants.WebResponseCodeOk);
                    }
                } else {
                    result.setData(Constants.REQUEST_SMS_REG_CODE_PHONE_FORMAT_NOT_CORRECT);
                    result.setMessage("手机格式不正确！");
                    result.setResponseCode(WebConstants.WebResponseCodeOk);
                }
            } else {
                result.setMessage("缺少手机号参数！");
                result.setResponseCode(WebConstants.WebResponseCodeInvalidRequest);
            }
        } else if (ActionSendEmailVerifyCode.equals(action)) {
            // 发送email验证码

        } else if (ActionSubmitSMSVerifyCode.equals(action)) {
            // 提交手机注册验证码
            String phone = request.getParameter(ParamPhone);
            String verifyCode = request.getParameter(ParamVerifyCode);
            if (phone != null && !"".equals(phone) && verifyCode != null && !"".equals(verifyCode)) {
                if (Util.isMobileNO(phone)) {
                    if (UserDBManger.getUserByPhone(phone).size() > 0) {
                        result.setData(Constants.REQUEST_SMS_REG_CODE_PHONE_REPEAT);
                        result.setResponseCode(WebConstants.WebResponseCodeOk);
                    } else {
                        List<PhoneReg> phoneRegs = UserDBManger.getPhoneRegByPhoneAndVerifyCode(phone, verifyCode);
                        if (phoneRegs.size() > 0) {
                            PhoneReg phoneReg = phoneRegs.get(0);
                            Date time = new Date();
                            if ((phoneReg.getValidTime().getTime() + 5 * 60 * 1000) > time.getTime()) {
                                String uuid = UUID.randomUUID().toString().replaceAll("-", "");
                                String iconUUID = UUID.randomUUID().toString().replaceAll("-", "");
                                User user = new User();
                                user.setUuid(uuid);
                                user.setPhone(phoneReg.getPhone());
                                user.setPassword(UserPassword);
                                user.setIconUUID(iconUUID);
                                boolean addUserOK = UserDBManger.addOaUser(user);
                                if (addUserOK) {
                                    int id = UserDBManger.getUserIdByUuid(uuid);
                                    String username = UserName + id;
                                    boolean upok = UserDBManger.updateUserNameByUuid(username, uuid);
                                    if (upok) {
                                        SessionHelp.loginSession(request, user);
                                        String userSessionUUid = ((User) request.getSession().getAttribute(
                                                WebConstants.SessionAttributeUser)).getUuid();
                                        String fileType = "jpg";
                                        UserDBManger.addResource(iconUUID, userSessionUUid, fileType, IconRealPath);
                                        user.setName(username);
                                        result.setData(Constants.SUBMIT_SMS_VERIFY_CODE_SUCCESS);
                                        result.setResponseCode(WebConstants.WebResponseCodeOk);
                                    } else {
                                        result.setResponseCode(WebConstants.WebResponseCodeFail);
                                    }
                                } else {
                                    result.setResponseCode(WebConstants.WebResponseCodeFail);
                                }
                            } else {
                                result.setData(Constants.SUBMIT_SMS_VERIFY_CODE_PROGRESS);
                                result.setResponseCode(WebConstants.WebResponseCodeOk);
                            }
                        } else {
                            result.setData(Constants.SUBMIT_SMS_VERIFY_CODE_FAIL);
                            result.setResponseCode(WebConstants.WebResponseCodeOk);
                        }
                    }
                } else {
                    result.setMessage("服务器错误！");
                    result.setResponseCode(WebConstants.WebResponseCodeFail);
                }
            }
        } else if (ActionSubmitSMSLoginCode.equals(action)) {
            // 提交手机短信登录码
            String Phone = request.getParameter(ParamPhone);
            String logincode = request.getParameter(ParamLoginCode);
            if (Phone != null && Phone != "" && logincode != null && logincode != "") {
                if (Util.isMobileNO(Phone)) {
                    List<User> userlist = UserDBManger.getUserByPhoneAndLoginCode(Phone, logincode);
                    if (userlist.size() > 0) {
                        User user = userlist.get(0);
                        Date time = new Date();
                        if ((user.getPhoneLoginCodeValidtime().getTime() + 5 * 60 * 1000) > time.getTime()) {
                            SessionHelp.loginSession(request, user);
                            result.setData(Constants.REQUEST_SMS_REG_CODE_SUCCESS);
                            result.setResponseCode(WebConstants.WebResponseCodeOk);
                        } else {
                            result.setData(Constants.SUBMIT_SMS_VERIFY_CODE_PROGRESS);
                            result.setResponseCode(WebConstants.WebResponseCodeOk);
                        }
                    } else {
                        result.setData(Constants.REQUEST_SMS_REG_CODE_PHONE_REPEAT);
                        result.setResponseCode(WebConstants.WebResponseCodeOk);
                    }
                }
            }
        } else if (ActionSubmitEmailVerifyCode.equals(action)) {
            // 提交Email验证码

        } else if (ActionUserLogin.equals(action)) {
            // 用户登录
            String userData = request.getParameter(ParamUser);
            // User user = Util.parseUser(Util.URLDecode(userData));
            User user = Util.parseUser(userData);
            if (user.getPhone() != null && user.getPassword() != null) {
                List<User> userList = UserDBManger.getUserByPhoneAndPassword(user.getPhone(), user.getPassword());
                if (userList.size() > 0) {
                    SessionHelp.loginSession(request, userList.get(0));
                    result.setData(userList.get(0));
                    result.setResponseCode(WebConstants.WebResponseCodeOk);
                } else {
                    result.setMessage("账号或密码错误！");
                    result.setResponseCode(WebConstants.WebResponseCodeFail);
                }
            } else if (user.getName() != null && user.getPassword() != null) {
                List<User> userList = UserDBManger.getUserByNameAndPassword(user.getPhone(), user.getPassword());
                if (userList.size() > 0) {
                    SessionHelp.loginSession(request, userList.get(0));
                    result.setData(userList.get(0));
                    result.setResponseCode(WebConstants.WebResponseCodeOk);
                } else {
                    result.setMessage("账号或密码错误！");
                    result.setResponseCode(WebConstants.WebResponseCodeFail);
                }
            } else if (user.getEmail() != null && user.getPassword() != null) {
                List<User> userList = UserDBManger.getUserByEmailAndPassword(user.getPhone(), user.getPassword());
                if (userList.size() > 0) {
                    SessionHelp.loginSession(request, userList.get(0));
                    result.setData(userList.get(0));
                    result.setResponseCode(WebConstants.WebResponseCodeOk);
                } else {
                    result.setMessage("账号或密码错误！");
                    result.setResponseCode(WebConstants.WebResponseCodeFail);
                }
            }
        } else if (ActionResetPassword.equals(action)) {
            // 重置用户密码
            if (userSession != null) {
                String oldPassword = request.getParameter(ParamOldPassword);
                String newPassword = request.getParameter(ParamNewPassword);
                if (userSession.getPassword() != oldPassword) {
                    result.setData(Constants.RESET_PASSWORD_FAIL);
                    result.setResponseCode(WebConstants.WebResponseCodeOk);
                } else {
                    boolean upok = UserDBManger.updateUserPasswordByUuid(newPassword, userSession.getUuid());
                    if (upok) {
                        result.setData(Constants.RESET_PASSWORD_SUCCESS);
                        result.setResponseCode(WebConstants.WebResponseCodeOk);
                    } else {
                        result.setMessage("服务器出错！");
                        result.setResponseCode(WebConstants.WebResponseCodeFail);
                    }
                }
            } else {
                result.setMessage("请先登录！");
                result.setResponseCode(WebConstants.WebResponseCodeFail);
            }
        } else if (ActionQueryInfo.equals(action)) {
            // 获取我的信息
            if (userSession != null) {
                UserInfo userInfo = UserDBManger.getUserInfoByUUID(userSession.getUuid());
                User user = new User();
                user.setUuid(userInfo.getUUID());
                user.setIconUUID(userInfo.getIconUUID());
                user.setLastLoginTime(userInfo.getLastLoginTime());
                user.setName(userInfo.getName());
                user.setNickname(userInfo.getNickName());
                user.setPhone(userInfo.getPhone());
                result.setData(user);
                result.setResponseCode(WebConstants.WebResponseCodeOk);
            } else {
                result.setMessage("请先登录！");
                result.setResponseCode(WebConstants.WebResponseCodeFail);
            }
        } else if (ActionQueryUserInfo.equals(action)) {
            // 查询用户信息
            String userName = request.getParameter(ParamUsername);
            String phone = request.getParameter(ParamPhone);
            String userUUID = request.getParameter(ParamUUID);
            String userSearch = request.getParameter(ParamUserSearch);
            if (userName != null && userName.length() > 0) {
                List<UserInfo> userList = UserDBManger.getUserInfoByNickName(userName);
                result.setData(userList);
                result.setResponseCode(WebConstants.WebResponseCodeOk);
            } else if (phone != null && phone.length() > 0) {
                UserInfo user = UserDBManger.getUserInfoByPhone(phone);
                result.setData(user);
                result.setResponseCode(WebConstants.WebResponseCodeOk);
            } else if (userUUID != null && userUUID.length() > 0) {
                UserInfo user = UserDBManger.getUserInfoByUUID(userUUID);
                result.setData(user);
                result.setResponseCode(WebConstants.WebResponseCodeOk);
            } else if (userSearch != null && userSearch.length() > 0) {
                UserInfo user = UserDBManger.getUserInfo(userSearch);
                result.setData(user);
                result.setResponseCode(WebConstants.WebResponseCodeOk);
            }
        } else if (ActionUploadUserIcon.equals(action)) {
            if (userSession != null) {
                String userUUID = userSession.getUuid();
                if (userUUID != null) {
                    Date now = Calendar.getInstance().getTime();
                    String dataUUID = Util.generateUUID(); // TODO: data
                                                           // UUID
                                                           // 从之前的申请接口获取
                    String fileDirAbsolutePath = Util.getDataDirAbsolutePath(now, dataUUID);
                    String filePath = Util.getDataPath(now, dataUUID);
                    String fileAbsolutePath = Util.getDataAbsolutePath(now, dataUUID);

                    boolean createDirOk = Util.createFolder(fileDirAbsolutePath);
                    if (createDirOk) {
                        boolean uploadresult = SessionHelp.upload(fileAbsolutePath, request, dataUUID, userUUID,
                                filePath);
                        if (uploadresult) {
                            User user = new User();
                            user.setUuid(userUUID);
                            user.setIconUUID(dataUUID);
                            UserDBManger.updateUserIcon(user);
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
            } else {
                result.setMessage("请先登录！");
                result.setResponseCode(WebConstants.WebResponseCodeOk);
            }
        } else if (ActionDownloadUserIcon.equals(action)) {
            String userUUID = request.getParameter(ParamUUID);
            String iconUUID = null;
            if (userUUID != null && userUUID != "") {
                iconUUID = UserDBManger.getIconPathByUserUUID(userUUID);
            } else {
                iconUUID = UserDBManger.getIconPathByUserUUID(userSession.getUuid());
            }
            if (iconUUID != null) {
                ResourceFile resFile = UserDBManger.getFilePathByUUID(iconUUID);
                String filepath = Util.getUserRootDir() + File.separator + resFile.getFilePath();
                if (Util.isExistDir(filepath)) {
                    result.setResponseCode(WebConstants.WebResponseCodeFail);
                    result.setMessage("目录无法下载");
                } else if (!Util.isExistFile(filepath)) {
                    result.setResponseCode(WebConstants.WebResponseCodeFail);
                    result.setMessage("源文件不存在");
                } else {
                    String defaultType = MimeUtil.lookupMime(resFile.getFileExt());
                    Util.download(filepath, defaultType, request, response);
                    result.setResponseCode(WebConstants.WebResponseCodeOk);
                    return;
                }
            }
        } else if (ActionUploadUserIconNosession.equals(action)) {
            String userUUID = request.getParameter(ParamUserUuid);
            if (userUUID != null) {
                Date now = Calendar.getInstance().getTime();
                String dataUUID = Util.generateUUID(); // TODO: data
                                                       // UUID
                                                       // 从之前的申请接口获取
                String fileDirAbsolutePath = Util.getDataDirAbsolutePath(now, dataUUID);
                String filePath = Util.getDataPath(now, dataUUID);
                String fileAbsolutePath = Util.getDataAbsolutePath(now, dataUUID);

                boolean createDirOk = Util.createFolder(fileDirAbsolutePath);
                if (createDirOk) {
                    boolean uploadresult = SessionHelp.upload(fileAbsolutePath, request, dataUUID, userUUID, filePath);
                    if (uploadresult) {
                        User user = new User();
                        user.setUuid(userUUID);
                        user.setIconUUID(dataUUID);
                        UserDBManger.updateUserIcon(user);
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
