package com.kh.tong.web.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.kh.db.ConnectionProvider;
import com.kh.db.DBUtil;
import com.kh.tong.web.bean.PhoneReg;
import com.kh.tong.web.bean.ResourceFile;
import com.kh.tong.web.bean.User;
import com.kh.tong.web.bean.UserInfo;
import com.kh.tong.web.servlet.helper.Util;

public class UserDBManger {
    static Logger log = Logger.getLogger(UserDBManger.class);

    private static ConnectionProvider conf = null;

    protected UserDBManger() {
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

    private static final String SqlAddPhoneReg = "INSERT INTO phone_reg ( uuid, phone, verify_code, valid_time, "
            + "create_time ) VALUES (?,?,?,?,?)";

    public static boolean addPhoneReg(PhoneReg phoneReg) {
        boolean result = false;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(SqlAddPhoneReg);
            stmt.setString(1, phoneReg.getUuid());
            stmt.setString(2, phoneReg.getPhone());
            stmt.setString(3, phoneReg.getVerifyCode());
            stmt.setTimestamp(4, Util.dateToTimestamp(phoneReg.getValidTime()));
            stmt.setTimestamp(5, Util.dateToTimestamp(phoneReg.getCreateTime()));
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

    // kuzou_user_ id
    private static final String SqlAddUser = "INSERT INTO `user` ( uuid, phone, `password`, icon_url, reg_time ) "
            + "VALUES (?, ?, ?, ?, NOW())";

    public static boolean addOaUser(User user) {
        boolean result = false;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(SqlAddUser);
            stmt.setString(1, user.getUuid());
            stmt.setString(2, user.getPhone());
            stmt.setString(3, user.getPassword());
            stmt.setString(4, user.getIconUUID());
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

    private static final String SqlDelOaUserByUuid = "delete from oa_user " + "where uuid = ?";

    public static boolean delOaUserByUuid(String uuid) {
        boolean result = true;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(SqlDelOaUserByUuid);
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

    private static final String SqlGetUserByPhone = "SELECT id, uuid, phone, `name`, nickname, email, "
            + "phone_login_code, phone_login_code_validtime, email_verify_code, email_verify_code_validtime, "
            + "email_verify_status, icon_url, `password`, reg_time, last_login_time FROM `user` "
            + " WHERE phone=? ORDER BY id ASC";

    public static List<User> getUserByPhone(String phone) {
        List<User> users = new ArrayList<User>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(SqlGetUserByPhone);
            stmt.setString(1, phone);
            rs = stmt.executeQuery();
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt(1));
                user.setUuid(rs.getString(2));
                user.setPhone(rs.getString(3));
                user.setName(rs.getString(4));
                user.setNickname(rs.getString(5));
                user.setEmail(rs.getString(6));
                user.setPhoneLoginCode(rs.getString(7));
                user.setPhoneLoginCodeValidtime(Util.timestampToDate(rs.getTimestamp(8)));
                user.setEmailVerifyCode(rs.getString(9));
                user.setEmailVerifyCodeValidtime(Util.timestampToDate(rs.getTimestamp(10)));
                user.setEmailVerifyCode(rs.getString(11));
                user.setPassword(rs.getString(12));
                user.setRegTime(Util.timestampToDate(rs.getTimestamp(13)));
                user.setLastLoginTime(Util.timestampToDate(rs.getTimestamp(14)));
                users.add(user);
            }
        } catch (SQLException e) {
            log.error("Error execute sql.", e);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return users;
    }

    private static final String SqlGetPhoneRegByphoneAndVerifyCode = "SELECT id, uuid, phone, verify_code, valid_time, create_time FROM phone_reg "
            + " WHERE phone=? and verify_code=? order by create_time desc";

    public static List<PhoneReg> getPhoneRegByPhoneAndVerifyCode(String phone, String code) {
        List<PhoneReg> userL = new ArrayList<PhoneReg>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(SqlGetPhoneRegByphoneAndVerifyCode);
            stmt.setString(1, phone);
            stmt.setString(2, code);
            rs = stmt.executeQuery();
            while (rs.next()) {
                PhoneReg phoneReg = new PhoneReg();
                phoneReg.setId(rs.getInt(1));
                phoneReg.setUuid(rs.getString(2));
                phoneReg.setPhone(rs.getString(3));
                phoneReg.setVerifyCode(rs.getString(4));
                phoneReg.setValidTime(Util.timestampToDate(rs.getTimestamp(5)));
                phoneReg.setCreateTime(Util.timestampToDate(rs.getTimestamp(6)));
                userL.add(phoneReg);
            }
        } catch (SQLException e) {
            log.error("Error execute sql.", e);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return userL;
    }

    private static final String SqlGetUserIdByUUID = "SELECT id FROM user " + "WHERE uuid=?";

    public static int getUserIdByUuid(String uuid) {
        int result = 0;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(SqlGetUserIdByUUID);
            stmt.setString(1, uuid);
            rs = stmt.executeQuery();
            if (rs.next()) {
                result = rs.getInt(1);
            }
        } catch (SQLException e) {
            log.error("Error execute sql.", e);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return result;
    }

    private static final String SqlUpdateOaUserByUUID = "update user set name = ? where uuid = ?";

    public static boolean updateUserNameByUuid(String username, String uuid) {
        boolean result = false;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(SqlUpdateOaUserByUUID);
            stmt.setString(1, username);
            stmt.setString(2, uuid);
            int ret = stmt.executeUpdate();
            if (ret == 1) {
                result = true;
            } else {
                log.error("Unable to update");
            }
        } catch (SQLException e) {
            log.error("Error execute sql.", e);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return result;
    }

    private static final String SqlUpdateUserByUUID = "update user set"
            + " nickname = ?,password =?, email=? where uuid = ?";

    public static boolean updateUser(User user) {
        boolean result = false;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(SqlUpdateUserByUUID);
            stmt.setString(1, user.getNickname());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getUuid());
            int ret = stmt.executeUpdate();
            if (ret == 1) {
                result = true;
            } else {
                log.error("Unable to update");
            }
        } catch (SQLException e) {
            log.error("Error execute sql.", e);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return result;
    }

    private static final String SqlUpdateUserNickNameByUUID = "update user set" + " nickname = ? where uuid = ?";

    public static boolean updateUserNickName(User user) {
        boolean result = false;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(SqlUpdateUserNickNameByUUID);
            stmt.setString(1, user.getNickname());
            stmt.setString(2, user.getUuid());
            int ret = stmt.executeUpdate();
            if (ret == 1) {
                result = true;
            } else {
                log.error("Unable to update");
            }
        } catch (SQLException e) {
            log.error("Error execute sql.", e);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return result;
    }

    private static final String SqlUpdateUserPasswordByUUID = "update user set" + " password =? where uuid = ?";

    public static boolean updateUserPassword(User user) {
        boolean result = false;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(SqlUpdateUserPasswordByUUID);
            stmt.setString(1, user.getPassword());
            stmt.setString(2, user.getUuid());
            int ret = stmt.executeUpdate();
            if (ret == 1) {
                result = true;
            } else {
                log.error("Unable to update");
            }
        } catch (SQLException e) {
            log.error("Error execute sql.", e);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return result;
    }

    private static final String SqlUpdateUserEmailByUUID = "update user set" + "email=? where uuid = ?";

    public static boolean updateUserEmail(User user) {
        boolean result = false;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(SqlUpdateUserEmailByUUID);
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getUuid());
            int ret = stmt.executeUpdate();
            if (ret == 1) {
                result = true;
            } else {
                log.error("Unable to update");
            }
        } catch (SQLException e) {
            log.error("Error execute sql.", e);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return result;
    }

