package xin.banana.binding;

import android.view.View;

import java.util.LinkedList;
import java.util.List;

import xin.banana.base.BiConsumer;
import xin.banana.base.Consumer;
import xin.banana.base.Function;

import static xin.banana.base.Objects.requireNonNull;

/**
 * 数据绑定
 * Created by wangwei on 2018/08/01.
 */
public class Binding {

    public <V extends View> Binder<V> on(V view) {
        return new Binder<>(requireNonNull(view));
    }

    public <T> void bind(Consumer<? super T> action, Variable<T> variable) {
        requireNonNull(action);
        requireNonNull(variable);

        addUnBinder(variable.registerObserver(() -> action.accept(variable.get())));
    }

    public <T, R> void bind(Consumer<? super R> action, Variable<T> variable, Function<T, R> transform) {
        requireNonNull(action);
        requireNonNull(variable);
        requireNonNull(transform);

        addUnBinder(variable.registerObserver(() -> action.accept(transform.apply(variable.get()))));
    }

    private final List<Runnable> unBinders = new LinkedList<>();

    private void addUnBinder(Runnable unBinder) {
        unBinders.add(requireNonNull(unBinder));
    }

    public void unbind() {
        while (!unBinders.isEmpty()) {
            final Runnable unbind = unBinders.remove(0);
            unbind.run();
        }
    }

    public class Binder<V extends View> {

        private final V view;

        Binder(V view) {
            this.view = view;
        }

        public <T> Binder<V> bind(BiConsumer<V, ? super T> attrSetter, T value) {
            requireNonNull(attrSetter);
            requireNonNull(value);

            attrSetter.accept(view, value);
            return this;
        }

        public <T> Binder<V> bind(BiConsumer<V, ? super T> attrSetter, Variable<? extends T> variable) {
            requireNonNull(attrSetter);
            requireNonNull(variable);

            addUnBinder(variable.registerObserver(() -> attrSetter.accept(view, variable.get())));
            return this;
        }

        public <T, R> Binder<V> bind(BiConsumer<V, R> attrSetter, Variable<? extends T> variable, Function<T, ? extends R> transform) {
            requireNonNull(attrSetter);
            requireNonNull(variable);
            requireNonNull(transform);

            addUnBinder(variable.registerObserver(() -> attrSetter.accept(view, transform.apply(variable.get()))));
            return this;
        }

        @SuppressWarnings("unused")
        public <T> Binder<V> bind2(Consumer<? super T> action, Variable<? extends T> variable) {
            requireNonNull(action);
            requireNonNull(variable);

            addUnBinder(variable.registerObserver(() -> action.accept(variable.get())));
            return this;
        }

        public Binder<V> view(Consumer<V> consumer) {
            requireNonNull(consumer).accept(view);
            return this;
        }

        public V getView() {
            return view;
        }

        @SuppressWarnings("UnusedReturnValue")
        public Binder<V> onClick(View.OnClickListener onClickListener) {
            view.setOnClickListener(requireNonNull(onClickListener));
            return this;
        }
    }
}
