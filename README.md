
# Banana

## 移动开发的几个重要方面:
### 1. 组件化/解耦
- 移动端最大的挑战在于快速的变化
- 编程最大的敌人是复杂度
- 移动端的复杂度主要来源于：业务纷繁复杂，业务需求快速变化
- 业务的变化是发散的，倾向于弥漫发散，探索性质。需求特性和系统的整体性，内聚性是相悖的
- 移动端的业务深度比较浅，数据经过后台接口的整合里，距离ui非常近了，业务逻辑会相对简单
- 简单的业务逻辑，应该用简单直白的代码实现（典型的mvp模板套路太多，喧宾夺主）
- 独立是应对变化的大杀器
- 快速变化的另一个推论是: 可理解,可维护性十分重要
- 移动端编码第一原则是 清晰，而后是简单
- 在快速的变化中保持清晰
    1. 结构上要模块独立，以不变应对变化，降低关联改动风险
    2. DRY 优先级要降低，适度的copy 要比 不清晰的依赖关系更可取
    3. 独立，业务代码不要追求通用性，满足当前的需求，自己的需求即可
    4. 因为快速的发散式变化，移动业务几乎不需要为预想的需求预留设计冗余
    5. 越特殊的问题，越容易解决，假设更多，解集空间更小, 变化更少. 移动业务代码应该利用好这一点
 - 类比: 移动端类似颗粒物理 - 例如：沙堆
    - 看似独立的颗粒数量非常多 / 业务繁杂, 几乎所有的入口
    - 系统不够稳定，颗粒之间关系松散，多变 / 快速的变化
    - 系统可预测性比较低 / 业务需求演变倾向于探索式 和 发散变化
    - 长程关联很少出现（后台业务），各种尺度的短程关联动态变化 / 各组件之间会相互关联，但深度关联较少


*=> 组件化解决方案: `Components + ServiceFetcher` （类似微信方案）*

### 2. 非四大组件上下文中对生命周期的需求
- mixin 是组合的一种简化形式
- mixin 模式是最适合的生命周期注入方式
*=> 生命周期注入方案: `LifecycleAwareMixin`*
### 3. 单一module内的代码组织和职责划分
- reactive view 趋势性, vue, react, flutter ... 等等
- reactive view 模式 相较于 passive View 模式更简洁清晰，生产力更高 => `Binding` / `Muggle`
- 复杂业务下，单向数据流更容易管理和理解 => `StoreMixin`
- 系统内业务的另一个倾向是相互关联，store center 提供了简洁可控的 数据/状态层面的关联基础 => `StoreMixin`

## 其它技术选型倾向
 (简洁,清晰,高实现质量 最重要，性能次之，功能多/复杂度高的排除):
1. 网络库 => retrofit
2. 图像库 => glide
3. 持久化 => json-base 或者方便的 Room，sugar等
4. 存储管理 => storage
5. 线程管理 => bolts-task
6. 函数式编程 => `stream`

## 生产力TODO:
1. view 的在线预览，调试
2. 部分动态化

## TODO
1. 引用
2. 各个模块的说明示例代码


### demo - modify user name

Store where hold states and deal business actions

```java
public class ModifyUserNameStore implements StoreMixin, LifecycleAwareMixin {

    public static final int USER_NAME_MAX_LENGTH = 16;

    public static final int ACTION_SUBMIT = 0x01;
    public static final int ACTION_USER_INPUT_CHANGED = 0x02;

    /*
    expose 给View层的是 可观察的Variable
    内部状态 大部分应该为 pojo

    final! final! final!
     */
    public final Variable<String> userInputName = new Variable<>("");
    public final Variable<Boolean> modifySuccess = new Variable<>();

    ModifyUserNameStore() {

        /*
        store.dispatch 是单向数据流
        但是非 Redux 模式的不可变对象

        binding 模式的 reactive view 适合可变化对象
         */
        bindAction(ACTION_SUBMIT, (obj) -> {

                    /*
                    选择 bolts + stream 代替 RxJava
                    10%的复杂度，覆盖80%的功能
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

        bindAction(
                ACTION_USER_INPUT_CHANGED,
                userInputName,
                (BiConsumer<Variable<String>, String>) Variable::set);
    }
}
```

Activity - build user interface and binding, trigger business actions - glue code 

```java
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
                store.modifySuccess,
                isSuccess -> isSuccess ? "修改成功" : "修改失败"
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
                            .bind(Button::setEnabled, store.userInputName, userInput -> !TextUtils.isEmpty(userInput) && userInput.length() >= 3);
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

                    binding.on(textView).bind(
                            TextView::setText,
                            store.userInputName,
                            s -> {
                                final int len = s.length();
                                return String.format(Locale.US, "您已经输入%d字，还剩%d字", len, ModifyUserNameStore.USER_NAME_MAX_LENGTH - len);

                            });
                });
    }

    private Muggle.Leaf<EditText, LinearLayout.LayoutParams> userInputView() {
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
```
