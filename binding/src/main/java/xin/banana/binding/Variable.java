package xin.banana.binding;

import android.os.Handler;
import android.os.Looper;

import java.util.ArrayList;
import java.util.List;

import xin.banana.base.Function;
import xin.banana.base.Objects;
import xin.banana.stream.Stream;

import static xin.banana.base.Objects.checkState;
import static xin.banana.base.Objects.requireNonNull;

/**
 * 可观察的变量
 * Created by wangwei on 2018/08/01.
 */
public class Variable<T> {

    private T mValue;

    @SuppressWarnings("unused")
    public Variable() {
    }

    public Variable(T defaultValue) {
        this.mValue = defaultValue;
    }

    public void post(T newValue) {
        new Handler(Looper.getMainLooper()).post(() -> this.set(newValue));
    }

    public void set(T newValue) {
        checkState(isUIThread());
        if (mValue == newValue || Objects.equals(mValue, newValue)) return;

        mValue = newValue;
        notifyObservables();
    }

    public T get() {
        return mValue;
    }

    private final List<Runnable> observables = new ArrayList<>();

    public Runnable registerObserver(Runnable action) {

        observables.add(requireNonNull(action));

        if (get() != null) {
            // immediately dispatch
            action.run();
        }

        return () -> observables.remove(action);
    }

    private void notifyObservables() {
        Stream.forEach_(observables, Runnable::run);
    }

    private static boolean isUIThread() {
        return Thread.currentThread() == Looper.getMainLooper().getThread();
    }

    public <Result> Variable<Result> map(Function<T, ? extends Result> transformer) {
        final Variable<Result> var = new Variable<Result>() {

            @Override
            public Result get() {
                final T value = Variable.this.get();
                if (value == null) {
                    return null;
                }
                return transformer.apply(value);
            }

            @Override
            public void set(Result newValue) {
                throw new UnsupportedOperationException();
            }

            @Override
            public void post(Result newValue) {
                throw new UnsupportedOperationException();
            }
        };

        registerObserver(var::notifyObservables);
        return var;
    }

    @Override
    public String toString() {
        return "Variable{" +
                "mValue=" + mValue +
                '}';
    }
}
