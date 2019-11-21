package com.sanq.product.utils.es.entity;

import java.io.Serializable;

/**
 * com.sanq.product.utils.es.entity.RangeSearch
 *
 * @author sanq.Yan
 * @date 2019/11/21
 */
public class Search implements Serializable {

    private String searchTitle;

    private Object searchValue;

    private Object start;

    private Object end;

    public Search() {
    }

    public Search(String searchTitle, Object searchValue, Object start, Object end) {
        this.searchTitle = searchTitle;
        this.searchValue = searchValue;
        this.start = start;
        this.end = end;
    }

    public String getSearchTitle() {
        return searchTitle;
    }

    public void setSearchTitle(String searchTitle) {
        this.searchTitle = searchTitle;
    }

    public Object getSearchValue() {
        return searchValue;
    }

    public void setSearchValue(Object searchValue) {
        this.searchValue = searchValue;
    }

    public Object getStart() {
        return start;
    }

    public void setStart(Object start) {
        this.start = start;
    }

    public Object getEnd() {
        return end;
    }

    public void setEnd(Object end) {
        this.end = end;
    }

    public static class Build implements Serializable {
        private String searchTitle;

        private Object searchValue;

        private Object start;

        private Object end;

        public Build setSearchTitle(String searchTitle) {
            this.searchTitle = searchTitle;
            return this;
        }

        public Build setSearchValue(Object searchValue) {
            this.searchValue = searchValue;
            return this;
        }

        public Build setStart(Object start) {
            this.start = start;
            return this;
        }

        public Build setEnd(Object end) {
            this.end = end;
            return this;
        }

        public Search builder() {
            return new Search(searchTitle, searchValue, start, end);
        }
    }
}
