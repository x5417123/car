package com.kh.tong.web.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.geo.utils.Pair;
import com.kh.tong.term.bean.TrackPoint;
import com.kh.tong.web.bean.PSession;
import com.kh.tong.web.bean.PSessionLocation;
import com.kh.tong.web.bean.Session;
import com.kh.tong.web.bean.SessionMessage;
import com.kh.tong.web.bean.SessionUser;
import com.kh.tong.web.bean.User;
import com.kh.tong.web.db.SessionDBManager;
import com.kh.tong.web.servlet.helper.Constants;
import com.kh.tong.web.servlet.helper.Util;
import com.kh.webdata.WebResponse;
import com.kh.webutils.WebConstants;
import com.kh.webutils.WebUtil;

public class SessionServlet extends HttpServlet {

    private static final long serialVersionUID = 4077588730252539778L;

    static Logger log = Logger.getLogger(SessionServlet.class);

    public static final String ParamAction = "act";
    public static final String ParamUUID = "UUID";
    public static final String ParamSessionUUID = "sessionUUID";
    public static final String ParamUserUUID = "userUUID";
    public static final String ParamPsessionUUID = "psessionUUID";
    public static final String ParamName = "name";
    public static final String ParamMessage = "message";
    public static final String ParamStarttime = "starttime";
    public static final String ParamEndTime = "endtime";
    public static final String ParamOffset = "offset";
    public static final String ParamLen = "len";
    public static final String ParamUUIDList = "UUIDList";
    public static final String ParamFlag = "Invite_flag";
    public static final String ParamMessageUUID = "messageUUID";
    public static final String ParamTrackPoint = "track_point";
    public static final String ParamUserUuid = "user_uuid";
    public static final String ParamPessionUuid = "psession_uuid";
    public static final String ParamSessionUuid = "session_uuid";

