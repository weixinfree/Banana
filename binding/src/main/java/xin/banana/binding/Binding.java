package xin.banana.binding;

import android.view.View;

import java.util.ArrayList;
import java.util.List;

import xin.banana.base.BiConsumer;
import xin.banana.base.Consumer;
import xin.banana.stream.Stream;

import static xin.banana.base.Objects.requireNonNull;

/**
 * 数据绑定
 * Created by wangwei on 2018/08/01.
 */
public class Binding {

    public <V extends View> Binder<V> on(V view) {
        return new Binder<>(requireNonNull(view));
    }

    private final List<Runnable> unBinders = new ArrayList<>();

    private void addUnBinder(Runnable unBinder) {
        unBinders.add(requireNonNull(unBinder));
    }

    public void unbind() {
        Stream.forEach_(unBinders, Runnable::run);
    }

    public class Binder<V extends View> {

        private final V view;

        Binder(V view) {
            this.view = view;
        }

        public <T> Binder<V> bind(BiConsumer<V, ? super T> attrSetter, Variable<? extends T> variable) {
            requireNonNull(attrSetter);
            requireNonNull(variable);

            addUnBinder(variable.registerObserver(() -> attrSetter.accept(view, variable.get())));
            return this;
        }

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

        public Binder<V> onClick(View.OnClickListener onClickListener) {
            view.setOnClickListener(requireNonNull(onClickListener));
            return this;
        }
    }
}
