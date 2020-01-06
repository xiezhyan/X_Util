package com.sanq.product.config.utils.mvc.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * com.sanq.product.config.utils.mvc.mapper.BaseMapper
 *
 * @author sanq.Yan
 * @date 2019/12/13
 */
public interface BaseMapper<T, K> {

    int save(T save);

    int delete(T delete);

    int update(T update);

    T findById(K id);

    List<T> findList(@Param("query")  T query);

    List<T> findListByPage(@Param("query") T query, @Param("startPage") int startPage, @Param("pageSize") int pageSize);

    int findCount(@Param("query")  T query);

    void saveByList(@Param("saves") List<T> save);
}
