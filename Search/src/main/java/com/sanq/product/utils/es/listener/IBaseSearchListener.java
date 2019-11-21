package com.sanq.product.utils.es.listener;

import com.sanq.product.utils.es.entity.Search;
import org.elasticsearch.search.sort.SortOrder;

import java.util.List;
import java.util.Map;

/**
 * com.sanq.product.utils.es.listener.ISearchListener
 *
 * @author sanq.Yan
 * @date 2019/11/19
 */
public interface IBaseSearchListener {

    /**
     * 查询参数
     *
     * @return
     */
    List<Search> params();

    /**
     * 设置排序规则
     *
     * @return
     */
    Map<String, SortOrder> sortMap();
}
