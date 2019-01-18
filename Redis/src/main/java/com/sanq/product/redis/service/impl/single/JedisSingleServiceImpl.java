package com.sanq.product.redis.service.impl.single;

import com.sanq.product.redis.service.JedisPoolService;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Xiezhyan on 2019/1/18.
 */

@Service("jedisSingleService")
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
    public boolean rmList(String key, long start, long end) {
        String lockValue = _lockValue();

        try (Jedis jedis = jedisPool.getResource()) {
            boolean isLocked = _lock(jedis, key, lockValue);
            if (isLocked) {
                boolean ltrim = jedis.ltrim(key, start, end) != null;
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
    public Set<String> keys(String pattern) {
        try (Jedis jedis = jedisPool.getResource()) {
            Set<String> result = jedis.keys(pattern);

            return result;
        }
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
    public boolean pfAdd(String key, String... value) {
        String lockValue = _lockValue();

        try (Jedis jedis = jedisPool.getResource()) {
            boolean isLocked = _lock(jedis, key, lockValue);
            if (isLocked) {
                boolean result = jedis.pfadd(key, value) != null;
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
    public boolean setBit(String key, long offset, String value) {
        String lockValue = _lockValue();

        try (Jedis jedis = jedisPool.getResource()) {
            boolean isLocked = _lock(jedis, key, lockValue);
            if (isLocked) {
                boolean result = jedis.setbit(key, offset, value) != null;
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
    public boolean eval(String script, List<String> keys, List<String> args) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.eval(script, keys, args) != null;
        }
    }

    @Override
    public boolean deletes(String pattern) {
        Set<String> keys = keys(pattern);
        if(keys != null && keys.size() > 0) {
            for(String key: keys)
                delete(key);

            return true;
        }
        return false;
    }
}
