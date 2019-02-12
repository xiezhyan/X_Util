package com.sanq.product.redis.service;

import java.util.List;
import java.util.Set;

/**
 * Created by Xiezhyan on 2019/1/18.
 */
public interface JedisPoolService {

    boolean set(String key, String val);

    boolean set(String key, String val, int seconds);

    String get(String key);

    boolean delete(String...key);

    long incr(String key);

    long incr(String key, int max);

    boolean exists(String key);

    Set<String> keys(String pattern);

    boolean expire(String key, int seconds);

    /**************************华丽丽的分割线**************************************/
    long putList(String key, String value);

    boolean rmList(String key, long start, long end);

    List<String> getList(String key, long start, long end);

    long llen(String key);

    /**************************华丽丽的分割线**************************************/

    boolean putSet(String key, double score, String value);

    Set<String> getSet(String key, long start, long end, String order);

    /**
     * 获取set长度
     * @param key
     * @return
     */
    long zcard(String key);

    /**
     * 判断value是否存在于set中
     * @param key
     * @param val
     * @return
     */
    boolean zrank(String key, String val);

    /**
     * 移除元素
     * @param key
     * @param start
     * @param end
     * @return
     */
    long rmSet(String key,long start, long end);

    /**************************华丽丽的分割线**************************************/

    //不标准去重统计  100000  -->  443
    boolean pfAdd(String key, String...value);

    long pfCount(String key);

    //位图
    boolean setBit(String key, long offset, boolean value);

    boolean getBit(String key, long offset);

    //执行lua脚本
    boolean eval(String script, List<String> keys, List<String> args);

    boolean deletes(String pattern);



}
