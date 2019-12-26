package com.sanq.product.config.utils.http;

import com.sanq.product.config.utils.web.JsonUtil;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.io.InputStream;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpUtil {

    public static final String ENCODING = "utf-8";

    private static final String JSON = "JSON";
    private static final String FORM = "FORM";

    private static volatile HttpUtil instance;

    public static HttpUtil getInstance() {
        if (instance == null) {
            synchronized (HttpUtil.class) {
                if (instance == null) {
                    instance = new HttpUtil();
                }
            }
        }
        return instance;
    }

    private CloseableHttpClient mHttpClient;

    private HttpUtil() {
        try {
            mHttpClient = new SSLClient();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * get请求
     */
    public String get(String url, Map<String, String> head, Map<String, String> params, String encoding) {
        try {
            URIBuilder uriBuilder = new URIBuilder(url);
            if (params != null && !params.isEmpty()) {
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    uriBuilder.addParameter(entry.getKey(), entry.getValue());
                }
            }

            HttpGet httpGet = new HttpGet(uriBuilder.build());

            if (head != null && !head.isEmpty()) {
                head.forEach(httpGet::addHeader);
            }

            CloseableHttpResponse response = mHttpClient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                return EntityUtils.toString(response.getEntity(), encoding);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        return "";
    }

    /**
     * json
     */
    public String postJson(String url, Map<String, String> head, Map<String, Object> params, String encoding) {
        return post(url, head, params, encoding, JSON);
    }

    /**
     * form
     */
    public String postForm(String url, Map<String, String> head, Map<String, Object> params, String encoding) {
        return post(url, head, params, encoding, FORM);
    }

    /**
     * post
     */
    private String post(String url, Map<String, String> head, Map<String, Object> params, String encoding, String type) {
        try {
            HttpPost httpPost = new HttpPost(url);

            if (head != null && !head.isEmpty()) {
                head.forEach(httpPost::addHeader);
            }

            if (params != null && !params.isEmpty()) {
                StringEntity entity = null;
                switch (type) {
                    case JSON:
                        entity = new StringEntity(JsonUtil.obj2Json(params), ENCODING);
                        entity.setContentType("application/json");
                        break;
                    case FORM:
                        List<NameValuePair> nameValuePairs = new ArrayList<>(params.size());
                        params.forEach((key, value) -> nameValuePairs.add(new BasicNameValuePair(key, value.toString())));
                        entity = new UrlEncodedFormEntity(nameValuePairs, encoding);
                        break;
                }
                httpPost.setEntity(entity);
            }


            CloseableHttpResponse response = mHttpClient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                return EntityUtils.toString(response.getEntity(), encoding);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        return "";
    }

    /**
     * 返回流
     */
    public InputStream getInputStream(String url, Map<String, String> head) {
        HttpGet get = new HttpGet(url);
        if (head != null && !head.isEmpty()) {
            head.forEach(get::addHeader);
        }

        try {
            HttpResponse response = mHttpClient.execute(get);

            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                return response.getEntity().getContent();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

class SSLClient extends DefaultHttpClient {
    public SSLClient() throws Exception {
        super();
        SSLContext ctx = SSLContext.getInstance("TLS");
        X509TrustManager tm = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };
        ctx.init(null, new TrustManager[]{tm}, null);
        SSLSocketFactory ssf = new SSLSocketFactory(ctx, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        ClientConnectionManager ccm = this.getConnectionManager();
        SchemeRegistry sr = ccm.getSchemeRegistry();
        sr.register(new Scheme("https", 443, ssf));
    }
}
