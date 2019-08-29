package com.sanq.product.utils.es.support.impl;

import com.sanq.product.config.utils.entity.Pager;
import com.sanq.product.config.utils.entity.Pagination;
import com.sanq.product.config.utils.string.StringUtil;
import com.sanq.product.config.utils.web.GlobalUtil;
import com.sanq.product.config.utils.web.JsonUtil;
import com.sanq.product.utils.es.support.BaseSearchSupport;
import com.sun.corba.se.spi.ior.ObjectKey;
import com.sun.deploy.cache.InMemoryLocalApplicationProperties;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Assert;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;

@Component
public class BaseSearchSupportImpl<T extends Serializable> implements BaseSearchSupport<T> {

    @Resource
    private RestHighLevelClient restClient;

    private Class<T> getGenericClass() {
        ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();
        return (Class<T>) parameterizedType.getActualTypeArguments()[0];
    }

    @Override
    public boolean check(String index) {
        GetIndexRequest getIndexRequest = new GetIndexRequest(index);
        try {
            return restClient.indices().exists(getIndexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 创建索引
     *
     * @param index
     * @param type
     * @return
     */
    @Override
    public boolean createIndex(String index, String type) {
        CreateIndexRequest createIndexRequest = new CreateIndexRequest(index)
                .settings(Settings.builder().put("index.number_of_shards", 5).put("index.number_of_replicas", 1))
                .mapping(type, XContentType.JSON);

        try {
            CreateIndexResponse createIndexResponse = restClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);
            return createIndexResponse.isAcknowledged() && createIndexResponse.isShardsAcknowledged();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * 通过ID获取详情
     *
     * @param index
     * @param type
     * @param id
     * @return
     */
    @Override
    public T findById(String index, String type, String id) {
        GetRequest getRequest = new GetRequest(index, type, id);
        try {
            GetResponse getResponse = restClient.get(getRequest, RequestOptions.DEFAULT);
            return JsonUtil.json2Obj(getResponse.getSourceAsString(), getGenericClass());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 保存
     *
     * @param index
     * @param type
     * @param entity
     */
    @Override
    public String save(String index, String type, T entity) {

        Map<String, Object> map = GlobalUtil.bean2Map(entity);

        IndexRequest indexRequest = new IndexRequest(index, type, map.get("id").toString()).source(map).opType(DocWriteRequest.OpType.CREATE);
        try {
            IndexResponse indexResponse = restClient.index(indexRequest, RequestOptions.DEFAULT);

            return indexResponse.getId();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 批量保存
     *
     * @param index
     * @param type
     * @param entityList
     */
    @Override
    public boolean saveList(String index, String type, List<T> entityList) {
        BulkRequest bulkRequest = new BulkRequest();

        entityList.stream().forEach(entity -> {
            Map<String, Object> map = GlobalUtil.bean2Map(entity);
            bulkRequest.add(new IndexRequest(index, type, map.get("id").toString()).source(map).opType(DocWriteRequest.OpType.CREATE));
        });

        try {
            restClient.bulk(bulkRequest, RequestOptions.DEFAULT);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 修改
     *
     * @param index
     * @param type
     * @param entity
     * @return
     */
    @Override
    public boolean update(String index, String type, T entity){
        Map<String, Object> map = GlobalUtil.bean2Map(entity);

        UpdateRequest request = new UpdateRequest(index, type, map.get("id").toString());
        request.doc(map);

        try {
            restClient.update(request, RequestOptions.DEFAULT);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 删除
     *
     * @param index
     * @param type
     * @param id
     * @return
     */
    @Override
    public boolean delete(String index, String type, String id) {
        DeleteRequest deleteRequest = new DeleteRequest(index, type, id);

        try {
            DeleteResponse delete = restClient.delete(deleteRequest, RequestOptions.DEFAULT);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * 批量删除
     *
     * @param index
     * @param type
     * @param ids
     * @return
     */
    @Override
    public boolean deleteList(String index, String type, List<String> ids) {
        BulkRequest bulkRequest = new BulkRequest();

        ids.stream().forEach(id -> {
            bulkRequest.add(new DeleteRequest(index, type, id));
        });

        try {
            restClient.bulk(bulkRequest, RequestOptions.DEFAULT);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * 删除 索引
     *
     * @param index
     * @return
     */
    @Override
    public boolean deleteIndex(String index) {
        try {
            AcknowledgedResponse delete = restClient.indices().delete(new DeleteIndexRequest(index), RequestOptions.DEFAULT);

            return delete.isAcknowledged();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
