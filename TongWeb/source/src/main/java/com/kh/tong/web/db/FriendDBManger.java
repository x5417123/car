package com.kh.tong.web.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.kh.db.ConnectionProvider;
import com.kh.db.DBUtil;
import com.kh.tong.web.bean.Friend;
import com.kh.tong.web.bean.FriendUserInfo;
import com.kh.tong.web.bean.UserInfo;

public class FriendDBManger {
    static Logger log = Logger.getLogger(FriendDBManger.class);

    private static ConnectionProvider conf = null;

    protected FriendDBManger() {
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

    private static final String sqlAddFriend = "INSERT INTO friend "
            + "(id,uuid,user_uuid,friend_user_uuid,flag,request_remark,remark_name)VALUES(?,?,?,?,?,?,?)";

    public static boolean addFriend(Friend friend) {
        boolean result = false;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(sqlAddFriend);
            stmt.setInt(1, friend.getId());
            stmt.setString(2, friend.getUuid());
            stmt.setString(3, friend.getUserUuid());
            stmt.setString(4, friend.getFriendUserUuid());
            stmt.setInt(5, friend.getFlag());
            stmt.setString(6, friend.getRequestRemark());
            stmt.setString(7, friend.getRemarkName());
            int ret = stmt.executeUpdate();
            if (ret == 1) {
                result = true;
            } else {
                log.error("Unable to add");
            }
        } catch (SQLException e) {
            log.error("Error execute sql.", e);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return result;
    }

    private static final String sqladdFriendAuthByFriendUUIDAndAuthsList = "INSERT INTO friend_auth "
            + "(friend_uuid,auth_id)VALUES((SELECT uuid FROM friend WHERE user_uuid =? and friend_user_uuid=?),?)";

    public static boolean addFriendAuthByFriendUUIDAndAuthsList(String friendUUID, int auth, String userUUID) {
        boolean result = false;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(sqladdFriendAuthByFriendUUIDAndAuthsList);
            stmt.setString(1, userUUID);
            stmt.setString(2, friendUUID);
            stmt.setInt(3, auth);
            int ret = stmt.executeUpdate();
            if (ret == 1) {
                result = true;
            } else {
                log.error("Unable to add");
            }
        } catch (SQLException e) {
            log.error("Error execute sql.", e);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return result;
    }

    private static final String sqlAddFriendTagByFriendUUIDAndTags = "INSERT INTO friend_tag"
            + "(friend_uuid,tag)VALUES((SELECT uuid FROM friend WHERE user_uuid =? and friend_user_uuid=?),?)";

    public static boolean addFriendTagByFriendUUIDAndTags(String friendUUID, String tags, String userUUID) {
        boolean result = false;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(sqlAddFriendTagByFriendUUIDAndTags);
            stmt.setString(1, userUUID);
            stmt.setString(2, friendUUID);
            stmt.setString(3, tags);
            int ret = stmt.executeUpdate();
            if (ret == 1) {
                result = true;
            } else {
                log.error("Unable to add");
            }
        } catch (SQLException e) {
            log.error("Error execute sql.", e);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return result;
    }

    private static final String sqlDelFriendAuthByFriendUUIDAndUserUUID = "delete from friend_auth "
            + "where friend_uuid = (SELECT uuid FROM friend WHERE user_uuid =? and friend_user_uuid=?)";

    public static boolean delFriendAuthByFriendUUIDAndUserUUID(String friendUUID, String userUUid) {
        boolean result = true;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(sqlDelFriendAuthByFriendUUIDAndUserUUID);
            stmt.setString(1, userUUid);
            stmt.setString(2, friendUUID);
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

    private static final String sqlDelFriendByFriendUUID = "delete from friend "
            + "where user_uuid = ? and friend_user_uuid =?";

    public static boolean delFriendByFriendUUID(String friendUUID, String userUUid) {
        boolean result = true;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(sqlDelFriendByFriendUUID);
            stmt.setString(1, userUUid);
            stmt.setString(2, friendUUID);
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

    private static final String sqlDelFriendTagByFriendUUIDAndTags = " delete from friend_tag "
            + "WHERE friend_uuid=(SELECT uuid FROM friend WHERE user_uuid =? and friend_user_uuid=?)";

    public static boolean delFriendTagByFriendUUIDAndTags(String friendUUID, String userUUID) {
        boolean result = false;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(sqlDelFriendTagByFriendUUIDAndTags);
            stmt.setString(1, userUUID);
            stmt.setString(2, friendUUID);
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

    private static final String sqlFindFriendflagByfriendUUIDAndUserUUID = "SELECT flag FROM friend "
            + "WHERE user_uuid=? and friend_user_uuid=?";

    public static int findFriendFlagByfriendUUIDAndUserUUID(String friendUUID, String userUUID) {
        int result = 0;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(sqlFindFriendflagByfriendUUIDAndUserUUID);
            stmt.setString(1, userUUID);
            stmt.setString(2, friendUUID);
            rs = stmt.executeQuery();
            while (rs.next()) {
                result = rs.getInt(1);
            }
        } catch (SQLException e) {
            log.error("Error execute sql.", e);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return result;
    }

    public static String sqlFindFriendByflagAndUserUUID = "SELECT * FROM friend " + "WHERE user_uuid=? and flag in (";

    public static List<FriendUserInfo> findFriendByflagAndUserUUID(List<String> flagList, String userUUID) {
        List<FriendUserInfo> friendList = new ArrayList<FriendUserInfo>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = sqlFindFriendByflagAndUserUUID;
        try {
            conn = getConn();
            for (int i = 0; i < flagList.size(); i++) {
                if (i != flagList.size() - 1) {
                    sql += flagList.get(i) + ",";
                } else {
                    sql += flagList.get(i) + ")";
                }
            }
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, userUUID);
            rs = stmt.executeQuery();
            while (rs.next()) {
                FriendUserInfo FriendUserInfo = new FriendUserInfo();
                Friend friend = new Friend();
                friend.setId(rs.getInt(1));
                friend.setUuid(rs.getString(2));
                friend.setUserUuid(rs.getString(3));
                friend.setFriendUserUuid(rs.getString(4));
                friend.setFlag(rs.getInt(5));
                friend.setRequestRemark(rs.getString(6));
                friend.setRemarkName(rs.getString(7));

                UserInfo user = UserDBManger.getUserInfoByUUID(rs.getString(4));
                FriendUserInfo.setFriend(friend);
                FriendUserInfo.setUserInfo(user);
                friendList.add(FriendUserInfo);
            }
        } catch (SQLException e) {
            log.error("Error execute sql.", e);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return friendList;
    }

    private static final String sqlFindFriendByUserUUID = "SELECT * FROM friend " + "WHERE user_uuid=?";

    public static List<FriendUserInfo> findFriendByUserUUID(String userUuid) {
        List<FriendUserInfo> friendList = new ArrayList<FriendUserInfo>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(sqlFindFriendByUserUUID);
            stmt.setString(1, userUuid);
            rs = stmt.executeQuery();
            while (rs.next()) {
                FriendUserInfo FriendUserInfo = new FriendUserInfo();
                Friend friend = new Friend();
                friend.setId(rs.getInt(1));
                friend.setUuid(rs.getString(2));
                friend.setUserUuid(rs.getString(3));
                friend.setFriendUserUuid(rs.getString(4));
                friend.setFlag(rs.getInt(5));
                friend.setRequestRemark(rs.getString(6));
                friend.setRemarkName(rs.getString(7));

                UserInfo user = UserDBManger.getUserInfoByUUID(rs.getString(4));

                FriendUserInfo.setFriend(friend);
                FriendUserInfo.setUserInfo(user);
                friendList.add(FriendUserInfo);

            }
        } catch (SQLException e) {
            log.error("Error execute sql.", e);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return friendList;
    }

    private static final String sqlFindFriendAuthByFriendUUIDAndUserUUID = " SELECT auth_id FROM friend_auth "
            + "WHERE friend_uuid=(SELECT uuid FROM friend WHERE user_uuid=? and friend_user_uuid=?); ";

    public static List<Integer> findFriendAuthByFriendUUIDAndUserUUID(String friendUUID, String userUUID) {
        List<Integer> authList = new ArrayList<Integer>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(sqlFindFriendAuthByFriendUUIDAndUserUUID);
            stmt.setString(1, userUUID);
            stmt.setString(2, friendUUID);
            rs = stmt.executeQuery();
            while (rs.next()) {
                authList.add(rs.getInt(1));
            }
        } catch (SQLException e) {
            log.error("Error execute sql.", e);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return authList;
    }

    private static final String sqlFindFriendTagByFriendUUID = " SELECT tag FROM friend_tag "
            + "WHERE friend_uuid=(SELECT uuid FROM friend WHERE user_uuid=? and friend_user_uuid=?); ";

    public static List<String> findFriendTagByFriendUUIDAndUserUUID(String friendUUID, String userUUID) {
        List<String> tagList = new ArrayList<String>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(sqlFindFriendTagByFriendUUID);
            stmt.setString(1, userUUID);
            stmt.setString(2, friendUUID);
            rs = stmt.executeQuery();
            while (rs.next()) {
                tagList.add(rs.getString(1));
            }
        } catch (SQLException e) {
            log.error("Error execute sql.", e);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return tagList;
    }

    private static final String sqlUpFriendNickNameByFriendUUIDAndUserUUID = " UPDATE friend SET  "
            + "remark_name=? WHERE user_uuid=? and friend_user_uuid=?";

    public static boolean upFriendNickNameByFriendUUIDAndUserUUID(String friendUUID, String remarkName, String userUUID) {
        boolean result = false;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(sqlUpFriendNickNameByFriendUUIDAndUserUUID);
            stmt.setString(1, remarkName);
            stmt.setString(2, userUUID);
            stmt.setString(3, friendUUID);
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

    private static final String sqlUpFriendFlagByFriendUUID = " UPDATE friend SET  "
            + "flag=? WHERE user_uuid=? and friend_user_uuid=?";

    public static boolean upFriendFlagByFriendUUID(String friendUUID, int flag, String userUUID) {
        boolean result = false;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(sqlUpFriendFlagByFriendUUID);
            stmt.setInt(1, flag);
            stmt.setString(2, userUUID);
            stmt.setString(3, friendUUID);
            log.info(stmt);
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

}
