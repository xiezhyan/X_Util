package com.sanq.product.utils.es.support.impl;

import com.sanq.product.config.utils.string.StringUtil;
import com.sanq.product.config.utils.web.GlobalUtil;
import com.sanq.product.config.utils.web.JsonUtil;
import com.sanq.product.utils.es.entity.Search;
import com.sanq.product.utils.es.entity.SearchPager;
import com.sanq.product.utils.es.entity.SearchPagination;
import com.sanq.product.utils.es.listener.IBaseSearchListener;
import com.sanq.product.utils.es.listener.ISearchListener;
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
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
public class BaseSearchSupportImpl<T, K> implements BaseSearchSupport<T, K> {

    @Resource
    private RestHighLevelClient restClient;

    public static final String ID = "id";

    /**
     * 返回当前客户端
     *
     * @return
     */
    @Override
    public RestHighLevelClient getClient() {
        return this.restClient;
    }

    private Class<T> getGenericClass() {
        try {
            ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();
            return (Class<T>) parameterizedType.getActualTypeArguments()[0];
        } catch (Exception e) {
            throw e;
        }
    }

    private Class<K> getQueryClass() {
        try {
            ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();
            return (Class<K>) parameterizedType.getActualTypeArguments()[1];
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 验证该索引是否存在
     *
     * @param index 索引
     * @return boolean
     * @throws Exception 抛出异常
     */
    @Override
    public boolean check(String index) throws Exception {
        GetIndexRequest getIndexRequest = new GetIndexRequest(index);
        return restClient.indices().exists(getIndexRequest, RequestOptions.DEFAULT);
    }

    /**
     * 创建索引
     *
     * @param index   索引
     * @param mapping 文档
     * @return boolean
     * @throws Exception 抛出异常
     */
    @Override
    public boolean createIndex(String index, String mapping) throws Exception {
        CreateIndexRequest createIndexRequest = new CreateIndexRequest(index)
                .settings(Settings.builder()
                        .put("index.number_of_shards", 3)
                        .put("index.number_of_replicas", 2))
                .mapping(mapping, XContentType.JSON);

        CreateIndexResponse createIndexResponse = restClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);
        return createIndexResponse.isAcknowledged() && createIndexResponse.isShardsAcknowledged();
    }

    /**
     * 通过ID获取详情
     *
     * @param index 索引
     * @param id    ID
     * @return K
     */
    @Override
    public K findById(String index, String id) throws Exception {
        GetRequest getRequest = new GetRequest(index, id);
        GetResponse getResponse = restClient.get(getRequest, RequestOptions.DEFAULT);
        return JsonUtil.json2Obj(getResponse.getSourceAsString(), getQueryClass());
    }

    /**
     * 保存
     *
     * @param index  索引
     * @param entity 内容实体
     */
    @Override
    public String save(String index, T entity) throws Exception {

        Map<String, Object> map = bean2Map(entity);

        String id = getId(map);

        IndexRequest indexRequest = new IndexRequest(index).id(id).source(map).opType(DocWriteRequest.OpType.CREATE);
        IndexResponse indexResponse = restClient.index(indexRequest, RequestOptions.DEFAULT);

        return indexResponse.getId();
    }

    private String getId(Map<String, Object> map) {
        Object o = map.get(ID);
        if (o == null)
            return String.valueOf(System.nanoTime());
        else
            return o.toString();
    }

    /**
     * 将实体转化成map
     *
     * @param entity
     * @return
     */
    private Map<String, Object> bean2Map(Object entity) {
        return entity == null ? Collections.EMPTY_MAP : entity instanceof Map ? (Map<String, Object>) entity : GlobalUtil.bean2Map(entity);
    }

    /**
     * 批量保存
     *
     * @param index      索引
     * @param entityList 内容实体
     */
    @Override
    public boolean saveList(String index, List<T> entityList) throws Exception {
        BulkRequest bulkRequest = new BulkRequest();

        entityList.forEach(entity -> {
            try {
                Map<String, Object> map = bean2Map(entity);

                String id = getId(map);

                bulkRequest.add(new IndexRequest(index).id(id).source(JsonUtil.obj2Json(map), XContentType.JSON));
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
     * @param index  索引
     * @param entity 内容实体
     * @return boolean
     */
    @Override
    public boolean update(String index, T entity) throws Exception {
        Map<String, Object> map = bean2Map(entity);

        UpdateRequest request = new UpdateRequest(index,  getId(map));
        request.doc(map);

        UpdateResponse update = restClient.update(request, RequestOptions.DEFAULT);
        return update.status().getStatus() == RestStatus.OK.getStatus();
    }

    /**
     * 删除
     *
     * @param index 索引
     * @param id    id
     * @return boolean
     */
    @Override
    public boolean delete(String index, String id) throws Exception {
        DeleteRequest deleteRequest = new DeleteRequest(index, id);

        DeleteResponse delete = restClient.delete(deleteRequest, RequestOptions.DEFAULT);
        return delete.status().getStatus() == RestStatus.OK.getStatus();
    }

    /**
     * 批量删除
     *
     * @param index 索引
     * @param ids   ids
     * @return boolean
     */
    @Override
    public boolean deleteList(String index,  List<String> ids) throws Exception {
        BulkRequest bulkRequest = new BulkRequest();

        ids.stream().forEach(id -> {
            bulkRequest.add(new DeleteRequest(index, id));
        });

        BulkResponse bulk = restClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        return bulk.status().getStatus() == RestStatus.OK.getStatus();
    }

    /**
     * 删除 索引
     *
     * @param index 索引
     * @return boolean
     */
    @Override
    public boolean deleteIndex(String index) throws Exception {
        AcknowledgedResponse delete = restClient.indices().delete(new DeleteIndexRequest(index), RequestOptions.DEFAULT);

        return delete.isAcknowledged();
    }

    /**
     * 查询数据 分页
     *
     * @param index      索引
     * @param entity     实体
     * @param pagination 分页
     * @return SearchPager<K>
     */
    @Override
    public SearchPager<K> findListByPage(String index, K entity, SearchPagination pagination) throws Exception {

        return findListByPage(index, Search.bean2Search(entity), null, null, pagination);
    }

    /**
     * 查询数据 分页
     *
     * @param index      索引
     * @param listener   自定义参数
     * @param pagination 分页
     * @return SearchPager<K>
     * @throws Exception 抛出异常
     */
    @Override
    public SearchPager<K> findListByPage(String index,  IBaseSearchListener listener, SearchPagination pagination) throws Exception {
        return findListByPage(index, listener.params(), listener.sortMap(), null, pagination);
    }


    @Override
    public SearchPager<K> findListByPage(String index,  ISearchListener listener, SearchPagination pagination) throws Exception {
        return findListByPage(index,  listener.params(), listener.sortMap(), listener.highlightField(), pagination);
    }

    private SearchPager<K> findListByPage(String index,
                                          List<Search> params,  //简单查询
                                          Map<String, SortOrder> sortMap,
                                          List<String> highFields,
                                          SearchPagination pagination) throws Exception {
        //这里是通过scrollId分页
        if (!StringUtil.isEmpty(pagination.getScrollId()) && pagination.getCurrentIndex() <= 0) {
            return getScrollPager(pagination);
        }

        SearchRequest searchRequest = new SearchRequest(index);

        SearchSourceBuilder sourceBuilder = getSearchRequest(params);

        //通过页码分页
        if (pagination.getCurrentIndex() > 0) {
            sourceBuilder.from(pagination.getSearchStartPage());
        }
        sourceBuilder.size(pagination.getPageSize());
        /**
         * 设置排序
         */
        if (sortMap != null && !sortMap.isEmpty()) {
            sortMap.forEach(sourceBuilder::sort);

        } else sourceBuilder.sort("id", SortOrder.ASC);
        searchRequest.source(sourceBuilder);
        /**
         * 设置高亮
         */
        if (highFields != null && !highFields.isEmpty()) {
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            highlightBuilder.preTags("<strong>").postTags("</strong>");

            highFields.forEach(field -> {
                HighlightBuilder.Field highlightTitle = new HighlightBuilder.Field(field);
                highlightBuilder.field(highlightTitle);
            });
            sourceBuilder.highlighter(highlightBuilder);
        }


        if (pagination.getCurrentIndex() <= 0) {
            searchRequest.scroll(TimeValue.timeValueMinutes(1L));
        }

        SearchResponse searchResponse = restClient.search(searchRequest, RequestOptions.DEFAULT);

        if (pagination.getCurrentIndex() <= 0) {
            //滚动标志
            pagination.setScrollId(searchResponse.getScrollId());
        }
        //总条数
        pagination.setTotalCount(StringUtil.toInteger(searchResponse.getHits().getTotalHits()));

        List<K> data = getListData(searchResponse.getHits());

        return new SearchPager<K>(pagination, data);
    }

    private List<K> getListData(SearchHits hits) {
        List<K> data = new ArrayList<>(hits.getHits().length);
        for (SearchHit hit : hits.getHits()) {
            K obj = JsonUtil.json2Obj(hit.getSourceAsString(), getQueryClass());

            //验证是否出现高亮字段
            if (hit.getHighlightFields() != null && !hit.getHighlightFields().isEmpty()) {

                // 1. 出现高亮字段才会反射获取高亮值
                Field[] fields = obj.getClass().getDeclaredFields();

                if (fields == null || fields.length <= 0)
                    fields = obj.getClass().getSuperclass().getDeclaredFields();

                //通过反射对高亮进行获取
                for (Field field : fields) {
                    field.setAccessible(true);
                    String fieldName = field.getName();
                    HighlightField highlightField = hit.getHighlightFields().get(fieldName);

                    if (highlightField != null) {
                        try {
                            field.set(obj, highlightField.fragments()[0].toString());
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                    field.setAccessible(false);
                }
            }
            data.add(obj);

        }
        return data;
    }

    private SearchPager<K> getScrollPager(SearchPagination pagination) throws Exception {
        SearchScrollRequest scrollRequest = new SearchScrollRequest(pagination.getScrollId());
        scrollRequest.scroll(TimeValue.timeValueMinutes(1L));
        SearchResponse searchScrollResponse = restClient.scroll(scrollRequest, RequestOptions.DEFAULT);

        //设置新的scrollId
        pagination.setScrollId(searchScrollResponse.getScrollId());

        List<K> data = getListData(searchScrollResponse.getHits());

        return new SearchPager<K>(pagination, data);
    }

    /**
     * 清除之前的scrollId
     *
     * @param scrollId
     */
    private void clearScrollId(String scrollId) throws Exception {
        ClearScrollRequest request = new ClearScrollRequest();
        request.addScrollId(scrollId);
        restClient.clearScroll(request, RequestOptions.DEFAULT);
    }

    private SearchSourceBuilder getSearchRequest(List<Search> params) {
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();

        if (params == null || params.isEmpty())
            boolQueryBuilder.must().add(QueryBuilders.matchAllQuery());
        else {
            params.forEach(param -> {
                //普通查询
                if (param.getStart() == null && param.getEnd() == null) {
                    Object value = param.getSearchValue();
                    if (value instanceof Integer ||
                            value instanceof Long ||
                            value instanceof Float ||
                            value instanceof Double ||
                            value instanceof Boolean
                    ) {
                        boolQueryBuilder.must().add(QueryBuilders.termQuery(param.getSearchTitle(), value));
                    } else
                        boolQueryBuilder.must().add(QueryBuilders.matchQuery(param.getSearchTitle(), value));
                } else {
                    // 区间查询
                    boolQueryBuilder.filter(QueryBuilders.rangeQuery(param.getSearchTitle())
                            .from(param.getStart())
                            .to(param.getEnd(), true));
                }

            });
        }

        sourceBuilder.query(boolQueryBuilder);

        return sourceBuilder;
    }
}
