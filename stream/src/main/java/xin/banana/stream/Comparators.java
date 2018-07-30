package xin.banana.stream;

import java.util.Comparator;

import xin.banana.base.Function;

import static xin.banana.base.Objects.requireNonNull;

/**
 * $end$
 * Created by wangwei on 2018/07/28.
 */
public class Comparators {

    private Comparators() {
        //no instance
    }

    public static <T> Comparator<T> compareInt(Function<T, Integer> func) {
        requireNonNull(func);
        return (o1, o2) -> Integer.compare(func.apply(o1), func.apply(o2));
    }

    public static <T> Comparator<T> compareLong(Function<T, Long> func) {
        requireNonNull(func);
        return (o1, o2) -> Long.compare(func.apply(o1), func.apply(o2));
    }

    public static <T> Comparator<T> compareFloat(Function<T, Float> func) {
        requireNonNull(func);
        return (o1, o2) -> Float.compare(func.apply(o1), func.apply(o2));
    }

    public static <T> Comparator<T> compareDouble(Function<T, Double> func) {
        requireNonNull(func);
        return (o1, o2) -> Double.compare(func.apply(o1), func.apply(o2));
    }

    public static <T> Comparator<T> compareStr(Function<T, String> func) {
        requireNonNull(func);
        return (o1, o2) -> func.apply(o1).compareTo(func.apply(o2));
    }

    public static <T> Comparator<T> compareStrIgnoreCase(Function<T, String> func) {
        requireNonNull(func);
        return (o1, o2) -> func.apply(o1).compareToIgnoreCase(func.apply(o2));
    }

}
