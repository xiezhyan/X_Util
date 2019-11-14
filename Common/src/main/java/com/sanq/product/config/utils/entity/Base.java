package com.sanq.product.config.utils.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * com.sanq.product.config.utils.entity.Base
 *
 * @author sanq.Yan
 * @date 2019/7/16
 */
public class Base implements Serializable {
    private String token;
    private Long timestamp;
    private String sign;

    private Character isDelete;

    private Date createTime;
    private Date updateTime;
    private Date deleteTime;

    private Long creater;
    private Long updater;
    private Long deleter;

    public Character getIsDelete() {
        return isDelete;
    }

    public Base setIsDelete(Character isDelete) {
        this.isDelete = isDelete;
        return this;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public Base setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public Base setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public Date getDeleteTime() {
        return deleteTime;
    }

    public Base setDeleteTime(Date deleteTime) {
        this.deleteTime = deleteTime;
        return this;
    }

    public Long getCreater() {
        return creater;
    }

    public Base setCreater(Long creater) {
        this.creater = creater;
        return this;
    }

    public Long getUpdater() {
        return updater;
    }

    public Base setUpdater(Long updater) {
        this.updater = updater;
        return this;
    }

    public Long getDeleter() {
        return deleter;
    }

    public Base setDeleter(Long deleter) {
        this.deleter = deleter;
        return this;
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
}