    private static final String SqlUpdateUserIconByUUID = "update user set" + " icon_url = ? where uuid = ?";

    public static boolean updateUserIcon(User user) {
        boolean result = false;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(SqlUpdateUserIconByUUID);
            stmt.setString(1, user.getIconUUID());
            stmt.setString(2, user.getUuid());
            int ret = stmt.executeUpdate();
            if (ret == 1) {
                result = true;
            } else {
                log.error("Unable to update");
            }
        } catch (SQLException e) {
            log.error("Error execute sql.", e);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return result;
    }

    private static final String SqlIsUserByPhoneOk = "SELECT count(*) FROM user " + "WHERE phone=?";

    public static boolean isUserPhoneExist(String phone) {
        boolean res = false;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(SqlIsUserByPhoneOk);
            stmt.setString(1, phone);
            rs = stmt.executeQuery();
            if (rs.next()) {
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

    private static final String SqlUpdateUserLoginCode = "update user set"
            + " phone_login_code=?,phone_login_code_validtime=? where phone = ?";

    public static boolean updateUserLoginCode(String code, String phone) {
        boolean res = false;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(SqlUpdateUserLoginCode);
            stmt.setString(1, code);
            stmt.setTimestamp(2, new Timestamp(new Date().getTime()));
            stmt.setString(3, phone);
            int ret = stmt.executeUpdate();
            if (ret == 1) {
                res = true;
            } else {
                log.error("Unable to update");
            }
        } catch (SQLException e) {
            log.error("Error execute sql.", e);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return res;
    }

    private static final String SqlGetUserByPhoneAndLoginCode = "SELECT * FROM user "
            + "WHERE phone=? and phone_login_code=?";

    public static List<User> getUserByPhoneAndLoginCode(String phone, String logincode) {
        List<User> userL = new ArrayList<User>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(SqlGetUserByPhoneAndLoginCode);
            stmt.setString(1, phone);
            stmt.setString(2, logincode);
            rs = stmt.executeQuery();
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt(1));
                user.setUuid(rs.getString(2));
                user.setPhone(rs.getString(3));
                user.setName(rs.getString(4));
                user.setNickname(rs.getString(5));
                user.setEmail(rs.getString(6));
                user.setPhoneLoginCode(rs.getString(7));
                user.setPhoneLoginCodeValidtime(Util.timestampToDate(rs.getTimestamp(8)));
                user.setEmailVerifyCode(rs.getString(9));
                user.setEmailVerifyCodeValidtime(Util.timestampToDate(rs.getTimestamp(10)));
                user.setEmailVerifyStatus(rs.getInt(11));
                user.setIconUUID(rs.getString(12));
                user.setPassword("");
                user.setRegTime(Util.timestampToDate(rs.getTimestamp(14)));
                user.setLastLoginTime(Util.timestampToDate(rs.getTimestamp(15)));
                userL.add(user);
            }
        } catch (SQLException e) {
            log.error("Error execute sql.", e);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return userL;
    }

