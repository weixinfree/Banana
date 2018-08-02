package xin.banana.binding;

import android.os.Looper;

import java.util.ArrayList;
import java.util.List;

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

        if (mValue != null) {
            // immediately dispatch
            action.run();
        }

        return () -> observables.remove(action);
    }

    private void notifyObservables() {
        Stream.forEach_(observables, Runnable::run);
    }

    private static boolean isUIThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    @Override
    public String toString() {
        return "Variable{" +
                "mValue=" + mValue +
                '}';
    }
}
