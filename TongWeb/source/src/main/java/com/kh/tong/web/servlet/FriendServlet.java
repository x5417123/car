package com.kh.tong.web.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.kh.tong.web.bean.Friend;
import com.kh.tong.web.bean.FriendUserInfo;
import com.kh.tong.web.bean.PhoneMatchFriendInfo;
import com.kh.tong.web.bean.User;
import com.kh.tong.web.bean.UserInfo;
import com.kh.tong.web.db.FriendDBManger;
import com.kh.tong.web.db.UserDBManger;
import com.kh.tong.web.servlet.helper.Constants;
import com.kh.tong.web.servlet.helper.Util;
import com.kh.webdata.WebResponse;
import com.kh.webutils.WebConstants;
import com.kh.webutils.WebUtil;

public class FriendServlet extends HttpServlet {

    /**
     * 
     */
    private static final long serialVersionUID = -4143229689765131040L;

    static Logger log = Logger.getLogger(FriendServlet.class);

    public static final String ParamAction = "act";
    public static final String ParamUUID = "UUID";
    public static final String ParamRequestRemark = "request_remark";
    public static final String ParamRemarkName = "remark_name";
    public static final String ParamFlag = "flag";
    public static final String ParamAuths = "auths";
    public static final String ParamTags = "tags";
    public static final String ParamPhones = "phones";
    public static final String ParamAgree = "agree";
    public static final String ParamUserUUID = "userUUID";

    public static final String ActionRequestAddFriend = "request_add_friend";
    public static final String ActionQueryAddFriendResult = "query_add_friend_result";
    public static final String ActionQueryFriendList = "query_friend_list";
    public static final String ActionGetFriendAuth = "get_friend_auth";
    public static final String ActionSetFriendAuth = "set_friend_auth";
    public static final String ActionSetFriendRemarkName = "set_friend_remark_name";
    public static final String ActionGetFriendTags = "get_friend_tags";
    public static final String ActionSetFriendTags = "set_friend_tags";
    public static final String ActionDeleteFriend = "delete_friend";
    public static final String ActionMoveFriendToBlacklist = "move_friend_to_blacklist";
    public static final String ActionPhoneAddressBookMatch = "phone_address_book_match";
    public static final String ActionReponseToFriendInvite = "reponse_to_friend_invite";

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
        User userSession = null;
        if (request.getSession() != null) {
            userSession = (User) request.getSession().getAttribute(WebConstants.SessionAttributeUser);
        }

