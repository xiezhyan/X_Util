package com.sanq.product.config.utils.http;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
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
import java.net.URISyntaxException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpUtil {

    private static HttpUtil instance;

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
     * httpClient 发送get请求
     *
     * @param url
     * @param head
     * @param params
     * @return
     */
    public String get(String url, Map<String, String> head, Map<String, String> params) {
        try {
            URIBuilder uriBuilder = new URIBuilder(url);
            if (params != null && !params.isEmpty()) {
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    uriBuilder.addParameter(entry.getKey(), entry.getValue());
                }
            }

            HttpGet httpGet = new HttpGet(uriBuilder.build());

            if (head != null && !head.isEmpty()) {
                for (Map.Entry<String, String> entry : head.entrySet()) {
                    httpGet.addHeader(entry.getKey(), entry.getValue());
                }
            }

            CloseableHttpResponse response = mHttpClient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == 200) {
                return EntityUtils.toString(response.getEntity(), "utf-8");
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return "";
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            return "";
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
        return "";
    }

    /**
     * httpClient 发送get请求
     *
     * @param url
     * @param head
     * @return
     */
    public String get(String url, Map<String, String> head) {
        try {

            HttpGet httpGet = new HttpGet(url);

            if (head != null && !head.isEmpty()) {
                for (Map.Entry<String, String> entry : head.entrySet()) {
                    httpGet.addHeader(entry.getKey(), entry.getValue());
                }
            }

            CloseableHttpResponse response = mHttpClient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == 200) {
                return EntityUtils.toString(response.getEntity(), "utf-8");
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            return "";
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
        return "";
    }
    /**
     * post
     *
     * @param url
     * @param head
     * @param params
     * @return
     */
    public String post(String url, Map<String, String> head, Map<String, String> params) {
        try {
            HttpPost httpPost = new HttpPost(url);

            List<NameValuePair> nameValuePairs = new ArrayList<>();
            if (params != null && !params.isEmpty()) {
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    nameValuePairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                }
                StringEntity entity = new UrlEncodedFormEntity(nameValuePairs, "utf-8");
                httpPost.setEntity(entity);
            }

            if (head != null && !head.isEmpty()) {
                for (Map.Entry<String, String> entry : head.entrySet()) {
                    httpPost.addHeader(entry.getKey(), entry.getValue());
                }
            }

            CloseableHttpResponse response = mHttpClient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == 200) {
                return EntityUtils.toString(response.getEntity(), "utf-8");
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            return "";
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
        return "";
    }

    public InputStream downloadImg(String url) {
        HttpGet get = new HttpGet(url);
        HttpGet httpget = new HttpGet(url);

        httpget.setHeader(
                "User-Agent",
                "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/21.0.1180.79 Safari/537.1");
        httpget.setHeader("Accept",
                "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");

        try {
            HttpResponse response = mHttpClient.execute(get);

            if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
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