    public static final String ActionChatSession = "get_chat_session";
    public static final String ActionInviteFriendToSession = "invite_friend_to_session";
    public static final String ActionQuerySessionList = "query_session_list";
    public static final String ActionQuerySessionUserlist = "query_session_userlist";
    public static final String ActionKickUserOutSession = "kick_user_out_session";
    public static final String ActionQuitSession = "quit_session";
    public static final String ActionSendSessionMessage = "send_session_message";
    public static final String ActionQuerySessionMessage = "query_session_message";
    public static final String ActionQueryPsession = "query_psession";
    public static final String ActionInviteUserToPression = "invite_user_to_psession";
    public static final String ActionQueryMeInviteToPession = "query_me_invite_to_pession";
    public static final String ActionCongifPsessionLeader = "congif_psession_leader";
    public static final String ActionUpdateSessionInfo = "update_session_info";
    public static final String ActionQuitPsession = "quit_psession";
    public static final String ActionQuitClosePsession = "close_psession";
    public static final String ActionReponseToPsessionInvite = "reponse_to_psession_invite";
    public static final String ActionUploadMyLocation = "upload_mylocation";
    public static final String ActionPSessionUserLocations = "psession_user_locations";
    public static final String ActionSendPSessionMessageNoSession = "send_psession_message_nosession";
    public static final String ActionGetPSessionMessageNoSession = "get_psession_message_nosession";

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
        User userSession = null;
        if (request.getSession() != null) {
            userSession = (User) request.getSession().getAttribute(WebConstants.SessionAttributeUser);
        }
        String action = request.getParameter(ParamAction);
        if (userSession != null) {
            String userUUID = userSession.getUuid();
            if (ActionChatSession.equals(action)) {
                // 邀请好友一对一会话
                String friendUUID = request.getParameter(ParamUUID);
                if (friendUUID != null) {
                    boolean checkFriend = SessionDBManager.checkFriend(friendUUID, userUUID);
                    String sessionUUID = Util.generateUUID();
                    if (checkFriend) {
                        Session sess = new Session();
                        sess.setUuid(sessionUUID);
                        sess.setSessionType(Constants.SESSION_TYPE_PRIVATE_CHAT);
                        sess.setInitUserUUID(userUUID);
                        List<Session> sessionList = SessionDBManager.getSessionUser(userUUID, friendUUID,
                                Constants.SESSION_TYPE_PRIVATE_CHAT);
                        if (sessionList.size() > 0) {
                            result.setData(sessionList.get(0).getUuid());
                            result.setMessage("邀请成功");
                            result.setResponseCode(WebConstants.WebResponseCodeOk);
                        } else {
                            boolean addOK = SessionDBManager.addSession(sess);
                            if (addOK) {
                                SessionDBManager.addSessionUser(sess.getUuid(), userUUID);
                                SessionDBManager.addSessionUser(sess.getUuid(), friendUUID);
                                result.setData(sessionUUID);
                                result.setMessage("邀请成功");
                                result.setResponseCode(WebConstants.WebResponseCodeOk);
                            } else {
                                result.setMessage("服务器错误！");
                                result.setResponseCode(WebConstants.WebResponseCodeFail);
                            }
                        }
                    } else {
                        result.setMessage("不是好友无法邀请");
                        result.setResponseCode(WebConstants.WebResponseCodeFail);
                    }
                } else {
                    result.setResponseCode(WebConstants.WebResponseCodeInvalidRequest);
                }
            } else if (ActionInviteFriendToSession.equals(action)) {
                // 邀请好友加入群聊
                String uuid = request.getParameter(ParamUUID);
                String sessionUUID = request.getParameter(ParamSessionUUID);
                String sessionuuid = null;
                if (uuid != null && uuid != "") {
                    boolean addOK = false;
                    List<String> UUIDlists = Util.parseListString(uuid);
                    if (sessionUUID != null && sessionUUID != "") {
                        sessionuuid = sessionUUID;
                        for (String useruuid : UUIDlists) {
                            boolean findOK = SessionDBManager.findSessionUserBySessionAndUser(sessionUUID, useruuid);
                            if (!findOK) {
                                addOK = SessionDBManager.addSessionUser(sessionUUID, useruuid);
                            }
                        }
                    } else {
                        sessionuuid = Util.generateUUID();
                        Session sess = new Session();
                        sess.setUuid(sessionuuid);
                        sess.setSessionType(Constants.SESSION_TYPE_GROUP_CHAT);
                        sess.setInitUserUUID(userUUID);
                        addOK = SessionDBManager.addSession(sess);
                        for (String useruuid : UUIDlists) {
                            addOK = SessionDBManager.addSessionUser(sessionuuid, useruuid);
                        }
                    }
                    if (addOK) {
                        result.setData(sessionuuid);
                        result.setMessage("邀请成功");
                        result.setResponseCode(WebConstants.WebResponseCodeOk);
                    } else {
                        result.setMessage("服务器错误！");
                        result.setResponseCode(WebConstants.WebResponseCodeFail);
                    }
                }
            } else if (ActionQuerySessionUserlist.equals(action)) {
                // 查询会话用户列表
                String sessionUUID = request.getParameter(ParamSessionUUID);
                if (sessionUUID != null) {
                    List<SessionUser> sess = SessionDBManager.getSessionUserBySessUUID(sessionUUID);
                    result.setData(sess);
                    result.setResponseCode(WebConstants.WebResponseCodeOk);
                }
            } else if (ActionQuerySessionList.equals(action)) {
                // 查询我的会话列表
                List<Session> sess = SessionDBManager.getSessionByInit(userUUID);
                result.setData(sess);
                result.setResponseCode(WebConstants.WebResponseCodeOk);
            } else if (ActionKickUserOutSession.equals(action)) {
                // 踢出群聊用户
                String chatUserUUID = request.getParameter(ParamUserUUID);
                String sessionUUID = request.getParameter(ParamSessionUUID);
                if (chatUserUUID != null && sessionUUID != null) {
                    boolean delOK = SessionDBManager.delUserByUUID(chatUserUUID, sessionUUID);
                    if (delOK) {
                        result.setMessage("踢出成功！");
                        result.setResponseCode(WebConstants.WebResponseCodeOk);
                    } else {
                        result.setMessage("服务器错误！");
                        result.setResponseCode(WebConstants.WebResponseCodeFail);
                    }
                }
            } else if (ActionQuitSession.equals(action)) {
                // 退出群聊
                String sessionUUID = request.getParameter(ParamSessionUUID);
                if (sessionUUID != null) {
                    boolean delOK = SessionDBManager.delUserByUUID(userUUID, sessionUUID);
                    if (delOK) {
                        result.setMessage("成功！");
                        result.setResponseCode(WebConstants.WebResponseCodeOk);
                    } else {
                        result.setMessage("服务器错误！");
                        result.setResponseCode(WebConstants.WebResponseCodeFail);
                    }
                }
            } else if (ActionSendSessionMessage.equals(action)) {
                // 发送会话消息
                String sessionMessage = request.getParameter(ParamMessage);
                if (sessionMessage != null) {
                    SessionMessage message = new SessionMessage();
                    // sessionMessage = Util.URLDecode(sessionMessage);
                    message = Util.parseSessionMessage(sessionMessage);
                    message.setUUID(UUID.randomUUID().toString().replaceAll("-", ""));
                    message.setSenderUUID(userUUID);
                    message.setCreateTime(new Date());
                    Boolean addOK = SessionDBManager.addMessage(message);
                    SessionDBManager.updateSessionTime(message.getSessionUUID());
                    if (addOK) {
                        result.setMessage("发送成功！");
                        result.setResponseCode(WebConstants.WebResponseCodeOk);
                    } else {
                        result.setMessage("服务器错误！");
                        result.setResponseCode(WebConstants.WebResponseCodeFail);
                    }
                }
            } else if (ActionQuerySessionMessage.equals(action)) {
                // 获取会话消息
                String sessionUUID = request.getParameter(ParamSessionUUID);
                String startTimeStr = request.getParameter(ParamStarttime);
                String endTimeStr = request.getParameter(ParamEndTime);
                String offsetStr = request.getParameter(ParamOffset);
                String lenStr = request.getParameter(ParamLen);
                String messageUUID = request.getParameter(ParamMessageUUID);
                int offset = 0;
                int len = 0;
                Date startTime = null;
                Date endTime = null;
                try {
                    if (startTimeStr != null && !startTimeStr.equals("-1")) {
                        startTime = new Date(Long.parseLong(startTimeStr));
                    }
                } catch (Exception ex) {
                    startTime = null;
                }
                try {
                    if (endTimeStr != null && !endTimeStr.equals("-1")) {
                        endTime = new Date(Long.parseLong(endTimeStr));
                    }
                } catch (Exception ex) {
                    endTime = null;
                }
                if (offsetStr != null && offsetStr.length() > 0 && lenStr != null && lenStr.length() > 0) {
                    offset = Integer.valueOf(offsetStr);
                    len = Integer.valueOf(lenStr);
                }
                if (sessionUUID != null) {
                    if (request.getParameter(ParamOffset) == null) {
                        offset = 0;
                    }
                    if (request.getParameter(ParamLen) == null) {
                        len = 100;
                    }
                    List<SessionMessage> msglist = SessionDBManager.findmessage(sessionUUID, startTime, endTime,
                            offset, len, messageUUID);
                    int rows = SessionDBManager.getMessageNum(sessionUUID, startTime, endTime, messageUUID);
                    Pair<List<SessionMessage>, Boolean> pair = new Pair<List<SessionMessage>, Boolean>();
                    pair.setFirst(msglist);
                    if (rows > offset + msglist.size()) {
                        pair.setSecond(true);
                    } else {
                        pair.setSecond(false);
                    }
                    result.setData(pair);
                    result.setResponseCode(WebConstants.WebResponseCodeOk);
                }
            } else if (ActionQueryPsession.equals(action)) {
                // 查询我的结伴出行
                PSession ps = SessionDBManager.getPSessionByUserUUID(userUUID);
                if (ps.getUUID() != null && ps.getUUID().length() > 0) {
                    result.setData(ps);
                }
                result.setResponseCode(WebConstants.WebResponseCodeOk);
            } else if (ActionInviteUserToPression.equals(action)) {
                // 邀请用户加入结伴出行
                String psessionUUID = request.getParameter(ParamPsessionUUID);
                List<String> friendUUIDList = Util.parseListString(request.getParameter(ParamUUIDList));
                boolean addOK = false;
                if (psessionUUID == null || psessionUUID.length() < 1) {
                    String sessionUUID = Util.generateUUID();
                    psessionUUID = Util.generateUUID();
                    Session sess = new Session();
                    sess.setInitUserUUID(userUUID);
                    sess.setSessionType(Constants.SESSION_TYPE_GO_CHAT);
                    sess.setUuid(sessionUUID);
                    addOK = SessionDBManager.addSession(sess);
                    PSession psession = new PSession();
                    psession.setUUID(psessionUUID);
                    psession.setSessionUUID(sessionUUID);
                    psession.setLeaderUUID(userUUID);
                    psession.setCreateTime(new Date());
                    psession.setFlag(Constants.PSESSION_START);
                    addOK = SessionDBManager.addPSession(psession);
                    SessionDBManager.addSessionUser(sessionUUID, userUUID);
                    addOK = SessionDBManager.addSessionInvite(friendUUIDList, userUUID, sessionUUID);
                    if (addOK) {
                        result.setData(psessionUUID);
                        result.setMessage("邀请成功！");
                        result.setResponseCode(WebConstants.WebResponseCodeOk);
                    } else {
                        result.setMessage("邀请失败！");
                        result.setResponseCode(WebConstants.WebResponseCodeFail);
                    }
                } else {
                    String sessionUUID = SessionDBManager.getPSessionSessionUUIDByUUID(psessionUUID);
                    if (sessionUUID != null && !"".equals(sessionUUID)) {
                        addOK = SessionDBManager.addSessionInvite(friendUUIDList, userUUID, sessionUUID);
                        if (addOK) {
                            result.setData(psessionUUID);
                            result.setMessage("邀请成功！");
                            result.setResponseCode(WebConstants.WebResponseCodeOk);
                        } else {
                            result.setMessage("邀请失败！");
                            result.setResponseCode(WebConstants.WebResponseCodeFail);
                        }
                    } else {
                        result.setMessage("psessionUUID不存在！");
                        result.setResponseCode(WebConstants.WebResponseCodeFail);
                    }
                }
            } else if (ActionQueryMeInviteToPession.equals(action)) {
                // 查看我是否被邀请加入结伴出行
                List<PSession> PSession = SessionDBManager.findUserInvite(userUUID);
                if (PSession.size() > 0) {
                    result.setData(PSession);
                    result.setResponseCode(WebConstants.WebResponseCodeOk);
                } else {
                    result.setMessage("没有被邀请");
                    result.setResponseCode(WebConstants.WebResponseCodeFail);
                }
            } else if (ActionReponseToPsessionInvite.equals(action)) {
                // 响应结伴出行请求（接受或拒绝）
                int flag = WebUtil.parseInt(request.getParameter(ParamFlag), -1);
                String sessionUUID = request.getParameter(ParamSessionUUID);
                boolean res = false;
                if (flag >= 0 && sessionUUID != null && sessionUUID.length() > 0) {
                    res = SessionDBManager.updateSessionInvite(sessionUUID, flag, userUUID);
                    if (flag == 1) {
                        res = SessionDBManager.addSessionUser(sessionUUID, userUUID);
                    }
                    if (res) {
                        result.setResponseCode(WebConstants.WebResponseCodeOk);
                    } else {
                        result.setResponseCode(WebConstants.WebResponseCodeFail);
                    }
                } else {
                    result.setResponseCode(WebConstants.WebResponseCodeInvalidRequest);
                }
            } else if (ActionCongifPsessionLeader.equals(action)) {
                // 设置结伴出行领队
                String psessionUUID = request.getParameter(ParamPsessionUUID);
                String leaderUUID = request.getParameter(ParamUserUUID);
                boolean updateOK = SessionDBManager.updatePSession(psessionUUID, leaderUUID);
                if (updateOK) {
                    result.setMessage("更新成功！");
                    result.setResponseCode(WebConstants.WebResponseCodeOk);
                } else {
                    result.setMessage("服务器错误!");
                    result.setResponseCode(WebConstants.WebResponseCodeFail);
                }
            } else if (ActionUpdateSessionInfo.equals(action)) {
                // 设置会话名称（PSession的名称也通过Session的名称来变更
                String sessionUUID = request.getParameter(ParamSessionUUID);
                String name = request.getParameter(ParamName);
                if (sessionUUID != null && name != null) {
                    boolean upOK = SessionDBManager.updateSessionName(sessionUUID, name);
                    if (upOK) {
                        result.setMessage("更新成功！");
                        result.setResponseCode(WebConstants.WebResponseCodeOk);
                    } else {
                        result.setMessage("服务器错误!");
                        result.setResponseCode(WebConstants.WebResponseCodeFail);
                    }
                }
            } else if (ActionQuitPsession.equals(action)) {
                // 退出结伴出行（当前用户）
                String psessionUUID = request.getParameter(ParamPsessionUUID);
                String sessionUUID = SessionDBManager.findSessionUUidByPSession(psessionUUID);
                boolean delOK = SessionDBManager.delUserByUUID(userUUID, sessionUUID);
                if (delOK) {
                    result.setMessage("退出成功！");
                    result.setResponseCode(WebConstants.WebResponseCodeOk);
                } else {
                    result.setMessage("服务器错误！");
                    result.setResponseCode(WebConstants.WebResponseCodeFail);
                }
            } else if (ActionQuitClosePsession.equals(action)) {
                // 结束结伴出行
                String psessionUUID = request.getParameter(ParamPsessionUUID);
                String sessionUUID = SessionDBManager.findSessionUUidByPSession(psessionUUID);
                if (sessionUUID == null) {
                    result.setMessage("psessionUUID不存在！");
                    result.setResponseCode(WebConstants.WebResponseCodeFail);
                } else {
                    boolean updateOK = SessionDBManager.updateSessionType(sessionUUID, Constants.SESSION_TYPE_GROUP_CHAT);
                    updateOK = SessionDBManager.updatePSessionFlag(psessionUUID, Constants.PSESSION_END);
                    if (updateOK) {
                        result.setMessage("成功！");
                        result.setResponseCode(WebConstants.WebResponseCodeOk);
                    } else {
                        result.setMessage("服务器错误！");
                        result.setResponseCode(WebConstants.WebResponseCodeFail);
                    }
                }
            }
        } else if (ActionUploadMyLocation.equals(action)) {
            String trackPoint = request.getParameter(ParamTrackPoint);
            String userUuid = request.getParameter(ParamUserUuid);
            TrackPoint TrackPoint = Util.parseTrackPoint(trackPoint);
            if (trackPoint != null) {
                boolean addOK = SessionDBManager.addTrackpoints(TrackPoint, userUuid);
                if (addOK) {
                    result.setResponseCode(WebConstants.WebResponseCodeOk);
                } else {
                    result.setResponseCode(WebConstants.WebResponseCodeFail);
                }
            } else {
                result.setResponseCode(WebConstants.WebResponseCodeInvalidRequest);
            }
        } else if (ActionPSessionUserLocations.equals(action)) {
            String PessionUuid = request.getParameter(ParamPessionUuid);
            String SessionUuid = request.getParameter(ParamSessionUuid);
            PSessionLocation pSessionLocation = SessionDBManager.getPSessionUserLocations(PessionUuid, SessionUuid);
            result.setData(pSessionLocation);
            result.setResponseCode(WebConstants.WebResponseCodeOk);
        } else if (ActionSendPSessionMessageNoSession.equals(action)) {
            String userUUID = request.getParameter(ParamUserUuid);
            String sessionMessage = request.getParameter(ParamMessage);
            if (userUUID != null && userUUID.length() > 0) {
                SessionMessage message = new SessionMessage();
                message = Util.parseSessionMessage(sessionMessage);
                if (message != null) {
                    PSession pSession = SessionDBManager.getPSessionByUserUUID(userUUID);
                    if (pSession.getUUID() != null) {
                        message.setUUID(UUID.randomUUID().toString().replaceAll("-", ""));
                        message.setSenderUUID(userUUID);
                        message.setCreateTime(new Date());
                        message.setSessionUUID(pSession.getSessionUUID());
                        Boolean addOK = SessionDBManager.addMessage(message);
                        SessionDBManager.updateSessionTime(message.getSessionUUID());
                        if (addOK) {
                            result.setData(Constants.SUBMIT_SMS_VERIFY_CODE_SUCCESS);
                            result.setMessage("发送成功！");
                            result.setResponseCode(WebConstants.WebResponseCodeOk);
                        } else {
                            result.setMessage("服务器错误！");
                            result.setResponseCode(WebConstants.WebResponseCodeFail);
                        }
                    } else {
                        result.setData(Constants.SUBMIT_SMS_VERIFY_CODE_FAIL);
                        result.setMessage("用户没有PSession！");
                        result.setResponseCode(WebConstants.WebResponseCodeOk);
                    }
                } else {
                    result.setResponseCode(WebConstants.WebResponseCodeInvalidRequest);
                }
            } else {
                result.setResponseCode(WebConstants.WebResponseCodeInvalidRequest);
            }
        } else if (ActionGetPSessionMessageNoSession.equals(action)) {
            String userUUID = request.getParameter(ParamUserUuid);
            String startTimeStr = request.getParameter(ParamStarttime);
            String endTimeStr = request.getParameter(ParamEndTime);
            String offsetStr = request.getParameter(ParamOffset);
            String lenStr = request.getParameter(ParamLen);
            String messageUUID = request.getParameter(ParamMessageUUID);
            PSession pSession = SessionDBManager.getPSessionByUserUUID(userUUID);
            if (pSession.getUUID() != null) {
                String sessionUUID = pSession.getSessionUUID();
                int offset = 0;
                int len = 0;
                Date startTime = null;
                Date endTime = null;
                try {
                    if (startTimeStr != null && !startTimeStr.equals("-1")) {
                        startTime = new Date(Long.parseLong(startTimeStr));
                    }
                } catch (Exception ex) {
                    startTime = null;
                }
                try {
                    if (endTimeStr != null && !endTimeStr.equals("-1")) {
                        endTime = new Date(Long.parseLong(endTimeStr));
                    }
                } catch (Exception ex) {
                    endTime = null;
                }
                if (offsetStr != null && offsetStr.length() > 0 && lenStr != null && lenStr.length() > 0) {
                    offset = Integer.valueOf(offsetStr);
                    len = Integer.valueOf(lenStr);
                }
                if (sessionUUID != null) {
                    if (request.getParameter(ParamOffset) == null) {
                        offset = 0;
                    }
                    if (request.getParameter(ParamLen) == null) {
                        len = 100;
                    }
                    List<SessionMessage> msglist = SessionDBManager.findmessage(sessionUUID, startTime, endTime,
                            offset, len, messageUUID);
                    int rows = SessionDBManager.getMessageNum(sessionUUID, startTime, endTime, messageUUID);
                    Pair<List<SessionMessage>, Boolean> pair = new Pair<List<SessionMessage>, Boolean>();
                    pair.setFirst(msglist);
                    if (rows > offset + msglist.size()) {
                        pair.setSecond(true);
                    } else {
                        pair.setSecond(false);
                    }
                    result.setData(pair);
                    result.setResponseCode(WebConstants.WebResponseCodeOk);
                }
            } else {
                result.setData(Constants.SUBMIT_SMS_VERIFY_CODE_FAIL);
                result.setMessage("用户没有PSession！");
                result.setResponseCode(WebConstants.WebResponseCodeOk);
            }
        } else {
            result.setMessage("请登录!");
            result.setResponseCode(WebConstants.WebResponseCodeFail);
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
