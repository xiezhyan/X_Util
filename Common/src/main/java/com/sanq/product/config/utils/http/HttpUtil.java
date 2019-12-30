package com.sanq.product.config.utils.http;

import com.sanq.product.config.utils.web.JsonUtil;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.omg.PortableInterceptor.INACTIVE;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
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
            mHttpClient = HttpFactory.HttpInstance.INSTANCE.getInstance().getHttpClient();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * get请求
     */
    public synchronized String get(String url, Map<String, String> head, Map<String, String> params, String encoding) {
        CloseableHttpResponse response = null;
        HttpGet httpGet = null;
        try {
            URIBuilder uriBuilder = new URIBuilder(url);
            if (params != null && !params.isEmpty()) {
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    uriBuilder.addParameter(entry.getKey(), entry.getValue());
                }
            }

            httpGet = new HttpGet(uriBuilder.build());

            if (head != null && !head.isEmpty()) {
                head.forEach(httpGet::addHeader);
            }

            response = mHttpClient.execute(httpGet);
            if (response != null) {
                return EntityUtils.toString(response.getEntity(), encoding);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        } finally {
            release(response, httpGet);
        }
        return "";
    }

    private void release(CloseableHttpResponse response, HttpRequestBase http) {
        if (response != null) {
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (http != null)
            http.releaseConnection();


    }

    /**
     * json
     */
    public synchronized String postJson(String url, Map<String, String> head, Map<String, Object> params, String encoding) {
        return post(url, head, params, encoding, JSON);
    }

    /**
     * form
     */
    public synchronized String postForm(String url, Map<String, String> head, Map<String, Object> params, String encoding) {
        return post(url, head, params, encoding, FORM);
    }

    /**
     * post
     */
    private String post(String url, Map<String, String> head, Map<String, Object> params, String encoding, String type) {
        HttpPost httpPost = null;
        CloseableHttpResponse response = null;
        try {
            httpPost = new HttpPost(url);

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


            response = mHttpClient.execute(httpPost);
            if (response != null) {
                return EntityUtils.toString(response.getEntity(), encoding);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        } finally {
            release(response, httpPost);
        }
        return "";
    }

    /**
     * 返回流
     */
    public synchronized InputStream getInputStream(String url, Map<String, String> head) {
        HttpGet get = null;
        CloseableHttpResponse response = null;
        try {
            get = new HttpGet(url);
            if (head != null && !head.isEmpty()) {
                head.forEach(get::addHeader);
            }

            response = mHttpClient.execute(get);

            if (response != null) {
                return response.getEntity().getContent();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            release(response, get);
        }
        return null;
    }

    /**
     * 上传文件
     */
    public synchronized String uploadFile(String url,
                                          Map<String, String> headMap,
                                          Map<String, Object> paramMap,     //额外参数
                                          Map<String, File> fileMap, //文件传输
                                          String encoding) {
        CloseableHttpResponse response = null;
        HttpPost httpPost = null;

        try {

            httpPost = new HttpPost(url);

            if (headMap != null && !headMap.isEmpty()) {
                headMap.forEach(httpPost::addHeader);
            }

            //文件参数
            MultipartEntityBuilder builder = MultipartEntityBuilder
                    .create()
                    .setCharset(Consts.UTF_8)
                    .setMode(HttpMultipartMode.BROWSER_COMPATIBLE);//加上此行代码解决返回中文乱码问题

            if (fileMap != null && !fileMap.isEmpty()) {
                fileMap.forEach((key, value) -> {
                    builder.addPart(key,
                            new FileBody(value, ContentType.create("multipart/form-data", Consts.UTF_8), value.getName()));
                });
            }

            if (paramMap != null && !paramMap.isEmpty()) {
                paramMap.forEach((key, value) -> {
                    builder.addTextBody(key, value.toString(), ContentType.create("text/plain", Consts.UTF_8));
                });
            }

            HttpEntity httpEntity = builder.build();

            httpPost.setEntity(httpEntity);

            response = mHttpClient.execute(httpPost);

            if (response != null) {
                return EntityUtils.toString(response.getEntity(), encoding);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        } finally {
            release(response, httpPost);
        }
        return "";
    }
}

//class SSLClient extends DefaultHttpClient {
//    public SSLClient() throws Exception {
//        super();
//        SSLContext ctx = SSLContext.getInstance("TLS");
//        X509TrustManager tm = new X509TrustManager() {
//            @Override
//            public void checkClientTrusted(X509Certificate[] chain,
//                                           String authType) throws CertificateException {
//            }
//
//            @Override
//            public void checkServerTrusted(X509Certificate[] chain,
//                                           String authType) throws CertificateException {
//            }
//
//            @Override
//            public X509Certificate[] getAcceptedIssuers() {
//                return null;
//            }
//        };
//        ctx.init(null, new TrustManager[]{tm}, null);
//        SSLSocketFactory ssf = new SSLSocketFactory(ctx, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
//        ClientConnectionManager ccm = this.getConnectionManager();
//        SchemeRegistry sr = ccm.getSchemeRegistry();
//        sr.register(new Scheme("https", 443, ssf));
//    }
//}

class HttpFactory {

    enum HttpInstance {
        INSTANCE;

        private HttpFactory instance;

        HttpInstance() {
            instance = new HttpFactory();
        }

        public HttpFactory getInstance() {
            return instance;
        }
    }

    public CloseableHttpClient getHttpClient() throws Exception {
        // 在调用SSL之前需要重写验证方法，取消检测SSL
        X509TrustManager trustManager = new X509TrustManager() {
            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            @Override
            public void checkClientTrusted(X509Certificate[] xcs, String str) {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] xcs, String str) {
            }
        };

        SSLContext ctx = SSLContext.getInstance(SSLConnectionSocketFactory.TLS);
        ctx.init(null, new TrustManager[]{trustManager}, null);
        SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(ctx, NoopHostnameVerifier.INSTANCE);

        // 创建Registry
        RequestConfig requestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD_STRICT)
                .setExpectContinueEnabled(Boolean.TRUE).setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM, AuthSchemes.DIGEST))
                .setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC)).build();
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", socketFactory).build();

        // 创建ConnectionManager，添加Connection配置信息
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        return HttpClients.custom().setConnectionManager(connectionManager)
                .setDefaultRequestConfig(requestConfig).build();
    }
}