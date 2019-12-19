package com.sanq.product.redis.service;

import redis.clients.jedis.GeoCoordinate;
import redis.clients.jedis.GeoRadiusResponse;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * version: Redis操作
 * ---------------------
 *
 * @author sanq.Yan
 * @date 2019/12/19
 */
public interface JedisPoolService {

    //*****************字符串start*****************//

    /**
     * 保存字符串
     */
    void set(String key, String val);

    /**
     * 保存字符串
     *
     */
    void set(String key, String val, int seconds);

    /**
     * 获取字符串
     */
    String get(String key);


    //*****************字符串end*****************//


    //*****************key操作 start*****************//

    /**
     * 删除
     */
    void deleteKey(String key);


    /**
     * 批量删除key
     */
    void deleteKeys(String pattern);

    /**
     * 判断redis中是否存在
     */
    boolean existsKeys(String key);

    /**
     * 根据条件查询出key
     */
    List<String> keys(String pattern);

    /**
     * 设置过期时间
     */
    void expire(String key, int seconds);

    //*****************key操作 end*****************//


    //*****************自增自减操作 start*****************//

    /**
     * 自增
     */
    long incr(String key);

    /**
     * 自增 存在有效期
     */
    long incrAtTime(String key, int second);

    /**
     * 自增
     *
     */
    long incr(String key, int max);


    /**
     * 自减
     */
    long decr(String key);

    /**
     * 自减
     *
     */
    long decrBy(String key, long minus);

    //*****************自增自减操作 end*****************//

    //*****************list操作 start*****************//

    /**
     * rpush
     * 将元素push在list的尾部  ==> rpop 删除尾部元素
     */
    void putListR(String key, String...values);

    /**
     * lpush
     * 类似于压栈操作，将元素放入头部 ==> lpop 删除头部元素
     */
    void putListL(String key, String...values);

    /**
     * 获取List
     */
    List<String> getList(String key, long start, long end);

    /**
     * 移除List
     * 保留start-end中间的数据
     */
    void deleteList(String key, long start, long end);

    /**
     * 删除列表元素
     */
    void deleteListItem(String key, String value);


    /**
     * 获取List长度
     */
    long getListSize(String key);

    //*****************list操作 end*****************//

    //*****************Hash操作 start*****************//

    /**
     * 将field-value (字段-值)对设置到哈希表中
     */
    void putHash(String key, Map<String, String> map);

    /**
     * 对哈希表中的字段赋值
     */
    void putHash(String key, String field, String value);

    /**
     * 获取在哈希表中指定 key 的所有字段和值
     */
    Map<String, String> getHash(String key);

    /**
     * 返回哈希表中指定字段的值
     */
    String getHash(String key, String field);

    /**
     * 返回哈希表中，一个或多个给定字段的值
     */
    List<String> getHash(String key, String... fields);

    /**
     * 删除一个或多个哈希表字段
     */
    void deleteHash(String key, String...fields);

    //*****************Hash操作 end*****************//

    //*****************有序set操作 start*****************//

    /**
     * 保存到有序set
     */
    void putSortedSet(String key, double score, String value);

    /**
     * 获取set
     */
    Set<String> getSortedSet(String key, long start, long end, String order);

    /**
     * 获取set长度
     */
    long getSortedSetSize(String key);

    /**
     * 判断value是否存在于set中
     */
    boolean itemExistsSortedSet(String key, String val);

    /**
     * 批量移除元素
     * <p>
     * 通过下标移除
     */
    void deleteSortedSet(String key, long start, long end);

    /**
     * 批量移除元素
     * <p>
     * 移除分数区间内的元素
     */
    void deleteSortedSet(String key, double start, double end);

    /**
     * 移除单个元素或多个元素
     */
    void deleteSortedSetItem(String key, String... members);

    /**
     * 添加指定分数
     */
    double updateScopeByItem(String key, double score, String member);

    /**
     * 得到指定元素的分数
     */
    double getScopeByItem(String key, String member);

    //*****************有序set操作 end*****************//

    //*****************HyperLogLog操作 start*****************//

    /**
     * 添加到统计
     */
    void pfAdd(String key, String... value);

    /**
     * 获取统计数量
     */
    long pfCount(String key);

    //*****************HyperLogLog操作 end*****************//

    //*****************bit操作 start*****************//

    /**
     * 位图
     */
    void setBit(String key, long offset, boolean value);

    /**
     * 获取位图
     */
    boolean getBit(String key, long offset);

    /**
     * 获取位图个数
     */
    Long getBitCount(String key);


    //*****************bit操作 end*****************//


    //*****************脚本  start*****************//

    /**
     * 执行lua脚本
     */
    Object eval(String script, List<String> keys, List<String> args);

    //*****************脚本  end*****************//

    //*****************其他操作 start*****************//

    /**
     * 切换到其他数据库 db
     */
    void select(int dbIndex) throws Exception;

    /**
     * 将当前数据库的 key 移动到给定的数据库 db 当中
     */
    void move(String key, int dbIndex) throws Exception;

    //*****************其他操作 end*****************//

    //*****************位置 start*****************//

    /**
     * 批量保存地理位置
     */
    void putGeo(String key, Map<String, GeoCoordinate> map);

    /**
     * 保存地位位置
     */
    void putGeo(String key, double longitude, double latitude, String member);

    /**
     * 返回指定member的位置信息
     */
    List<GeoCoordinate> getGeo(String key, String... member);

    /**
     * 根据指定的成员， 返回附近范围内的元素
     */
    List<GeoRadiusResponse> getGeo(String key, String member, double radius);

    /**
     * 根据指定的位置， 返回附近范围内的元素
     */
    List<GeoRadiusResponse> getGeo(String key, double longitude, double latitude, double radius);

    /**
     * 返回指定成员间的距离
     */
    double getDist(String key, String member1, String member2);

    //*****************位置 end*****************//
}
