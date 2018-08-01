package xin.banana.base;

/**
 * 2 元素 tuple
 * Created by wangwei on 2018/08/01.
 */
public class Tuple3<T1, T2, T3> {

    public final T1 first;
    public final T2 second;
    public final T3 third;

    public Tuple3(T1 first, T2 second, T3 third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tuple3<?, ?, ?> tuple3 = (Tuple3<?, ?, ?>) o;
        return Objects.equals(first, tuple3.first) &&
                Objects.equals(second, tuple3.second) &&
                Objects.equals(third, tuple3.third);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(first) ^ Objects.hashCode(second) ^ Objects.hashCode(third);
    }

    @Override
    public String toString() {
        return "Tuple3{" +
                "first=" + first +
                ", second=" + second +
                ", third=" + third +
                '}';
    }
}
