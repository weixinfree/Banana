package xin.banana.mixin.lifecycle;

import xin.banana.mixin.dynamic.DynamicPropMixin;

import static xin.banana.base.Objects.requireNonNull;

/**
 * Activity 组件生命周期可感知的 Mixin
 * <p>
 * 可以使用的上下文:
 * 1. Activity
 * 2. Fragment
 * 3. View
 * <p>
 * Created by wangwei on 2018/07/27.
 */
@SuppressWarnings("unused")
public interface LifecycleAwareMixin extends DynamicPropMixin {

    default void bindLifecycleTo(Object obj) {
        setProp("__lifecycle_aware_obj__", requireNonNull(obj));
    }

    default Object __getLifeCycleAwareObj() {
        final LifecycleAwareMixin captureThis = this;
        return getProp("__lifecycle_aware_obj__", () -> captureThis);
    }

    default void runOnceOnCreate(Runnable cancelAction) {
        LifeCycleAware.runOnLifeCycleOnce(__getLifeCycleAwareObj(), LifeCycle.OnCreate, cancelAction);
    }

    default void runOnceOnStart(Runnable cancelAction) {
        LifeCycleAware.runOnLifeCycleOnce(__getLifeCycleAwareObj(), LifeCycle.OnStart, cancelAction);
    }

    default void runOnceOnResume(Runnable cancelAction) {
        LifeCycleAware.runOnLifeCycleOnce(__getLifeCycleAwareObj(), LifeCycle.OnResume, cancelAction);
    }

    default void runOnceOnDestroy(Runnable cancelAction) {
        LifeCycleAware.runOnLifeCycleOnce(__getLifeCycleAwareObj(), LifeCycle.OnDestroy, cancelAction);
    }

    default void cancelOnDestroy(Runnable action) {
        runOnceOnDestroy(action);
    }

    default void runOnceOnPause(Runnable cancelAction) {
        LifeCycleAware.runOnLifeCycleOnce(__getLifeCycleAwareObj(), LifeCycle.OnPause, cancelAction);
    }

    default void cancelOnPause(Runnable action) {
        runOnceOnPause(action);
    }

    default void runOnceOnStop(Runnable cancelAction) {
        LifeCycleAware.runOnLifeCycleOnce(__getLifeCycleAwareObj(), LifeCycle.OnStop, cancelAction);
    }

    default void cancelOnStop(Runnable action) {
        runOnceOnStop(action);
    }
}
