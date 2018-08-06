package xin.banana.binding;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Property;
import android.view.View;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

import xin.banana.base.BiConsumer;
import xin.banana.base.Consumer;
import xin.banana.mixin.lifecycle.LifeCycle;
import xin.banana.mixin.lifecycle.LifeCycleAware;

import static xin.banana.base.Objects.checkState;
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

    private final List<Runnable> unBinders = new LinkedList<>();

    private void addUnBinder(Runnable unBinder) {
        unBinders.add(requireNonNull(unBinder));
    }

    @SuppressWarnings("WeakerAccess")
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

        @SuppressWarnings("UnusedReturnValue")
        public <T> Binder<V> bind(BiConsumer<V, ? super T> attrSetter, Variable<? extends T> variable) {
            requireNonNull(attrSetter);
            requireNonNull(variable);

            addUnBinder(variable.registerObserver(() -> attrSetter.accept(view, variable.get())));
            return this;
        }

        public <T> Binder<V> bind(Property<V, ? super T> propertySetter, Variable<? extends T> variable) {
            requireNonNull(propertySetter);
            return bind(propertySetter::set, variable);
        }

        @SuppressWarnings("UnusedReturnValue")
        public Binder<V> onClick(View.OnClickListener onClickListener) {
            view.setOnClickListener(requireNonNull(onClickListener));
            return this;
        }

        public Binder<V> enabled(Variable<? extends Boolean> variable) {
            return bind(View::setEnabled, variable);
        }

        public Binder<V> onLongClick(View.OnLongClickListener onLongClickListener) {
            view.setOnLongClickListener(requireNonNull(onLongClickListener));
            return this;
        }

        public Binder<V> text(Variable<? extends CharSequence> variable) {
            checkState(view instanceof TextView);
            return bind((v, text) -> ((TextView) v).setText(text), variable);
        }

        public Binder<V> afterTextChanged(Consumer<? super Editable> onText) {

            requireNonNull(onText);
            checkState(view instanceof TextView);

            ((TextView) view).addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    onText.accept(s);
                }
            });

            return this;
        }
    }
}
