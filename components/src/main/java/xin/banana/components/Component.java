package xin.banana.components;

import android.app.Application;
import android.content.ComponentCallbacks2;
import android.content.res.Configuration;

public interface Component extends ComponentCallbacks2 {

    default void onAppCreate(@SuppressWarnings("unused") Application application) {

    }

    default void onUserLoginSuccess() {

    }

    default void onUserLogoutSuccess() {

    }

    @Override
    default void onTrimMemory(int level) {

    }

    @Override
    default void onLowMemory() {

    }

    @Override
    default void onConfigurationChanged(Configuration newConfig) {

    }
}
