package com.sanq.product.utils.es.entity;

import com.sanq.product.config.utils.entity.Pagination;

import java.io.Serializable;
import java.util.List;

public class SearchPager<T> implements Serializable {

    private SearchPagination pagination;

    private List<T> data;

    public SearchPager() {
    }

    public SearchPager(SearchPagination pagination, List<T> data) {
        this.pagination = pagination;
        this.data = data;
    }

    public SearchPagination getPagination() {
        return pagination;
    }

    public void setPagination(SearchPagination pagination) {
        this.pagination = pagination;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

}
