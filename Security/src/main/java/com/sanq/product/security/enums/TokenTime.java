package com.sanq.product.security.enums;

public enum  TokenTime {

    TOKEN_TIME(10 * 60);

    private int time;

    TokenTime(int time) {
        this.time = time;
    }

    public int getTime() {
        return time;
    }
}
