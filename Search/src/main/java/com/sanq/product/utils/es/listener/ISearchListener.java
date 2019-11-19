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
public interface ISearchListener extends IBaseSearchListener {

    /**
     * 高亮字段
     * @return
     */
    List<String> highlightField();
}
