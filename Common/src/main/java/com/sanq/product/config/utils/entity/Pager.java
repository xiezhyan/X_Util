package com.sanq.product.config.utils.entity;

import java.io.Serializable;
import java.util.List;

public class Pager<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private Pagination pagination;

    //结果集
    private List<T> datas;

    //页码列表的开始索引
    private int beginPageIndex;

    //页码列表的结束索引
    private int endPageIndex;

    public Pager() {
    }

    public Pager(Pagination pagination, List<T> datas) {
        super();

        this.datas = datas;
        this.pagination = pagination;

        if (pagination.getTotalPage() < 10) {
            beginPageIndex = 1;
            endPageIndex = pagination.getTotalPage();
        } else {
            beginPageIndex = pagination.getCurrentIndex() - 4;
            endPageIndex = pagination.getCurrentIndex() + 5;

            if (beginPageIndex < 1) {
                beginPageIndex = 1;
                endPageIndex = 10;
            } else if (endPageIndex > pagination.getTotalPage()) {
                endPageIndex = pagination.getTotalPage();
                beginPageIndex = pagination.getTotalPage() - 9;
            }
        }
    }

    public Pagination getPagination() {
        return pagination != null ? pagination : new Pagination();
    }


    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }


    public int getBeginPageIndex() {

        return beginPageIndex;
    }

    public int getEndPageIndex() {
        return endPageIndex;
    }


    public List<T> getDatas() {
        return datas;
    }

    public void setDatas(List<T> datas) {
        this.datas = datas;
    }
}
