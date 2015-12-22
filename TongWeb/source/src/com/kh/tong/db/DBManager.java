package com.kh.tong.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;

import com.kh.db.ConnectionProvider;
import com.kh.db.DBUtil;
import com.kh.tong.data.AppUser;
import com.kh.tong.data.Area;
import com.kh.tong.data.Blog;
import com.kh.tong.data.BlogPost;
import com.kh.tong.data.BuddyRequest;
import com.kh.tong.data.OaAuth;
import com.kh.tong.data.OaUser;
import com.kh.tong.data.OaUserGroup;
import com.kh.tong.data.Radio;
import com.kh.tong.data.ResourceFile;
import com.kh.tong.data.Session;
import com.kh.tong.data.SessionMessage;
import com.kh.tong.data.UserLocation;
import com.kh.tong.test.TrackPoint;

public class DBManager {
    static Logger log = Logger.getLogger(DBManager.class);

    private static final int validTime = 120000;

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

    private static Date timestampToDate(Timestamp timestamp) {
        if (timestamp == null) {
            return null;
        }
        return new Date(timestamp.getTime());
    }

    private static final String sqlAddOaUser = "INSERT INTO oa_user "
            + "(id,uuid,name,password,phone,email,remark)VALUES(?,?,?,?,?,?,?)";

