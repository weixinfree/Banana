package xin.banana.demo.setting;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import xin.banana.binding.Binding;
import xin.banana.mixin.lifecycle.LifecycleAwareMixin;

/**
 * $end$
 * Created by wangwei on 2018/08/01.
 */
public class ModifyUserNameActivity extends Activity implements LifecycleAwareMixin {

    public static void start(Context context) {
        Intent starter = new Intent(context, ModifyUserNameActivity.class);
        context.startActivity(starter);
    }

    private final ModifyUserNameStore store = new ModifyUserNameStore();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        store.bindLifecycleTo(this);

        Binding binding = new Binding();
        cancelOnDestroy(binding::unbind);


        final LinearLayout container = new LinearLayout(this);
        container.setOrientation(LinearLayout.VERTICAL);

        final EditText userInput = new EditText(this);
        binding.on(userInput)
                .view(view -> view.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        store.dispatch(ModifyUserNameStore.ACTION_USER_INPUT_CHANGED, s.toString());
                    }
                }));
        container.addView(userInput);

        final TextView inputLength = new TextView(this);
        binding.on(inputLength)
                .bind(TextView::setText, store.userInputName, str -> {
                    final int len = str.length();
                    return String.format(Locale.US, "您已经输入%d字，还剩%d字", len, ModifyUserNameStore.USER_NAME_MAX_LENGTH - len);
                });
        container.addView(inputLength);

        final Button button = new Button(this);
        binding.on(button)
                .bind(Button::setEnabled, store.userInputName, input -> input.length() > 3)
                .bind(Button::setText, "提交")
                .onClick(v -> store.dispatch(ModifyUserNameStore.ACTION_SUBMIT, null));
        container.addView(button);

        // 修改成功，提示
        binding.bind(msg -> {
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        }, store.modifySuccess, isSuccess -> isSuccess ? "修改成功" : "修改失败");

        setContentView(container);
    }
}
