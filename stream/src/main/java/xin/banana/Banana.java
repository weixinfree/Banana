package xin.banana;

import android.app.Application;

import static xin.banana.base.Objects.requireNonNull;

/**
 * $end$
 * Created by wangwei on 2018/07/30.
 */
public class Banana {

    private Banana() {
        //no instance
    }

    private static volatile Application sApp;

    public static void install(Application application) {
        sApp = requireNonNull(application);
    }

    public static Application getApplication() {
        return sApp;
    }
}
