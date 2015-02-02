package com.hc.nmj.base;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

public class GetNetData {
    
    // get形式
    public static String getResultForHttpGet(String url)
            throws ClientProtocolException, IOException {
        String result = "";

        HttpGet httpGet = new HttpGet(url);// 编者按：与HttpPost区别所在，这里是将参数在地址中传递
        HttpResponse response = new DefaultHttpClient().execute(httpGet);
        if (response.getStatusLine().getStatusCode() == 200) {
            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity, HTTP.UTF_8);
        }
        return result;
    }
    
}
