package xin.banana.components;

import android.app.Application;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static xin.banana.base.Objects.requireNonNull;
import static xin.banana.stream.Stream.forEach_;

/**
 * 组件
 * Created by wangwei on 2018/07/30.
 */
public class Components {

    private Components() {
        //no instance
    }

    /**
     * [
     * {
     *      "component":"com.test.UserComponent",
     *      "initOrder": 1
     * },
     * {
     *      "component":"com.test.StorageComponent",
     *      "initOrder": 2
     * }
     * ]
     */
    public static String sComponentAssetsDir = "components";

    private static volatile List<Component> sComponents = Collections.emptyList();

    private static final AtomicBoolean hasLoad = new AtomicBoolean(false);
    private static final ComponentConfigLoader sLoader = new ComponentConfigLoader();

    public static void install(Application application) {
        requireNonNull(application);

        if (hasLoad.compareAndSet(false, true)) {
            sComponents = sLoader.loadComponentsFromAssetsConfig(application);
        }
    }

    public static void onUserLogin() {
        forEach_(sComponents, Component::onUserLoginSuccess);
    }

    public static void onUserLogout() {
        forEach_(sComponents, Component::onUserLogoutSuccess);
    }
}
