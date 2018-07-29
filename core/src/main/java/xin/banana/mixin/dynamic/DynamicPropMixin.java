package xin.banana.mixin.dynamic;


import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.WeakHashMap;

import xin.banana.base.Supplier;

import static xin.banana.base.Maps.getOrDefaultWithoutSizeEffect;
import static xin.banana.base.Maps.putIfAbsent;

/**
 * 支持对象的动态属性, 对象销毁时，这些属性会自动回收
 * Created by wangwei on 2018/07/27.
 */
@SuppressWarnings("unused")
public interface DynamicPropMixin {

    // note that this is static
    Map<Object, Map<String, Object>> __Props__ = new WeakHashMap<>();

    default void setProp(String propName, Object value) {
        putIfAbsent(__Props__, this, LinkedHashMap::new).put(propName, value);
    }

    default <T> T getProp(String propName, Supplier<T> defaultValueSupplier) {
        final Map<String, Object> props = __Props__.get(this);
        if (props == null) {
            return defaultValueSupplier.get();
        }
        //noinspection unchecked
        return ((T) getOrDefaultWithoutSizeEffect(props, propName, ((Supplier<Object>) defaultValueSupplier)));
    }

    @SuppressWarnings("UnusedReturnValue")
    default boolean delProp(String propName) {
        final Map<String, Object> props = __Props__.get(this);
        return props != null && props.remove(propName) != null;
    }

    default <T> T getProp(String propName) {
        final Map<String, Object> props = __Props__.get(this);
        if (props == null) {
            return null;
        }

        final Object prop = props.get(propName);
        if (prop != null) {
            //noinspection unchecked
            return ((T) prop);
        }

        return null;
    }

    default boolean hasProp(String propName) {
        final Map<String, Object> props = __Props__.get(this);
        return props != null && props.containsKey(propName);
    }

    default Map<String, Object> props() {
        return __Props__.containsKey(this)
                ? Collections.unmodifiableMap(__Props__.get(this))
                : Collections.emptyMap();
    }

    default String dump() {
        return __Props__.toString();
    }
}
