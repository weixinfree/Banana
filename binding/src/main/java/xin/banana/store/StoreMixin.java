package xin.banana.store;

import android.support.annotation.CallSuper;

import xin.banana.base.BiConsumer;
import xin.banana.base.Consumer;
import xin.banana.binding.Variable;
import xin.banana.mixin.dynamic.DynamicPropMixin;

/**
 * Store Mixin
 * <p>
 * 统一的管理状态，业务逻辑部分
 * <p>
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

    default <Var, ActionData> void bindAction(int action, Variable<Var> val, BiConsumer<Variable<Var>, ActionData> update) {
        setProp("_bind_" + action, (Consumer<ActionData>) (ActionData actionData) -> update.accept(val, actionData));
    }

    default <ActionData> void bindAction(int action, Consumer<? extends ActionData> update) {
        setProp("_bind_" + action, update);
    }
}
