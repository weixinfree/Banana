package xin.banana.store;

import android.support.annotation.CallSuper;

import xin.banana.base.Consumer;
import xin.banana.mixin.dynamic.DynamicPropMixin;

import static xin.banana.base.Objects.requireNonNull;

/**
 * Store Mixin
 * 统一的管理状态，业务逻辑部分，单向数据流
 * Created by wangwei on 2018/08/01.
 */
public interface StoreMixin extends DynamicPropMixin {

    @CallSuper
    default void dispatch(int action, Object value) {
        final Consumer<Object> update = getProp("_bind_" + action);
        if (update != null) {
            update.accept(value);
        }
    }

    default <ActionData> void bindAction(int action, Consumer<? extends ActionData> update) {
        requireNonNull(update);

        setProp("_bind_" + action, update);
    }
}
