package com.sanq.product.utils.es.entity;

import com.sanq.product.config.utils.entity.Pagination;

import java.io.Serializable;

public class SearchPagination extends Pagination implements Serializable {
    //当前查询的滚动点
    private String scrollId;

    public SearchPagination() {
    }

    public SearchPagination(int pageSize, String scrollId, int currentIndex) {
        super(pageSize, currentIndex);
        this.scrollId = scrollId;
    }

    public int getSearchStartPage() {
        if (getCurrentIndex() <= 0)
            setCurrentIndex(1);
        int startPage = (getCurrentIndex()- 1) * getPageSize();
        return startPage < 0 ? 0 : startPage;
    }

    public String getScrollId() {
        return scrollId;
    }

    public void setScrollId(String scrollId) {
        this.scrollId = scrollId;
    }

    public static class Build implements Serializable {
        //当前显示的条数,默认显示20条
        private int pageSize = 20;

        //当前查询的滚动点
        private String scrollId;

        //页码
        private int currentIndex;

        public Build setPageSize(int pageSize) {
            this.pageSize = pageSize;
            return this;
        }

        public Build setScrollId(String scrollId) {
            this.scrollId = scrollId;
            return this;
        }

        public Build setCurrentIndex(int currentIndex) {
            this.currentIndex = currentIndex;
            return this;
        }

        public SearchPagination builder() {
            return new SearchPagination(pageSize, scrollId, currentIndex);
        }
    }
}
