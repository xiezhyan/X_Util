package com.sanq.product.utils.es.config;

import java.util.Map;

public class Config {
    private Map<String, Object> map;

    public Config(Map<String, Object> map) {
        this.map = map;
    }

    public Map<String, Object> getMap() {
        return map;
    }

    public void setMap(Map<String, Object> map) {
        this.map = map;
    }
}
