package com.sanq.product.redis.service.impl.single;

import com.sanq.product.config.utils.string.StringUtil;
import com.sanq.product.redis.service.JedisPoolService;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by Xiezhyan on 2019/1/18.
 */

@Service("jedisPoolService")
public class JedisSingleServiceImpl implements JedisPoolService {

    private static final String SET_IF_NOT_EXIST = "NX";
    private static final String SET_WITH_EXPIRE_TIME = "PX";
    private static final int LOCK_EXPIRE_TIME = 200;

    @Resource
    private JedisPool jedisPool;

    private String _lockValue() {
        return UUID.randomUUID().toString();
    }

    private boolean _lock(Jedis jedis, String key, String lockValue) {
        StringBuffer sb = new StringBuffer("lock:").append(key).append(":").append(lockValue);
        return jedis.set(sb.toString(), lockValue, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, LOCK_EXPIRE_TIME) != null;
    }

    private boolean _unLock(Jedis jedis, String key, String lockValue) {
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        StringBuffer sb = new StringBuffer("lock:").append(key).append(":").append(lockValue);
        return jedis.eval(script, Collections.singletonList(sb.toString()), Collections.singletonList(lockValue)) != null;
    }


    @Override
    public boolean set(String key, String val) {
        String lockValue = _lockValue();

        try (Jedis jedis = jedisPool.getResource()) {
            boolean isLocked = _lock(jedis, key, lockValue);
            if (isLocked) {
                boolean result = jedis.set(key, val) != null;
                _unLock(jedis, key, lockValue);

                return result;
            }
            return false;
        }
    }

    @Override
    public boolean set(String key, String val, int seconds) {
        String lockValue = _lockValue();

        try (Jedis jedis = jedisPool.getResource()) {
            boolean isLocked = _lock(jedis, key, lockValue);
            if (isLocked) {
                boolean result = jedis.setex(key, seconds, val) != null;
                _unLock(jedis, key, lockValue);

                return result;
            }
            return false;
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
    public boolean delete(String... key) {
        String lockValue = _lockValue();

        try (Jedis jedis = jedisPool.getResource()) {
            boolean isLocked = _lock(jedis, key[0], lockValue);
            if (isLocked) {
                boolean result = jedis.del(key) != null;
                _unLock(jedis, key[0], lockValue);

                return result;
            }
        }
        return false;
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
        if (!exists(key)) {
            set(key, max + "");
        }
        return incr(key);
    }

    @Override
    public boolean exists(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.exists(key);
        }
    }

    /**
     * 右进
     *
     * @param key
     * @param value
     * @return
     */
    @Override
    public long putList(String key, String value) {
        String lockValue = _lockValue();

        try (Jedis jedis = jedisPool.getResource()) {
            boolean isLocked = _lock(jedis, key, lockValue);
            if (isLocked) {
                Long result = jedis.rpush(key, value);
                _unLock(jedis, key, lockValue);

                return result;
            }
        }
        return 0;
    }


    @Override
    public boolean rmList(String key, long count) {
        String lockValue = _lockValue();

        try (Jedis jedis = jedisPool.getResource()) {
            boolean isLocked = _lock(jedis, key, lockValue);
            if (isLocked) {
                boolean ltrim = jedis.ltrim(key, count, -1) != null;
                _unLock(jedis, key, lockValue);

                return ltrim;
            }
        }
        return false;
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

            List<String> keys = getKeysByScan(jedis, pattern);

            return keys;
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

            cursor = scan.getStringCursor();

            List<String> result = scan.getResult();
            if(result != null && result.size() > 0) {
                keys.addAll(result);
            }

            if("0".equals(cursor))
            	continue;

        } while (!"0".equals(cursor));

        return keys;
    }

    @Override
    public boolean expire(String key, int seconds) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.expire(key, seconds) != null;
        }
    }

    @Override
    public long llen(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.llen(key);
        }
    }

    @Override
    public boolean putSet(String key, double score, String value) {
        String lockValue = _lockValue();

        try (Jedis jedis = jedisPool.getResource()) {
            boolean isLocked = _lock(jedis, key, lockValue);
            if (isLocked) {
                boolean result = jedis.zadd(key,score,  value) != 0;
                _unLock(jedis, key, lockValue);

                return result;
            }
        }
        return false;
    }

    @Override
    public Set<String> getSet(String key, long start, long end, String order) {
        String lockValue = _lockValue();

        if(StringUtil.isEmpty(order))
            order = "ASC";

        try (Jedis jedis = jedisPool.getResource()) {
            boolean isLocked = _lock(jedis, key, lockValue);
            if (isLocked) {
                Set<String> set = null;
                if("ASC".equals(order)) {
                    set = jedis.zrange(key, start, end);
                } else if("DESC".equals(order)) {
                    set = jedis.zrevrange(key, start, end);
                }
                _unLock(jedis, key, lockValue);
                return set;
            }
        }
        return null;
    }

    @Override
    public long zcard(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.zcard(key);
        }
    }

    @Override
    public boolean zrank(String key, String val) {
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
    public long rmSet(String key, long start, long end) {
        String lockValue = _lockValue();

        try (Jedis jedis = jedisPool.getResource()) {
            boolean isLocked = _lock(jedis, key, lockValue);
            if (isLocked) {
                long result = jedis.zremrangeByRank(key, start, end);
                _unLock(jedis, key, lockValue);
                return result;
            }
        }
        return 0;
    }

    @Override
    public boolean pfAdd(String key, String... value) {
        String lockValue = _lockValue();

        try (Jedis jedis = jedisPool.getResource()) {
            boolean isLocked = _lock(jedis, key, lockValue);
            if (isLocked) {
                boolean result = jedis.pfadd(key, value) != 0;
                _unLock(jedis, key, lockValue);

                return result;
            }
        }
        return false;
    }

    @Override
    public long pfCount(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.pfcount(key);
        }
    }

    @Override
    public boolean setBit(String key, long offset, boolean value) {
        String lockValue = _lockValue();

        try (Jedis jedis = jedisPool.getResource()) {
            boolean isLocked = _lock(jedis, key, lockValue);
            if (isLocked) {
                boolean result = jedis.setbit(key, offset, value);
                _unLock(jedis, key, lockValue);

                return result;
            }
        }
        return false;
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
    public Long bitCount(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.bitcount(key);
        }
    }

    @Override
    public boolean eval(String script, List<String> keys, List<String> args) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.eval(script, keys, args) != null;
        }
    }

    @Override
    public boolean deletes(String pattern) {
        List<String> keys = keys(pattern);
        if(keys != null && keys.size() > 0) {
            for(String key: keys)
                delete(key);

            return true;
        }
        return false;
    }
}