        if (action != null) {
            if (userSession != null) {
                String userUUID = userSession.getUuid();
                if (action.equals(ActionRequestAddFriend)) {
                    // 发送好友请求
                    String friendUUID = request.getParameter(ParamUUID);
                    // String requestRemark = Util.URLDecode(request
                    // .getParameter(ParamRequestRemark));
                    String requestRemark = request.getParameter(ParamRequestRemark);
                    // String remarkName = Util.URLDecode(request
                    // .getParameter(ParamRemarkName));
                    String remarkName = request.getParameter(ParamRemarkName);
                    int flag = Constants.FRIEND_FLAG_PROGRESS;// 2,正在申请好友状态
                    Friend friend = new Friend();
                    friend.setUuid(UUID.randomUUID().toString().replaceAll("-", ""));
                    friend.setUserUuid(userUUID);
                    friend.setFriendUserUuid(friendUUID);
                    friend.setFlag(flag);
                    friend.setRequestRemark(requestRemark);
                    friend.setRemarkName(remarkName);
                    int flag2 = Constants.FRIEND_FLAG_FRIEND_PROGRESS;// 5.我被好友邀请（等待验证）
                    Friend friend2 = new Friend();
                    friend2.setUuid(UUID.randomUUID().toString().replaceAll("-", ""));
                    friend2.setUserUuid(friendUUID);
                    friend2.setFriendUserUuid(userUUID);
                    friend2.setFlag(flag2);
                    int userFlag = FriendDBManger.findFriendFlagByfriendUUIDAndUserUUID(friendUUID, userUUID);
                    boolean addok = false;
                    if (userFlag == 0) {
                        addok = FriendDBManger.addFriend(friend);
                        addok = FriendDBManger.addFriend(friend2);
                    } else {
                        addok = FriendDBManger.upFriendFlagByFriendUUID(friendUUID, flag2, userUUID);
                        addok = FriendDBManger.upFriendFlagByFriendUUID(userUUID, flag, friendUUID);
                    }
                    if (addok) {
                        result.setMessage("添加成功！");
                        result.setResponseCode(WebConstants.WebResponseCodeOk);
                    } else {
                        result.setMessage("服务器错误！");
                        result.setResponseCode(WebConstants.WebResponseCodeFail);
                    }
                } else if (action.equals(ActionQueryAddFriendResult)) {
                    // 查询好友请求结果
                    String friendUUID = request.getParameter(ParamUUID);
                    if (friendUUID != null) {
                        int flag = FriendDBManger.findFriendFlagByfriendUUIDAndUserUUID(friendUUID, userUUID);
                        int res = 0;
                        switch (flag) {
                            case 1:
                                res = 2;
                                break;
                            case 2:
                                res = 1;
                                break;
                            case 3:
                                res = 3;
                                break;
                        }
                        result.setData(res);
                        result.setResponseCode(WebConstants.WebResponseCodeOk);
                    } else {
                        result.setResponseCode(WebConstants.WebResponseCodeInvalidRequest);
                    }
                } else if (action.equals(ActionQueryFriendList)) {
                    // 获取好友列表
                    String flag = request.getParameter(ParamFlag);
                    List<String> flagList = Util.parseListString(flag);
                    if (flag != null) {
                        List<FriendUserInfo> friendList = FriendDBManger
                                .findFriendByflagAndUserUUID(flagList, userUUID);
                        result.setData(friendList);
                        result.setResponseCode(WebConstants.WebResponseCodeOk);
                    } else {
                        List<FriendUserInfo> friendList = FriendDBManger.findFriendByUserUUID(userUUID);
                        result.setData(friendList);
                        result.setResponseCode(WebConstants.WebResponseCodeOk);
                    }
                } else if (action.equals(ActionGetFriendAuth)) {
                    // 获取好友权限
                    String friendUUID = request.getParameter(ParamUUID);
                    if (friendUUID != null) {
                        List<Integer> authList = FriendDBManger.findFriendAuthByFriendUUIDAndUserUUID(friendUUID,
                                userUUID);
                        result.setData(authList);
                        result.setResponseCode(WebConstants.WebResponseCodeOk);
                    } else {
                        result.setResponseCode(WebConstants.WebResponseCodeInvalidRequest);
                    }
                } else if (action.equals(ActionSetFriendAuth)) {
                    // 设置好友权限
                    String friendUUID = request.getParameter(ParamUUID);
                    String authsList = request.getParameter(ParamAuths);
                    if (friendUUID != null && authsList != null) {
                        List<Integer> authList = Util.parseListInteger(authsList);
                        FriendDBManger.delFriendAuthByFriendUUIDAndUserUUID(friendUUID, userUUID);
                        boolean upok = false;
                        for (int auth : authList) {
                            upok = FriendDBManger.addFriendAuthByFriendUUIDAndAuthsList(friendUUID, auth, userUUID);
                        }
                        if (upok) {
                            result.setMessage("更新成功！");
                            result.setResponseCode(WebConstants.WebResponseCodeOk);
                        } else {
                            result.setResponseCode(WebConstants.WebResponseCodeFail);
                        }
                    } else {
                        result.setResponseCode(WebConstants.WebResponseCodeInvalidRequest);
                    }
                } else if (action.equals(ActionSetFriendRemarkName)) {
                    // 设置好友备注名
                    String friendUUID = request.getParameter(ParamUUID);
                    String remarkName = request.getParameter(ParamRemarkName);
                    if (friendUUID != null && remarkName != null) {
                        boolean upok = FriendDBManger.upFriendNickNameByFriendUUIDAndUserUUID(friendUUID, remarkName,
                                userUUID);
                        if (upok) {
                            result.setMessage("更新成功！");
                            result.setResponseCode(WebConstants.WebResponseCodeOk);
                        } else {
                            result.setResponseCode(WebConstants.WebResponseCodeFail);
                        }
                    } else {
                        result.setResponseCode(WebConstants.WebResponseCodeInvalidRequest);
                    }
                } else if (action.equals(ActionGetFriendTags)) {
                    // 获取好友标签
                    String friendUUID = request.getParameter(ParamUUID);
                    if (friendUUID != null) {
                        List<String> tagList = FriendDBManger
                                .findFriendTagByFriendUUIDAndUserUUID(friendUUID, userUUID);
                        result.setData(tagList);
                        result.setResponseCode(WebConstants.WebResponseCodeOk);
                    } else {
                        result.setResponseCode(WebConstants.WebResponseCodeInvalidRequest);
                    }
                } else if (action.equals(ActionSetFriendTags)) {
                    // 设置好友标签
                    String friendUUID = request.getParameter(ParamUUID);
                    String tags = request.getParameter(ParamTags);
                    List<String> taglist = Util.parseListString(tags);
                    if (friendUUID != null) {
                        FriendDBManger.delFriendTagByFriendUUIDAndTags(friendUUID, userUUID);
                        boolean addOK = false;
                        for (String tag : taglist) {
                            addOK = FriendDBManger.addFriendTagByFriendUUIDAndTags(friendUUID, tag, userUUID);
                        }
                        if (addOK) {
                            result.setResponseCode(WebConstants.WebResponseCodeOk);
                        } else {
                            result.setResponseCode(WebConstants.WebResponseCodeFail);
                        }
                    } else {
                        result.setResponseCode(WebConstants.WebResponseCodeInvalidRequest);
                    }
                } else if (action.equals(ActionDeleteFriend)) {
                    // 删除好友
                    String friendUUID = request.getParameter(ParamUUID);
                    if (friendUUID != null) {
                        boolean delok = FriendDBManger.delFriendByFriendUUID(friendUUID, userUUID);
                        if (delok) {
                            result.setMessage("删除成功!");
                            result.setResponseCode(WebConstants.WebResponseCodeOk);
                        } else {
                            result.setResponseCode(WebConstants.WebResponseCodeFail);
                        }
                    } else {
                        result.setResponseCode(WebConstants.WebResponseCodeInvalidRequest);
                    }
                } else if (action.equals(ActionMoveFriendToBlacklist)) {
                    // 将联系人加入黑名单
                    String friendUUID = request.getParameter(ParamUUID);
                    if (friendUUID != null) {
                        int Flag = 4;
                        boolean upok = FriendDBManger.upFriendFlagByFriendUUID(friendUUID, Flag, userUUID);
                        if (upok) {
                            result.setMessage("设置成功!");
                            result.setResponseCode(WebConstants.WebResponseCodeOk);
                        } else {
                            result.setResponseCode(WebConstants.WebResponseCodeFail);
                        }
                    } else {
                        result.setResponseCode(WebConstants.WebResponseCodeInvalidRequest);
                    }
                } else if (action.equals(ActionPhoneAddressBookMatch)) {
                    String Phones = request.getParameter(ParamPhones);
                    if (Phones != null) {
                        List<String> phoneList = Util.parseListString(Phones);
                        List<PhoneMatchFriendInfo> phoneMatchFriendInfoList = new ArrayList<PhoneMatchFriendInfo>();
                        for (int i = 0; i < phoneList.size(); i++) {
                            String phoneInfo = phoneList.get(i).replace("\\+86", "");
                            PhoneMatchFriendInfo phoneMatchFriendInfo = new PhoneMatchFriendInfo();
                            UserInfo userInfo = UserDBManger.getUserInfoByPhone(phoneInfo);
                            boolean isMobile = Util.isMobileNO(phoneInfo);
                            if (userInfo.getUUID() != null && userInfo.getUUID() != "" && isMobile) {
                                phoneMatchFriendInfo.setPhone(phoneInfo);
                                phoneMatchFriendInfo.setUserInfo(userInfo);
                                int flag = FriendDBManger.findFriendFlagByfriendUUIDAndUserUUID(userInfo.getUUID(),
                                        userUUID);
                                switch (flag) {
                                    case 1:
                                        // 1 已经是好友状态
                                        phoneMatchFriendInfo.setFlag(Constants.PHONE_MATCH_FRIEND_INFO_FRIENDS);
                                        break;
                                    case 2:
                                        // 2 好友申请等待验证
                                        phoneMatchFriendInfo.setFlag(Constants.PHONE_MATCH_FRIEND_INFO_PROGRESS);
                                        break;
                                    case 3:
                                        // 3 好友申请被拒绝
                                        phoneMatchFriendInfo.setFlag(Constants.PHONE_MATCH_FRIEND_INFO_NO_ADD);
                                        break;
                                    case 4:
                                        // 4 好友被拉入黑名单
                                        phoneMatchFriendInfo.setFlag(Constants.PHONE_MATCH_FRIEND_INFO_BLOCK_APP);
                                        break;
                                    case 5:
                                        // 5 我被好友邀请（等待验证）
                                        phoneMatchFriendInfo.setFlag(Constants.PHONE_MATCH_FRIEND_INFO_ME_PROGRESS);
                                        break;
                                    case 6:
                                        // 6 拒绝好友邀请
                                        phoneMatchFriendInfo.setFlag(Constants.PHONE_MATCH_FRIEND_INFO_ADD_ME_PROFRESS);
                                        break;
                                    default:
                                        // 7.酷走用户（未添加过）
                                        phoneMatchFriendInfo.setFlag(Constants.PHONE_MATCH_FRIEND_INFO_APP);
                                        break;
                                }
                            } else {
                                phoneMatchFriendInfo.setPhone(phoneInfo);
                                phoneMatchFriendInfo.setUserInfo(null);
                                phoneMatchFriendInfo.setFlag(Constants.PHONE_MATCH_FRIEND_INFO_NO_APP);
                            }
                            phoneMatchFriendInfoList.add(i, phoneMatchFriendInfo);
                        }
                        result.setData(phoneMatchFriendInfoList);
                        result.setMessage("设置成功!");
                        result.setResponseCode(WebConstants.WebResponseCodeOk);
                    }
                } else if (action.equals(ActionReponseToFriendInvite)) {
                    String agree = request.getParameter(ParamAgree).toLowerCase();
                    String userUuid = request.getParameter(ParamUserUUID);
                    if (agree != null && userUuid != null && agree != "" && userUuid != "") {
                        boolean upOK = false;
                        if (agree.equals("true")) {
                            int flag = Constants.FRIEND_FLAG_SUCCESS;
                            upOK = FriendDBManger.upFriendFlagByFriendUUID(userUuid, flag, userUUID);
                            upOK = FriendDBManger.upFriendFlagByFriendUUID(userUUID, flag, userUuid);
                        } else if (agree.equals("false")) {
                            int flag = Constants.FRIEND_FLAG_FRIEND_FAIL;
                            upOK = FriendDBManger.upFriendFlagByFriendUUID(userUUID, flag, userUuid);
                            flag = Constants.FRIEND_FLAG_FAIL;
                            upOK = FriendDBManger.upFriendFlagByFriendUUID(userUuid, flag, userUUID);
                        }
                        if (upOK) {
                            result.setMessage("设置成功!");
                            result.setResponseCode(WebConstants.WebResponseCodeOk);
                        } else {
                            result.setMessage("服务器出错！");
                            result.setResponseCode(WebConstants.WebResponseCodeFail);
                        }
                    }
                }
            } else {
                result.setMessage("请登录!");
                result.setResponseCode(WebConstants.WebResponseCodeFail);
            }
        } else {
            result.setMessage("缺少act参数！");
            result.setResponseCode(WebConstants.WebResponseCodeInvalidRequest);
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
