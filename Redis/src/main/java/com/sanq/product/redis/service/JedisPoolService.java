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

    long putList(String key, String value);

    boolean rmList(String key, long start, long end);

    List<String> getList(String key, long start, long end);

    Set<String> keys(String pattern);

    boolean expire(String key, int seconds);

    long llen(String key);

    //不标准去重统计  0.81%
    boolean pfAdd(String key, String...value);

    long pfCount(String key);

    //位图
    boolean setBit(String key, long offset, String value);

    boolean getBit(String key, long offset);

    //执行lua脚本
    boolean eval(String script, List<String> keys, List<String> args);

    boolean deletes(String pattern);
}
