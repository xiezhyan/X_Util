package com.sanq.product.config.utils.mvc.entity;

import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * com.sanq.product.config.utils.mvc.entity.Base
 *
 * @author sanq.Yan
 * @date 2019/12/13
 */
public class Base<T> implements Serializable {

    public static final Character IS_DELETE_Y = 'Y';
    public static final Character IS_DELETE_N = 'N';

    private T id;

    private String token;
    private Long timestamp;
    private String sign;

    private Character isDelete = IS_DELETE_N;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date deleteTime;

    private T creater;
    private T updater;
    private T deleter;

    //所属成员
    private T appertainId;

    public T getId() {
        return id;
    }

    public void setId(T id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public Character getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Character isDelete) {
        this.isDelete = isDelete;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getDeleteTime() {
        return deleteTime;
    }

    public void setDeleteTime(Date deleteTime) {
        this.deleteTime = deleteTime;
    }

    public T getCreater() {
        return creater;
    }

    public void setCreater(T creater) {
        this.creater = creater;
    }

    public T getUpdater() {
        return updater;
    }

    public void setUpdater(T updater) {
        this.updater = updater;
    }

    public T getDeleter() {
        return deleter;
    }

    public void setDeleter(T deleter) {
        this.deleter = deleter;
    }

    public T getAppertainId() {
        return appertainId;
    }

    public void setAppertainId(T appertainId) {
        this.appertainId = appertainId;
    }
}
