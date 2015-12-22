package com.kh.tong.web.servlet.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.kh.tong.term.bean.TrackPoint;
import com.kh.tong.web.bean.SessionMessage;
import com.kh.tong.web.bean.TrackProgress;
import com.kh.tong.web.bean.UncaughtExceptionData;
import com.kh.tong.web.bean.User;

public class Util {
    static Logger log = Logger.getLogger(Util.class);
    private static final String WindowsUserPath = "D:/opt";
    private static final String LinuxUserPath = "/opt/tong";
    private static final String DataDirDateTemplate = "yyyy/MM/dd/HH";
    private static final SimpleDateFormat DataDirDateFormat = new SimpleDateFormat(DataDirDateTemplate);
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

    public static final boolean isDevMode() {
        return devMode;
    }

    /**
     * 验证邮箱地址是否正确
     * 
     * @param email
     * @return
     */
    public static boolean checkEmail(String email) {
        boolean flag = false;
        try {
            String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(email);
            flag = matcher.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    /**
     * 验证手机号码
     * 
     * @param mobiles
     * @return [0-9]{5,9}
     */
    public static boolean isMobileNO(String mobiles) {
        boolean flag = false;
        try {
            Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[^1^4,\\D]))\\d{8}");
            Matcher m = p.matcher(mobiles);
            flag = m.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    public static boolean isNum(String number) {
        boolean flag = false;
        try {
            Pattern p = Pattern.compile("^[0-9]{5}$");
            Matcher m = p.matcher(number);
            flag = m.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    public static Date timestampToDate(Timestamp timestamp) {
        if (timestamp == null) {
            return null;
        }
        return new Date(timestamp.getTime());
    }

    public static User parseUser(String data) {
        User result = null;
        if (data != null) {
            try {
                JSONObject jsonObject = JSONObject.fromObject(data);
                result = (User) JSONObject.toBean(jsonObject, User.class);
            } catch (Exception e) {
                log.error("Invalid terminal Event data", e);
            }
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public static List<String> parseListString(String data) {
        List<String> result = null;
        if (data != null) {
            try {
                JSONArray jsonArray = JSONArray.fromObject(data);
                result = (List<String>) JSONArray.toCollection(jsonArray, String.class);
            } catch (Exception e) {
                log.error("Invalid List<String> data", e);
            }
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public static List<Integer> parseListInteger(String data) {
        List<Integer> result = null;
        if (data != null) {
            try {
                JSONArray jsonArray = JSONArray.fromObject(data);
                result = (List<Integer>) JSONArray.toCollection(jsonArray, Integer.class);
            } catch (Exception e) {
                log.error("Invalid List<String> data", e);
            }
        }
        return result;
    }

    public static TrackPoint parseTrackPoint(String data) {
        TrackPoint result = null;
        if (data != null) {
            try {
                Gson gson = new GsonBuilder().setDateFormat("yyyyMMddHHmmss").create();
                result = gson.fromJson(data, TrackPoint.class);
            } catch (Exception e) {
                log.error("Invalid List<String> data", e);
            }
        }
        return result;
    }

    public static SessionMessage parseSessionMessage(String data) {
        SessionMessage result = null;
        if (data != null) {
            try {
                JSONObject jsonObject = JSONObject.fromObject(data);
                result = (SessionMessage) JSONObject.toBean(jsonObject, SessionMessage.class);
            } catch (Exception e) {
                log.error("Invalid terminal Event data", e);
            }
        }
        return result;
    }

    // 新建文件夹
    public static boolean createFolder(String filename) {
        boolean result = true;
        try {
            File file = new File(filename);
            if (file.exists()) {
                result = file.isDirectory();
            } else {
                result = file.mkdirs();
            }
        } catch (Exception e) {
            log.error("error open file : " + filename, e);
            result = false;
        }
        return result;
    }

    public static boolean isExistDir(String path) {
        File file = new File(path);
        return file.exists() && file.isDirectory();
    }

    public static boolean isExistFile(String path) {
        File file = new File(path);
        return file.exists() && file.isFile();
    }

    public static void download(String filePath, String contentType, HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        response.reset();
        long pos = 0;

        OutputStream os = null;
        FileInputStream is = null;
        try {
            File downloadFile = new File(filePath);
            is = new FileInputStream(downloadFile);
            long fSize = downloadFile.length();
            byte b[] = new byte[4096];
            String fileName = URLEncoder.encode(downloadFile.getName(), "UTF-8");
            response.setContentType(contentType);
            response.setHeader("Accept-Ranges", "bytes");
            response.setHeader("Content-Length", fSize + "");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            if (request.getHeader("Range") != null) {
                // 若客户端传来Range，说明之前下载了一部分，设置206状态(SC_PARTIAL_CONTENT)
                response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
                pos = Long.parseLong(request.getHeader("Range").replaceAll("bytes=", "").replaceAll("-", ""));
            }
            if (pos != 0) {
                String contentRange = new StringBuffer("bytes ").append(new Long(pos).toString()).append("-")
                        .append(new Long(fSize - 1).toString()).append("/").append(new Long(fSize).toString())
                        .toString();
                response.setHeader("Content-Range", contentRange);
                // 略过已经传输过的字节
                is.skip(pos);
            }
            os = response.getOutputStream();
            boolean all = false;
            while (!all) {
                int size = is.read(b);
                if (size != -1) {
                    os.write(b, 0, size);
                } else {
                    all = true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        } finally {
            if (os != null)
                os.close();
            if (is != null)
                is.close();
        }
    }

    public static boolean isValidDate(String str) {
        boolean convertSuccess = true;
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        try {
            format.setLenient(false);
            format.parse(str);
        } catch (Exception e) {
            convertSuccess = false;
        }
        return convertSuccess;
    }

    public static final String generateUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static final String getUserRootDir() {
        return isDevMode() ? WindowsUserPath : LinuxUserPath;
    }

    public static final String getDateDir(Date time) {
        return DataDirDateFormat.format(time);
    }

    /*
     * 获取用户数据的绝对路径
     */
    public static final String getDataAbsolutePath(Date time, String uuid) {
        StringBuffer sb = new StringBuffer();
        sb.append(getUserRootDir());
        sb.append(File.separator);
        sb.append(getDataPath(time, uuid));
        return sb.toString();
    }

    /*
     * 获取用户数据目录的绝对路径
     */
    public static final String getDataDirAbsolutePath(Date time, String uuid) {
        StringBuffer sb = new StringBuffer();
        sb.append(getUserRootDir());
        sb.append(File.separator);
        sb.append(getDataDir(time, uuid));
        return sb.toString();
    }

    /*
     * 获取用户数据目录的相对路径
     */
    public static final String getDataDir(Date time, String uuid) {
        StringBuffer sb = new StringBuffer();
        sb.append(getDateDir(time));
        sb.append(File.separator);
        sb.append(uuid.substring(0, 3));
        return sb.toString();
    }

    /*
     * 获取用户数据的相对路径
     */
    public static final String getDataPath(Date time, String uuid) {
        StringBuffer sb = new StringBuffer();
        sb.append(getDataDir(time, uuid));
        sb.append(File.separator);
        sb.append(uuid);
        return sb.toString();
    }

    public static Timestamp dateToTimestamp(Date date) {
        if (date == null)
            return null;
        return new Timestamp(date.getTime());
    }

    public static List<Long> parseListLong(String data) {
        List<Long> result = null;
        if (data != null) {
            try {
                Gson gson = new Gson();
                result = gson.fromJson(data, new TypeToken<List<Long>>() {
                }.getType());
            } catch (Exception e) {
                log.error("Invalid List<Long> data", e);
            }
        }
        return result;
    }

    public static TrackProgress parseTrackProgress(String data) {
        TrackProgress result = null;
        if (data != null) {
            try {
                Gson gson = new Gson();
                result = gson.fromJson(data, TrackProgress.class);
            } catch (Exception e) {
                log.error("TrackProgress 数据类型转换错误！", e);
            }
        }
        return result;
    }

    public static UncaughtExceptionData parseUncaughtExceptionData(String data) {
        UncaughtExceptionData result = null;
        if (data != null) {
            try {
                Gson gson = new GsonBuilder().setDateFormat("yyyyMMddHHmmss").create();
                result = gson.fromJson(data, UncaughtExceptionData.class);
            } catch (Exception e) {
                log.error("UncaughtExceptionData 数据类型转换错误！", e);
            }
        }
        return result;
    }

    public static String listLongToString(List<Long> list) {
        if (list == null)
            return "";
        StringBuffer sb = new StringBuffer();
        for (Long l : list) {
            sb.append(l);
            if (list.indexOf(l) != (list.size() - 1)) {
                sb.append(",");
            }
        }
        return sb.toString();
    }

    public static String listStringToString(List<String> list) {
        if (list == null)
            return "";
        StringBuffer sb = new StringBuffer();
        for (String s : list) {
            sb.append(s);
            if (list.indexOf(s) != (list.size() - 1)) {
                sb.append(",");
            }
        }
        return sb.toString();
    }
}
