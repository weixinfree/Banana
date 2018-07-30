package xin.banana.stream;

import xin.banana.base.Predicate;

/**
 * 常见的针对基础类型的Operator
 * <p>
 * Created by wangwei on 2018/07/28.
 */
public class Operators {

    private Operators() {
        //no instance
    }

    public static int add(int a, int b) {
        return a + b;
    }

    public static long add(long a, long b) {
        return a + b;
    }

    public static float add(float a, float b) {
        return a + b;
    }

    public static double add(double a, double b) {
        return a + b;
    }

    public static String add(String a, String b) {
        return a + b;
    }

    public static int inc(int a) {
        return a + 1;
    }

    public static long inc(long a) {
        return a + 1;
    }

    public static int dec(int a) {
        return a - 1;
    }

    public static long dec(long a) {
        return a - 1;
    }

    public static <T extends Number> Predicate<Number> gt(T num) {
        return v -> Double.compare(v.doubleValue(), num.doubleValue()) > 0;
    }

    public static <T extends Number> Predicate<Number> lt(T num) {
        return v -> Double.compare(v.doubleValue(), num.doubleValue()) < 0;
    }

    public static <T extends Number> Predicate<Number> eq(T num) {
        return v -> Double.compare(v.doubleValue(), num.doubleValue()) == 0;
    }

    public static <T extends Number> Predicate<Number> le(T num) {
        return v -> Double.compare(v.doubleValue(), num.doubleValue()) <= 0;
    }

    public static <T extends Number> Predicate<Number> ge(T num) {
        return v -> Double.compare(v.doubleValue(), num.doubleValue()) >= 0;
    }
}
