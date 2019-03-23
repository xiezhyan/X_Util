package com.sanq.product.algorithm;

/**
 * Created by Xiezhyan on 2019/3/23.
 *
 * 判断是否为奇数
 */
public class Old {

    private static boolean isOdd(int i) {
        return (i & 1) == 1;
    }

    public static void main(String[] args) {

        System.out.println(isOdd(2));

    }
}
