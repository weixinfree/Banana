package xin.banana.base;

/**
 * 2 元素 tuple
 * Created by wangwei on 2018/08/01.
 */
public class Tuple2<T1, T2> {

    public final T1 first;
    public final T2 second;

    public Tuple2(T1 first, T2 second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tuple2<?, ?> tuple2 = (Tuple2<?, ?>) o;
        return Objects.equals(first, tuple2.first) &&
                Objects.equals(second, tuple2.second);
    }

    @Override
    public int hashCode() {

        return Objects.hashCode(first) ^ Objects.hashCode(second);
    }

    @Override
    public String toString() {
        return "Tuple2{" +
                "first=" + first +
                ", second=" + second +
                '}';
    }
}
