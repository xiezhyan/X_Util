package com.sanq.product.utils.es.entity;

import java.io.Serializable;

public class SearchPagination implements Serializable {
    //当前显示的条数,默认显示20条
    private int pageSize = 20;

    //当前查询的滚动点
    private String scrollId;

    public SearchPagination() {
    }

    public SearchPagination(int pageSize, String scrollId) {
        this.pageSize = pageSize;
        this.scrollId = scrollId;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getScrollId() {
        return scrollId;
    }

    public void setScrollId(String scrollId) {
        this.scrollId = scrollId;
    }
}
