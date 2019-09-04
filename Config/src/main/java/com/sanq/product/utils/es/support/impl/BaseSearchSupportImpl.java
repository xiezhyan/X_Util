package com.sanq.product.utils.es.support.impl;

import com.sanq.product.config.utils.entity.Pagination;
import com.sanq.product.config.utils.string.StringUtil;
import com.sanq.product.config.utils.web.GlobalUtil;
import com.sanq.product.config.utils.web.JsonUtil;
import com.sanq.product.utils.es.entity.SearchPager;
import com.sanq.product.utils.es.entity.SearchPagination;
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
import org.elasticsearch.action.search.ClearScrollRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchScrollRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
public class BaseSearchSupportImpl<T> implements BaseSearchSupport<T> {

    @Resource
    private RestHighLevelClient restClient;

    private Class<T> getGenericClass() {
        try {
            ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();
            return (Class<T>) parameterizedType.getActualTypeArguments()[0];
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean check(String index) throws Exception {
        GetIndexRequest getIndexRequest = new GetIndexRequest(index);
        return restClient.indices().exists(getIndexRequest, RequestOptions.DEFAULT);
    }

    /**
     * 创建索引
     *
     * @param index
     * @param type
     * @return
     */
    @Override
    public boolean createIndex(String index, String type) throws Exception {
        CreateIndexRequest createIndexRequest = new CreateIndexRequest(index)
                .settings(Settings.builder()
                        .put("index.number_of_shards", 3)
                        .put("index.number_of_replicas", 2))
                .mapping(type, XContentType.JSON);

        CreateIndexResponse createIndexResponse = restClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);
        return createIndexResponse.isAcknowledged() && createIndexResponse.isShardsAcknowledged();
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
    public T findById(String index, String type, String id) throws Exception {
        GetRequest getRequest = new GetRequest(index, type, id);
        GetResponse getResponse = restClient.get(getRequest, RequestOptions.DEFAULT);
        return JsonUtil.json2Obj(getResponse.getSourceAsString(), getGenericClass());
    }

    /**
     * 保存
     *
     * @param index
     * @param type
     * @param entity
     */
    @Override
    public String save(String index, String type, T entity) throws Exception {

        Map<String, Object> map = bean2Map(entity);

        IndexRequest indexRequest = new IndexRequest(index, type, map.get("id").toString()).source(map).opType(DocWriteRequest.OpType.CREATE);
        IndexResponse indexResponse = restClient.index(indexRequest, RequestOptions.DEFAULT);

        return indexResponse.getId();
    }

    private Map<String, Object> bean2Map(T entity) {
        return entity == null ? Collections.EMPTY_MAP : entity instanceof Map ? (Map<String, Object>) entity : GlobalUtil.bean2Map(entity);
    }

    /**
     * 批量保存
     *
     * @param index
     * @param type
     * @param entityList
     */
    @Override
    public boolean saveList(String index, String type, List<T> entityList) throws Exception {
        BulkRequest bulkRequest = new BulkRequest();

        entityList.stream().forEach(entity -> {
            try {
                Map<String, Object> map = bean2Map(entity);

                String id = String.valueOf(map.get("id"));

                bulkRequest.add(new IndexRequest(index, type, id).source(JsonUtil.obj2Json(map), XContentType.JSON));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        BulkResponse bulk = restClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        return bulk.status().getStatus() == RestStatus.OK.getStatus();
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
    public boolean update(String index, String type, T entity) throws Exception {
        Map<String, Object> map = bean2Map(entity);

        UpdateRequest request = new UpdateRequest(index, type, map.get("id").toString());
        request.doc(map);

        UpdateResponse update = restClient.update(request, RequestOptions.DEFAULT);
        return update.status().getStatus() == RestStatus.OK.getStatus();
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
    public boolean delete(String index, String type, String id) throws Exception {
        DeleteRequest deleteRequest = new DeleteRequest(index, type, id);

        DeleteResponse delete = restClient.delete(deleteRequest, RequestOptions.DEFAULT);
        return delete.status().getStatus() == RestStatus.OK.getStatus();
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
    public boolean deleteList(String index, String type, List<String> ids) throws Exception {
        BulkRequest bulkRequest = new BulkRequest();

        ids.stream().forEach(id -> {
            bulkRequest.add(new DeleteRequest(index, type, id));
        });

        BulkResponse bulk = restClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        return bulk.status().getStatus() == RestStatus.OK.getStatus();
    }

    /**
     * 删除 索引
     *
     * @param index
     * @return
     */
    @Override
    public boolean deleteIndex(String index) throws Exception {
        AcknowledgedResponse delete = restClient.indices().delete(new DeleteIndexRequest(index), RequestOptions.DEFAULT);

        return delete.isAcknowledged();
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
    public SearchPager<T> findListByPager(String index, String type, T entity, SearchPagination pagination) throws Exception {
        if (!StringUtil.isEmpty(pagination.getScrollId())) {
            return getScrollPager(pagination);
        }

        Map<String, Object> map = bean2Map(entity);

        SearchRequest searchRequest = new SearchRequest(index).types(type);

        SearchSourceBuilder sourceBuilder = getSearchRequest(map);

        sourceBuilder.size(pagination.getPageSize());
        sourceBuilder.sort("id", SortOrder.ASC);

        searchRequest.source(sourceBuilder);

        searchRequest.scroll(TimeValue.timeValueMillis(5L));

        SearchResponse searchResponse = restClient.search(searchRequest, RequestOptions.DEFAULT);
        pagination.setScrollId(searchResponse.getScrollId());

        List<T> data = getListData(searchResponse.getHits());

        return new SearchPager<T>(pagination, data);
    }

    private List<T> getListData(SearchHits hits) {
        List<T> data = new ArrayList<>();
        for (SearchHit hit : hits.getHits()) {
            if (getGenericClass() == null) data.add((T) hit.getSourceAsMap());
            else data.add(JsonUtil.json2Obj(hit.getSourceAsString(), getGenericClass()));
        }
        return data;
    }

    private SearchPager<T> getScrollPager(SearchPagination pagination) throws Exception {
        SearchScrollRequest scrollRequest = new SearchScrollRequest(pagination.getScrollId());
        scrollRequest.scroll(TimeValue.timeValueMillis(5L));
        SearchResponse searchScrollResponse = restClient.scroll(scrollRequest, RequestOptions.DEFAULT);

        clearScrollId(pagination.getScrollId());

        //设置新的scrollId
        pagination.setScrollId(searchScrollResponse.getScrollId());

        List<T> data = getListData(searchScrollResponse.getHits());

        return new SearchPager<T>(pagination, data);
    }

    /**
     * 清除之前的scrollId
     * @param scrollId
     */
    private void clearScrollId(String scrollId) throws Exception {
        ClearScrollRequest request = new ClearScrollRequest();
        request.addScrollId(scrollId);
        restClient.clearScroll(request, RequestOptions.DEFAULT);
    }

    /**
     * 查询总条数
     *
     * @param index
     * @param type
     * @param entity
     * @return
     */
    @Override
    public int findListCount(String index, String type, T entity) throws Exception {
        Map<String, Object> map = bean2Map(entity);
        return findListCount(index, type, map);
    }

    /**
     * 获取到总条数
     *
     * @param index
     * @param type
     * @param map
     * @return
     */
    private int findListCount(String index, String type, Map<String, Object> map) throws Exception {

        SearchRequest searchRequest = new SearchRequest(index).types(type);

        SearchSourceBuilder sourceBuilder = getSearchRequest(map);
        searchRequest.source(sourceBuilder);

        SearchResponse searchResponse = restClient.search(searchRequest, RequestOptions.DEFAULT);

        return StringUtil.toInteger(searchResponse.getHits().getTotalHits());
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
