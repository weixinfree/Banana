
# Banana

### TODO
1. 定位改变，Android上的类库
2. Component + ServiceFetcher + (Glide/网络库/storage) + 动态dsl描述的reactive view体系 + module内的架构模式(状态管理)
3. bolts + stream 异步编程模型 替代java
4. mixin
5. 图片加载和管理
6. 网络库
7. binding/ increment dom

### 使用

[![](https://jitpack.io/v/weixinfree/Banana.svg)](https://jitpack.io/#weixinfree/Banana)

```
# 1. add this in project root build.gradle
allprojects {
    repositories {
                 ...
                 maven { url 'https://jitpack.io' }
    }
}

# 2. add implementation in module build.gradle
dependencies {
    implementation 'com.github.weixinfree.Banana:stream:0.0.2'
    implementation 'com.github.weixinfree.Banana:mixin:0.0.2'
}
```

### Stream

利用Java8 lambda实现的 java.util.Stream api like 库

```
final Integer result = Stream.of(1, 2, 3)
        .concat(Stream.range(4, 10))
        .concat(Stream.iterate(10, Operators::inc).limit(10))
        .concat(Stream.generate(() -> -1).limit(10).skip(3))
        .filter(v -> v % 2 == 0)
        .map(Operators::inc)
        .reduce(0, Operators::add);

System.out.println("result = " + result); // result = 99
```

### LifecycleAwareMixin

使用Java8 Interface default method 和 static method 实现的生命周期mixin。
可以方便的将Activity生命周期注入到各种业务上下文中

*例子1*

```
class Presenter implements LifecycleAwareMixin {
    
    public Presenter(Context context) {
        bindLifecycleTo(context);
    }
    
    void requestNetwork() {
        
        final Canncellable cancelRequest = networkCalls();
        
        cancelOnDestroy(canceRequest::cancel);
        
        # cancelOnStop
        # cancelOnPause
        
        ...
    
    }
}
```

*例子2:*
```
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
```
