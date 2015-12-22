package servlet;

import junit.framework.Assert;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebRequest;
import com.meterware.httpunit.WebResponse;

public class HttpUnitTest {
    public static final String BASIC_URL = "http://localhost:8080/tong/";

    public static void testPostMethod(WebRequest req) {
        WebConversation wc = new WebConversation();
        JSONObject json = null;
        WebResponse resp;
        try {
            resp = wc.getResponse(req);
            System.out.println(resp.getText());
            json = JSONObject.fromObject(resp.getText());
        } catch (Exception e) {
            e.printStackTrace();
        }
        int responseCode = json.getInt("responseCode");
        Assert.assertEquals(100, responseCode);
    }

    public static JSONArray testDataJSONArray(WebRequest req) {
        WebConversation wc = new WebConversation();
        JSONObject json = null;
        WebResponse resp;
        try {
            resp = wc.getResponse(req);
            System.out.println(resp.getText());
            json = JSONObject.fromObject(resp.getText());
        } catch (Exception e) {
            e.printStackTrace();
        }
        int responseCode = json.getInt("responseCode");
        Assert.assertEquals(100, responseCode);
        JSONArray data = json.getJSONArray("data");
        return data;
    }

    public static JSONObject testDataJSONObject(WebRequest req) {
        WebConversation wc = new WebConversation();
        JSONObject json = null;
        WebResponse resp;
        try {
            resp = wc.getResponse(req);
            System.out.println(resp.getText());
            json = JSONObject.fromObject(resp.getText());
        } catch (Exception e) {
            e.printStackTrace();
        }
        int responseCode = json.getInt("responseCode");
        Assert.assertEquals(100, responseCode);
        JSONObject data = json.getJSONObject("data");
        return data;
    }
}
