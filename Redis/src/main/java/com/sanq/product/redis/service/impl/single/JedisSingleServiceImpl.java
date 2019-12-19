package com.sanq.product.redis.service.impl.single;

import com.sanq.product.config.utils.string.StringUtil;
import com.sanq.product.redis.service.JedisPoolService;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Service;
import redis.clients.jedis.*;

import javax.annotation.Resource;
import java.util.*;

@Service("jedisPoolService")
public class JedisSingleServiceImpl implements JedisPoolService {

    @Resource
    private JedisPool jedisPool;

    private String _lockValue() {
        return UUID.randomUUID().toString();
    }

    private boolean _lock(Jedis jedis, String key, String lockValue) {

        return jedis.set("lock:" + key + ":" + lockValue, lockValue) != null;
    }

    private void _unLock(Jedis jedis, String key, String lockValue) {
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        jedis.eval(script, Collections.singletonList("lock:" + key + ":" + lockValue), Collections.singletonList(lockValue));
    }


    @Override
    public void set(String key, String val) {
        String lockValue = _lockValue();

        try (Jedis jedis = jedisPool.getResource()) {
            boolean isLocked = _lock(jedis, key, lockValue);
            if (isLocked) {
                jedis.set(key, val);
                _unLock(jedis, key, lockValue);
            }
        }
    }

    @Override
    public void set(String key, String val, int seconds) {
        String lockValue = _lockValue();

        try (Jedis jedis = jedisPool.getResource()) {
            boolean isLocked = _lock(jedis, key, lockValue);
            if (isLocked) {
                jedis.setex(key, seconds + RandomUtils.nextInt(0, 300), val);
                _unLock(jedis, key, lockValue);
            }
        }
    }

    @Override
    public String get(String key) {
        String lockValue = _lockValue();

        try (Jedis jedis = jedisPool.getResource()) {
            boolean isLocked = _lock(jedis, key, lockValue);
            if (isLocked) {
                String result = jedis.get(key);
                _unLock(jedis, key, lockValue);

                return result;
            }
        }
        return null;
    }

    @Override
    public void deleteKey(String key) {
        String lockValue = _lockValue();

        try (Jedis jedis = jedisPool.getResource()) {
            boolean isLocked = _lock(jedis, key, lockValue);
            if (isLocked) {
                jedis.del(key);
                _unLock(jedis, key, lockValue);
            }
        }
    }

    @Override
    public long incr(String key) {
        String lockValue = _lockValue();

        try (Jedis jedis = jedisPool.getResource()) {
            boolean isLocked = _lock(jedis, key, lockValue);
            if (isLocked) {
                Long incr = jedis.incr(key);
                _unLock(jedis, key, lockValue);

                return incr;
            }
        }
        return 0;
    }

    @Override
    public long incrAtTime(String key, int second) {
        String lockValue = _lockValue();

        try (Jedis jedis = jedisPool.getResource()) {
            boolean isLocked = _lock(jedis, key, lockValue);
            if (isLocked) {
                Long incr = jedis.incr(key);
                expire(key, second);
                _unLock(jedis, key, lockValue);

                return incr;
            }
        }
        return 0;
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
    public long decr(String key) {
        String lockValue = _lockValue();

        try (Jedis jedis = jedisPool.getResource()) {
            boolean isLocked = _lock(jedis, key, lockValue);
            if (isLocked) {
                Long decr = jedis.decr(key);
                _unLock(jedis, key, lockValue);

                return decr;
            }
        }
        return 0;
    }

    @Override
    public long decrBy(String key, long minus) {
        String lockValue = _lockValue();

        try (Jedis jedis = jedisPool.getResource()) {
            boolean isLocked = _lock(jedis, key, lockValue);
            if (isLocked) {
                Long decr = jedis.decrBy(key, minus);
                _unLock(jedis, key, lockValue);

                return decr;
            }
        }
        return 0;
    }

    @Override
    public boolean existsKeys(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.exists(key);
        }
    }

    @Override
    public void putListR(String key, String...values) {
        String lockValue = _lockValue();

        try (Jedis jedis = jedisPool.getResource()) {
            boolean isLocked = _lock(jedis, key, lockValue);
            if (isLocked) {
                jedis.rpush(key, values);
                _unLock(jedis, key, lockValue);
            }
        }
    }


    @Override
    public void deleteList(String key, long start, long end) {
        String lockValue = _lockValue();

        try (Jedis jedis = jedisPool.getResource()) {
            boolean isLocked = _lock(jedis, key, lockValue);
            if (isLocked) {
                jedis.ltrim(key, start, end);
                _unLock(jedis, key, lockValue);
            }
        }
    }

    @Override
    public List<String> getList(String key, long start, long end) {
        String lockValue = _lockValue();

        try (Jedis jedis = jedisPool.getResource()) {
            boolean isLocked = _lock(jedis, key, lockValue);
            if (isLocked) {
                List<String> result = jedis.lrange(key, start, end);
                _unLock(jedis, key, lockValue);

                return result;
            }
        }
        return null;
    }

