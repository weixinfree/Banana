package xin.banana.service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import xin.banana.Banana;
import xin.banana.base.Supplier;

import static xin.banana.base.Objects.requireNonNull;

public class ServiceFetcher {

    /**
     * [
     * {
     * "service":"com.test.UserService",
     * "impl":"com.test.impl.UserServiceImpl"
     * },
     * {
     * "service":"com.test.FollowService",
     * "impl":"com.test.impl.FollowServiceImpl",
     * "singleton":false
     * }
     * ]
     */
    public static String sServiceAssetsDir = "services";

    private static final AtomicBoolean hasLoad = new AtomicBoolean(false);
    private static final ServiceConfigLoader sLoader = new ServiceConfigLoader();

    public synchronized static <T> T get(Class<T> clazz) {
        if (hasLoad.compareAndSet(false, true)) {
            sLoader.loadServicesFromAssetsConfig(Banana.getApplication())
                    .forEach_(item -> {
                        final Class<?> service = item.first;
                        final Supplier<?> implSupplier = item.second;
                        registerWithoutTypeRestrict(service, implSupplier);
                    });
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

    private synchronized static void registerWithoutTypeRestrict(Class<?> service, Supplier<?> implSupplier) {
        requireNonNull(service);
        requireNonNull(implSupplier);

        services.put(service, implSupplier);
    }
}
