package xin.banana.mixin.lifecycle;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;

import static xin.banana.base.Objects.requireNonNull;


/**
 * Activity 生命周期感知
 * Created by wangwei on 2018/07/27.
 */
public class LifeCycleAware {

    public static void runOnLifeCycleOnce(Object obj, LifeCycle lifeCycle, Runnable cancelAction) {
        if (obj instanceof FragmentActivity) {
            runOnLifeCycleOnce(((FragmentActivity) obj).getSupportFragmentManager(), lifeCycle, cancelAction);
            return;
        }

        if (obj instanceof Activity) {
            runOnLifeCycleOnce(((Activity) obj).getFragmentManager(), lifeCycle, cancelAction);
            return;
        }

        if (obj instanceof Fragment) {
            runOnLifeCycleOnce(((Fragment) obj).getFragmentManager(), lifeCycle, cancelAction);
            return;
        }

        if (obj instanceof android.app.Fragment) {
            runOnLifeCycleOnce(((android.app.Fragment) obj).getFragmentManager(), lifeCycle, cancelAction);
            return;
        }

        if (obj instanceof View) {
            final Context context = ((View) obj).getContext();
            runOnLifeCycleOnce(context, lifeCycle, cancelAction);
            return;
        }


        throw new UnsupportedOperationException("can not cancelOnPause in " + obj + ", only support FragmentActivity and Fragment(support.v4)");

    }

    public static void runOnLifeCycleOnce(FragmentManager fragmentManager, LifeCycle lifeCycle, Runnable cancelAction) {
        requireNonNull(lifeCycle);
        requireNonNull(fragmentManager);
        requireNonNull(cancelAction);

        final String TAG = ">>>LifeCycleAware-Support<<<";

        final Fragment fragment = fragmentManager.findFragmentByTag(TAG);
        if (fragment == null) {
            final InnerLifecycleAwareSupportFragment lifecycleAware = new InnerLifecycleAwareSupportFragment();
            lifecycleAware.registerOnceOnLifeCycleState(lifeCycle, cancelAction);
            fragmentManager.beginTransaction().add(lifecycleAware, TAG).commitAllowingStateLoss();
        } else {
            ((InnerLifecycleAwareSupportFragment) fragment).registerOnceOnLifeCycleState(lifeCycle, cancelAction);
        }
    }

    public static void runOnLifeCycleOnce(android.app.FragmentManager fragmentManager, LifeCycle lifeCycle, Runnable cancelAction) {
        requireNonNull(lifeCycle);
        requireNonNull(fragmentManager);
        requireNonNull(cancelAction);

        final String TAG = ">>>LifeCycleAware<<<";

        final android.app.Fragment fragment = fragmentManager.findFragmentByTag(TAG);
        if (fragment == null) {
            final InnerLifecycleAwareFragment lifecycleAware = new InnerLifecycleAwareFragment();
            lifecycleAware.registerOnceOnLifeCycleState(lifeCycle, cancelAction);
            fragmentManager.beginTransaction().add(lifecycleAware, TAG).commitAllowingStateLoss();
        } else {
            ((InnerLifecycleAwareFragment) fragment).registerOnceOnLifeCycleState(lifeCycle, cancelAction);
        }
    }
}
