package com.sanq.product.security.enums;

import org.apache.commons.lang3.RandomUtils;

public enum  TokenTime {

    /**
     * 10分钟
     */
    TOKEN_TIME(10 * 60),    //10分钟
    /**
     * 30分钟
     */
    THIRTY_MINUTES(30 * 60),

    /**
     * 5分钟
     */
    FIVE_MINUTES(5 * 60),

    /**
     * 2分钟
     */
    TWO_MINUTES(2 * 60);

    private int time;

    TokenTime(int time) {
        this.time = time;
    }

    public int getTime() {
        return time + RandomUtils.nextInt(0, 300);
    }
}
