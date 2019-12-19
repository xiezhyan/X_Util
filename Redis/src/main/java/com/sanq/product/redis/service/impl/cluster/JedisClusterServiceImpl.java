package com.sanq.product.redis.service.impl.cluster;

import com.sanq.product.config.utils.string.StringUtil;
import com.sanq.product.redis.service.JedisPoolService;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Service;
import redis.clients.jedis.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;


@Service("jedisPoolService")
public class JedisClusterServiceImpl implements JedisPoolService {

    @Resource
    private JedisCluster jedisCluster;

    @Override
    public void set(String key, String val) {
        jedisCluster.set(key, val);
    }

    @Override
    public void set(String key, String val, int seconds) {
        jedisCluster.setex(key, seconds + RandomUtils.nextInt(0, 300), val);
    }

    @Override
    public String get(String key) {
        return jedisCluster.get(key);
    }

    @Override
    public void deleteKey(String key) {
        jedisCluster.del(key);
    }

    @Override
    public long incr(String key) {
        return jedisCluster.incr(key);
    }

    @Override
    public long incrAtTime(String key, int second) {
        long incr = incr(key);
        expire(key, second);
        return incr;
    }

    @Override
    public long incr(String key, int max) {
        return StringUtil.toLong(eval(
                "if redis.call('EXISTS', KEYS[1]) == 1 then return redis.call('INCR', KEYS[1]) else redis.call('SET', KEYS[1], ARGV[1]) return redis.call('INCR', KEYS[1]) end",
                new ArrayList<String>(1) {{
                    add(key);
                }},
                new ArrayList<String>(1) {{
                    add(String.valueOf(max));
                }}));
    }

    @Override
    public boolean existsKeys(String key) {
        return jedisCluster.exists(key);
    }

    @Override
    public void putListR(String key, String... values) {
        jedisCluster.lpush(key, values);
    }

    @Override
    public void deleteList(String key, long start, long end) {
        jedisCluster.ltrim(key, start, end);
    }

    @Override
    public List<String> getList(String key, long start, long end) {
        return jedisCluster.lrange(key, start, end);
    }

    @Override
    public List<String> keys(String pattern) {
        return getKeysByScan(jedisCluster, pattern);
    }

    private List<String> getKeysByScan(JedisCluster jedisCluster, String pattern) {
        Map<String, JedisPool> nodes = jedisCluster.getClusterNodes();

        List<String> keys = new ArrayList<>();

        for (Map.Entry<String, JedisPool> entry : nodes.entrySet()) {
            keys.addAll(getJedisByScan(entry.getValue(), pattern));
        }
        return keys;
    }

    @Override
    public long decr(String key) {
        return jedisCluster.decr(key);
    }

    @Override
    public long decrBy(String key, long minus) {
        return jedisCluster.decrBy(key, minus);
    }

    @Override
    public double updateScopeByItem(String key, double score, String member) {
        return jedisCluster.zincrby(key, score, member);
    }

    private List<String> getJedisByScan(JedisPool jedisPool, String pattern) {

        List<String> keys = new ArrayList<>();

        try (Jedis jedis = jedisPool.getResource()) {
            ScanParams params = new ScanParams();
            params.match(pattern);
            params.count(100);

            String cursor = "0";

            do {
                ScanResult<String> scan = jedis.scan(cursor, params);

                cursor = scan.getCursor();

                List<String> result = scan.getResult();
                if (result != null && result.size() > 0) {
                    keys.addAll(result);
                }

                if ("0".equals(cursor))
                    continue;
            } while (!"0".equals(cursor));
        }

        return keys;
    }

    @Override
    public void expire(String key, int seconds) {
        jedisCluster.expire(key, seconds + RandomUtils.nextInt(0, 300));
    }

    @Override
    public long getListSize(String key) {
        return jedisCluster.llen(key);
    }

    @Override
    public void putSortedSet(String key, double score, String value) {
        jedisCluster.zadd(key, score, value);
    }

