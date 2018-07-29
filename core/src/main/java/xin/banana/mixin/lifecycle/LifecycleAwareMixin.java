package xin.banana.mixin.lifecycle;

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
public interface LifecycleAwareMixin {

    default void runOnceOnDestroy(Runnable cancelAction) {
        LifeCycleAware.runOnLifeCycleOnce(this, LifeCycle.OnDestroy, cancelAction);
    }

    default void cancelOnDestroy(Runnable action) {
        runOnceOnDestroy(action);
    }

    default void runOnceOnPause(Runnable cancelAction) {
        LifeCycleAware.runOnLifeCycleOnce(this, LifeCycle.OnPause, cancelAction);
    }

    default void cancelOnPause(Runnable action) {
        runOnceOnPause(action);
    }

    default void runOnceOnStop(Runnable cancelAction) {
        LifeCycleAware.runOnLifeCycleOnce(this, LifeCycle.OnStop, cancelAction);
    }

    default void cancelOnStop(Runnable action) {
        runOnceOnStop(action);
    }
}
