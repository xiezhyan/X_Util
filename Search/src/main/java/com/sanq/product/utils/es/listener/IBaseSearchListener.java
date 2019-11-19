package com.sanq.product.utils.es.listener;

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
     * 参数
     * @return
     */
    Map<String, Object> paramMap();

    /**
     * 排序
     * @return
     */
    Map<String, String> sortMap();


}
