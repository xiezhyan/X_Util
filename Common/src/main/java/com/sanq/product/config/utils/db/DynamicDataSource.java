package com.sanq.product.config.utils.db;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * com.sanq.product.config.utils.db.DynamicDataSource
 *
 * @author sanq.Yan
 * @date 2020/1/16
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        return DynamicDataSourceHolder.getDataSouce();
    }
}
