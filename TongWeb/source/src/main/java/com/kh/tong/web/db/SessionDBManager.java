package com.kh.tong.web.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.kh.db.ConnectionProvider;
import com.kh.db.DBUtil;
import com.kh.tong.term.bean.TrackPoint;
import com.kh.tong.web.bean.PSession;
import com.kh.tong.web.bean.PSessionLocation;
import com.kh.tong.web.bean.Session;
import com.kh.tong.web.bean.SessionMessage;
import com.kh.tong.web.bean.SessionUser;
import com.kh.tong.web.bean.UserTrackPoint;
import com.kh.tong.web.servlet.helper.Util;

public class SessionDBManager {
    static Logger log = Logger.getLogger(SessionDBManager.class);

    private static ConnectionProvider conf = null;

    protected SessionDBManager() {
    }

    public static void setConnectionProider(ConnectionProvider cp) {
        conf = cp;
    }

    public static Connection getConn() throws SQLException {
        if (conf == null) {
            conf = DBConfigure.getConfigure();
        }
        return conf.getDbConnection();
    }

    private static final String SqlcheckFriend = "SELECT count(*) FROM friend "
            + "WHERE user_uuid=? and friend_user_uuid=?";

    public static boolean checkFriend(String friendUUID, String useruuid) {
        boolean res = false;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(SqlcheckFriend);
            stmt.setString(1, useruuid);
            stmt.setString(2, friendUUID);
            rs = stmt.executeQuery();
            while (rs.next()) {
                if (rs.getInt(1) > 0) {
                    res = true;
                }
            }
        } catch (SQLException e) {
            log.error("Error execute sql.", e);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return res;
    }

    private static final String SqlAddSession = "INSERT INTO `session` ( uuid, `name`, session_type, init_user_uuid, create_time, msg_update_time ) "
            + "VALUE (?, ?, ?, ?, NOW(), NOW())";

    public static boolean addSession(Session session) {
        boolean res = false;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(SqlAddSession);
            stmt.setString(1, session.getUuid());
            stmt.setString(2, session.getName());
            stmt.setInt(3, session.getSessionType());
            stmt.setString(4, session.getInitUserUUID());
            int ret = stmt.executeUpdate();
            if (ret > 0) {
                res = true;
            }
        } catch (SQLException e) {
            log.error("Error execute sql.", e);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return res;
    }

    private static final String SqlAddSessionUser = "INSERT INTO session_user( uuid, session_uuid, user_uuid, create_time )"
            + " VALUE ( REPLACE (UUID(), '-', ''), ?, ?, NOW())";

