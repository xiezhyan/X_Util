package com.sanq.product.config.utils.mvc.service;

import com.sanq.product.config.utils.entity.Pager;
import com.sanq.product.config.utils.entity.Pagination;

import java.util.List;

/**
 * com.sanq.product.config.utils.mvc.service.BaseService
 *
 * @author sanq.Yan
 * @date 2019/12/13
 */
public interface BaseService<T, K> {

    int save(T save);

    int delete(T delete);

    int update(T update, K id);

    T findById(K id);

    List<T> findList(T query);

    Pager<T> findListByPage(T query, Pagination pagination);

    int findCount(T query);

    void saveByList(List<T> save);

}
