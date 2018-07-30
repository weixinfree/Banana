package xin.banana.mixin.lifecycle;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

/**
 * Created by wangwei on 2018/07/27.
 */
public class InnerLifecycleAwareSupportFragment extends Fragment implements InnerLifeCycleStateMixin {

    @Override
    public void onDestroy() {
        super.onDestroy();
        callOnDestroyOnceHooks();
    }

    @Override
    public void onStop() {
        super.onStop();
        callOnStopOnceHooks();
    }

    @Override
    public void onPause() {
        super.onPause();
        callOnPauseOnceHooks();
    }

    @Override
    public void onResume() {
        super.onResume();
        callOnResumeOnceHooks();
    }

    @Override
    public void onStart() {
        super.onStart();
        callOnStartOnceHooks();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        callOnCreateOnceHooks();
    }
}