    public static boolean addSessionUser(String sessionUUID, String useruuid) {
        boolean res = false;
        Connection conn = null;
        PreparedStatement stmt = null;
        int rs = 0;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(SqlAddSessionUser);
            stmt.setString(1, sessionUUID);
            stmt.setString(2, useruuid);
            rs = stmt.executeUpdate();
            if (rs > 0) {
                res = true;
            }
        } catch (SQLException e) {
            log.error("Error execute sql.", e);
        }
        DBUtil.safeClose(conn, stmt);
        return res;
    }

    private static final String SqlGetSessionByUserUUID = "SELECT * FROM `session` "
            + "WHERE uuid=(SELECT session_uuid FROM session_user WHERE user_uuid=?)";

    public static List<Session> getSessionByUserUUID(String useruuid) {
        List<Session> res = new ArrayList<Session>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(SqlGetSessionByUserUUID);
            stmt.setString(1, useruuid);
            rs = stmt.executeQuery();
            while (rs.next()) {
                Session session = new Session();
                session.setId(rs.getLong(1));
                session.setUuid(rs.getString(2));
                session.setName(rs.getString(3));
                session.setSessionType(rs.getInt(4));
                session.setInitUserUUID(rs.getString(5));
                session.setCreateTime(Util.timestampToDate(rs.getTimestamp(6)));
                session.setMsgUpdateTime(Util.timestampToDate(rs.getTimestamp(7)));
                res.add(session);
            }
        } catch (SQLException e) {
            log.error("Error execute sql.", e);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return res;
    }

    private static final String SqlGetSessionByInit = "SELECT * FROM `session` "
            + "WHERE uuid in (SELECT session_uuid FROM session_user WHERE user_uuid=?)";

    public static List<Session> getSessionByInit(String useruuid) {
        List<Session> res = new ArrayList<Session>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(SqlGetSessionByInit);
            stmt.setString(1, useruuid);
            rs = stmt.executeQuery();
            while (rs.next()) {
                Session session = new Session();
                session.setId(rs.getLong(1));
                session.setUuid(rs.getString(2));
                session.setName(rs.getString(3));
                session.setSessionType(rs.getInt(4));
                session.setInitUserUUID(rs.getString(5));
                session.setUsers(getSessionUserBySessUUID(rs.getString(2)));
                res.add(session);
            }
        } catch (SQLException e) {
            log.error("Error execute sql.", e);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return res;
    }

    private static final String SqlGetSessionUserBySessUUID = "SELECT * FROM session_user " + "WHERE session_uuid=?";

    public static List<SessionUser> getSessionUserBySessUUID(String sessionUUID) {
        List<SessionUser> res = new ArrayList<SessionUser>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(SqlGetSessionUserBySessUUID);
            stmt.setString(1, sessionUUID);
            rs = stmt.executeQuery();
            while (rs.next()) {
                SessionUser session = new SessionUser();
                session.setId(rs.getLong(1));
                session.setUUID(rs.getString(2));
                session.setSessionUUID(rs.getString(3));
                session.setUserUUID(rs.getString(4));
                session.setRemarkName(rs.getString(5));
                session.setCreateTime(Util.timestampToDate(rs.getTimestamp(5)));
                session.setUserInfo(UserDBManger.getUserInfoByUUID(rs.getString(4)));
                res.add(session);
            }
        } catch (SQLException e) {
            log.error("Error execute sql.", e);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return res;
    }

    private static final String SqlDelUserByUUID = "DELETE FROM session_user where " + "session_uuid=? and user_uuid=?";

    public static boolean delUserByUUID(String chatUserUUID, String sessionUUID) {
        boolean result = true;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(SqlDelUserByUUID);
            stmt.setString(1, sessionUUID);
            stmt.setString(2, chatUserUUID);
            int i = stmt.executeUpdate();
            if (i > 0) {
                result = true;
            } else {
                result = false;
            }
        } catch (SQLException e) {
            result = false;
            log.error("Error execute sql.", e);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return result;
    }

    private static final String SqlAddMessage = "insert into session_message "
            + "(uuid,session_uuid,type,content,sender_uuid,create_time,from_device_type,from_device_id)value(?,?,?,?,?,?,?,?)";

    public static Boolean addMessage(SessionMessage message) {
        boolean res = false;
        Connection conn = null;
        PreparedStatement stmt = null;
        int rs = 0;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(SqlAddMessage);
            stmt.setString(1, message.getUUID());
            stmt.setString(2, message.getSessionUUID());
            stmt.setInt(3, message.getType());
            stmt.setString(4, message.getContent());
            stmt.setString(5, message.getSenderUUID());
            stmt.setTimestamp(6, Util.dateToTimestamp(message.getCreateTime()));
            stmt.setInt(7, message.getFromDeviceType());
            if (message.getFromDeviceId() != null) {
                stmt.setString(8, message.getFromDeviceId());
            } else {
                stmt.setString(8, "");
            }
            rs = stmt.executeUpdate();
            if (rs > 0) {
                res = true;
            }
        } catch (SQLException e) {
            log.error("Error execute sql.", e);
        }
        DBUtil.safeClose(conn, stmt);
        return res;
    }

    private static final String sqlFindMessage = "SELECT * FROM session_message WHERE session_uuid=?";
    private static final String sqlFindMessage1 = " and create_time > ? and create_time < ?";
    private static final String sqlFindMessage2 = " limit ?,?";
    private static final String sqlFindMessage3 = "SELECT * FROM session_message WHERE id > (SELECT id FROM session_message WHERE uuid=?) and session_uuid=?";

    public static List<SessionMessage> findmessage(String sessionUUID, Date startTime, Date endTime, int offset,
            int len, String messageUUID) {
        List<SessionMessage> res = new ArrayList<SessionMessage>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            String sql = null;
            if (messageUUID == null || messageUUID.length() < 1) {
                sql = sqlFindMessage;
                if (startTime != null) {
                    sql += sqlFindMessage1;
                }
                sql += sqlFindMessage2;
            } else {
                sql = sqlFindMessage3;
                sql += sqlFindMessage2;
            }
            stmt = conn.prepareStatement(sql);
            if (messageUUID == null || messageUUID.length() < 1) {
                stmt.setString(1, sessionUUID);
                if (startTime != null) {
                    if (endTime == null) {
                        endTime = new Date();
                    }
                    Format format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String startTimeStr = format.format(startTime);
                    String endTimeStr = format.format(endTime);
                    stmt.setString(2, startTimeStr);
                    stmt.setString(3, endTimeStr);
                    stmt.setInt(4, offset);
                    stmt.setInt(5, offset + len);
                } else {
                    stmt.setInt(2, offset);
                    stmt.setInt(3, offset + len);
                }
            } else {
                stmt.setString(1, messageUUID);
                stmt.setString(2, sessionUUID);
                stmt.setInt(3, offset);
                stmt.setInt(4, offset + len);
            }
            rs = stmt.executeQuery();
            while (rs.next()) {
                SessionMessage message = new SessionMessage();
                message.setId(rs.getLong(1));
                message.setUUID(rs.getString(2));
                message.setSessionUUID(rs.getString(3));
                message.setType(rs.getInt(4));
                message.setContent(rs.getString(5));
                message.setSenderUUID(rs.getString(6));
                message.setCreateTime(Util.timestampToDate(rs.getTimestamp(7)));
                message.setFromDeviceType(rs.getInt(8));
                message.setFromDeviceId(rs.getString(9));
                message.setUserInfo(UserDBManger.getUserInfoByUUID(rs.getString(6)));
                res.add(message);
            }
        } catch (SQLException e) {
            log.error("Error execute sql.", e);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return res;

    }

    private static final String sqlFindMessageNum = "SELECT count(*) FROM session_message WHERE session_uuid=?";
    private static final String sqlFindMessageNum1 = " and create_time > ? and create_time < ? ";
    private static final String sqlFindMessageNum2 = "SELECT count(*) FROM session_message WHERE id > (SELECT id FROM session_message WHERE uuid=?) and session_uuid=?";

    public static int getMessageNum(String sessionUUID, Date startTime, Date endTime, String messageUUID) {
        int res = 0;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = null;
        try {
            conn = getConn();
            if (messageUUID == null) {
                sql = sqlFindMessageNum;
                if (startTime != null) {
                    sql += sqlFindMessageNum1;
                }
            } else {
                sql = sqlFindMessageNum2;
            }
            stmt = conn.prepareStatement(sql);
            if (messageUUID == null) {
                stmt.setString(1, sessionUUID);
                if (startTime != null) {
                    if (endTime == null) {
                        endTime = new Date();
                    }
                    Format format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String startTimeStr = format.format(startTime);
                    String endTimeStr = format.format(endTime);
                    stmt.setString(2, startTimeStr);
                    stmt.setString(3, endTimeStr);
                }
            } else {
                stmt.setString(1, messageUUID);
                stmt.setString(2, sessionUUID);
            }
            rs = stmt.executeQuery();
            while (rs.next()) {
                res = rs.getInt(1);
            }
        } catch (SQLException e) {
            log.error("Error execute sql.", e);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return res;
    }

    private static final String sqlFindPSessionByUserUUID = "SELECT * FROM psession "
            + "WHERE session_uuid in(SELECT uuid FROM `session` WHERE session_type=4 "
            + "AND uuid in(SELECT session_uuid FROM session_user WHERE user_uuid=?))";

    public static PSession getPSessionByUserUUID(String userUUID) {
        PSession res = new PSession();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(sqlFindPSessionByUserUUID);
            stmt.setString(1, userUUID);
            rs = stmt.executeQuery();
            while (rs.next()) {
                res.setId(rs.getLong(1));
                res.setUUID(rs.getString(2));
                res.setSessionUUID(rs.getString(3));
                res.setLeaderUUID(rs.getString(4));
                res.setCreateTime(Util.timestampToDate(rs.getTimestamp(5)));
                res.setFlag(rs.getInt(6));
                res.setCloseTime(Util.timestampToDate(rs.getTimestamp(7)));
                res.setUsers(getSessionUserBySessUUID(rs.getString(3)));
                res.setSession(findSessionByUUID(rs.getString(3)));
            }
        } catch (SQLException e) {
            log.error("Error execute sql.", e);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return res;
    }

    private static final String sqlFindSessionByUUID = "SELECT * FROM `session` " + "WHERE uuid=? ";

    public static Session findSessionByUUID(String userUUID) {
        Session res = new Session();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(sqlFindSessionByUUID);
            stmt.setString(1, userUUID);
            rs = stmt.executeQuery();
            while (rs.next()) {
                res.setId(rs.getLong(1));
                res.setId(rs.getLong(1));
                res.setUuid(rs.getString(2));
                res.setName(rs.getString(3));
                res.setSessionType(rs.getInt(4));
                res.setInitUserUUID(rs.getString(5));
                res.setCreateTime(Util.timestampToDate(rs.getTimestamp(6)));
                res.setMsgUpdateTime(Util.timestampToDate(rs.getTimestamp(7)));
            }
        } catch (SQLException e) {
            log.error("Error execute sql.", e);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return res;
    }

    private static final String sqlFindPSessionByOK = "SELECT count(*) FROM psession WHERE uuid=?";

    public static boolean findPSessionByOK(String psessionUUID) {
        boolean res = false;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(sqlFindPSessionByOK);
            stmt.setString(1, psessionUUID);
            rs = stmt.executeQuery();
            while (rs.next()) {
                if (rs.getInt(1) > 0) {
                    res = true;
                }
            }
        } catch (SQLException e) {
            log.error("Error execute sql.", e);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return res;
    }

    private static final String sqlAddSessionInvite = "INSERT INTO session_invite ( uuid, session_uuid, request_user_uuid, invited_user_uuid, agree, reponse_time ) "
            + "VALUE ( REPLACE (UUID(), '-', '') ,?,?,?,?, NOW())";

    public static boolean addSessionInvite(List<String> friendUUIDList, String userUUID, String sessUUID) {
        boolean result = false;
        Connection conn = null;
        PreparedStatement stmt = null;
        int inviteFlag = 0;
        try {
            conn = getConn();
            conn.setAutoCommit(false);
            stmt = conn.prepareStatement(sqlAddSessionInvite);
            for (String friendUUID : friendUUIDList) {
                stmt.setString(1, sessUUID);
                stmt.setString(2, userUUID);
                stmt.setString(3, friendUUID);
                stmt.setInt(4, inviteFlag);
                stmt.addBatch();
            }
            stmt.executeBatch();
            conn.commit();
            result = true;
        } catch (SQLException e) {
            log.error("Error execute sql.", e);
        }
        DBUtil.safeClose(conn, stmt);
        return result;
    }

    private static final String sqlFindUserInvite = "SELECT * FROM psession WHERE session_uuid IN(SELECT session_uuid FROM session_invite WHERE invited_user_uuid=? and agree=0)";

    public static List<PSession> findUserInvite(String userUUID) {
        List<PSession> res = new ArrayList<PSession>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(sqlFindUserInvite);
            stmt.setString(1, userUUID);
            rs = stmt.executeQuery();
            while (rs.next()) {
                PSession ps = new PSession();
                ps.setId(rs.getLong(1));
                ps.setUUID(rs.getString(2));
                ps.setSessionUUID(rs.getString(3));
                ps.setLeaderUUID(rs.getString(4));
                ps.setCreateTime(Util.timestampToDate(rs.getTimestamp(5)));
                ps.setFlag(rs.getInt(6));
                ps.setCloseTime(Util.timestampToDate(rs.getTimestamp(7)));
                ps.setUsers(getSessionUserBySessUUID(rs.getString(3)));
                res.add(ps);
            }
        } catch (SQLException e) {
            log.error("Error execute sql.", e);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return res;
    }

    private static final String SqlUpdateSessionInvite = "update session_invite set"
            + " agree = ? where session_uuid = ? and invited_user_uuid = ?";

    public static boolean updateSessionInvite(String sessionUUID, int flag, String userUUID) {
        boolean result = false;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(SqlUpdateSessionInvite);
            stmt.setInt(1, flag);
            stmt.setString(2, sessionUUID);
            stmt.setString(3, userUUID);
            int ret = stmt.executeUpdate();
            if (ret == 1) {
                result = true;
            } else {
                log.error("Unable to updata");
            }
        } catch (SQLException e) {
            log.error("Error execute sql.", e);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return result;

    }

    private static final String SqlUpdatePSession = "update psession set" + " leader_uuid = ? where uuid = ?";

    public static boolean updatePSession(String psessionUUID, String leaderUUID) {
        boolean result = false;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(SqlUpdatePSession);
            stmt.setString(1, leaderUUID);
            stmt.setString(2, psessionUUID);
            int ret = stmt.executeUpdate();
            if (ret == 1) {
                result = true;
            } else {
                log.error("Unable to updata");
            }
        } catch (SQLException e) {
            log.error("Error execute sql.", e);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return result;
    }

    private static final String SqlUpdateSessionName = "update `session` set" + " name = ? where uuid = ?";

    public static boolean updateSessionName(String sessionUUID, String name) {
        boolean result = false;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(SqlUpdateSessionName);
            stmt.setString(1, name);
            stmt.setString(2, sessionUUID);
            int ret = stmt.executeUpdate();
            if (ret == 1) {
                result = true;
            } else {
                log.error("Unable to updata");
            }
        } catch (SQLException e) {
            log.error("Error execute sql.", e);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return result;
    }

    private static final String sqlFindSessionUUid = "SELECT session_uuid FROM psession" + " WHERE uuid=?";

    public static String findSessionUUidByPSession(String psessionUUID) {
        String res = null;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(sqlFindSessionUUid);
            stmt.setString(1, psessionUUID);
            rs = stmt.executeQuery();
            while (rs.next()) {
                res = rs.getString(1);
            }
        } catch (SQLException e) {
            log.error("Error execute sql.", e);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return res;
    }

    private static final String SqlUpdateSessionType = "update `session` set" + " session_type = ? where uuid = ?";

    public static boolean updateSessionType(String sessionUUID, int type) {
        boolean result = false;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(SqlUpdateSessionType);
            stmt.setInt(1, type);
            stmt.setString(2, sessionUUID);
            int ret = stmt.executeUpdate();
            if (ret == 1) {
                result = true;
            } else {
                log.error("Unable to updata");
            }
        } catch (SQLException e) {
            log.error("Error execute sql.", e);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return result;
    }

    private static final String SqlUpdatePSessionFlag = "update psession set" + " flag = ? where uuid = ?";

    public static boolean updatePSessionFlag(String psessionUUID, int flag) {
        boolean result = false;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(SqlUpdatePSessionFlag);
            stmt.setInt(1, flag);
            stmt.setString(2, psessionUUID);
            int ret = stmt.executeUpdate();
            if (ret == 1) {
                result = true;
            } else {
                log.error("Unable to updata");
            }
        } catch (SQLException e) {
            log.error("Error execute sql.", e);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return result;
    }

    private static final String SqlGetSessionUser = "SELECT * FROM `session` WHERE uuid in(SELECT session_uuid "
            + "FROM session_user WHERE user_uuid = ? and session_uuid in(SELECT session_uuid FROM session_user WHERE user_uuid ="
            + "?)) and session_type= ?";

    public static List<Session> getSessionUser(String userUUID, String friendUUID, int sessionType) {
        List<Session> res = new ArrayList<Session>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(SqlGetSessionUser);
            stmt.setString(1, userUUID);
            stmt.setString(2, friendUUID);
            stmt.setInt(3, sessionType);
            rs = stmt.executeQuery();
            while (rs.next()) {
                Session session = new Session();
                session.setId(rs.getLong(1));
                session.setUuid(rs.getString(2));
                session.setName(rs.getString(3));
                session.setSessionType(rs.getInt(4));
                session.setInitUserUUID(rs.getString(5));
                session.setCreateTime(Util.timestampToDate(rs.getTimestamp(6)));
                session.setMsgUpdateTime(Util.timestampToDate(rs.getTimestamp(7)));
                res.add(session);
            }
        } catch (SQLException e) {
            log.error("Error execute sql.", e);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return res;
    }

    private static final String SqlUpdateSessionTime = "update `session` set" + " msg_update_time = ? where uuid = ?";

    public static boolean updateSessionTime(String sessionUUID) {
        boolean result = false;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(SqlUpdateSessionTime);
            stmt.setTimestamp(1, new Timestamp(new Date().getTime()));
            stmt.setString(2, sessionUUID);
            int ret = stmt.executeUpdate();
            if (ret == 1) {
                result = true;
            } else {
                log.error("Unable to updata");
            }
        } catch (SQLException e) {
            log.error("Error execute sql.", e);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return result;

    }

    private static final String sqlFindSessionUserBySessionAndUser = "SELECT count(*) FROM session_user "
            + "WHERE session_uuid=? and user_uuid=?";

    public static boolean findSessionUserBySessionAndUser(String sessionUUID, String useruuid) {
        boolean res = false;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(sqlFindSessionUserBySessionAndUser);
            stmt.setString(1, sessionUUID);
            stmt.setString(2, useruuid);
            rs = stmt.executeQuery();
            while (rs.next()) {
                if (rs.getInt(1) > 0) {
                    res = true;
                }
            }
        } catch (SQLException e) {
            log.error("Error execute sql.", e);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return res;
    }

    private static final String sqlAddPSession = "insert into psession "
            + "(uuid,session_uuid,leader_uuid,create_time,flag)" + "value(?,?,?,?,?)";

    public static boolean addPSession(PSession psession) {
        boolean res = false;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(sqlAddPSession);
            stmt.setString(1, psession.getUUID());
            stmt.setString(2, psession.getSessionUUID());
            stmt.setString(3, psession.getLeaderUUID());
            stmt.setTimestamp(4, Util.dateToTimestamp(psession.getCreateTime()));
            stmt.setInt(5, psession.getFlag());
            int ret = stmt.executeUpdate();
            if (ret > 0) {
                res = true;
            }
        } catch (SQLException e) {
            log.error("Error execute sql.", e);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return res;
    }

    private static final String SqlGetPSessionSessionUUIDByUUID = "SELECT session_uuid FROM psession "
            + "WHERE uuid = ?";

    public static String getPSessionSessionUUIDByUUID(String psessionUUID) {
        String sessionUUID = null;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(SqlGetPSessionSessionUUIDByUUID);
            stmt.setString(1, psessionUUID);
            rs = stmt.executeQuery();
            while (rs.next()) {
                sessionUUID = rs.getString(1);
            }
        } catch (SQLException e) {
            log.error("Error execute sql.", e);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return sessionUUID;
    }

    private static final String SqlAddTrackPoint = "INSERT INTO user_trackpoints ( uuid, user_uuid, gps_time, "
            + "lon, lat, sys_time, speed, angle, altitude, accuary, type, create_time ) VALUES ( REPLACE (UUID(), '-', ''),"
            + " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW())";

    public static boolean addTrackpoints(TrackPoint trackPoint, String userUuid) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean result = false;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(SqlAddTrackPoint);
            stmt.setString(1, userUuid);
            stmt.setTimestamp(2, Util.dateToTimestamp(trackPoint.getGpsTime()));
            stmt.setDouble(3, trackPoint.getLon());
            stmt.setDouble(4, trackPoint.getLat());
            stmt.setTimestamp(5, Util.dateToTimestamp(trackPoint.getSysTime()));
            stmt.setFloat(6, trackPoint.getSpeed());
            stmt.setFloat(7, trackPoint.getAngle());
            stmt.setFloat(8, trackPoint.getAltitude());
            stmt.setFloat(9, trackPoint.getAccuracy());
            stmt.setInt(10, trackPoint.getType());
            int ret = stmt.executeUpdate();
            if (ret > 0) {
                result = true;
            }
        } catch (SQLException e) {
            log.error("Error execute sql.", e);
        }
        DBUtil.safeClose(conn, stmt);
        return result;
    }

    private static final String SqlFindPSessionUserLocations = "SELECT * FROM user_trackpoints WHERE user_uuid IN("
            + "SELECT user_uuid FROM session_user where session_uuid =?) order by id DESC limit 1";

    public static PSessionLocation getPSessionUserLocations(String pessionUuid, String sessionUuid) {
        PSessionLocation pSessionLocation = new PSessionLocation();
        pSessionLocation.setpSessionUUID(pessionUuid);
        pSessionLocation.setSessionUUID(sessionUuid);
        List<UserTrackPoint> userTrackPointsLs = new ArrayList<UserTrackPoint>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(SqlFindPSessionUserLocations);
            stmt.setString(1, sessionUuid);
            rs = stmt.executeQuery();
            while (rs.next()) {
                UserTrackPoint userTrackPoint = new UserTrackPoint();
                TrackPoint trackPoint = new TrackPoint();
                trackPoint.setId(rs.getLong(1));
                trackPoint.setGpsTime(Util.timestampToDate(rs.getTimestamp(4)));
                trackPoint.setLon(rs.getFloat(5));
                trackPoint.setLat(rs.getFloat(6));
                trackPoint.setSysTime(Util.timestampToDate(rs.getTimestamp(7)));
                trackPoint.setSpeed(rs.getFloat(8));
                trackPoint.setAngle(rs.getFloat(9));
                trackPoint.setAltitude(rs.getFloat(10));
                trackPoint.setAccuracy(rs.getFloat(11));
                trackPoint.setType(rs.getInt(12));
                userTrackPoint.setUserUUID(rs.getString(3));
                userTrackPoint.setTrackPoint(trackPoint);
                userTrackPointsLs.add(userTrackPoint);
            }
            pSessionLocation.setUserTrackPoints(userTrackPointsLs);
        } catch (SQLException e) {
            log.error("Error execute sql.", e);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return pSessionLocation;
    }
}
