package com.sanq.product.utils.es.support;

import com.sanq.product.config.utils.entity.Pager;
import com.sanq.product.config.utils.entity.Pagination;
import com.sanq.product.utils.es.entity.SearchPager;
import com.sanq.product.utils.es.entity.SearchPagination;
import org.elasticsearch.client.RestHighLevelClient;

import java.util.List;

public interface BaseSearchSupport<T> {

    /**
     * 返回当前客户端
     * @return
     */
    RestHighLevelClient getClient();
    /**
     * 验证当前 index 是否存在
     *
     * @param index
     * @return
     */
    boolean check(String index) throws Exception;

    /**
     * 创建索引
     *
     * @param index
     * @param type
     * @return
     */
    boolean createIndex(String index, String type) throws Exception;

    /**
     * 通过ID获取详情
     *
     * @param index
     * @param type
     * @param id
     * @return
     */
    T findById(String index, String type, String id) throws Exception;

    /**
     * 保存
     *
     * @param index
     * @param type
     * @param entity
     */
    String save(String index, String type, T entity) throws Exception;

    /**
     * 批量保存
     *
     * @param index
     * @param type
     * @param entityList
     */
    boolean saveList(String index, String type, List<T> entityList) throws Exception;

    /**
     * 修改
     *
     * @param index
     * @param type
     * @param entity
     * @return
     */
    boolean update(String index, String type, T entity) throws Exception;

    /**
     * 删除
     * @param index
     * @param type
     * @param id
     * @return
     */
    boolean delete(String index, String type, String id) throws Exception;

    /**
     * 批量删除
     * @param index
     * @param type
     * @param ids
     * @return
     */
    boolean deleteList(String index, String type, List<String> ids) throws Exception;

    /**
     * 删除 索引
     * @param index
     * @return
     */
    boolean deleteIndex(String index) throws Exception, Exception;

    /**
     * 查询数据 分页
     * @param index
     * @param type
     * @param entity
     * @param pagination
     * @return
     */
    SearchPager<T> findListByPager(String index, String type, T entity, SearchPagination pagination) throws Exception;

    /**
     * 查询总条数
     * @param index
     * @param type
     * @param entity
     * @return
     */
    int findListCount(String index, String type, T entity) throws Exception;
}
