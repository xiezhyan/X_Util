package com.sanq.product.utils.es.support.impl;

import com.sanq.product.config.utils.entity.Pager;
import com.sanq.product.config.utils.entity.Pagination;
import com.sanq.product.config.utils.string.StringUtil;
import com.sanq.product.config.utils.web.GlobalUtil;
import com.sanq.product.config.utils.web.JsonUtil;
import com.sanq.product.utils.es.support.BaseSearchSupport;
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
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
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
            try {
                Map<String, Object> map = GlobalUtil.bean2Map(entity);
                try {
                    String id = String.valueOf(map.get("id"));

                    bulkRequest.add(new IndexRequest(index, type, id).source(JsonUtil.obj2Json(map), XContentType.JSON));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        try {
            BulkResponse bulk = restClient.bulk(bulkRequest, RequestOptions.DEFAULT);
            return bulk.status().getStatus() == RestStatus.OK.getStatus();
        } catch (Exception e) {
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
    public boolean update(String index, String type, T entity) {
        Map<String, Object> map = GlobalUtil.bean2Map(entity);

        UpdateRequest request = new UpdateRequest(index, type, map.get("id").toString());
        request.doc(map);

        try {
            UpdateResponse update = restClient.update(request, RequestOptions.DEFAULT);
            return update.status().getStatus() == RestStatus.OK.getStatus();
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
            return delete.status().getStatus() == RestStatus.OK.getStatus();
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
            BulkResponse bulk = restClient.bulk(bulkRequest, RequestOptions.DEFAULT);
            return bulk.status().getStatus() == RestStatus.OK.getStatus();
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

    @Override
    public T findById(String id) {
        return findById(getGenericClass().getSimpleName(), getGenericClass().getSimpleName(), id);
    }

    @Override
    public String save(T entity) {
        return save(getGenericClass().getSimpleName(), getGenericClass().getSimpleName(), entity);
    }

    @Override
    public boolean saveList(List<T> entityList) {
        return saveList(getGenericClass().getSimpleName(), getGenericClass().getSimpleName(), entityList);
    }

    @Override
    public boolean update(T entity) {
        return update(getGenericClass().getSimpleName(), getGenericClass().getSimpleName(), entity);
    }

    @Override
    public boolean delete(String id) {
        return delete(getGenericClass().getSimpleName(), getGenericClass().getSimpleName(), id);
    }

    @Override
    public boolean deleteList(List<String> ids) {
        return deleteList(getGenericClass().getSimpleName(), getGenericClass().getSimpleName(), ids);
    }

    /**
     * 查询数据 分页
     *
     * @param index
     * @param type
     * @param entity
     * @param pagination
     * @return
     */
    @Override
    public Pager<T> findListByPager(String index, String type, T entity, Pagination pagination) {
        Map<String, Object> map = GlobalUtil.bean2Map(entity);

        pagination.setTotalCount(findListCount(index, type, map));

        SearchRequest searchRequest = new SearchRequest(index).types(type);

        SearchSourceBuilder sourceBuilder = getSearchRequest(map);

        sourceBuilder.from(pagination.getStartPage());
        sourceBuilder.size(pagination.getPageSize());
        searchRequest.source(sourceBuilder);

        try {
            SearchResponse searchResponse = restClient.search(searchRequest, RequestOptions.DEFAULT);

            List<T> data = new ArrayList<>(pagination.getPageSize());

            for (SearchHit hits : searchResponse.getHits().getHits()) {
                data.add(JsonUtil.json2Obj(hits.getSourceAsString(), getGenericClass()));
            }
            return new Pager<T>(pagination, data);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Pager<T> findListByPager(T entity, Pagination pagination) {
        return findListByPager(getGenericClass().getSimpleName(), getGenericClass().getSimpleName(), entity, pagination);
    }

    /**
     * 获取到总条数
     *
     * @param index
     * @param type
     * @param map
     * @return
     */
    private int findListCount(String index, String type, Map<String, Object> map) {

        SearchRequest searchRequest = new SearchRequest(index).types(type);

        SearchSourceBuilder sourceBuilder = getSearchRequest(map);
        searchRequest.source(sourceBuilder);

        try {
            SearchResponse searchResponse = restClient.search(searchRequest, RequestOptions.DEFAULT);
            return StringUtil.toInteger(searchResponse.getHits().getTotalHits());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private SearchSourceBuilder getSearchRequest(Map<String, Object> map) {
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();

        map.entrySet().stream().forEach(entry -> {
            Object value = entry.getValue();
            if (value instanceof Integer ||
                    value instanceof Long ||
                    value instanceof Float ||
                    value instanceof Double ||
                    value instanceof Boolean
            ) {
                boolQueryBuilder.must().add(QueryBuilders.termQuery(entry.getKey(), value));
            } else
                boolQueryBuilder.must().add(QueryBuilders.matchQuery(entry.getKey(), value));
        });

        sourceBuilder.query(boolQueryBuilder);

        return sourceBuilder;
    }
}
