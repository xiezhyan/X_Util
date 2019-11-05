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

/**
 * Created by Xiezhyan on 2019/1/18.
 */
@Service("jedisPoolService")
public class JedisClusterServiceImpl implements JedisPoolService {

    @Resource
    private JedisCluster jedisCluster;

    @Override
    public boolean set(String key, String val) {

        return jedisCluster.set(key, val) != null;
    }

    @Override
    public boolean set(String key, String val, int seconds) {
        return jedisCluster.setex(key, seconds + RandomUtils.nextInt(0, 300), val) != null;
    }

    @Override
    public String get(String key) {
        return jedisCluster.get(key);
    }

    @Override
    public boolean delete(String key) {
        return jedisCluster.del(key) != null;
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
        if (!exists(key)) {
            set(key, max + "");
        }
        return incr(key);
    }

    @Override
    public boolean exists(String key) {
        return jedisCluster.exists(key);
    }

    @Override
    public long putList(String key, String value) {
        return jedisCluster.lpush(key, value);
    }

    @Override
    public boolean rmList(String key, long start, long end) {
        return jedisCluster.ltrim(key, start, end) != null;
    }

    @Override
    public List<String> getList(String key, long start, long end) {
        return jedisCluster.lrange(key, start, end);
    }

    @Override
    public List<String> keys(String pattern) {
        List<String> keys = getKeysByScan(jedisCluster, pattern);

        return keys;
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
    public double incrByScope(String key, double score, String member) {
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
    public boolean expire(String key, int seconds) {
        return jedisCluster.expire(key, seconds + RandomUtils.nextInt(0, 300)) != null;
    }

    @Override
    public long llen(String key) {
        return jedisCluster.llen(key);
    }

    @Override
    public boolean putSet(String key, double score, String value) {
        return jedisCluster.zadd(key, score, value) != 0;
    }

    @Override
    public Set<String> getSet(String key, long start, long end, String order) {
        if (StringUtil.isEmpty(order))
            order = "ASC";

        if ("ASC".equals(order))
            return jedisCluster.zrange(key, start, end);
        else if ("DESC".equals(order))
            return jedisCluster.zrevrange(key, start, end);

        return null;
    }

    @Override
    public long zcard(String key) {
        return jedisCluster.zcard(key);
    }

    @Override
    public boolean zrank(String key, String val) {
        return jedisCluster.zrank(key, val) != null;
    }

    @Override
    public long rmSet(String key, long start, long end) {
        return jedisCluster.zremrangeByRank(key, start, end);
    }

    @Override
    public boolean pfAdd(String key, String... value) {
        return jedisCluster.pfadd(key, value) != 0;
    }

    @Override
    public long pfCount(String key) {
        return jedisCluster.pfcount(key);
    }

    @Override
    public boolean setBit(String key, long offset, boolean value) {
        return jedisCluster.setbit(key, offset, value);
    }

    @Override
    public boolean getBit(String key, long offset) {
        return jedisCluster.getbit(key, offset);
    }

    @Override
    public Long bitCount(String key) {
        return jedisCluster.bitcount(key);
    }

    @Override
    public boolean eval(String script, List<String> keys, List<String> args) {
        return jedisCluster.eval(script, keys, args) != null;
    }

    @Override
    public boolean deletes(String pattern) {
        List<String> keys = keys(pattern);
        if (keys != null && keys.size() > 0) {
            keys.stream().forEach(key -> delete(key));
        }
        return false;
    }
}
