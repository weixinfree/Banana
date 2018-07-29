package xin.banana.mixin.lifecycle;

import android.support.v4.app.Fragment;

/**
 * Created by wangwei on 2018/07/27.
 */
public class InnerLifecycleAwareSupportFragment extends Fragment implements InnerLifeCycleStateMixin {

    @Override
    public void onDestroy() {
        super.onDestroy();
        callDestroyOnceHooks();
    }

    @Override
    public void onStop() {
        super.onStop();
        callStopOnceHooks();
    }

    @Override
    public void onPause() {
        super.onPause();
        callPauseOnceHooks();
    }
}
