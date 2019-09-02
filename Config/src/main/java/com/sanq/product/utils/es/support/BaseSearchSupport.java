package com.sanq.product.utils.es.support;

import com.sanq.product.config.utils.entity.Pager;
import com.sanq.product.config.utils.entity.Pagination;

import java.io.Serializable;
import java.util.List;

public interface BaseSearchSupport<T> {

    /**
     * 验证当前 index 是否存在
     *
     * @param index
     * @return
     */
    boolean check(String index);

    /**
     * 创建索引
     *
     * @param index
     * @param type
     * @return
     */
    boolean createIndex(String index, String type);

    /**
     * 通过ID获取详情
     *
     * @param index
     * @param type
     * @param id
     * @return
     */
    T findById(String index, String type, String id);

    /**
     * 保存
     *
     * @param index
     * @param type
     * @param entity
     */
    String save(String index, String type, T entity);

    /**
     * 批量保存
     *
     * @param index
     * @param type
     * @param entityList
     */
    boolean saveList(String index, String type, List<T> entityList);

    /**
     * 修改
     *
     * @param index
     * @param type
     * @param entity
     * @return
     */
    boolean update(String index, String type, T entity);

    /**
     * 删除
     * @param index
     * @param type
     * @param id
     * @return
     */
    boolean delete(String index, String type, String id);

    /**
     * 批量删除
     * @param index
     * @param type
     * @param ids
     * @return
     */
    boolean deleteList(String index, String type, List<String> ids);

    /**
     * 删除 索引
     * @param index
     * @return
     */
    boolean deleteIndex(String index);

    /**
     * 查询数据 分页
     * @param index
     * @param type
     * @param entity
     * @param pagination
     * @return
     */
    Pager<T> findListByPager(String index, String type, T entity, Pagination pagination);
}
