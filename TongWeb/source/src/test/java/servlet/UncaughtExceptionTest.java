package servlet;

import java.util.Date;

import junit.framework.Assert;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kh.tong.web.bean.UncaughtExceptionData;
import com.meterware.httpunit.PostMethodWebRequest;
import com.meterware.httpunit.WebRequest;

public class UncaughtExceptionTest {
    public static final String SERVLET_URL = "servlet/other";
    public static final String URL = HttpUnitTest.BASIC_URL + SERVLET_URL;

    @Test
    public void testGetUncaughtException() {
        WebRequest req = new PostMethodWebRequest(URL);
        req.setParameter("act", "query_exception");
        req.setParameter("starttime", "20151212000000");
        req.setParameter("endtime", "20151216000000");
        JSONArray data = HttpUnitTest.testDataJSONArray(req);
        JSONObject jsonObject = data.getJSONObject(0);
        String deviceId = jsonObject.getString("deviceId");
        Assert.assertEquals("2", deviceId);
    }

    @Test
    public void testInsertUncaughtException() {
        WebRequest req = new PostMethodWebRequest(URL);
        req.setParameter("act", "report_exception");
        UncaughtExceptionData exception = new UncaughtExceptionData();
        exception.setDeviceId("3");
        exception.setData("data test");
        exception.setLanguage("English");
        exception.setType(1);
        exception.setSystemVersion("1");
        exception.setVersion("1");
        exception.setTime(new Date());
        Gson gson = new GsonBuilder().setDateFormat("yyyyMMddHHmmss").create();
        String exceptionStr = gson.toJson(exception);
        req.setParameter("exception", exceptionStr);
        HttpUnitTest.testPostMethod(req);
    }
}
