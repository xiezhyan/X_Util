package com.sanq.product.utils.es.factory;

import com.sanq.product.utils.es.config.Config;
import com.sanq.product.utils.es.config.HostAndPort;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import java.util.*;

/**
 * 配置ES链接
 *
 * 官方地址
 *  https://www.elastic.co/guide/en/elasticsearch/client/java-rest/current/java-rest-overview.html
 */
public class EsCluster implements FactoryBean<RestHighLevelClient>, InitializingBean, DisposableBean {

    private Set<HostAndPort> sets;

    private RestHighLevelClient client;

    public EsCluster() {
    }

    public EsCluster(Set<HostAndPort> sets) {
        this.sets = sets;
    }

    public Set<HostAndPort> getSets() {
        return sets;
    }

    public void setSets(Set<HostAndPort> sets) {
        this.sets = sets;
    }


    @Override
    public void destroy() throws Exception {
        if (this.client != null)
            client.close();
    }

    @Override
    public RestHighLevelClient getObject() throws Exception {
        return this.client;
    }

    @Override
    public Class<?> getObjectType() {
        return EsCluster.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        initClient();
    }

    private void initClient() {
        List<HttpHost> httpHostList = new ArrayList<>(sets.size());

        sets.stream().forEach(s -> {
            httpHostList.add(new HttpHost(s.getHost(), s.getPort(), "http"));
        });
        client = new RestHighLevelClient(RestClient.builder(httpHostList.toArray(new HttpHost[httpHostList.size()])));
    }
}
