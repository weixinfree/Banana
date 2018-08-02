package xin.banana.demo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import xin.banana.Banana;
import xin.banana.base.BiConsumer;
import xin.banana.binding.Binding;
import xin.banana.binding.Variable;
import xin.banana.demo.setting.ModifyUserNameActivity;
import xin.banana.mixin.lifecycle.LifecycleAwareMixin;

public class MainActivity extends Activity implements LifecycleAwareMixin {

    private static final String TAG = ">>>MainActivity<<<";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Banana.install(getApplication());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView text = findViewById(R.id.text);

        final Binding binding = Binding.with(this);
        final Variable<String> nameVar = new Variable<>("click me");

        binding.on(text)
                .bind((BiConsumer<TextView, ? super String>) TextView::setText, nameVar)
                .onClick(v -> {
                    nameVar.set(String.valueOf(Math.random()));
                    ModifyUserNameActivity.start(v.getContext());
                });

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
