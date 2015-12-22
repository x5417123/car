package com.kh.tong.web.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.kh.db.ConnectionProvider;
import com.kh.db.DBUtil;
import com.kh.tong.web.bean.TrackProgress;
import com.kh.tong.web.bean.UncaughtExceptionData;
import com.kh.tong.web.bean.User;
import com.kh.tong.web.servlet.helper.Util;

public class DBManager {
    static Logger log = Logger.getLogger(DBManager.class);

    private static ConnectionProvider conf = null;

    protected DBManager() {
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

    private static final String SqlGetUser = "SELECT uuid, phone, `name`, email FROM `user` "
            + "WHERE name = ? and password = ? ";

    public static User getUser(String userName, String password) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        User user = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(SqlGetUser);
            stmt.setString(1, userName);
            stmt.setString(2, password);
            rs = stmt.executeQuery();
            if (rs.next()) {
                user = new User();
                user.setUuid(rs.getString(1));
                user.setPhone(rs.getString(2));
                user.setName(rs.getString(3));
                user.setEmail(rs.getString(4));
            }
        } catch (SQLException ex) {
            log.error("Error execute sql.", ex);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return user;
    }

    private static final String SqlGetCollectRadioIdList = "SELECT radio_id FROM user_collect_radio WHERE user_uuid = ?";

    public static List<Long> getCollectRadioIdList(String userUUID) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Long> result = new ArrayList<Long>();
        try {
            conn = getConn();
            stmt = conn.prepareStatement(SqlGetCollectRadioIdList);
            stmt.setString(1, userUUID);
            rs = stmt.executeQuery();
            while (rs.next()) {
                result.add(rs.getLong(1));
            }
        } catch (SQLException ex) {
            log.error("Error execute sql.", ex);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return result;
    }

    private static final String SqlDelCollectRadioIdList = "DELETE FROM user_collect_radio WHERE user_uuid=? ";
    private static final String SqlInsertCollectRadioIdList = "INSERT INTO user_collect_radio (radio_id, user_uuid) VALUES (?, ?)";