    private static final String SqlGetUserByPhoneAndPassword = "SELECT * FROM user " + "WHERE phone=? and password=?";

    public static List<User> getUserByPhoneAndPassword(String phone, String password) {
        List<User> userL = new ArrayList<User>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(SqlGetUserByPhoneAndPassword);
            stmt.setString(1, phone);
            stmt.setString(2, password);
            rs = stmt.executeQuery();
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt(1));
                user.setUuid(rs.getString(2));
                user.setPhone(rs.getString(3));
                user.setName(rs.getString(4));
                user.setNickname(rs.getString(5));
                user.setEmail(rs.getString(6));
                user.setPhoneLoginCode(rs.getString(7));
                user.setPhoneLoginCodeValidtime(Util.timestampToDate(rs.getTimestamp(8)));
                user.setEmailVerifyCode(rs.getString(9));
                user.setEmailVerifyCodeValidtime(Util.timestampToDate(rs.getTimestamp(10)));
                user.setEmailVerifyStatus(rs.getInt(11));
                user.setIconUUID(rs.getString(12));
                user.setPassword("");
                user.setRegTime(Util.timestampToDate(rs.getTimestamp(14)));
                user.setLastLoginTime(Util.timestampToDate(rs.getTimestamp(15)));
                userL.add(user);
            }
        } catch (SQLException e) {
            log.error("Error execute sql.", e);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return userL;
    }

    private static final String SqlGetUserByEmailAndPassword = "SELECT * FROM user " + "WHERE email=? and password=?";

    public static List<User> getUserByEmailAndPassword(String email, String password) {
        List<User> userL = new ArrayList<User>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(SqlGetUserByEmailAndPassword);
            stmt.setString(1, email);
            stmt.setString(2, password);
            rs = stmt.executeQuery();
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt(1));
                user.setUuid(rs.getString(2));
                user.setPhone(rs.getString(3));
                user.setName(rs.getString(4));
                user.setNickname(rs.getString(5));
                user.setEmail(rs.getString(6));
                user.setPhoneLoginCode(rs.getString(7));
                user.setPhoneLoginCodeValidtime(Util.timestampToDate(rs.getTimestamp(8)));
                user.setEmailVerifyCode(rs.getString(9));
                user.setEmailVerifyCodeValidtime(Util.timestampToDate(rs.getTimestamp(10)));
                user.setEmailVerifyStatus(rs.getInt(11));
                user.setIconUUID(rs.getString(12));
                user.setPassword("");
                user.setRegTime(Util.timestampToDate(rs.getTimestamp(14)));
                user.setLastLoginTime(Util.timestampToDate(rs.getTimestamp(15)));
                userL.add(user);
            }
        } catch (SQLException e) {
            log.error("Error execute sql.", e);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return userL;
    }

    private static final String SqlGetUserByNameAndPassword = "SELECT * FROM user " + "WHERE name=? and password=?";

    public static List<User> getUserByNameAndPassword(String name, String password) {
        List<User> users = new ArrayList<User>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(SqlGetUserByNameAndPassword);
            stmt.setString(1, name);
            stmt.setString(2, password);
            rs = stmt.executeQuery();
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt(1));
                user.setUuid(rs.getString(2));
                user.setPhone(rs.getString(3));
                user.setName(rs.getString(4));
                user.setNickname(rs.getString(5));
                user.setEmail(rs.getString(6));
                user.setPhoneLoginCode(rs.getString(7));
                user.setPhoneLoginCodeValidtime(Util.timestampToDate(rs.getTimestamp(8)));
                user.setEmailVerifyCode(rs.getString(9));
                user.setEmailVerifyCodeValidtime(Util.timestampToDate(rs.getTimestamp(10)));
                user.setEmailVerifyStatus(rs.getInt(11));
                user.setIconUUID(rs.getString(12));
                user.setPassword("");
                user.setRegTime(Util.timestampToDate(rs.getTimestamp(14)));
                user.setLastLoginTime(Util.timestampToDate(rs.getTimestamp(15)));
                users.add(user);
            }
        } catch (SQLException e) {
            log.error("Error execute sql.", e);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return users;
    }

    private static final String SqlUpdateAppUserLastLoginTime = "update user set" + " last_login_time=? where uuid = ?";

    public static boolean updateAppUserLastLoginTime(String uuid) {
        boolean res = false;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(SqlUpdateAppUserLastLoginTime);
            stmt.setTimestamp(1, Util.dateToTimestamp(new Date()));
            stmt.setString(2, uuid);
            int ret = stmt.executeUpdate();
            if (ret == 1) {
                res = true;
            } else {
                log.error("Unable to update");
            }
        } catch (SQLException e) {
            log.error("Error execute sql.", e);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return res;
    }

    private static final String SqlUpdateUserPasswordByUuid = "update user set" + " password=? where uuid = ?";

    public static boolean updateUserPasswordByUuid(String newPassword, String uuid) {
        boolean res = false;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(SqlUpdateUserPasswordByUuid);
            stmt.setString(1, newPassword);
            stmt.setString(2, uuid);
            int ret = stmt.executeUpdate();
            if (ret == 1) {
                res = true;
            } else {
                log.error("Unable to update");
            }
        } catch (SQLException e) {
            log.error("Error execute sql.", e);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return res;
    }

    private static final String sqlGetUserByphone = "SELECT uuid, `name` ,nickname ,phone ,icon_url ,last_login_time FROM `user` "
            + "WHERE phone=?";

    public static UserInfo getUserInfoByPhone(String phone) {
        UserInfo user = new UserInfo();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(sqlGetUserByphone);
            stmt.setString(1, phone);
            rs = stmt.executeQuery();
            while (rs.next()) {
                user.setUUID(rs.getString(1));
                user.setName(rs.getString(2));
                user.setNickName(rs.getString(3));
                user.setPhone(rs.getString(4));
                user.setIconUUID(rs.getString(5));
                user.setLastLoginTime(Util.timestampToDate(rs.getTimestamp(6)));
            }
        } catch (SQLException e) {
            log.error("Error execute sql.", e);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return user;
    }

    private static final String SqlGetUserByNickName = "SELECT uuid, `name` ,nickname ,phone ,icon_url ,last_login_time FROM `user` "
            + "WHERE nickname=?";

    public static List<UserInfo> getUserInfoByNickName(String username) {
        List<UserInfo> userL = new ArrayList<UserInfo>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(SqlGetUserByNickName);
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            while (rs.next()) {
                UserInfo user = new UserInfo();
                user.setUUID(rs.getString(1));
                user.setName(rs.getString(2));
                user.setNickName(rs.getString(3));
                user.setPhone(rs.getString(4));
                user.setIconUUID(rs.getString(5));
                user.setLastLoginTime(Util.timestampToDate(rs.getTimestamp(6)));
                userL.add(user);
            }
        } catch (SQLException e) {
            log.error("Error execute sql.", e);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return userL;
    }

    private static final String SqlGetPhoneRegByPhone = "SELECT * FROM phone_reg " + "WHERE uuid=? ORDER BY id ASC";

    public static List<PhoneReg> getPhoneRegByPhone(String uuid) {
        List<PhoneReg> phoneRegs = new ArrayList<PhoneReg>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(SqlGetPhoneRegByPhone);
            stmt.setString(1, uuid);
            rs = stmt.executeQuery();
            while (rs.next()) {
                PhoneReg phone = new PhoneReg();
                phone.setId(rs.getInt(1));
                phone.setUuid(rs.getString(2));
                phone.setPhone(rs.getString(3));
                phone.setVerifyCode(rs.getString(4));
                phone.setValidTime(Util.timestampToDate(rs.getTimestamp(5)));
                phone.setCreateTime(Util.timestampToDate(rs.getTimestamp(6)));
                phoneRegs.add(phone);
            }
        } catch (SQLException e) {
            log.error("Error execute sql.", e);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return phoneRegs;
    }

    private static final String SqlUpdatePhoneRegVerifyInfoByUUID = "update phone_reg set"
            + " verify_code=? and valid_time=? where uuid = ?";

    public static boolean updatePhoneRegVerifyInfoByUUID(String uuid, String verifyCode) {
        boolean res = false;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(SqlUpdatePhoneRegVerifyInfoByUUID);
            stmt.setString(1, verifyCode);
            stmt.setTimestamp(2, Util.dateToTimestamp(new Date()));
            stmt.setString(3, uuid);
            int ret = stmt.executeUpdate();
            if (ret == 1) {
                res = true;
            } else {
                log.error("Unable to update");
            }
        } catch (SQLException e) {
            log.error("Error execute sql.", e);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return res;
    }

    private static final String SqlAddResource = "INSERT INTO resource_file "
            + "(uuid,user_uuid,resource_file_ext,file_path,create_time)VALUES(?,?,?,?,NOW())";

    public static boolean addResource(String dataUUID, String userUUID, String fileType, String path) {
        boolean result = false;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(SqlAddResource);
            stmt.setString(1, dataUUID);
            stmt.setString(2, userUUID);
            stmt.setString(3, fileType);
            stmt.setString(4, path);
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

    private static final String SqlUpdateResourcePath = "UPDATE resource_file set file_path = ? where uuid=?";

    public static boolean updateResourcePath(String filePath, String uuid) {
        boolean result = false;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(SqlUpdateResourcePath);
            stmt.setString(1, filePath);
            stmt.setString(2, uuid);
            int ret = stmt.executeUpdate();
            if (ret == 1) {
                result = true;
            } else {
                log.error("Unable to update");
            }
        } catch (SQLException e) {
            log.error("Error execute sql.", e);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return result;
    }

    private static final String SqlGetFilePathByUUID = "SELECT * FROM resource_file "
            + "WHERE uuid = ? ORDER BY id ASC";

    public static ResourceFile getFilePathByUUID(String uuid) {
        ResourceFile res = new ResourceFile();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(SqlGetFilePathByUUID);
            stmt.setString(1, uuid);
            rs = stmt.executeQuery();
            if (rs.next()) {
                res.setId(rs.getLong(1));
                res.setUuid(rs.getString(2));
                res.setUserUUID(rs.getString(3));
                res.setResourceType(rs.getString(4));
                res.setFileExt(rs.getString(5));
                res.setResouceFileMd5(rs.getString(6));
                res.setFilePath(rs.getString(7));
                res.setCreateTime(rs.getTimestamp(8));
                res.setRequestFileSize(rs.getLong(9));
                res.setUploadDataLength(rs.getLong(10));
                res.setUploadDataMd5(rs.getString(11));
            }
        } catch (SQLException e) {
            log.error("Error execute sql.", e);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return res;
    }

    private static final String SqlGetIconPathByUserUUID = "SELECT icon_url FROM user "
            + "WHERE uuid = ? ORDER BY id ASC";

    public static String getIconPathByUserUUID(String uuid) {
        String res = null;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(SqlGetIconPathByUserUUID);
            stmt.setString(1, uuid);
            rs = stmt.executeQuery();
            if (rs.next()) {
                res = rs.getString(1);
            }
        } catch (SQLException e) {
            log.error("Error execute sql.", e);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return res;
    }

    private static final String SqlGetUserinfoByUUID = "SELECT uuid, `name` ,nickname ,phone ,icon_url ,last_login_time FROM `user` "
            + "WHERE uuid=?";

    public static UserInfo getUserInfoByUUID(String uuid) {
        UserInfo user = new UserInfo();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(SqlGetUserinfoByUUID);
            stmt.setString(1, uuid);
            rs = stmt.executeQuery();
            while (rs.next()) {
                user.setUUID(rs.getString(1));
                user.setName(rs.getString(2));
                user.setNickName(rs.getString(3));
                user.setPhone(rs.getString(4));
                user.setIconUUID(rs.getString(5));
                user.setLastLoginTime(Util.timestampToDate(rs.getTimestamp(6)));
            }
        } catch (SQLException e) {
            log.error("Error execute sql.", e);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return user;
    }

    private static final String SqlGetUser = "SELECT uuid, `name` ,nickname ,phone ,icon_url ,last_login_time FROM `user` "
            + "WHERE uuid=? or name =? or phone=? ";

    public static UserInfo getUserInfo(String usersearch) {
        UserInfo user = new UserInfo();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(SqlGetUser);
            stmt.setString(1, usersearch);
            stmt.setString(2, usersearch);
            stmt.setString(3, usersearch);
            rs = stmt.executeQuery();
            while (rs.next()) {
                user.setUUID(rs.getString(1));
                user.setName(rs.getString(2));
                user.setNickName(rs.getString(3));
                user.setPhone(rs.getString(4));
                user.setIconUUID(rs.getString(5));
                user.setLastLoginTime(Util.timestampToDate(rs.getTimestamp(6)));
            }
        } catch (SQLException e) {
            log.error("Error execute sql.", e);
        }
        DBUtil.safeClose(conn, stmt, rs);
        return user;
    }
}
