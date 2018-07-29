
# Banana

在Android工程中，使用Java8语言特性，提高生产力的Lib

主要提供了2部分功能：
1. 借助lambda表达式，实现的函数式编程工具类 Stream（类似 Java8 的 java.util.Stream）
2. 借助接口default方法和静态方法实现的Mixin（类似ruby 的mixin）。主要实现了 LifecycleAwareMixin，可以实现低侵入式的生命周期注入

### Stream
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


例子:

```
class Presenter implements LifecycleAwareMixin {
    
    public Presenter(Context context) {
        bindLifecycleOn(context);
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
