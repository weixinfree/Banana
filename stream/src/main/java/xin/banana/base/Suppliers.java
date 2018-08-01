package xin.banana.base;

import static xin.banana.base.Objects.requireNonNull;

/**
 * $end$
 * Created by wangwei on 2018/08/01.
 */
public class Suppliers {

    private Suppliers() {
        //no instance
    }

    public static <T> Supplier<T> singleton(Supplier<? extends T> supplier) {
        requireNonNull(supplier);

        return new Supplier<T>() {

            final Object lock = new Object();
            private T result;

            @Override
            public T get() {
                synchronized (lock) {
                    if (result == null) {
                        result = supplier.get();
                    }

                    return result;
                }
            }
        };

    }
}
