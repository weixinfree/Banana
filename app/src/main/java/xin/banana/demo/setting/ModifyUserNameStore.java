package xin.banana.demo.setting;

import bolts.Continuation;
import bolts.Task;
import xin.banana.base.BiConsumer;
import xin.banana.binding.Variable;
import xin.banana.mixin.lifecycle.LifecycleAwareMixin;
import xin.banana.store.StoreMixin;

/**
 * $end$
 * Created by wangwei on 2018/08/01.
 */
public class ModifyUserNameStore implements StoreMixin, LifecycleAwareMixin {

    public static final int USER_NAME_MAX_LENGTH = 16;

    public static final int ACTION_SUBMIT = 0x01;
    public static final int ACTION_USER_INPUT_CHANGED = 0x02;

    public final Variable<String> userInputName = new Variable<>("");
    public final Variable<Boolean> modifySuccess = new Variable<>();

    public ModifyUserNameStore() {
        bindAction(ACTION_SUBMIT, (obj) ->
                Task.delay(1000)
                        .onSuccess((Continuation<Void, Void>) task -> {
                            modifySuccess.set(Math.random() > 0.5);
                            return null;
                        }, Task.UI_THREAD_EXECUTOR));

        bindAction(
                ACTION_USER_INPUT_CHANGED,
                userInputName,
                (BiConsumer<Variable<String>, String>) Variable::set);
    }
}