    public static boolean addOaUser(OaUser user) {
        boolean result = false;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(sqlAddOaUser);
            stmt.setInt(1, user.getId());
            stmt.setString(2, user.getUuid());
            stmt.setString(3, user.getName());
            stmt.setString(4, user.getPassword());
            stmt.setString(5, user.getPhone());
            stmt.setString(6, user.getEmail());
            stmt.setString(7, user.getRemark());
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

    private static final String sqlDelOaUserByUuid = "delete from oa_user " + "where uuid = ?";

    public static boolean DelOaUserByUuid(String uuid) {
        boolean result = true;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(sqlDelOaUserByUuid);
            stmt.setString(1, uuid);
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

    private static final String sqlUpdataOaUser = "update oa_user set"
            + " id = ? , uuid = ? ,name = ? ,password = ? ,phone = ? , email = ? ,remark = ? where uuid = ?";

    public static boolean UpdataOaUse(OaUser user) {
        boolean result = false;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(sqlUpdataOaUser);
            stmt.setInt(1, user.getId());
            stmt.setString(2, user.getUuid());
            stmt.setString(3, user.getName());
            stmt.setString(4, user.getPassword());
            stmt.setString(5, user.getPhone());
            stmt.setString(6, user.getEmail());
            stmt.setString(7, user.getRemark());
            stmt.setString(8, user.getUuid());
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

    private static final String sqlFindOaUserByEmail = "SELECT * FROM oa_user " + "WHERE email=? ORDER BY id ASC";

    public static List<OaUser> FindOaUserByEmail(String email) {
        List<OaUser> oaUser = new ArrayList<OaUser>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(sqlFindOaUserByEmail);
            stmt.setString(1, email);
            rs = stmt.executeQuery();
            while (rs.next()) {
                OaUser user = new OaUser();
                user.setId(rs.getInt(1));
                user.setUuid(rs.getString(2));
                user.setName(rs.getString(3));
                user.setPassword(rs.getString(4));
                user.setPhone(rs.getString(5));
                user.setEmail(rs.getString(6));
                user.setRemark(rs.getString(7));
                oaUser.add(user);
            }
        } catch (SQLException e) {
            log.error("Error execute sql.", e);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return oaUser;
    }

    private static final String sqlFindOaUserByName = "SELECT * FROM oa_user " + "WHERE name=? ORDER BY id ASC";

    public static List<OaUser> FindOaUserByName(String name) {
        List<OaUser> oaUser = new ArrayList<OaUser>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(sqlFindOaUserByName);
            stmt.setString(1, name);
            rs = stmt.executeQuery();
            while (rs.next()) {
                OaUser user = new OaUser();
                user.setId(rs.getInt(1));
                user.setUuid(rs.getString(2));
                user.setName(rs.getString(3));
                user.setPassword(rs.getString(4));
                user.setPhone(rs.getString(5));
                user.setEmail(rs.getString(6));
                user.setRemark(rs.getString(7));
                oaUser.add(user);
            }
        } catch (SQLException e) {
            log.error("Error execute sql.", e);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return oaUser;
    }

    private static final String sqlFindOaUserByUuid = "SELECT * FROM oa_user " + "WHERE uuid=? ORDER BY id ASC";

    public static List<OaUser> FindOaUserByUuid(String uuid) {
        List<OaUser> oaUser = new ArrayList<OaUser>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(sqlFindOaUserByUuid);
            stmt.setString(1, uuid);
            rs = stmt.executeQuery();
            while (rs.next()) {
                OaUser user = new OaUser();
                user.setId(rs.getInt(1));
                user.setUuid(rs.getString(2));
                user.setName(rs.getString(3));
                user.setPassword(rs.getString(4));
                user.setPhone(rs.getString(5));
                user.setEmail(rs.getString(6));
                user.setRemark(rs.getString(7));
                oaUser.add(user);
            }
        } catch (SQLException e) {
            log.error("Error execute sql.", e);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return oaUser;
    }

    private static final String sqlCheckOaUserByname = "SELECT count(*) FROM oa_user " + "WHERE name=?";

    public static boolean checkOaUserByname(String name) {
        boolean result = false;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(sqlCheckOaUserByname);
            stmt.setString(1, name);
            rs = stmt.executeQuery();
            while (rs.next()) {
                if (rs.getInt(1) >= 1) {
                    result = true;
                }
                ;
            }
        } catch (SQLException e) {
            log.error("Error execute sql.", e);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return result;
    }

    private static final String sqlCheckOaUserByEmail = "SELECT count(*) FROM oa_user " + "WHERE email=?";

    public static boolean checkOaUserByEmail(String email) {
        boolean result = false;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(sqlCheckOaUserByEmail);
            stmt.setString(1, email);
            rs = stmt.executeQuery();
            while (rs.next()) {
                if (rs.getInt(1) >= 1) {
                    result = true;
                }
                ;
            }
        } catch (SQLException e) {
            log.error("Error execute sql.", e);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return result;
    }

    private static final String sqlCheckOaUserByPhone = "SELECT count(*) FROM oa_user " + "WHERE phone=?";

    public static boolean checkOaUserByPhone(String phone) {
        boolean result = false;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(sqlCheckOaUserByPhone);
            stmt.setString(1, phone);
            rs = stmt.executeQuery();
            while (rs.next()) {
                if (rs.getInt(1) >= 1) {
                    result = true;
                }
            }
        } catch (SQLException e) {
            log.error("Error execute sql.", e);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return result;
    }

    private static final String sqlCheckOaUserByUuid = "SELECT count(*) FROM oa_user " + "WHERE uuid=?";

    public static boolean checkOaUserByUuid(String uuid) {
        boolean result = false;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(sqlCheckOaUserByUuid);
            stmt.setString(1, uuid);
            rs = stmt.executeQuery();
            while (rs.next()) {
                if (rs.getInt(1) >= 1) {
                    result = true;
                }
            }
        } catch (SQLException e) {
            log.error("Error execute sql.", e);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return result;
    }

    private static final String SqlGetUser = "SELECT id, uuid, `name`, `password`, phone , email , remark FROM `oa_user` "
            + "WHERE name = ? and password = ? ";

    public static OaUser getUser(String userName, String password) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        OaUser user = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(SqlGetUser);
            stmt.setString(1, userName);
            stmt.setString(2, password);
            log.info(SqlGetUser);
            rs = stmt.executeQuery();
            if (rs.next()) {
                user = new OaUser();
                user.setId(rs.getInt(1));
                user.setUuid(rs.getString(2));
                user.setName(rs.getString(3));
                user.setPassword(rs.getString(4));
                user.setPhone(rs.getString(5));
                user.setEmail(rs.getString(6));
                user.setRemark(rs.getString(7));
            }
        } catch (SQLException ex) {
            log.error("Error execute sql.", ex);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return user;
    }

    private static final String SqlGetUserAccess = "SELECT c.id FROM oa_auth as c WHERE  c.uuid in "
            + "(SELECT b.oa_auth_uuid FROM `oa_usergroup_auth` as b "
            + "WHERE b.oa_usergroup_uuid in  (SELECT a.oa_usergroup_uuid from oa_user_usergroup as a where oa_user_uuid = ?))";

    public static List<Integer> getUserAccess(String oaUserUuid) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Integer> result = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(SqlGetUserAccess);
            stmt.setString(1, oaUserUuid);
            rs = stmt.executeQuery();
            result = new ArrayList<Integer>();
            while (rs.next()) {
                result.add(rs.getInt(1));
            }
        } catch (SQLException ex) {
            log.error("Error execute sql.", ex);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return result;
    }

    private static final String SqlGetUserList = "SELECT * FROM `oa_user`";
    private static final String SqlGetUserListWhere = " WHERE `name` = ?";
    private static final String SqlGetUserListOrder = " ORDER BY id ASC";

    public static List<OaUser> getUserList(String name) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = SqlGetUserList;
        if (name != null && !"".equals(name)) {
            sql += SqlGetUserListWhere;
        }
        sql += SqlGetUserListOrder;
        List<OaUser> result = new ArrayList<OaUser>();
        try {
            conn = getConn();
            stmt = conn.prepareStatement(sql);
            if (name != null && !"".equals(name)) {
                stmt.setString(1, name);
            }
            rs = stmt.executeQuery();
            while (rs.next()) {
                OaUser user = new OaUser();
                user.setId(rs.getInt(1));
                user.setUuid(rs.getString(2));
                user.setName(rs.getString(3));
                user.setPassword(rs.getString(4));
                user.setPhone(rs.getString(5));
                user.setEmail(rs.getString(6));
                user.setRemark(rs.getString(7));
                result.add(user);
            }
        } catch (SQLException ex) {
            log.error("Error execute sql.", ex);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return result;
    }

    private static final String sqlGetAreaList = "SELECT * FROM area";

    public static List<Area> getAreaList() {
        List<Area> areaList = new ArrayList<Area>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(sqlGetAreaList);
            rs = stmt.executeQuery();
            while (rs.next()) {
                Area area = new Area();
                area.setId(rs.getInt(1));
                area.setUuid(rs.getString(2));
                area.setName(rs.getString(3));
                area.setParentUuid(rs.getString(4));
                areaList.add(area);
            }
        } catch (SQLException e) {
            log.error("Error execute sql.", e);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return areaList;
    }

    private static final String sqlCheckAreaIfParentArea = "SELECT count(*) FROM area "
            + "WHERE parent_uuid=? and name=? ";

    // 检测节点下名称是否重复
    public static boolean checkAreaIfParentArea(String parentAreaUuid, String areaName) {
        boolean result = false;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(sqlCheckAreaIfParentArea);
            stmt.setString(1, parentAreaUuid);
            stmt.setString(2, areaName);
            rs = stmt.executeQuery();
            while (rs.next()) {
                if (rs.getInt(1) >= 1) {
                    result = true;
                }
            }
        } catch (SQLException e) {
            log.error("Error execute sql.", e);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return result;
    }

    private static final String sqlAddArea = "INSERT INTO area " + "(uuid,name,parent_uuid)VALUES(?,?,?)";

    public static boolean addArea(String parentAreaUuid, String areaName) {
        boolean result = false;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(sqlAddArea);
            stmt.setString(1, UUID.randomUUID().toString().replaceAll("-", ""));
            stmt.setString(2, areaName);
            stmt.setString(3, parentAreaUuid);
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

    private static final String sqlCheckAreaUuid = "SELECT count(*) FROM area " + "WHERE uuid=?";

    public static boolean checkAreaUuid(String areaUuid) {
        boolean result = false;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(sqlCheckAreaUuid);
            stmt.setString(1, areaUuid);
            rs = stmt.executeQuery();
            while (rs.next()) {
                if (rs.getInt(1) >= 1) {
                    result = true;
                }
            }
        } catch (SQLException e) {
            log.error("Error execute sql.", e);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return result;
    }

    private static final String sqlCheckAreaParentUuid = "SELECT count(*) FROM area " + "WHERE parent_uuid=?";

    public static boolean checkAreaParentUuid(String ParentUuid) {
        boolean result = false;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(sqlCheckAreaParentUuid);
            stmt.setString(1, ParentUuid);
            rs = stmt.executeQuery();
            while (rs.next()) {
                if (rs.getInt(1) >= 1) {
                    result = true;
                }
            }
        } catch (SQLException e) {
            log.error("Error execute sql.", e);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return result;
    }

    private static final String sqldelAreaByUuid = "delete from area " + "where uuid = ?";

    public static boolean delAreaByUuid(String delAreaUuid) {
        boolean result = true;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(sqldelAreaByUuid);
            stmt.setString(1, delAreaUuid);
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

    private static final String sqlGetRadioList = "SELECT * FROM radio_station";

    public static List<Radio> getRadioList() {
        List<Radio> radiolist = new ArrayList<Radio>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(sqlGetRadioList);
            rs = stmt.executeQuery();
            while (rs.next()) {
                Radio radio = new Radio();
                radio.setId(rs.getInt(1));
                radio.setUuid(rs.getString(2));
                radio.setName(rs.getString(3));
                radio.setUrl(rs.getString(4));
                radio.setArea_uuid(rs.getString(5));
                radiolist.add(radio);
            }
        } catch (SQLException e) {
            log.error("Error execute sql.", e);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return radiolist;
    }

    private static final String sqlAddRadio = "INSERT INTO radio_station " + "(uuid,name,url,area_uuid)VALUES(?,?,?,?)";

    public static boolean addRadio(Radio radio) {
        boolean result = false;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(sqlAddRadio);
            stmt.setString(1, radio.getUuid());
            stmt.setString(2, radio.getName());
            stmt.setString(3, radio.getUrl());
            stmt.setString(4, radio.getArea_uuid());
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

    private static final String sqlUpdataRadio = "update radio_station set" + " name = ?, url = ?  where uuid=?";

    public static boolean updataRadio(Radio radio) {
        boolean result = false;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(sqlUpdataRadio);
            stmt.setString(1, radio.getName());
            stmt.setString(2, radio.getUrl());
            stmt.setString(3, radio.getUuid());
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

    private static final String sqlCheckRadioIfRepeat = "SELECT count(*) FROM radio_station "
            + "WHERE area_uuid=? and name=?";

    public static boolean checkRadioIfRepeat(Radio radio) {
        boolean result = false;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(sqlCheckRadioIfRepeat);
            stmt.setString(1, radio.getArea_uuid());
            stmt.setString(2, radio.getName());
            rs = stmt.executeQuery();
            while (rs.next()) {
                if (rs.getInt(1) >= 1) {
                    result = true;
                }
            }
        } catch (SQLException e) {
            log.error("Error execute sql.", e);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return result;
    }

    private static final String sqlCheckRadioByUuid = "SELECT count(*) FROM radio_station " + "WHERE uuid=?";

    public static boolean checkRadioByUuid(String RadioUuid) {
        boolean result = false;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(sqlCheckRadioByUuid);
            stmt.setString(1, RadioUuid);
            rs = stmt.executeQuery();
            while (rs.next()) {
                if (rs.getInt(1) >= 1) {
                    result = true;
                }
            }
        } catch (SQLException e) {
            log.error("Error execute sql.", e);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return result;
    }

    private static final String sqlDelRadioByUuid = "DELETE FROM radio_station " + "WHERE uuid = ?";

    public static boolean delRadioByUuid(String RadioUuid) {
        boolean result = true;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(sqlDelRadioByUuid);
            stmt.setString(1, RadioUuid);
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

    private static final String sqlGetRadioColle = "SELECT * FROM radio_station WHERE uuid in "
            + "(SELECT radio_station_uuid FROM area_recommend_radio )";

    public static List<Radio> getRadioColle() {
        // 获取推荐收藏电台列表
        List<Radio> radiolist = new ArrayList<Radio>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(sqlGetRadioColle);
            rs = stmt.executeQuery();
            while (rs.next()) {
                Radio radio = new Radio();
                radio.setId(rs.getInt(1));
                radio.setUuid(rs.getString(2));
                radio.setName(rs.getString(3));
                radio.setUrl(rs.getString(4));
                radio.setArea_uuid(rs.getString(5));
                radiolist.add(radio);
            }
        } catch (SQLException e) {
            log.error("Error execute sql.", e);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return radiolist;

    }

    private static final String sqlGetRadioColle2 = "SELECT * FROM radio_station WHERE uuid in "
            + "(SELECT radio_station_uuid FROM area_recommend_radio where area_uuid = ?)";

    public static List<Radio> getRadioColle(String AreaUuid) {
        // 获取推荐收藏电台列表
        List<Radio> radiolist = new ArrayList<Radio>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(sqlGetRadioColle2);
            stmt.setString(1, AreaUuid);
            rs = stmt.executeQuery();
            while (rs.next()) {
                Radio radio = new Radio();
                radio.setId(rs.getInt(1));
                radio.setUuid(rs.getString(2));
                radio.setName(rs.getString(3));
                radio.setUrl(rs.getString(4));
                radio.setArea_uuid(rs.getString(5));
                radiolist.add(radio);
            }
        } catch (SQLException e) {
            log.error("Error execute sql.", e);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return radiolist;

    }

    /*
     * private static String sqlGetRadioColleByAppUser =
     * "SELECT radio_station.* FROM area_recommend_radio  LEFT " +
     * "JOIN radio_station ON radio_station.uuid = area_recommend_radio.radio_station_uuid "
     * +"WHERE area_recommend_radio.area_uuid  = ?";
     */
    private static final String sqlGetRadioColleByAppUser = "SELECT * FROM radio_station WHERE uuid in "
            + "(SELECT radio_station_uuid FROM user_radio_collection where app_user_uuid = ?)";

    public static List<Radio> getRadioColleByAppUser(String userUuid) {
        List<Radio> radiolist = new ArrayList<Radio>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(sqlGetRadioColleByAppUser);
            stmt.setString(1, userUuid);
            rs = stmt.executeQuery();
            while (rs.next()) {
                Radio radio = new Radio();
                radio.setId(rs.getInt(1));
                radio.setUuid(rs.getString(2));
                radio.setName(rs.getString(3));
                radio.setUrl(rs.getString(4));
                radio.setArea_uuid(rs.getString(5));
                radiolist.add(radio);
            }
        } catch (SQLException e) {
            log.error("Error execute sql.", e);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return radiolist;
    }

    private static final String sqlDelUserFavRadio = "delete from user_radio_collection " + "where app_user_uuid = ?";

    public static boolean delUserFavRadio(String appUserUuid) {
        boolean result = true;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(sqlDelUserFavRadio);
            stmt.setString(1, appUserUuid);
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

    private static final String sqladdUserFavRadio = "INSERT INTO user_radio_collection "
            + "(app_user_uuid,radio_station_uuid,sequence_number)VALUES(?,?,?)";

    public static boolean addUserFavRadio(String appUserUuid, String radioStationUuid, int sequenceNumber) {
        boolean result = false;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(sqladdUserFavRadio);
            stmt.setString(1, appUserUuid);
            stmt.setString(2, radioStationUuid);
            stmt.setInt(3, sequenceNumber);
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

    private static final String sqlAddRadioColle = "INSERT INTO area_recommend_radio "
            + "(area_uuid,radio_station_uuid,rank)VALUES(?,?,?)";

    public static boolean addRadioColle(String areaUuid, String radioUuid, int number) {
        boolean result = false;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(sqlAddRadioColle);
            stmt.setString(1, areaUuid);
            stmt.setString(2, radioUuid);
            stmt.setInt(3, number + 1);
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

    private static final String sqlCheckRadioColleMoreSize = "SELECT count(*) FROM area_recommend_radio "
            + "WHERE area_uuid=?";

    public static int checkRadioColleSize(String areaUuid) {
        int result = 0;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(sqlCheckRadioColleMoreSize);
            stmt.setString(1, areaUuid);
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

    private static final String sqlDelRadioColle = "delete from area_recommend_radio "
            + "where area_uuid = ? and radio_station_uuid=?";

    public static boolean delRadioColle(String areaUuid, String radioUuid) {
        boolean result = true;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(sqlDelRadioColle);
            stmt.setString(1, areaUuid);
            stmt.setString(2, radioUuid);
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

    private static final String sqlDelRadioColleByRadioUuid = "delete from area_recommend_radio "
            + "where radio_station_uuid=?";

    public static boolean delRadioColleByRadioUuid(String radioUuid) {
        boolean result = true;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(sqlDelRadioColleByRadioUuid);
            stmt.setString(1, radioUuid);
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

    private static final String sqlRenameArea = "update area set" + " name = ?  where uuid=?";

    public static boolean renameArea(String areaUuid, String areaName) {
        boolean result = false;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(sqlRenameArea);
            stmt.setString(1, areaName);
            stmt.setString(2, areaUuid);
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

    private static final String sqlCheckUserUuidByCollection = "SELECT count(*) FROM user_radio_collection "
            + "WHERE app_user_uuid=?";

    public static boolean checkUserUuidByCollection(String appUserUuid) {
        boolean result = false;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(sqlCheckUserUuidByCollection);
            stmt.setString(1, appUserUuid);
            rs = stmt.executeQuery();
            while (rs.next()) {
                if (rs.getInt(1) >= 1) {
                    result = true;
                }
            }
        } catch (SQLException e) {
            log.error("Error execute sql.", e);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return result;
    }

    private static final String SqlGetUserGroupList = "SELECT * FROM oa_usergroup";
    private static final String SqlGetUserGroupListWhere = " WHERE `name` = ?";

    public static List<OaUserGroup> getUserGroupList(String name) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = SqlGetUserGroupList;
        if (name != null && !"".equals(name)) {
            sql += SqlGetUserGroupListWhere;
        }
        List<OaUserGroup> result = new ArrayList<OaUserGroup>();
        try {
            conn = getConn();
            stmt = conn.prepareStatement(sql);
            if (name != null && !"".equals(name)) {
                stmt.setString(1, name);
            }
            rs = stmt.executeQuery();
            while (rs.next()) {
                OaUserGroup userGroup = new OaUserGroup();
                userGroup.setId(rs.getInt(1));
                userGroup.setUuid(rs.getString(2));
                userGroup.setName(rs.getString(3));
                userGroup.setCreateTime(rs.getTimestamp(4));
                userGroup.setRemark(rs.getString(5));
                userGroup.setOaAuth(getUserGroupOaAuthListByUserGroupUUid(rs.getString(2)));
                userGroup.setOaUser(getUserGroupOaUserListByUserGroupUUid(rs.getString(2)));
                result.add(userGroup);
            }
        } catch (SQLException ex) {
            log.error("Error execute sql.", ex);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return result;
    }

    private static final String SqlGetUserGroupOaAuthListByUserGroupUUid = "select * from oa_auth "
            + " where uuid in (select oa_auth_uuid from oa_usergroup_auth " + " where oa_usergroup_uuid = ?)";

    public static List<OaAuth> getUserGroupOaAuthListByUserGroupUUid(String userGroupUuid) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<OaAuth> result = new ArrayList<OaAuth>();
        try {
            conn = getConn();
            stmt = conn.prepareStatement(SqlGetUserGroupOaAuthListByUserGroupUUid);
            stmt.setString(1, userGroupUuid);
            rs = stmt.executeQuery();
            while (rs.next()) {
                OaAuth oaAuth = new OaAuth();
                oaAuth.setId(rs.getInt(1));
                oaAuth.setUuid(rs.getString(2));
                oaAuth.setName(rs.getString(3));
                oaAuth.setRemark(rs.getString(4));
                result.add(oaAuth);
            }
        } catch (SQLException ex) {
            log.error("Error execute sql.", ex);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return result;
    }

    private static final String SqlGetUserGroupOaUserListByUserGroupUUid = "select * from oa_user "
            + " where uuid in (select oa_user_uuid from oa_user_usergroup " + " where oa_usergroup_uuid = ?)";

    public static List<OaUser> getUserGroupOaUserListByUserGroupUUid(String userGroupUuid) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<OaUser> result = new ArrayList<OaUser>();
        try {
            conn = getConn();
            stmt = conn.prepareStatement(SqlGetUserGroupOaUserListByUserGroupUUid);
            stmt.setString(1, userGroupUuid);
            rs = stmt.executeQuery();
            while (rs.next()) {
                OaUser oaUser = new OaUser();
                oaUser.setId(rs.getInt(1));
                oaUser.setUuid(rs.getString(2));
                oaUser.setName(rs.getString(3));
                oaUser.setPassword(rs.getString(4));
                oaUser.setPhone(rs.getString(5));
                oaUser.setEmail(rs.getString(6));
                oaUser.setRemark(rs.getString(7));
                result.add(oaUser);
            }
        } catch (SQLException ex) {
            log.error("Error execute sql.", ex);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return result;
    }

    private static final String sqlCheckOaUserGroupByName = "SELECT count(*) FROM oa_usergroup " + "WHERE name=?";

    public static boolean checkOaUserGroupByName(String name) {
        boolean result = false;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(sqlCheckOaUserGroupByName);
            stmt.setString(1, name);
            rs = stmt.executeQuery();
            while (rs.next()) {
                if (rs.getInt(1) >= 1) {
                    result = true;
                }
            }
        } catch (SQLException e) {
            log.error("Error execute sql.", e);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return result;
    }

    private static final String sqlAddOaUserGroup = "INSERT INTO oa_usergroup "
            + "(id,uuid,name,createtime,remark)VALUES(?,?,?,?,?)";

    public static boolean addOaUserGroup(OaUserGroup ug) {
        boolean result = false;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(sqlAddOaUserGroup);
            stmt.setInt(1, ug.getId());
            stmt.setString(2, ug.getUuid());
            stmt.setString(3, ug.getName());
            stmt.setTimestamp(4, new Timestamp(new Date().getTime()));
            stmt.setString(5, ug.getRemark());
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

    private static final String sqlCheckOaUserGroupByUuid = "SELECT count(*) FROM oa_usergroup " + "WHERE uuid=?";

    public static boolean checkOaUserGroupByUuid(String uuid) {
        boolean result = false;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(sqlCheckOaUserGroupByUuid);
            stmt.setString(1, uuid);
            rs = stmt.executeQuery();
            while (rs.next()) {
                if (rs.getInt(1) >= 1) {
                    result = true;
                }
            }
        } catch (SQLException e) {
            log.error("Error execute sql.", e);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return result;
    }

    private static final String sqlDelOaUserGroupByUuid = "delete from oa_usergroup " + "where uuid=?";

    public static boolean delOaUserGroupByUuid(String uuid) {
        boolean result = true;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(sqlDelOaUserGroupByUuid);
            stmt.setString(1, uuid);
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

    private static final String sqlUpdataOaUserGroup = "update oa_usergroup set"
            + " name = ? ,remark = ?  where uuid = ?";

    public static boolean UpdataOaUserGroup(OaUserGroup ug) {
        boolean result = false;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(sqlUpdataOaUserGroup);
            stmt.setString(1, ug.getName());
            stmt.setString(2, ug.getRemark());
            stmt.setString(3, ug.getUuid());
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

    private static final String sqlAddUserGroupAuth = "INSERT INTO oa_usergroup_auth "
            + "( oa_usergroup_uuid , oa_auth_uuid ) VALUES ( ? , ? )";

    public static boolean addUserGroupAuth(String ugUuid, String authUuid) {
        boolean result = false;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(sqlAddUserGroupAuth);
            stmt.setString(1, ugUuid);
            stmt.setString(2, authUuid);
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

    private static final String sqlDelUserGroupAuth = "delete from oa_usergroup_auth "
            + "where oa_usergroup_uuid= ? and oa_auth_uuid = ?";

    public static boolean delUserGroupAuth(String ugUuid, String authUuid) {
        boolean result = true;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(sqlDelUserGroupAuth);
            stmt.setString(1, ugUuid);
            stmt.setString(2, authUuid);
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

    private static final String sqlAddUserGroupUser = "INSERT INTO oa_user_usergroup "
            + "( oa_user_uuid , oa_usergroup_uuid ) VALUES ( ? , ? )";

    public static boolean addUserGroupUser(String ugUuid, String userUuid) {
        boolean result = false;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(sqlAddUserGroupUser);
            stmt.setString(1, userUuid);
            stmt.setString(2, ugUuid);
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

    private static final String sqlDelUserGroupUser = "delete from oa_user_usergroup "
            + "where oa_user_uuid= ? and oa_usergroup_uuid = ?";

    public static boolean delUserGroupUser(String ugUuid, String userUuid) {
        boolean result = true;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(sqlDelUserGroupUser);
            stmt.setString(1, userUuid);
            stmt.setString(2, ugUuid);
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

    private static final String sqlGetOaAuthList = "SELECT * FROM oa_auth ";

    public static List<OaAuth> getOaAuthList() {
        List<OaAuth> result = new ArrayList<OaAuth>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(sqlGetOaAuthList);
            rs = stmt.executeQuery();
            while (rs.next()) {
                OaAuth auth = new OaAuth();
                auth.setId(rs.getInt(1));
                auth.setUuid(rs.getString(2));
                auth.setName(rs.getString(3));
                auth.setRemark(rs.getString(4));
                result.add(auth);
            }
        } catch (SQLException e) {
            log.error("Error execute sql.", e);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return result;
    }

    private static final String sqlAddPhoneVerifCode = "INSERT INTO phone_verif_code "
            + "( phone , verif_code , create_time , valid_time) VALUES (?,?,?,?)";

    public static boolean addPhoneVerifCode(String appUserPhone) {
        boolean result = false;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(sqlAddPhoneVerifCode);
            stmt.setString(1, appUserPhone);
            stmt.setString(2, appUserPhone.substring(appUserPhone.length() - 6, appUserPhone.length()));
            stmt.setTimestamp(3, new Timestamp(new Date().getTime()));
            stmt.setTimestamp(4, new Timestamp(new Date().getTime() + validTime));
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

    private static final String sqlCheckPhoneCodeByCodeAndPhone = "SELECT count(*) FROM phone_verif_code "
            + " WHERE phone=? and verif_code = ? ";

    public static boolean checkPhoneCodeByCodeAndPhone(String appUserPhone, String code) {
        boolean result = false;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(sqlCheckPhoneCodeByCodeAndPhone);
            stmt.setString(1, appUserPhone);
            stmt.setString(2, code);
            rs = stmt.executeQuery();
            while (rs.next()) {
                if (rs.getInt(1) >= 1) {
                    result = true;
                }
            }
        } catch (SQLException e) {
            log.error("Error execute sql.", e);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return result;
    }

    private static final String sqlCheckPhoneCodeByPhone = "SELECT count(*) FROM phone_verif_code " + " WHERE phone=?";

    public static boolean checkPhoneCodeByPhone(String appUserPhone) {
        boolean result = false;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(sqlCheckPhoneCodeByPhone);
            stmt.setString(1, appUserPhone);
            rs = stmt.executeQuery();
            while (rs.next()) {
                if (rs.getInt(1) >= 1) {
                    result = true;
                }
            }
        } catch (SQLException e) {
            log.error("Error execute sql.", e);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return result;
    }

    private static final String sqlUpdPhoneVerifCode = "update phone_verif_code set"
            + " verif_code = ? , create_time = ? , valid_time = ?  where phone = ?";

    public static boolean updPhoneVerifCode(String appUserPhone) {
        boolean result = false;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(sqlUpdPhoneVerifCode);
            stmt.setString(1, appUserPhone.substring(appUserPhone.length() - 6, appUserPhone.length()));
            stmt.setTimestamp(2, new Timestamp(new Date().getTime()));
            stmt.setTimestamp(3, new Timestamp(new Date().getTime() + 60000));
            stmt.setString(4, appUserPhone);
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

    private static final String sqlCheckPhoneIfAppUser = "SELECT count(*) FROM app_user " + " WHERE phone=?";

    public static boolean checkPhoneIfAppUser(String appUserPhone) {
        boolean result = false;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(sqlCheckPhoneIfAppUser);
            stmt.setString(1, appUserPhone);
            rs = stmt.executeQuery();
            while (rs.next()) {
                if (rs.getInt(1) >= 1) {
                    result = true;
                }
            }
        } catch (SQLException e) {
            log.error("Error execute sql.", e);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return result;
    }

    private static final String sqlCheckPhoneCodeIfValid = "SELECT valid_time , verif_time FROM phone_verif_code "
            + " WHERE phone= ? and verif_code = ? ";

    public static boolean checkPhoneCodeIfValid(String appUserPhone, String code) {
        boolean result = false;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(sqlCheckPhoneCodeIfValid);
            stmt.setString(1, appUserPhone);
            stmt.setString(2, code);
            rs = stmt.executeQuery();
            while (rs.next()) {
                long validTime = rs.getTimestamp(1).getTime();
                long verifTime = rs.getTimestamp(2).getTime();
                if (validTime > verifTime) {
                    result = true;
                }
            }
        } catch (SQLException e) {
            log.error("Error execute sql.", e);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return result;
    }

    private static final String sqlUpdPhoneVerifTime = "update phone_verif_code set"
            + " verif_time = ?  where phone = ?";

    public static boolean updPhoneVerifTime(String appUserPhone) {
        boolean result = false;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(sqlUpdPhoneVerifTime);
            stmt.setTimestamp(1, new Timestamp(new Date().getTime()));
            stmt.setString(2, appUserPhone);
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

    // #####################################################################
    private static final String SqlGetBuddyRequestList = "SELECT id, uuid, init_user_uuid, "
            + "invitee_user_uuid, msg, invite_time, result_code, result_time FROM buddy_request";
    private static final String SqlGetBuddyRequestListWhere = " WHERE uuid =?";

    public static List<BuddyRequest> getBuddyRequestList(String uuid) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<BuddyRequest> result = new ArrayList<BuddyRequest>();
        String sql = SqlGetBuddyRequestList;
        if (uuid != null && !"".equals(uuid)) {
            sql += SqlGetBuddyRequestListWhere;
        }
        try {
            conn = getConn();
            stmt = conn.prepareStatement(sql);
            if (uuid != null && !"".equals(uuid)) {
                stmt.setString(1, uuid);
            }
            rs = stmt.executeQuery();
            while (rs.next()) {
                BuddyRequest buddyRequest = new BuddyRequest();
                buddyRequest.setId(rs.getLong(1));
                buddyRequest.setUuid(rs.getString(2));
                buddyRequest.setInitUserUuid(rs.getString(3));
                buddyRequest.setInviteeUserUuid(rs.getString(4));
                buddyRequest.setMsg(rs.getString(5));
                buddyRequest.setInviteTime(timestampToDate(rs.getTimestamp(6)));
                buddyRequest.setResultCode(rs.getInt(7));
                buddyRequest.setResultTime(timestampToDate(rs.getTimestamp(8)));
                result.add(buddyRequest);
            }
        } catch (SQLException ex) {
            log.error("Error execute sql.", ex);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return result;
    }

    private static final String SqlAddBuddyRequest = "INSERT INTO buddy_request "
            + "( uuid, init_user_uuid, invitee_user_uuid, msg, invite_time ) "
            + "VALUES ( REPLACE (UUID(), '-', ''), ?, ?, ?, NOW())";

    public static boolean addBuddyRequest(BuddyRequest buddyRequest, String userUuid) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean result = false;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(SqlAddBuddyRequest);
            stmt.setString(1, userUuid);
            stmt.setString(2, buddyRequest.getInviteeUserUuid());
            stmt.setString(3, buddyRequest.getMsg());
            int ret = stmt.executeUpdate();
            result = ret == 1;
        } catch (SQLException ex) {
            log.error("Error execute sql.", ex);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return result;
    }

    private static final String SqlUpdBuddyRequest = "UPDATE buddy_request SET result_code = ?, "
            + "result_time = NOW() WHERE uuid = ?";

    public static boolean updBuddyRequest(String uuid, int resultCode) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean result = false;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(SqlUpdBuddyRequest);
            stmt.setInt(1, resultCode);
            stmt.setString(2, uuid);
            int ret = stmt.executeUpdate();
            result = ret == 1;
        } catch (SQLException ex) {
            log.error("Error execute sql.", ex);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return result;
    }

    private static final String SqlDelBuddyRequest = "DELETE FROM buddy_request WHERE uuid=?";

    public static boolean delBuddyRequest(String uuid) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean result = false;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(SqlDelBuddyRequest);
            stmt.setString(1, uuid);
            int ret = stmt.executeUpdate();
            result = ret == 1;
        } catch (SQLException ex) {
            log.error("Error execute sql.", ex);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return result;
    }

    private static final String SqlGetBuddyList = "SELECT a.id, a.uuid, a.phone, a.email, a.email_status,"
            + " a.`password`, a.nick_name, a.reg_time FROM app_user a JOIN buddy_list b "
            + "ON a.uuid = b.buddy_uuid WHERE b.user_uuid = ? ORDER BY CONVERT (`nick_name` USING gbk)";

    public static List<AppUser> getBuddyList(String useruid) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<AppUser> result = new ArrayList<AppUser>();
        try {
            conn = getConn();
            stmt = conn.prepareStatement(SqlGetBuddyList);
            stmt.setString(1, useruid);
            rs = stmt.executeQuery();
            while (rs.next()) {
                AppUser appUser = new AppUser();
                appUser.setId(rs.getLong(1));
                appUser.setUuid(rs.getString(2));
                appUser.setPhone(rs.getString(3));
                appUser.setEmail(rs.getString(4));
                appUser.setEmailStatus(rs.getInt(5));
                appUser.setPassword(rs.getString(6));
                appUser.setNickName(rs.getString(7));
                appUser.setNameFirstLetter();
                appUser.setRegTime(timestampToDate(rs.getTimestamp(8)));
                result.add(appUser);
            }
        } catch (SQLException ex) {
            log.error("Error execute sql.", ex);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return result;
    }

    private static final String SqlAddBuddy = "INSERT INTO buddy_list "
            + "( user_uuid, buddy_uuid, create_time, ref_request_uuid ) VALUES (?,?, NOW() ,?)";

    public static boolean addBuddy(String userUuid, String buddyUuid, String refRequestUuid) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean result = false;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(SqlAddBuddy);
            stmt.setString(1, userUuid);
            stmt.setString(2, buddyUuid);
            stmt.setString(3, refRequestUuid);
            int ret = stmt.executeUpdate();
            result = ret == 1;
        } catch (SQLException ex) {
            log.error("Error execute sql.", ex);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return result;
    }

    private static final String SqlDelBuddy = "DELETE FROM buddy_list WHERE user_uuid =? AND buddy_uuid =?";

    public static boolean delBuddy(String userUuid, String buddyUuid) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean result = false;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(SqlDelBuddy);
            stmt.setString(1, userUuid);
            stmt.setString(2, buddyUuid);
            int ret = stmt.executeUpdate();
            result = ret == 1;
        } catch (SQLException ex) {
            log.error("Error execute sql.", ex);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return result;
    }

    private static final String SqlGetUserLocation = "SELECT id, user_uuid, app_uuid, "
            + "app_access_time, term_sn, term_access_time FROM user_location WHERE user_uuid = ?";

    public static UserLocation getUserLocation(String userUuid) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        UserLocation result = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(SqlGetUserLocation);
            stmt.setString(1, userUuid);
            rs = stmt.executeQuery();
            if (rs.next()) {
                result = new UserLocation();
                result.setId(rs.getLong(1));
                result.setUserUuid(rs.getString(2));
                result.setAppUuid(rs.getString(3));
                result.setAppAccessTime(timestampToDate(rs.getTimestamp(4)));
                result.setTermSN(rs.getString(5));
                result.setTermAccessTime(timestampToDate(rs.getTimestamp(6)));
            }
        } catch (SQLException ex) {
            log.error("Error execute sql.", ex);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return result;
    }

    private static final String SqlGetSessionList = "SELECT id, uuid, session_type, init_user_uuid, create_time, "
            + "need_user_acceptance FROM `session` ";
    private static final String SqlGetSessionListWhere = " WHERE uuid = ?";

    public static List<Session> getSessionList(String uuid) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Session> result = new ArrayList<Session>();
        String sql = SqlGetSessionList;
        if (uuid != null && !"".equals(uuid)) {
            sql += SqlGetSessionListWhere;
        }
        try {
            conn = getConn();
            stmt = conn.prepareStatement(sql);
            if (uuid != null && !"".equals(uuid)) {
                stmt.setString(1, uuid);
            }
            rs = stmt.executeQuery();
            while (rs.next()) {
                Session session = new Session();
                session.setId(rs.getLong(1));
                session.setUuid(rs.getString(2));
                session.setSessionType(rs.getInt(3));
                session.setInitUserUuid(rs.getString(4));
                session.setCreateTime(timestampToDate(rs.getTimestamp(5)));
                session.setNeedUserAcceptance(rs.getInt(6) == 1 ? true : false);
                session.setUserUuid(getSessionParty(rs.getString(2)));
                result.add(session);
            }
        } catch (SQLException ex) {
            log.error("Error execute sql.", ex);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return result;
    }

    private static final String SqlGetSessionPartyList = "SELECT a.id, a.uuid, a.phone, a.email, a.email_status, "
            + "a.`password`, a.nick_name, a.reg_time FROM app_user a JOIN session_party s "
            + "ON a.uuid = s.user_uuid WHERE s.session_uuid = ? ORDER BY CONVERT (a.`nick_name` USING gbk)";

    private static List<AppUser> getSessionParty(String sessionUuid) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<AppUser> result = new ArrayList<AppUser>();
        try {
            conn = getConn();
            stmt = conn.prepareStatement(SqlGetSessionPartyList);
            stmt.setString(1, sessionUuid);
            rs = stmt.executeQuery();
            while (rs.next()) {
                AppUser appUser = new AppUser();
                appUser.setId(rs.getLong(1));
                appUser.setUuid(rs.getString(2));
                appUser.setPhone(rs.getString(3));
                appUser.setEmail(rs.getString(4));
                appUser.setEmailStatus(rs.getInt(5));
                appUser.setPassword(rs.getString(6));
                appUser.setNickName(rs.getString(7));
                appUser.setNameFirstLetter();
                appUser.setRegTime(timestampToDate(rs.getTimestamp(8)));
                result.add(appUser);
            }
        } catch (SQLException ex) {
            log.error("Error execute sql.", ex);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return result;
    }

    private static final String SqlAddSession = "INSERT INTO `session` ( uuid, session_type, init_user_uuid, create_time, "
            + "need_user_acceptance ) VALUES ( REPLACE (UUID(), '-', ''), ?, ?, NOW(), ? )";

    public static boolean addSession(Session session) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean result = false;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(SqlAddSession);
            stmt.setInt(1, session.getSessionType());
            stmt.setString(2, session.getInitUserUuid());
            stmt.setInt(3, session.isNeedUserAcceptance() == true ? 1 : 0);
            int ret = stmt.executeUpdate();
            result = ret == 1;
        } catch (SQLException ex) {
            log.error("Error execute sql.", ex);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return result;
    }

    private static final String SqlDelSession = "DELETE FROM `session` WHERE uuid = ?";

    public static boolean delSession(String uuid) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean result = false;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(SqlDelSession);
            stmt.setString(1, uuid);
            int ret = stmt.executeUpdate();
            result = ret == 1;
        } catch (SQLException ex) {
            log.error("Error execute sql.", ex);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return result;
    }

    private static final String SqlSessionParty = "INSERT INTO session_party ( uuid, session_uuid, user_uuid, join_time ) "
            + "VALUES ( REPLACE (UUID(), '-', ''), ?, ?, NOW())";

    public static boolean addSessionParty(String sessionUuid, String userUuid) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean result = false;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(SqlSessionParty);
            stmt.setString(1, sessionUuid);
            stmt.setString(2, userUuid);
            int ret = stmt.executeUpdate();
            result = ret == 1;
        } catch (SQLException ex) {
            log.error("Error execute sql.", ex);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return result;
    }

    private static final String SqlDelSessionParty = "DELETE FROM session_party "
            + "WHERE session_uuid =? AND user_uuid =?";

    public static boolean delSessionParty(String sessionUuid, String userUuid) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean result = false;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(SqlDelSessionParty);
            stmt.setString(1, sessionUuid);
            stmt.setString(2, userUuid);
            int ret = stmt.executeUpdate();
            result = ret == 1;
        } catch (SQLException ex) {
            log.error("Error execute sql.", ex);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return result;
    }

    private static final String SqlGetSessionMessageList = "SELECT id, uuid, session_uuid, type, content, sender_uuid, create_time, "
            + "from_dev_type, from_dev_uuid FROM session_message";
    private static final String SqlGetSessionMessageListWhere1 = " WHERE uuid =?";
    private static final String SqlGetSessionMessageListWhere2 = " WHERE session_uuid =?";
    private static final String SqlGetSessionMessageTime = " AND create_time > ?";
    private static final String SqlGetSessionMessageOrder = " ORDER BY create_time DESC";
    private static final String SqlGetSessionMessageLimit = " Limit 0,?";
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static List<SessionMessage> getSessionMessageList(String sessionUuid, String msgUuid, Date createTime) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<SessionMessage> result = new ArrayList<SessionMessage>();
        String sql = SqlGetSessionMessageList;
        if (msgUuid != null && !"".equals(msgUuid)) {
            sql += SqlGetSessionMessageListWhere1;
        } else if (sessionUuid != null && !"".equals(sessionUuid)) {
            sql += SqlGetSessionMessageListWhere2;
        }
        if (createTime != null) {
            sql += SqlGetSessionMessageTime;
        }
        log.info("getSessionMessageList sql?" + sql);
        try {
            conn = getConn();
            stmt = conn.prepareStatement(sql);
            if (msgUuid != null && !"".equals(msgUuid)) {
                stmt.setString(1, msgUuid);
            } else if (sessionUuid != null && !"".equals(sessionUuid)) {
                stmt.setString(1, sessionUuid);
            }
            if (createTime != null) {
                stmt.setString(2, sdf.format(createTime));
            }
            rs = stmt.executeQuery();
            while (rs.next()) {
                SessionMessage sessionMessage = new SessionMessage();
                sessionMessage.setId(rs.getLong(1));
                sessionMessage.setUuid(rs.getString(2));
                sessionMessage.setSessionUuid(rs.getString(3));
                sessionMessage.setType(rs.getInt(4));
                sessionMessage.setContent(rs.getString(5));
                sessionMessage.setSenderUuid(rs.getString(6));
                sessionMessage.setCreateTime(timestampToDate(rs.getTimestamp(7)));
                sessionMessage.setFromDevType(rs.getInt(8));
                sessionMessage.setFromDevUuid(rs.getString(9));
                result.add(sessionMessage);
            }
        } catch (SQLException ex) {
            log.error("Error execute sql.", ex);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return result;
    }

    public static List<SessionMessage> getLastSessionMessageList(String sessionUuid, int sum) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<SessionMessage> result = new ArrayList<SessionMessage>();
        String sql = SqlGetSessionMessageList;
        sql += SqlGetSessionMessageListWhere2;
        sql += SqlGetSessionMessageOrder;
        sql += SqlGetSessionMessageLimit;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, sessionUuid);
            stmt.setInt(2, sum);
            rs = stmt.executeQuery();
            while (rs.next()) {
                SessionMessage sessionMessage = new SessionMessage();
                sessionMessage.setId(rs.getLong(1));
                sessionMessage.setUuid(rs.getString(2));
                sessionMessage.setSessionUuid(rs.getString(3));
                sessionMessage.setType(rs.getInt(4));
                sessionMessage.setContent(rs.getString(5));
                sessionMessage.setSenderUuid(rs.getString(6));
                sessionMessage.setCreateTime(timestampToDate(rs.getTimestamp(7)));
                sessionMessage.setFromDevType(rs.getInt(8));
                sessionMessage.setFromDevUuid(rs.getString(9));
                result.add(sessionMessage);
            }
        } catch (SQLException ex) {
            log.error("Error execute sql.", ex);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return result;
    }

    private static final String SqlAddSessionMessage = "INSERT INTO session_message ( uuid, session_uuid, type, content, "
            + "sender_uuid, create_time, from_dev_type, from_dev_uuid ) VALUES ( REPLACE (UUID(), '-', ''), ?, ?, ?, ?, NOW(), ?, ? )";

    public static boolean addSessionMessage(SessionMessage sessionMessage) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean result = false;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(SqlAddSessionMessage);
            stmt.setString(1, sessionMessage.getSessionUuid());
            stmt.setInt(2, sessionMessage.getType());
            stmt.setString(3, sessionMessage.getContent());
            stmt.setString(4, sessionMessage.getSenderUuid());
            stmt.setInt(5, sessionMessage.getFromDevType());
            stmt.setString(6, sessionMessage.getFromDevUuid());
            int ret = stmt.executeUpdate();
            result = ret == 1;
        } catch (SQLException ex) {
            log.error("Error execute sql.", ex);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return result;
    }

    private static final String SqlDelSessionMessage = "DELETE FROM session_message ";
    private static final String SqlDelSessionMessageWhere1 = " WHERE uuid =?";
    private static final String SqlDelSessionMessageWhere2 = " WHERE session_uuid =?";

    public static boolean delSessionMessage(String sessionUuid, String msgUuid) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean result = false;
        String sql = SqlDelSessionMessage;
        if (msgUuid != null && !"".equals(msgUuid)) {
            sql += SqlDelSessionMessageWhere1;
        } else if (sessionUuid != null && !"".equals(sessionUuid)) {
            sql += SqlDelSessionMessageWhere2;
        }
        try {
            conn = getConn();
            stmt = conn.prepareStatement(sql);
            if (msgUuid != null && !"".equals(msgUuid)) {
                stmt.setString(1, msgUuid);
            } else if (sessionUuid != null && !"".equals(sessionUuid)) {
                stmt.setString(1, sessionUuid);
            }
            int ret = stmt.executeUpdate();
            result = ret >= 1;
        } catch (SQLException ex) {
            log.error("Error execute sql.", ex);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return result;
    }

    private static final String SqlGetBlogList = "SELECT id, uuid, user_uuid, "
            + "blog_name, blog_title_resource_uuid, create_time FROM blog ";
    private static final String SqlGetBlogListWhere = " WHERE uuid =?";

    public static List<Blog> getBlogList(String uuid) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Blog> result = new ArrayList<Blog>();
        String sql = SqlGetBlogList;
        if (uuid != null && !"".equals(uuid)) {
            sql += SqlGetBlogListWhere;
        }
        try {
            conn = getConn();
            stmt = conn.prepareStatement(sql);
            if (uuid != null && !"".equals(uuid)) {
                stmt.setString(1, uuid);
            }
            rs = stmt.executeQuery();
            while (rs.next()) {
                Blog blog = new Blog();
                blog.setId(rs.getLong(1));
                blog.setUuid(rs.getString(2));
                blog.setUserUuid(rs.getString(3));
                blog.setBlogName(rs.getString(4));
                blog.setBlogTitleResourceUuid(rs.getString(5));
                blog.setCreateTime(timestampToDate(rs.getTimestamp(6)));
                result.add(blog);
            }
        } catch (SQLException ex) {
            log.error("Error execute sql.", ex);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return result;
    }

    private static final String SqlAddBlog = "INSERT INTO blog ( uuid, user_uuid, "
            + "blog_name, blog_title_resource_uuid, create_time ) VALUES ( "
            + "REPLACE (UUID(), '-', ''), ?, ?, ?, NOW())";

    public static boolean addBlog(Blog blog, String userUuid) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean result = false;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(SqlAddBlog);
            stmt.setString(1, userUuid);
            stmt.setString(2, blog.getBlogName());
            stmt.setString(3, blog.getBlogTitleResourceUuid());
            int ret = stmt.executeUpdate();
            result = ret == 1;
        } catch (SQLException ex) {
            log.error("Error execute sql.", ex);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return result;
    }

    private static final String SqlUpdBlogName = "UPDATE blog SET blog_name = ? WHERE uuid = ?";

    public static boolean updBlogName(String uuid, String blogName) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean result = false;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(SqlUpdBlogName);
            stmt.setString(1, blogName);
            stmt.setString(2, uuid);
            int ret = stmt.executeUpdate();
            result = ret == 1;
        } catch (SQLException ex) {
            log.error("Error execute sql.", ex);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return result;
    }

    private static final String SqlUpdBlogTitle = "UPDATE blog SET blog_title_resource_uuid = ? WHERE uuid = ?";

    public static boolean updBlogTitle(String uuid, String resFileUuid) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean result = false;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(SqlUpdBlogTitle);
            stmt.setString(1, resFileUuid);
            stmt.setString(2, uuid);
            int ret = stmt.executeUpdate();
            result = ret == 1;
        } catch (SQLException ex) {
            log.error("Error execute sql.", ex);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return result;
    }

    private static final String SqlDelBlog = "DELETE FROM blog WHERE uuid = ?";

    public static boolean delBlog(String uuid) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean result = false;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(SqlDelBlog);
            stmt.setString(1, uuid);
            int ret = stmt.executeUpdate();
            result = ret == 1;
        } catch (SQLException ex) {
            log.error("Error execute sql.", ex);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return result;
    }

    private static final String SqlGetBlogPostList = "SELECT id, uuid, blog_uuid, content, create_time FROM blog_post";
    private static final String SqlGetBlogPostListWhere1 = " WHERE uuid =?";
    private static final String SqlGetBlogPostListWhere2 = " WHERE blog_uuid =?";

    public static List<BlogPost> getBlogPostList(String uuid, String blogUuid) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<BlogPost> result = new ArrayList<BlogPost>();
        String sql = SqlGetBlogPostList;
        if (uuid != null && !"".equals(uuid)) {
            sql += SqlGetBlogPostListWhere1;
        } else if (blogUuid != null && !"".equals(blogUuid)) {
            sql += SqlGetBlogPostListWhere2;
        }
        try {
            conn = getConn();
            stmt = conn.prepareStatement(sql);
            if (uuid != null && !"".equals(uuid)) {
                stmt.setString(1, uuid);
            } else if (blogUuid != null && !"".equals(blogUuid)) {
                stmt.setString(1, blogUuid);
            }
            rs = stmt.executeQuery();
            while (rs.next()) {
                BlogPost blogPost = new BlogPost();
                blogPost.setId(rs.getLong(1));
                blogPost.setUuid(rs.getString(2));
                blogPost.setBlogUuid(rs.getString(3));
                blogPost.setContent(rs.getString(4));
                blogPost.setCreateTime(timestampToDate(rs.getTimestamp(6)));
                blogPost.setResourceFile(getBlogResFileList(rs.getString(2)));
                result.add(blogPost);
            }
        } catch (SQLException ex) {
            log.error("Error execute sql.", ex);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return result;
    }

    private static final String SqlGetBlogResFileList = "SELECT r.id, r.uuid, r.user_uuid, r.resource_type, "
            + "r.file_path, r.create_time FROM resource_file AS r JOIN blog_post_resource_map AS b "
            + "ON r.uuid = b.resource_file_uuid WHERE b.blog_post_uuid =?";

    private static List<ResourceFile> getBlogResFileList(String blogUuid) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<ResourceFile> result = new ArrayList<ResourceFile>();
        try {
            conn = getConn();
            stmt = conn.prepareStatement(SqlGetBlogResFileList);
            stmt.setString(1, blogUuid);
            rs = stmt.executeQuery();
            while (rs.next()) {
                ResourceFile resourceFile = new ResourceFile();
                resourceFile.setId(rs.getLong(1));
                resourceFile.setUuid(rs.getString(2));
                resourceFile.setUserUuid(rs.getString(3));
                resourceFile.setResourceType(rs.getInt(4));
                resourceFile.setFilePath(rs.getString(5));
                resourceFile.setCreateTime(timestampToDate(rs.getTimestamp(86)));
                result.add(resourceFile);
            }
        } catch (SQLException ex) {
            log.error("Error execute sql.", ex);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return result;
    }

    private static final String SqlAddBlogPost = "INSERT INTO blog_post ( uuid, blog_uuid, content, create_time ) "
            + "VALUES ( REPLACE (UUID(), '-', '') ,?,?, NOW())";

    public static boolean addBlogPost(BlogPost blogPost) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean result = false;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(SqlAddBlogPost);
            stmt.setString(1, blogPost.getBlogUuid());
            stmt.setString(2, blogPost.getContent());
            int ret = stmt.executeUpdate();
            result = ret == 1;
        } catch (SQLException ex) {
            log.error("Error execute sql.", ex);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return result;
    }

    private static final String SqlDelBlogPost = "DELETE FROM blog_post ";
    private static final String SqlDelBlogPostWhere1 = " WHERE uuid =?";
    private static final String SqlDelBlogPostWhere2 = " WHERE blog_uuid =?";

    public static boolean delBlogPost(String uuid, String blogUuid) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean result = false;
        String sql = SqlDelBlogPost;
        if (uuid != null && !"".equals(uuid)) {
            sql += SqlDelBlogPostWhere1;
        } else if (blogUuid != null && !"".equals(blogUuid)) {
            sql += SqlDelBlogPostWhere2;
        }
        try {
            conn = getConn();
            stmt = conn.prepareStatement(sql);
            if (uuid != null && !"".equals(uuid)) {
                stmt.setString(1, uuid);
            } else if (blogUuid != null && !"".equals(blogUuid)) {
                stmt.setString(1, blogUuid);
            }
            int ret = stmt.executeUpdate();
            result = ret >= 1;
        } catch (SQLException ex) {
            log.error("Error execute sql.", ex);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return result;
    }

    private static final String SqlGetMaxResSeqByBlogPostUUID = "SELECT MAX(resource_seq) "
            + "FROM blog_post_resource_map WHERE blog_post_uuid=?";

    private static final String SqlAddBlogPostResFile = "INSERT INTO blog_post_resource_map "
            + "( blog_post_uuid, resource_file_uuid, resource_seq ) VALUES (?,?,?)";

    public static boolean addBlogPostResFile(String blogPostUuid, List<String> resFileUuidList) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean result = false;
        int resourceSeq = 0;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(SqlGetMaxResSeqByBlogPostUUID);
            stmt.setString(1, blogPostUuid);
            rs = stmt.executeQuery();
            if (rs.next()) {
                resourceSeq = rs.getInt(1) == 0 ? 0 : rs.getInt(1) + 1;
            }
            stmt.close();
            conn.setAutoCommit(false);
            stmt = conn.prepareStatement(SqlAddBlogPostResFile);
            for (String resFileUuid : resFileUuidList) {
                stmt.setString(1, blogPostUuid);
                stmt.setString(2, resFileUuid);
                stmt.setInt(3, resourceSeq++);
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

    private static final String SQLDelBlogPostResFile = "DELETE FROM blog_post_resource_map "
            + "WHERE blog_post_uuid=? AND resource_seq=?";

    public static boolean delBlogPostResFile(String blogPostUuid, int resSeq) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean result = false;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(SQLDelBlogPostResFile);
            stmt.setString(1, blogPostUuid);
            stmt.setInt(2, resSeq);
            int ret = stmt.executeUpdate();
            result = ret == 1;
        } catch (SQLException ex) {
            log.error("Error execute sql.", ex);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return result;
    }

    private static final String SqlGetResourceFileList = "SELECT id, uuid, user_uuid, resource_type, resource_file_ext, "
            + "resouce_file_md5, file_path, create_time, request_file_size, upload_data_length, "
            + "upload_data_md5 FROM resource_file ";
    private static final String SqlGetResourceFileListWhere = " WHERE uuid =?";

    public static List<ResourceFile> getResourceFileList(String uuid) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<ResourceFile> result = new ArrayList<ResourceFile>();
        String sql = SqlGetResourceFileList;
        if (uuid != null && !"".equals(uuid)) {
            sql += SqlGetResourceFileListWhere;
        }
        try {
            conn = getConn();
            stmt = conn.prepareStatement(sql);
            if (uuid != null && !"".equals(uuid)) {
                stmt.setString(1, uuid);
            }
            rs = stmt.executeQuery();
            while (rs.next()) {
                ResourceFile resourceFile = new ResourceFile();
                resourceFile.setId(rs.getLong(1));
                resourceFile.setUuid(rs.getString(2));
                resourceFile.setUserUuid(rs.getString(3));
                resourceFile.setResourceType(rs.getInt(4));
                resourceFile.setResourceFileExt(rs.getString(5));
                resourceFile.setResouceFileMd5(rs.getString(6));
                resourceFile.setFilePath(rs.getString(7));
                resourceFile.setCreateTime(timestampToDate(rs.getTimestamp(8)));
                resourceFile.setRequestFileSize(rs.getLong(9));
                resourceFile.setUploadDataLength(rs.getLong(10));
                resourceFile.setUploadDataMd5(rs.getString(11));
                result.add(resourceFile);
            }
        } catch (SQLException ex) {
            log.error("Error execute sql.", ex);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return result;
    }

    public static ResourceFile getResourceFile(String uuid) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ResourceFile result = null;
        String sql = SqlGetResourceFileList;
        sql += SqlGetResourceFileListWhere;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, uuid);
            rs = stmt.executeQuery();
            while (rs.next()) {
                result = new ResourceFile();
                result.setId(rs.getLong(1));
                result.setUuid(rs.getString(2));
                result.setUserUuid(rs.getString(3));
                result.setResourceType(rs.getInt(4));
                result.setResourceFileExt(rs.getString(5));
                result.setResouceFileMd5(rs.getString(6));
                result.setFilePath(rs.getString(7));
                result.setCreateTime(timestampToDate(rs.getTimestamp(8)));
                result.setRequestFileSize(rs.getLong(9));
                result.setUploadDataLength(rs.getLong(10));
                result.setUploadDataMd5(rs.getString(11));
            }
        } catch (SQLException ex) {
            log.error("Error execute sql.", ex);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return result;
    }

    private static final String SqlAddResourceFile = "INSERT INTO resource_file ( uuid, user_uuid, resource_type, resource_file_ext, resouce_file_md5, "
            + "create_time, request_file_size ) VALUES ( REPLACE (UUID(), '-', ''), ?, ?, ?, ?, NOW(), ? )";
    private static final String SqlGetLastInsertUUID = "SELECT uuid FROM resource_file WHERE id = last_insert_id()";

    public static String addResourceFile(ResourceFile resourceFile) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String UUID = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(SqlAddResourceFile);
            stmt.setString(1, resourceFile.getUserUuid());
            stmt.setInt(2, resourceFile.getResourceType());
            stmt.setString(3, resourceFile.getResourceFileExt());
            stmt.setString(4, resourceFile.getResouceFileMd5());
            stmt.setLong(5, resourceFile.getRequestFileSize());
            int ret = stmt.executeUpdate();
            if (ret == 1) {
                stmt.close();
                stmt = conn.prepareStatement(SqlGetLastInsertUUID);
                rs = stmt.executeQuery();
                if (rs.next()) {
                    UUID = rs.getString(1);
                }
            } else {
                log.error("Unable to insert user.");
            }
        } catch (SQLException ex) {
            log.error("Error execute sql.", ex);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return UUID;
    }

    private static final String SqlUpdResourceFile = "UPDATE resource_file SET file_path = ?, upload_data_length = ?, "
            + "upload_data_md5 = ?  WHERE uuid =?";

    public static boolean updResourceFile(ResourceFile resourceFile) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean result = false;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(SqlUpdResourceFile);
            stmt.setString(1, resourceFile.getFilePath());
            stmt.setLong(2, resourceFile.getUploadDataLength());
            stmt.setString(3, resourceFile.getUploadDataMd5());
            stmt.setString(4, resourceFile.getUuid());
            int ret = stmt.executeUpdate();
            result = ret == 1;
        } catch (SQLException ex) {
            log.error("Error execute sql.", ex);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return result;
    }

    private static final String SQLDelResourceFile = "DELETE FROM resource_file WHERE file_path = ?";

    public static boolean delResourceFile(String path) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean result = false;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(SQLDelResourceFile);
            stmt.setString(1, path);
            int ret = stmt.executeUpdate();
            result = ret == 1;
        } catch (SQLException ex) {
            log.error("Error execute sql.", ex);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return result;
    }

