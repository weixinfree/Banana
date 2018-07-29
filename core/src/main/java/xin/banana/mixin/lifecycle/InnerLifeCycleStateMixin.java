package xin.banana.mixin.lifecycle;

import android.support.annotation.NonNull;


import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import xin.banana.base.Supplier;
import xin.banana.mixin.dynamic.DynamicPropMixin;
import xin.banana.stream.Stream;

import static xin.banana.base.Objects.checkState;
import static xin.banana.base.Objects.requireNonNull;


/**
 *
 * Created by wangwei on 2018/07/27.
 */
interface InnerLifeCycleStateMixin extends DynamicPropMixin {

    default void registerOnceOnLifeCycleState(LifeCycle lifeCycle, Runnable action) {
        requireNonNull(lifeCycle);
        requireNonNull(action);

        final String propName = lifeCycle.name();

        final Set<Runnable> hooks = getProp(propName, (Supplier<Set<Runnable>>) LinkedHashSet::new);
        hooks.add(action);

        setProp(propName, hooks);
    }

    @NonNull
    default Iterable<Runnable> getAndClearActionsByLifecycle(LifeCycle lifeCycle) {
        final String propName = lifeCycle.name();

        final Set<Runnable> prop = getProp(propName, Collections::emptySet);
        // only work once, so once consume, del prop
        delProp(propName);

        checkState(prop != null);
        return prop;
    }

    default void callDestroyOnceHooks() {
        Stream.of(getAndClearActionsByLifecycle(LifeCycle.OnDestroy))
                .forEach_(Runnable::run);
    }

    default void callStopOnceHooks() {
        Stream.of(getAndClearActionsByLifecycle(LifeCycle.OnStop))
                .forEach_(Runnable::run);
    }

    default void callPauseOnceHooks() {
        Stream.of(getAndClearActionsByLifecycle(LifeCycle.OnPause))
                .forEach_(Runnable::run);
    }
}
