package com.sanq.product.config.utils.http;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Supplier;

/**
 * com.sanq.product.config.utils.futures.Future
 * <p>
 * 线程执行并集中返回结果
 *
 * @author sanq.Yan
 * @date 2019/8/6
 */
public class Future {

    private final WeakHashMap<String, Supplier> map;
    private Executor service;

    public Future(WeakHashMap<String, Supplier> map, Executor service) {
        this.map = map;
        this.service = service;
    }

    public Executor getService() {
        return service;
    }

    public WeakHashMap<String, Supplier> getMap() {
        return map;
    }


    public static class Builder {
        private WeakHashMap<String, Supplier> map;
        private Executor service;

        public Builder option(String key, Supplier supplier) {
            if (map == null || map.isEmpty())
                map = new WeakHashMap<>();

            map.put(key, supplier);
            return this;
        }

        public Builder setMap(WeakHashMap<String, Supplier> map) {
            this.map = map;
            return this;
        }

        public Builder setExecutor(Executor service) {
            this.service = service;
            return this;
        }

        public Future build() {
            return new Future(map, service);
        }
    }

    //最后的执行方法
    public Map<String, Object> run() {
        Map<String, Object> result = new HashMap<>();

        CompletableFuture[] cfs = getMap().entrySet()
                .stream()
                .map(entry -> {
                    CompletableFuture future;
                    if (service == null) future = CompletableFuture.supplyAsync(entry.getValue());
                    else future = CompletableFuture.supplyAsync(entry.getValue(), service);

                    return future.whenComplete((s, e) -> {
                        result.put(entry.getKey(), s);
                    });
                })
                .toArray(CompletableFuture[]::new);
        CompletableFuture.allOf(cfs).join();
        return result;
    }
}