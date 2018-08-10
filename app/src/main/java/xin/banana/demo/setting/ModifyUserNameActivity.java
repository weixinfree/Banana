package xin.banana.demo.setting;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import xin.banana.binding.Binding;
import xin.banana.mixin.lifecycle.LifecycleAwareMixin;
import xin.banana.ui.dsl.Muggle;

import static xin.banana.ui.dsl.Muggle.$dp;
import static xin.banana.ui.dsl.Muggle.leaf;
import static xin.banana.ui.dsl.Muggle.match_parent;
import static xin.banana.ui.dsl.Muggle.tree;
import static xin.banana.ui.dsl.Muggle.wrap_content;

/**
 * 用户修改名字
 * Created by wangwei on 2018/08/01.
 */
public class ModifyUserNameActivity extends Activity implements LifecycleAwareMixin {

    /*

    1. View + 胶水代码:
    Activity, Fragment, View 职责: 构建ViewTree，绑定，事件流触发Action

    2. MV* 中 * 部分的角色，由binding承担

    3. M 由store 以及 配置Store完成业务的代码部分承担
    */

    public static void start(Context context) {
        Intent starter = new Intent(context, ModifyUserNameActivity.class);
        context.startActivity(starter);
    }

    private final ModifyUserNameStore store = new ModifyUserNameStore();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        store.bindLifecycleTo(this);

        // auto unbind onDestroy
        final Binding binding = Binding.with(this);
        setContentView(
                container()
                        // input
                        .child(userInputView())
                        // 提示
                        .child(remindLenView(binding))
                        // 提交按钮
                        .child(submitButton(binding))
                        .getView());

        binding.bind(
                msg -> Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show(),
                store.modifySuccess.map(isSuccess -> isSuccess ? "修改成功" : "修改失败")
        );

    }

    private Muggle.Tree<LinearLayout, RelativeLayout.LayoutParams> container() {
        return tree(new LinearLayout(this), RelativeLayout.LayoutParams.class)
                .attrs(view -> {
                    view.setOrientation(LinearLayout.VERTICAL);
                    view.setGravity(Gravity.CENTER_HORIZONTAL);
                    view.setPadding($dp(16), 0, $dp(16), 0);
                })
                .layout(params -> {
                    params.width = match_parent;
                    params.height = match_parent;
                });
    }

    private Muggle.Leaf<Button, LinearLayout.LayoutParams> submitButton(Binding binding) {
        return leaf(new Button(this), LinearLayout.LayoutParams.class)
                .attrs(button -> {
                    button.setText("提交");
                    button.setOnClickListener(v ->
                            store.dispatch(ModifyUserNameStore.ACTION_SUBMIT, null));

                    binding.on(button)
                            .enabled(store.userInputName.map(userInput -> {
                                return !TextUtils.isEmpty(userInput) && userInput.length() >= 3;
                            }));
                })
                .layout(layoutParams -> {
                    layoutParams.width = match_parent;
                    layoutParams.height = wrap_content;
                    layoutParams.topMargin = $dp(10);
                });
    }

    @SuppressLint("RtlHardcoded")
    private Muggle.Leaf<TextView, LinearLayout.LayoutParams> remindLenView(Binding binding) {
        return leaf(new TextView(this), LinearLayout.LayoutParams.class)
                .layout(layoutParams -> {
                    layoutParams.width = match_parent;
                    layoutParams.height = wrap_content;
                    layoutParams.topMargin = $dp(10);
                    layoutParams.bottomMargin = $dp(10);
                })
                .attrs(textView -> {
                    textView.setGravity(Gravity.LEFT);
                    textView.setTextSize(12);
                    textView.setTextColor(Color.LTGRAY);

                    binding.on(textView).text(
                            store.userInputName.map(s -> {
                                final int len = s.length();
                                return String.format(Locale.US, "您已经输入%d字，还剩%d字", len, ModifyUserNameStore.USER_NAME_MAX_LENGTH - len);
                            }));
                });
    }

    private Muggle.Leaf<EditText, LinearLayout.LayoutParams> userInputView() {
        return leaf(new EditText(this), LinearLayout.LayoutParams.class)
                .attrs(view -> {
                    view.setTextSize(18);
                    view.setFilters(new InputFilter[]{new InputFilter.LengthFilter(ModifyUserNameStore.USER_NAME_MAX_LENGTH)});

                    view.addTextChangedListener(new TextWatcher() {
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
                    });
                })
                .layout(params -> {
                    params.height = wrap_content;
                    params.width = match_parent;
                });
    }
}
