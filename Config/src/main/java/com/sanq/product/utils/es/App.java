package com.sanq.product.utils.es;

import com.sanq.product.utils.es.factory.EsCluster;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.Collections;

@Component
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:es/spring-es.xml"})
public class App {

    @Resource
    private EsCluster esCluster;

    @Test
    public void get() {
        try {
            esCluster.getObject().get(new GetRequest("booksadds","booksadd", "1"), RequestOptions.DEFAULT).getSource();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void check() {
        try {
            GetIndexRequest existsRequest = new GetIndexRequest("books");
            boolean exists = esCluster.getObject().indices().exists(existsRequest, RequestOptions.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void create() {
        try {
            String type = "{" +
                    "\"booksadd\": {" +
                    "}" +
                    "}";

            CreateIndexRequest createIndexRequest = new CreateIndexRequest("books")
                        .settings(Settings.builder().put("index.number_of_shards", 5).put("index.number_of_replicas", 1))
                        .mapping(type, XContentType.JSON);
            CreateIndexResponse createIndexResponse = esCluster.getObject().indices().create(createIndexRequest, RequestOptions.DEFAULT);
            System.out.println(createIndexRequest.index());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void update() {
        try {
            UpdateRequest updateRequest = new UpdateRequest("booksadds", "booksadd", "1").doc(Collections.EMPTY_MAP);
            esCluster.getObject().update(updateRequest, RequestOptions.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void delete() {
        try {
            DeleteRequest deleteRequest = new DeleteRequest("booksadds", "booksadd", "1");
            esCluster.getObject().delete(deleteRequest, RequestOptions.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void bulk() {
        try {
            BulkRequest bulkRequest = new BulkRequest();
            bulkRequest.add(new IndexRequest("booksadds", "booksadd", "1").source("json", XContentType.JSON));
            bulkRequest.add(new IndexRequest("booksadds", "booksadd", "2").source("json", XContentType.JSON));
            bulkRequest.add(new IndexRequest("booksadds", "booksadd", "3").source("json", XContentType.JSON));

            BulkResponse bulk = esCluster.getObject().bulk(bulkRequest, RequestOptions.DEFAULT);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