    public static boolean updateCollectRadioList(List<Long> radioIdList, String userUUID) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean result = false;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(SqlDelCollectRadioIdList);
            stmt.setString(1, userUUID);
            stmt.executeUpdate();
            conn.setAutoCommit(false);
            stmt = conn.prepareStatement(SqlInsertCollectRadioIdList);
            for (Long radioId : radioIdList) {
                stmt.setLong(1, radioId);
                stmt.setString(2, userUUID);
                stmt.addBatch();
            }
            stmt.executeBatch();
            conn.commit();
            result = true;
        } catch (SQLException ex) {
            log.error("Error execute sql.", ex);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return result;
    }

    public static boolean insertCollectRadioList(List<Long> radioIdList, String userUUID) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean result = false;
        try {
            conn = getConn();
            conn.setAutoCommit(false);
            stmt = conn.prepareStatement(SqlInsertCollectRadioIdList);
            for (Long radioId : radioIdList) {
                stmt.setLong(1, radioId);
                stmt.setString(2, userUUID);
                stmt.addBatch();
            }
            stmt.executeBatch();
            conn.commit();
            result = true;
        } catch (SQLException ex) {
            log.error("Error execute sql.", ex);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return result;
    }

    private static final String SqlDeleteCollectRadioListWhere = " AND radio_id IN ( ";

    public static boolean deleteCollectRadioList(List<Long> radioIdList, String userUUID) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean result = false;
        String sql = SqlDelCollectRadioIdList + SqlDeleteCollectRadioListWhere + Util.listLongToString(radioIdList)
                + " )";
        try {
            conn = getConn();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, userUUID);
            stmt.executeUpdate();
            result = true;
        } catch (SQLException ex) {
            log.error("Error execute sql.", ex);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return result;
    }

    private static final String SqlInsertLastRadioId = "INSERT INTO user_last_play_radio(radio_id, user_uuid) VALUES (?, ?)";

    public static boolean insertLastRadioId(Long radioId, String currentUserUUID) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean result = false;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(SqlInsertLastRadioId);
            stmt.setLong(1, radioId);
            stmt.setString(2, currentUserUUID);
            int ret = stmt.executeUpdate();
            result = ret == 1;
        } catch (SQLException ex) {
            log.error("Error execute sql.", ex);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return result;
    }

    private static final String SqlLastRadioId = "SELECT radio_id FROM user_last_play_radio WHERE user_uuid = ? "
            + "ORDER BY id DESC LIMIT 0, 1";

    public static Long getLastRadioId(String userUUID) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Long result = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(SqlLastRadioId);
            stmt.setString(1, userUUID);
            rs = stmt.executeQuery();
            if (rs.next()) {
                result = rs.getLong(1);
            }
        } catch (SQLException ex) {
            log.error("Error execute sql.", ex);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return result;
    }

    private static final String SqlGetCollectAlbumIdList = "SELECT album_id FROM user_collect_album WHERE user_uuid = ?";

    public static List<Long> getCollectAlbumIdList(String userUUID) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Long> result = new ArrayList<Long>();
        try {
            conn = getConn();
            stmt = conn.prepareStatement(SqlGetCollectAlbumIdList);
            stmt.setString(1, userUUID);
            rs = stmt.executeQuery();
            while (rs.next()) {
                result.add(rs.getLong(1));
            }
        } catch (SQLException ex) {
            log.error("Error execute sql.", ex);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return result;
    }

    private static final String SqlDelCollectAlbumIdList = "DELETE FROM user_collect_album WHERE user_uuid=? ";
    private static final String SqlInsertCollectAlbumIdList = "INSERT INTO user_collect_album (album_Id, user_uuid) VALUES (?, ?) ";

    public static boolean updateCollectAlbumList(List<Long> albumIdList, String userUUID) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean result = false;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(SqlDelCollectAlbumIdList);
            stmt.setString(1, userUUID);
            stmt.executeUpdate();
            conn.setAutoCommit(false);
            stmt = conn.prepareStatement(SqlInsertCollectAlbumIdList);
            for (Long radioId : albumIdList) {
                stmt.setLong(1, radioId);
                stmt.setString(2, userUUID);
                stmt.addBatch();
            }
            stmt.executeBatch();
            conn.commit();
            result = true;
        } catch (SQLException ex) {
            log.error("Error execute sql.", ex);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return result;
    }

    public static boolean addCollectAlbumList(List<Long> albumIdList, String userUUID) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean result = false;
        try {
            conn = getConn();
            conn.setAutoCommit(false);
            stmt = conn.prepareStatement(SqlInsertCollectAlbumIdList);
            for (Long radioId : albumIdList) {
                stmt.setLong(1, radioId);
                stmt.setString(2, userUUID);
                stmt.addBatch();
            }
            stmt.executeBatch();
            conn.commit();
            result = true;
        } catch (SQLException ex) {
            log.error("Error execute sql.", ex);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return result;
    }

    private static final String SqlDelCollectAlbumIdListWhere = " AND album_id IN ( ";

    public static boolean deleteCollectAlbumList(List<Long> albumIdList, String userUUID) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean result = false;
        String sql = SqlDelCollectAlbumIdList + SqlDelCollectAlbumIdListWhere + Util.listLongToString(albumIdList)
                + " )";
        try {
            conn = getConn();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, userUUID);
            stmt.executeUpdate();
            result = true;
        } catch (SQLException ex) {
            log.error("Error execute sql.", ex);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return result;
    }

    private static final String SqlInsertLastTrackProgress = "INSERT INTO user_last_play_track_progress ( last_played_track_id, album_id, progress, user_uuid ) VALUES (?, ?, ?, ?)";

    public static boolean insertLastTrackProgress(TrackProgress progress, String currentUserUUID) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean result = false;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(SqlInsertLastTrackProgress);
            stmt.setLong(1, progress.getTrackId());
            stmt.setLong(2, progress.getAlbumId());
            stmt.setLong(3, progress.getProgress());
            stmt.setString(4, currentUserUUID);
            int ret = stmt.executeUpdate();
            result = ret == 1;
        } catch (SQLException ex) {
            log.error("Error execute sql.", ex);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return result;
    }

    private static final String SqlUpdateCollectAlbumListTrackProgress = "UPDATE user_collect_album SET last_play_track_id = ?, last_play_track_progress = ? WHERE album_id = ? AND user_uuid = ?";

    public static boolean updateCollectAlbumListTrackProgress(TrackProgress progress, String currentUserUUID) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean result = false;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(SqlUpdateCollectAlbumListTrackProgress);
            stmt.setLong(1, progress.getTrackId());
            stmt.setLong(2, progress.getProgress());
            stmt.setLong(3, progress.getAlbumId());
            stmt.setString(4, currentUserUUID);
            int ret = stmt.executeUpdate();
            result = ret == 1;
        } catch (SQLException ex) {
            log.error("Error execute sql.", ex);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return result;
    }

    private static final String SqlGetLastTrackProgress = "SELECT last_played_track_id, album_id, progress FROM user_last_play_track_progress "
            + "WHERE user_uuid = ? ORDER BY id DESC LIMIT 0, 1";

    public static TrackProgress getLastTrackProgress(String userUUID) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        TrackProgress result = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(SqlGetLastTrackProgress);
            stmt.setString(1, userUUID);
            rs = stmt.executeQuery();
            if (rs.next()) {
                result = new TrackProgress();
                result.setTrackId(rs.getLong(1));
                result.setAlbumId(rs.getLong(2));
                result.setProgress(rs.getLong(3));
            }
        } catch (SQLException ex) {
            log.error("Error execute sql.", ex);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return result;
    }

    private static final String SqlGetCollectTrackProgress = "SELECT album_id, last_play_track_id, last_play_track_progress "
            + "FROM user_collect_album WHERE user_uuid = ?";

    public static List<TrackProgress> getCollectTrackProgress(String userUUID) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<TrackProgress> result = new ArrayList<TrackProgress>();
        try {
            conn = getConn();
            stmt = conn.prepareStatement(SqlGetCollectTrackProgress);
            stmt.setString(1, userUUID);
            rs = stmt.executeQuery();
            while (rs.next()) {
                TrackProgress progress = new TrackProgress();
                progress.setAlbumId(rs.getLong(1));
                progress.setTrackId(rs.getLong(2));
                progress.setProgress(rs.getLong(3));
                result.add(progress);
            }
        } catch (SQLException ex) {
            log.error("Error execute sql.", ex);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return result;
    }

    private static final String SqlInsertUncaughtException = "INSERT INTO prog_exception ( device_id, `language`, version, system_version, type, "
            + "time, `data` ) VALUES (?, ?, ?, ?, ?, ?, ?)";

    public static boolean insertUncaughtExceptionData(UncaughtExceptionData exception) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean result = false;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(SqlInsertUncaughtException);
            stmt.setString(1, exception.getDeviceId());
            stmt.setString(2, exception.getLanguage());
            stmt.setString(3, exception.getVersion());
            stmt.setString(4, exception.getSystemVersion());
            stmt.setInt(5, exception.getType());
            stmt.setTimestamp(6, Util.dateToTimestamp(exception.getTime()));
            stmt.setString(7, exception.getData());
            int ret = stmt.executeUpdate();
            result = ret == 1;
        } catch (SQLException ex) {
            log.error("Error execute sql.", ex);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return result;
    }

    private static final String SqlGetUncaughtException = "SELECT device_id, `language`, version, system_version, "
            + "type, time, `data` FROM prog_exception ";
    private static final String SqlGetUncaughtExceptionWhere1 = " WHERE time < ? ";
    private static final String SqlGetUncaughtExceptionWhere2 = " AND time > ? ";
    private static final String SqlGetUncaughtExceptionWhere3 = " AND device_id IN ( ";
    private static final String SqlGetUncaughtExceptionOrder = " ORDER BY time DESC ";
    private static final String SqlGetUncaughtExceptionLimit = " LIMIT ?, ? ";

    public static List<UncaughtExceptionData> getUncaughtException(List<String> deviceId, Date startTime, Date endTime,
            int offset, int len) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<UncaughtExceptionData> result = new ArrayList<UncaughtExceptionData>();
        String sql = SqlGetUncaughtException + SqlGetUncaughtExceptionWhere1;
        if (startTime != null) {
            sql += SqlGetUncaughtExceptionWhere2;
        }
        if (deviceId != null && !deviceId.isEmpty()) {
            sql += SqlGetUncaughtExceptionWhere3 + Util.listStringToString(deviceId) + " ) ";
        }
        sql += SqlGetUncaughtExceptionOrder + SqlGetUncaughtExceptionLimit;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(sql);
            int parameterIndex = 1;
            stmt.setTimestamp(parameterIndex++, Util.dateToTimestamp(endTime));
            if (startTime != null) {
                stmt.setTimestamp(parameterIndex++, Util.dateToTimestamp(startTime));
            }
            stmt.setInt(parameterIndex++, offset);
            stmt.setInt(parameterIndex++, len);
            rs = stmt.executeQuery();
            while (rs.next()) {
                UncaughtExceptionData exception = new UncaughtExceptionData();
                exception.setDeviceId(rs.getString(1));
                exception.setLanguage(rs.getString(2));
                exception.setVersion(rs.getString(3));
                exception.setSystemVersion(rs.getString(4));
                exception.setType(rs.getInt(5));
                exception.setTime(Util.timestampToDate(rs.getTimestamp(6)));
                exception.setData(rs.getString(7));
                result.add(exception);
            }
        } catch (SQLException ex) {
            log.error("Error execute sql.", ex);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return result;
    }
}