    @Override
    public Set<String> getSortedSet(String key, long start, long end, String order) {
        if (StringUtil.isEmpty(order))
            order = "ASC";

        if ("ASC".equals(order))
            return jedisCluster.zrange(key, start, end);
        else if ("DESC".equals(order))
            return jedisCluster.zrevrange(key, start, end);

        return null;
    }

    @Override
    public long getSortedSetSize(String key) {
        return jedisCluster.zcard(key);
    }

    @Override
    public boolean itemExistsSortedSet(String key, String val) {
        return jedisCluster.zrank(key, val) != null;
    }

    @Override
    public void deleteSortedSet(String key, long start, long end) {
        jedisCluster.zremrangeByRank(key, start, end);
    }

    @Override
    public void pfAdd(String key, String... value) {
        jedisCluster.pfadd(key, value);
    }

    @Override
    public long pfCount(String key) {
        return jedisCluster.pfcount(key);
    }

    @Override
    public void setBit(String key, long offset, boolean value) {
        jedisCluster.setbit(key, offset, value);
    }

    @Override
    public boolean getBit(String key, long offset) {
        return jedisCluster.getbit(key, offset);
    }

    @Override
    public double getScopeByItem(String key, String member) {
        return jedisCluster.zscore(key, member);
    }

    @Override
    public Long getBitCount(String key) {
        return jedisCluster.bitcount(key);
    }

    @Override
    public Object eval(String script, List<String> keys, List<String> args) {
        return jedisCluster.eval(script, keys, args);
    }

    @Override
    public void deleteKeys(String pattern) {
        List<String> keys = keys(pattern);
        if (keys != null && keys.size() > 0) {
            keys.stream().filter(key -> !StringUtil.isEmpty(key)).forEach(this::deleteKey);
        }
    }

    @Override
    public void putListL(String key, String... values) {
        jedisCluster.lpush(key, values);
    }

    @Override
    public void deleteListItem(String key, String value) {
        jedisCluster.lrem(key, 0, value);
    }

    @Override
    public void putHash(String key, Map<String, String> map) {
        jedisCluster.hmset(key, map);
    }

    @Override
    public void putHash(String key, String field, String value) {
        jedisCluster.hset(key, field, value);
    }

    @Override
    public Map<String, String> getHash(String key) {
        return jedisCluster.hgetAll(key);
    }

    @Override
    public String getHash(String key, String field) {
        return jedisCluster.hget(key, field);
    }

    @Override
    public List<String> getHash(String key, String... fields) {
        return jedisCluster.hmget(key, fields);
    }

    @Override
    public void deleteHash(String key, String... fields) {
        jedisCluster.hdel(key, fields);
    }

    @Override
    public void deleteSortedSet(String key, double start, double end) {
        jedisCluster.zremrangeByScore(key, start, end);
    }

    @Override
    public void deleteSortedSetItem(String key, String... members) {
        jedisCluster.zrem(key, members);
    }

    @Override
    public void select(int dbIndex) throws Exception {
        throw new Exception("SELECT is not allowed in cluster mode");
    }

    @Override
    public void move(String key, int dbIndex) throws Exception {
        throw new Exception("MOVE is not allowed in cluster mode");
    }

    @Override
    public void putGeo(String key, Map<String, GeoCoordinate> map) {
        jedisCluster.geoadd(key, map);
    }

    @Override
    public void putGeo(String key, double longitude, double latitude, String member) {
        jedisCluster.geoadd(key, longitude, latitude, member);
    }

    @Override
    public List<GeoCoordinate> getGeo(String key, String... member) {
        return jedisCluster.geopos(key, member);
    }

    @Override
    public List<GeoRadiusResponse> getGeo(String key, String member, double radius) {
        return jedisCluster.georadiusByMember(key, member, radius, GeoUnit.KM);
    }

    @Override
    public List<GeoRadiusResponse> getGeo(String key, double longitude, double latitude, double radius) {
        return jedisCluster.georadius(key, longitude, latitude, radius, GeoUnit.KM);
    }

    @Override
    public double getDist(String key, String member1, String member2) {
        return jedisCluster.geodist(key, member1, member2);
    }
}
