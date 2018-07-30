package xin.banana.base;

import java.util.Map;

import static xin.banana.base.Objects.checkState;
import static xin.banana.base.Objects.requireNonNull;

/**
 * Map 工具类
 * Created by wangwei on 2018/07/28.
 */
public class Maps {

    public static <K, V> V putIfAbsent(Map<K, V> map, K key, Supplier<V> defaultValueSupplier) {
        requireNonNull(map);

        if (!map.containsKey(key)) {
            final V v = defaultValueSupplier.get();
            map.put(key, v);

            checkState(map.containsKey(key));
            return v;
        }

        return map.get(key);
    }

    public static <K, V> V getOrDefaultWithoutSizeEffect(Map<K, V> map, K key, Supplier<V> defaultValueSupplier) {
        requireNonNull(map);

        if (!map.containsKey(key)) {
            final V v = defaultValueSupplier.get();
            map.put(key, v);

            requireNonNull(map.containsKey(key));
            return v;
        }

        return map.get(key);
    }
}
