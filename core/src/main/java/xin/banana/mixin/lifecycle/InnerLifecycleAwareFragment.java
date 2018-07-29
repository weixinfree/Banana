package xin.banana.mixin.lifecycle;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by wangwei on 2018/07/27.
 */
public class InnerLifecycleAwareFragment extends Fragment implements InnerLifeCycleStateMixin {

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
