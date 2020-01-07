package com.sanq.product.config.utils.worker;

/**
 * com.sanq.product.config.utils.worker.Id
 *
 * @author sanq.Yan
 * @date 2020/1/7
 */
public class Id {

    private SnowflakeWorker WORKER;
    private volatile static Id INSTANCE = null;

    private Id (long workerId, long datacenterId) {
        WORKER = new SnowflakeWorker(workerId, datacenterId);
    }

    public static Id getInstance(long workerId, long datacenterId) {
        if (null == INSTANCE) {
            synchronized (Id.class) {
                if (null == INSTANCE) {
                    INSTANCE = new Id(workerId, datacenterId);
                }
            }
        }
        return INSTANCE;
    }

    public Long getId() {
        return WORKER.nextId();
    }
}
