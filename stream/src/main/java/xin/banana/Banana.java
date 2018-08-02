package xin.banana;

import android.app.Application;

import static xin.banana.base.Objects.checkState;
import static xin.banana.base.Objects.requireNonNull;

public class Banana {

    private Banana() {
        //no instance
    }

    private static volatile Application sApp;

    public static void install(Application application) {
        sApp = requireNonNull(application);
    }

    public static Application getApplication() {
        checkState(sApp != null, "Banana not installed, call Banana.install() first!");
        return sApp;
    }
}
