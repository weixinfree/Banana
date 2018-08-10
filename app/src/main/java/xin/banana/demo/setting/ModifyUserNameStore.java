package xin.banana.demo.setting;

import bolts.CancellationTokenSource;
import bolts.Continuation;
import bolts.Task;
import xin.banana.base.BiConsumer;
import xin.banana.binding.Variable;
import xin.banana.mixin.lifecycle.LifecycleAwareMixin;
import xin.banana.store.StoreMixin;

/**
 * 用户修改名字
 * Created by wangwei on 2018/08/01.
 */
public class ModifyUserNameStore implements StoreMixin, LifecycleAwareMixin {

    public static final int USER_NAME_MAX_LENGTH = 16;

    public static final int ACTION_SUBMIT = 0x01;
    public static final int ACTION_USER_INPUT_CHANGED = 0x02;

    /* expose 给View层的是 可观察的Variable 内部状态 大部分应该为 pojo*/
    public final Variable<String> userInputName = new Variable<>("");
    public final Variable<Boolean> modifySuccess = new Variable<>();

    ModifyUserNameStore() {
        /*
         store.dispatch 是单向数据流
         但是非 Redux 模式的不可变对象
         binding 模式的 reactive view 适合可变化对象
        */
        bindAction(ACTION_SUBMIT, (obj) -> {

            /* 选择 bolts + stream 代替 RxJava 10%的复杂度，覆盖80%的功能
             */
            final CancellationTokenSource cancelToken = new CancellationTokenSource();
            Task.delay(1000)
                    .onSuccess((Continuation<Void, Void>) task -> {
                        modifySuccess.set(Math.random() > 0.5);
                        return null;
                    }, Task.UI_THREAD_EXECUTOR, cancelToken.getToken());

            /*
             生命周期注入: 恰当的时候进行cancel
             */
            cancelOnDestroy(cancelToken::cancel);
        });

        bindAction(ACTION_USER_INPUT_CHANGED, userInputName::set);
    }
}
