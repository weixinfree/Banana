package xin.banana.demo.setting;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputFilter;
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
 * $end$
 * Created by wangwei on 2018/08/01.
 */
public class ModifyUserNameActivity extends Activity implements LifecycleAwareMixin {

    public static void start(Context context) {
        Intent starter = new Intent(context, ModifyUserNameActivity.class);
        context.startActivity(starter);
    }

    private final ModifyUserNameStore store = new ModifyUserNameStore();

    @SuppressLint("RtlHardcoded")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        store.bindLifecycleTo(this);

        final Binding binding = Binding.with(this);

        setContentView(
                newContainer()
                        // input
                        .child(newUserInputView())
                        // 提示
                        .child(newRemindLenView(binding))
                        // 提交按钮
                        .child(newSubmitButton())
                        .asView());

        binding.bind(
                msg -> Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show(),
                store.modifySuccess,
                isSuccess -> isSuccess ? "修改成功" : "修改失败"
        );

    }

    private Muggle.Tree<LinearLayout, RelativeLayout.LayoutParams> newContainer() {
        return tree(new LinearLayout(this), RelativeLayout.LayoutParams.class)
                .attrs(view -> {
                    view.setOrientation(LinearLayout.VERTICAL);
                    view.setGravity(Gravity.CENTER_HORIZONTAL);
                })
                .layout(params -> {
                    params.width = match_parent;
                    params.height = match_parent;
                });
    }

    private Muggle.Leaf<Button, LinearLayout.LayoutParams> newSubmitButton() {
        return leaf(new Button(this), LinearLayout.LayoutParams.class)
                .attrs(button -> {
                    button.setText("提交");
                    button.setTextSize(20);
                    button.setOnClickListener(v ->
                            store.dispatch(ModifyUserNameStore.ACTION_SUBMIT, null));
                })
                .layout(layoutParams -> {
                    layoutParams.width = match_parent;
                    layoutParams.height = wrap_content;
                    layoutParams.topMargin = $dp(10);
                });
    }

    @SuppressLint("RtlHardcoded")
    private Muggle.Leaf<TextView, LinearLayout.LayoutParams> newRemindLenView(Binding binding) {
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

                    binding.on(textView).bind(
                            TextView::setText,
                            store.userInputName,
                            s -> {
                                final int len = s.length();
                                return String.format(Locale.US, "您已经输入%d字，还剩%d字", len, ModifyUserNameStore.USER_NAME_MAX_LENGTH - len);

                            });
                });
    }

    private Muggle.Leaf<EditText, LinearLayout.LayoutParams> newUserInputView() {
        return leaf(new EditText(this), LinearLayout.LayoutParams.class)
                .attrs(view -> {
                    view.setTextSize(18);
                    view.setFilters(new InputFilter[]{new InputFilter.LengthFilter(ModifyUserNameStore.USER_NAME_MAX_LENGTH)});
                    Binding.onTextChanged(view, editable -> store.dispatch(ModifyUserNameStore.ACTION_USER_INPUT_CHANGED, editable.toString()));
                })
                .layout(params -> {
                    params.height = wrap_content;
                    params.width = match_parent;
                });
    }
}
