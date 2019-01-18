package com.sanq.product.generate;

/**
 * Created by Xiezhyan on 2018/11/26.
 */
public class AppMain {

    public static void main(String[] args) {
       String a = "com.seenlan.product.lucky";
        a = a.substring(a.lastIndexOf(".") + 1, a.length());
        System.out.print(a);
    }

}