    @Override
    public List<String> keys(String pattern) {
        try (Jedis jedis = jedisPool.getResource()) {

            return getKeysByScan(jedis, pattern);
        }
    }

    private List<String> getKeysByScan(Jedis jedis, String pattern) {
        ScanParams params = new ScanParams();
        params.match(pattern);
        params.count(100);

        String cursor = "0";

        List<String> keys = new ArrayList<>();

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

        return keys;
    }

    @Override
    public void expire(String key, int seconds) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.expire(key, seconds + RandomUtils.nextInt(0, 300));
        }
    }

    @Override
    public long getListSize(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.llen(key);
        }
    }

    @Override
    public void putSortedSet(String key, double score, String value) {
        String lockValue = _lockValue();

        try (Jedis jedis = jedisPool.getResource()) {
            boolean isLocked = _lock(jedis, key, lockValue);
            if (isLocked) {
                jedis.zadd(key, score, value);
                _unLock(jedis, key, lockValue);

            }
        }
    }

    @Override
    public Set<String> getSortedSet(String key, long start, long end, String order) {
        String lockValue = _lockValue();

        if (StringUtil.isEmpty(order))
            order = "ASC";

        try (Jedis jedis = jedisPool.getResource()) {
            boolean isLocked = _lock(jedis, key, lockValue);
            if (isLocked) {
                Set<String> set = null;
                if ("ASC".equals(order)) {
                    set = jedis.zrange(key, start, end);
                } else if ("DESC".equals(order)) {
                    set = jedis.zrevrange(key, start, end);
                }
                _unLock(jedis, key, lockValue);
                return set;
            }
        }
        return null;
    }

    @Override
    public long getSortedSetSize(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.zcard(key);
        }
    }

    @Override
    public boolean itemExistsSortedSet(String key, String val) {
        String lockValue = _lockValue();

        try (Jedis jedis = jedisPool.getResource()) {
            boolean isLocked = _lock(jedis, key, lockValue);
            if (isLocked) {
                boolean result = jedis.zrank(key, val) != null;
                _unLock(jedis, key, lockValue);
                return result;
            }
        }
        return false;
    }

    @Override
    public double getScopeByItem(String key, String member) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.zscore(key, member);
        }
    }

    @Override
    public void deleteSortedSet(String key, long start, long end) {
        String lockValue = _lockValue();

        try (Jedis jedis = jedisPool.getResource()) {
            boolean isLocked = _lock(jedis, key, lockValue);
            if (isLocked) {
                jedis.zremrangeByRank(key, start, end);
                _unLock(jedis, key, lockValue);
            }
        }
    }

    @Override
    public double updateScopeByItem(String key, double score, String member) {
        String lockValue = _lockValue();

        try (Jedis jedis = jedisPool.getResource()) {
            boolean isLocked = _lock(jedis, key, lockValue);
            if (isLocked) {
                double result = jedis.zincrby(key, score, member);
                _unLock(jedis, key, lockValue);
                return result;
            }
        }
        return 0;
    }

    @Override
    public void pfAdd(String key, String... value) {
        String lockValue = _lockValue();

        try (Jedis jedis = jedisPool.getResource()) {
            boolean isLocked = _lock(jedis, key, lockValue);
            if (isLocked) {
                jedis.pfadd(key, value);
                _unLock(jedis, key, lockValue);
            }
        }
    }

    @Override
    public long pfCount(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.pfcount(key);
        }
    }

    @Override
    public void setBit(String key, long offset, boolean value) {
        String lockValue = _lockValue();

        try (Jedis jedis = jedisPool.getResource()) {
            boolean isLocked = _lock(jedis, key, lockValue);
            if (isLocked) {
                jedis.setbit(key, offset, value);
                _unLock(jedis, key, lockValue);
            }
        }
    }

    @Override
    public boolean getBit(String key, long offset) {
        String lockValue = _lockValue();

        try (Jedis jedis = jedisPool.getResource()) {
            boolean isLocked = _lock(jedis, key, lockValue);
            if (isLocked) {
                Boolean result = jedis.getbit(key, offset);
                _unLock(jedis, key, lockValue);

                return result;
            }
        }
        return false;
    }

    @Override
    public Long getBitCount(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.bitcount(key);
        }
    }

    @Override
    public Object eval(String script, List<String> keys, List<String> args) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.eval(script, keys, args);
        }
    }

    @Override
    public void deleteKeys(String pattern) {
        List<String> keys = keys(pattern);
        if (keys != null && keys.size() > 0) {
            keys.stream().filter(key -> !StringUtil.isEmpty(key)).forEach(this::deleteKey);
        }
    }

    @Override
    public void putListL(String key, String...values) {
        String lockValue = _lockValue();

        try (Jedis jedis = jedisPool.getResource()) {
            boolean isLocked = _lock(jedis, key, lockValue);
            if (isLocked) {
                jedis.lpush(key, values);
                _unLock(jedis, key, lockValue);
            }
        }
    }

    @Override
    public void deleteListItem(String key, String value) {
        String lockValue = _lockValue();

        try (Jedis jedis = jedisPool.getResource()) {
            boolean isLocked = _lock(jedis, key, lockValue);
            if (isLocked) {
                jedis.lrem(key, 0, value);
                _unLock(jedis, key, lockValue);
            }
        }
    }

    @Override
    public void putHash(String key, Map<String, String> map) {
        String lockValue = _lockValue();

        try (Jedis jedis = jedisPool.getResource()) {
            boolean isLocked = _lock(jedis, key, lockValue);
            if (isLocked) {
                jedis.hmset(key, map);
                _unLock(jedis, key, lockValue);
            }
        }
    }

    @Override
    public void putHash(String key, String field, String value) {
        String lockValue = _lockValue();

        try (Jedis jedis = jedisPool.getResource()) {
            boolean isLocked = _lock(jedis, key, lockValue);
            if (isLocked) {
                jedis.hset(key, field, value);
                _unLock(jedis, key, lockValue);
            }
        }
    }

    @Override
    public Map<String, String> getHash(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.hgetAll(key);
        }
    }

    @Override
    public String getHash(String key, String field) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.hget(key, field);
        }
    }

    @Override
    public List<String> getHash(String key, String... fields) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.hmget(key, fields);
        }
    }

    @Override
    public void deleteHash(String key, String... fields) {
        String lockValue = _lockValue();

        try (Jedis jedis = jedisPool.getResource()) {
            boolean isLocked = _lock(jedis, key, lockValue);
            if (isLocked) {
                jedis.hdel(key, fields);
                _unLock(jedis, key, lockValue);
            }
        }
    }

    @Override
    public void deleteSortedSet(String key, double start, double end) {
        String lockValue = _lockValue();

        try (Jedis jedis = jedisPool.getResource()) {
            boolean isLocked = _lock(jedis, key, lockValue);
            if (isLocked) {
                jedis.zremrangeByScore(key, start, end);
                _unLock(jedis, key, lockValue);
            }
        }
    }

    @Override
    public void deleteSortedSetItem(String key, String... members) {
        String lockValue = _lockValue();

        try (Jedis jedis = jedisPool.getResource()) {
            boolean isLocked = _lock(jedis, key, lockValue);
            if (isLocked) {
                jedis.zrem(key, members);
                _unLock(jedis, key, lockValue);
            }
        }
    }

    @Override
    public void select(int dbIndex) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.select(dbIndex);
        }
    }

    @Override
    public void move(String key, int dbIndex) {
        String lockValue = _lockValue();

        try (Jedis jedis = jedisPool.getResource()) {
            boolean isLocked = _lock(jedis, key, lockValue);
            if (isLocked) {
                jedis.move(key, dbIndex);
                _unLock(jedis, key, lockValue);
            }
        }
    }

    @Override
    public void putGeo(String key, Map<String, GeoCoordinate> map) {
        String lockValue = _lockValue();

        try (Jedis jedis = jedisPool.getResource()) {
            boolean isLocked = _lock(jedis, key, lockValue);
            if (isLocked) {
                jedis.geoadd(key, map);
                _unLock(jedis, key, lockValue);
            }
        }
    }

    @Override
    public void putGeo(String key, double longitude, double latitude, String member) {
        String lockValue = _lockValue();

        try (Jedis jedis = jedisPool.getResource()) {
            boolean isLocked = _lock(jedis, key, lockValue);
            if (isLocked) {
                jedis.geoadd(key, longitude, latitude, member);
                _unLock(jedis, key, lockValue);
            }
        }
    }

    @Override
    public List<GeoCoordinate> getGeo(String key, String... member) {
        String lockValue = _lockValue();
        try (Jedis jedis = jedisPool.getResource()) {
            boolean isLocked = _lock(jedis, key, lockValue);
            if (isLocked) {
                List<GeoCoordinate> list = jedis.geopos(key, member);
                _unLock(jedis, key, lockValue);
                return list;
            }
            return null;
        }
    }

    @Override
    public List<GeoRadiusResponse> getGeo(String key, String member, double radius) {
        String lockValue = _lockValue();
        try (Jedis jedis = jedisPool.getResource()) {
            boolean isLocked = _lock(jedis, key, lockValue);
            if (isLocked) {
                List<GeoRadiusResponse> list = jedis.georadiusByMember(key, member, radius, GeoUnit.KM);
                _unLock(jedis, key, lockValue);
                return list;
            }
            return null;
        }
    }

    @Override
    public List<GeoRadiusResponse> getGeo(String key, double longitude, double latitude, double radius) {
        String lockValue = _lockValue();
        try (Jedis jedis = jedisPool.getResource()) {
            boolean isLocked = _lock(jedis, key, lockValue);
            if (isLocked) {
                List<GeoRadiusResponse> list = jedis.georadius(key, longitude, latitude, radius, GeoUnit.KM);
                _unLock(jedis, key, lockValue);
                return list;
            }
            return null;
        }
    }

    @Override
    public double getDist(String key, String member1, String member2) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.geodist(key, member1, member2);
        }
    }
}
