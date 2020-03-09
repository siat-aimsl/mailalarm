package org.jmqtt.client;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jmqtt.model.response.ActuatorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class IHttpClient {
    private static final Logger LOG = LoggerFactory.getLogger(IHttpClient.class);

    public static ActuatorResponse get(String url) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        ActuatorResponse actuatorResponse = null;

        try {
            // 创建httpget.
            HttpGet httpget = new HttpGet(url);
            LOG.info("executing request " + httpget.getURI());
            // 执行get请求.
            CloseableHttpResponse response = httpclient.execute(httpget);
            try {
                // 获取响应实体
                HttpEntity entity = response.getEntity();
                LOG.info("--------------------------------------");
                // 打印响应状态
                LOG.info(String.valueOf(response.getStatusLine()));
                if (entity != null) {
                    // 打印响应内容
                    String content = EntityUtils.toString(entity);
                    LOG.info("Response content: " + content);
                    actuatorResponse = new JSONObject().parseObject(content,ActuatorResponse.class);
                }
                LOG.info("------------------------------------");
            } finally {
                response.close();
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭连接,释放资源
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return actuatorResponse;
    }

}
