package com.sanq.product.algorithm;

/**
 * Created by Xiezhyan on 2019/3/23.
 * <p/>
 * 判断一个数是不是素数
 */
public class Primes {

    private static boolean isPrime(int number) {
        if (number == 2)
            return true;

        if (number < 2 || number % 2 == 0)
            return false;

        int v = (int) Math.sqrt(number + 1);

        for(int i = 3; i <= v; i+=2) {
            if(number % i == 0)
                return false;
        }

        return true;
    }

    public static void main(String[] args) {
        System.out.println(isPrime(21));
    }

}
