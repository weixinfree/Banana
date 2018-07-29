package xin.banana.demo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import xin.banana.mixin.lifecycle.LifecycleAwareMixin;

public class MainActivity extends Activity implements LifecycleAwareMixin {

    private static final String TAG = ">>>MainActivity<<<";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        runOnceOnDestroy(() -> Log.d(TAG, "this is will run on Destroy"));

        runOnceOnStop(() -> Log.d(TAG, "this is will run on Stop"));

        runOnceOnPause(() -> Log.d(TAG, "this is will run on pause"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "MainActivity.onDestroy");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "MainActivity.onStop");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "MainActivity.onPause");
    }
}
