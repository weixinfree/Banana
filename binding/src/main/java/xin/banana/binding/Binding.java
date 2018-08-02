package xin.banana.binding;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

import xin.banana.base.BiConsumer;
import xin.banana.base.Consumer;
import xin.banana.base.Function;
import xin.banana.mixin.lifecycle.LifeCycle;
import xin.banana.mixin.lifecycle.LifeCycleAware;

import static xin.banana.base.Objects.requireNonNull;

/**
 * 数据绑定
 * Created by wangwei on 2018/08/01.
 */
@SuppressWarnings("unused")
public class Binding {

    public static Binding with(Object lifeCycleAwareObj) {
        requireNonNull(lifeCycleAwareObj);

        final Binding binding = new Binding();
        LifeCycleAware.runOnLifeCycleOnce(lifeCycleAwareObj, LifeCycle.OnDestroy, binding::unbind);
        return binding;
    }

    @SuppressWarnings("WeakerAccess")
    public Binding() {
    }

    public <V extends View> Binder<V> on(V view) {
        return new Binder<>(requireNonNull(view));
    }

    public <T> void bind(Consumer<? super T> action, Variable<T> variable) {
        requireNonNull(action);
        requireNonNull(variable);

        addUnBinder(variable.registerObserver(() -> action.accept(variable.get())));
    }

    public <T, A> void bind(Consumer<? super A> action, Variable<T> variable, Function<T, A> transform) {
        requireNonNull(action);
        requireNonNull(variable);
        requireNonNull(transform);

        addUnBinder(variable.registerObserver(() -> action.accept(transform.apply(variable.get()))));
    }

    public static <V extends TextView> void onTextChanged(V text, Consumer<Editable> consumer) {
        requireNonNull(text);
        requireNonNull(consumer);
        
        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                consumer.accept(s);
            }
        });
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
