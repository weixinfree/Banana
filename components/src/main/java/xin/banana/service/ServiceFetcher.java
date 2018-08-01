package xin.banana.service;

import android.app.Application;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import xin.banana.Banana;
import xin.banana.base.Supplier;

import static xin.banana.base.Objects.requireNonNull;

/**
 * $end$
 * Created by wangwei on 2018/07/30.
 */
public class ServiceFetcher {

    // config loader
    // api 文件

    private static final AtomicBoolean hasLoadConfig = new AtomicBoolean(false);

    /**
     * [
     * {
     * "service":"com.test.UserService",
     * "impl":"com.test.impl.UserServiceImpl"
     * },
     * {
     * "service":"com.test.FollowService",
     * "impl":"com.test.impl.FollowServiceImpl"
     * }
     * ]
     */
    public static String sServiceAssetsDir = "services";

    private static void loadFromAsserts(Application app) {
//        Utils.readAssetsFiles(app, sServiceAssetsDir)
//                .flatMap()
    }

    public synchronized static <T> T get(Class<T> clazz) {

        if (hasLoadConfig.compareAndSet(false, true)) {
            loadFromAsserts(Banana.getApplication());
        }

        final Supplier<?> supplier = services.get(requireNonNull(clazz));
        //noinspection unchecked
        return supplier != null ? (T) supplier.get() : null;
    }

    private static final Map<Class<?>, Supplier<?>> services = new HashMap<>();

    public synchronized static <T> void register(Class<T> service, Supplier<? extends T> implSupplier) {
        requireNonNull(service);
        requireNonNull(implSupplier);

        services.put(service, implSupplier);
    }
}