    private static final String SqlGetResourceFilePathByUUID = "SELECT file_path FROM resource_file WHERE uuid =?";

    public static String getResourceFilePathByUUID(String uuid) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String path = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(SqlGetResourceFilePathByUUID);
            stmt.setString(1, uuid);
            rs = stmt.executeQuery();
            if (rs.next()) {
                path = rs.getString(1);
            }
        } catch (SQLException ex) {
            log.error("Error execute sql.", ex);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return path;
    }

    private static final String SqlGetAppUserList = "SELECT id, uuid, phone, "
            + "email, email_status, `password`, nick_name, reg_time FROM app_user";
    private static final String SqlGetAPPUserListWhere = " WHERE uuid = ?";
    private static final String SqlGetAPPUserWhere = " WHERE phone = ? AND `password`= ?";
    private static final String SqlGetAPPUserOrder = " ORDER BY CONVERT (`nick_name` USING gbk)";

    public static List<AppUser> getAppUserList(String uuid) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = SqlGetAppUserList;
        if (uuid != null && !"".equals(uuid)) {
            sql += SqlGetAPPUserListWhere;
        }
        sql += SqlGetAPPUserOrder;
        List<AppUser> result = new ArrayList<AppUser>();
        try {
            conn = getConn();
            stmt = conn.prepareStatement(sql);
            if (uuid != null && !"".equals(uuid)) {
                stmt.setString(1, uuid);
            }
            rs = stmt.executeQuery();
            while (rs.next()) {
                AppUser user = new AppUser();
                user.setId(rs.getInt(1));
                user.setUuid(rs.getString(2));
                user.setPhone(rs.getString(3));
                user.setEmail(rs.getString(4));
                user.setEmailStatus(rs.getInt(5));
                user.setPassword(null);
                user.setNickName(rs.getString(7));
                user.setNameFirstLetter();
                user.setRegTime(timestampToDate(rs.getTimestamp(8)));
                result.add(user);
            }
        } catch (SQLException ex) {
            log.error("Error execute sql.", ex);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return result;
    }

    public static AppUser getAppUserByPhone(String phone, String password) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = SqlGetAppUserList;
        sql += SqlGetAPPUserWhere;
        AppUser result = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, phone);
            stmt.setString(2, password);
            rs = stmt.executeQuery();
            if (rs.next()) {
                result = new AppUser();
                result.setId(rs.getInt(1));
                result.setUuid(rs.getString(2));
                result.setPhone(rs.getString(3));
                result.setEmail(rs.getString(4));
                result.setEmailStatus(rs.getInt(5));
                result.setPassword(rs.getString(6));
                result.setNickName(rs.getString(7));
                result.setNameFirstLetter();
                result.setRegTime(timestampToDate(rs.getTimestamp(8)));
            }
        } catch (SQLException ex) {
            log.error("Error execute sql.", ex);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return result;
    }

    private static final String SqlGetBlindSearchAppUserList = "SELECT id, uuid, phone, email, email_status, `password`, nick_name, reg_time FROM app_user "
            + "WHERE phone LIKE ? OR email LIKE ? OR nick_name LIKE ?";

    public static List<AppUser> getBlindSearchAppUserList(String search) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<AppUser> result = new ArrayList<AppUser>();
        try {
            conn = getConn();
            stmt = conn.prepareStatement(SqlGetBlindSearchAppUserList);
            stmt.setString(1, "%" + search + "%");
            stmt.setString(2, "%" + search + "%");
            stmt.setString(3, "%" + search + "%");
            rs = stmt.executeQuery();
            while (rs.next()) {
                AppUser user = new AppUser();
                user.setId(rs.getInt(1));
                user.setUuid(rs.getString(2));
                user.setPhone(rs.getString(3));
                user.setEmail(rs.getString(4));
                user.setEmailStatus(rs.getInt(5));
                user.setPassword(rs.getString(6));
                user.setNickName(rs.getString(7));
                user.setNameFirstLetter();
                user.setRegTime(timestampToDate(rs.getTimestamp(8)));
                result.add(user);
            }
        } catch (SQLException ex) {
            log.error("Error execute sql.", ex);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return result;
    }

    private static final String SqlAddAppUser = "INSERT INTO app_user ( uuid, phone, email, email_status, `password`, nick_name, reg_time ) "
            + "VALUES ( REPLACE (UUID(), '-', ''), ?, ?, ?, ?, ?, NOW())";

    public static boolean addAppUser(AppUser appUser) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean result = false;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(SqlAddAppUser);
            stmt.setString(1, appUser.getPhone());
            stmt.setString(2, appUser.getEmail());
            stmt.setInt(3, appUser.getEmailStatus());
            stmt.setString(4, appUser.getPassword());
            stmt.setString(5, appUser.getNickName());
            int ret = stmt.executeUpdate();
            if (ret == 1) {
                result = true;
            } else {
                log.error("Unable to insert user.");
            }
        } catch (SQLException ex) {
            log.error("Error execute sql.", ex);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return result;
    }

    private static final String SqlDelAppUser = "DELETE FROM app_user WHERE uuid=?";

    public static boolean delAppUser(String uuid) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean result = false;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(SqlDelAppUser);
            stmt.setString(1, uuid);
            int ret = stmt.executeUpdate();
            result = ret == 1;
        } catch (SQLException ex) {
            log.error("Error execute sql.", ex);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return result;
    }

    private static final String SqlUpdAppUserPassword = "UPDATE app_user SET `password` = ? "
            + "WHERE uuid = ? AND `password` = ?";

    public static boolean updAppUserPassword(String uuid, String oldw, String newPw) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean result = false;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(SqlUpdAppUserPassword);
            stmt.setString(1, newPw);
            stmt.setString(2, uuid);
            stmt.setString(3, oldw);
            int ret = stmt.executeUpdate();
            result = ret == 1;
        } catch (SQLException ex) {
            log.error("Error execute sql.", ex);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return result;
    }

    public static void main(String[] args) {
        ResourceFile file = new ResourceFile();
        file.setUuid("8144737c73c511e58c555cb901fc8242");
        file.setFilePath("D:\\voice");
        file.setUploadDataLength(1000);
        file.setResouceFileMd5(null);
        System.out.println(updResourceFile(file));
    }

    private static final String SqlAddTrackPoint = "INSERT INTO trackpoint "
            + "(GpsTime, Lon, Lat, SysTime, Speed, Angle, Altitude, Accuracy, Type ) VALUES (?,?,?,?,?,?,?,?,?)";

    public static boolean addTrackPoint(TrackPoint trackPoint) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean result = false;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(SqlAddTrackPoint);
            stmt.setTimestamp(1, dateToTimestamp(trackPoint.getGpsTime()));
            stmt.setDouble(2, trackPoint.getLon());
            stmt.setDouble(3, trackPoint.getLat());
            stmt.setTimestamp(4, dateToTimestamp(trackPoint.getSysTime()));
            stmt.setFloat(5, trackPoint.getSpeed());
            stmt.setInt(6, trackPoint.getAngle());
            stmt.setFloat(7, trackPoint.getAltitude());
            stmt.setFloat(8, trackPoint.getAccuracy());
            stmt.setInt(9, trackPoint.getType());
            int ret = stmt.executeUpdate();
            result = ret == 1;
        } catch (SQLException ex) {
            log.error("Error execute sql.", ex);
        }
        DBUtil.safeClose(conn, stmt);
        return result;
    }

    private static Timestamp dateToTimestamp(java.util.Date date) {
        if (date == null)
            return null;
        return new Timestamp(date.getTime());
    }

}
