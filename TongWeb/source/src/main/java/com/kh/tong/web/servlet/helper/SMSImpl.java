package com.kh.tong.web.servlet.helper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

public class SMSImpl {
    static Logger log = Logger.getLogger(SMSImpl.class);

    /**
     * 
     * @param content
     *            发送内容（1-500 个汉字）
     * @param mobile
     *            手机号码。多个以英文逗号隔开
     * @param stime
     *            可选参数,发送时间。 填写时已填写的时间发送，不填时为当前时间发送
     * @return 是否提交成功
     */
    public static boolean sendOrdinarySMSMessages(String content, String mobile, Date time) {

        String name = "hpylsms";
        String pwd = "9FA6EB4D075A98F84F896CF0D451";
        String sign = "汉普云联";
        String stime = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (time != null) {
            stime = sdf.format(sdf.format(time));
        }
        StringBuffer sb = new StringBuffer("http://sms.1xinxi.cn/asmx/smsservice.aspx?");
        sb.append("name=" + name);
        sb.append("&pwd=" + pwd);
        sb.append("&mobile=" + mobile);
        try {
            sb.append("&content=" + URLEncoder.encode(content, "UTF-8"));
            sb.append("&stime=" + URLEncoder.encode(stime, "UTF-8"));
            sb.append("&sign=" + URLEncoder.encode(sign, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            log.error(e);
            e.printStackTrace();
        }
        sb.append("&type=pt");
        return send(sb.toString());
    }

    public static boolean send(String urlString) {
        URL url = null;
        HttpURLConnection connection = null;
        BufferedReader in = null;
        String inputline = null;
        try {
            url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            in = new BufferedReader(new InputStreamReader(url.openStream()));
            inputline = in.readLine();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        String array[] = inputline.split(",");
        if (Integer.parseInt(array[0]) == 0) {
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
    }
}
