# Banana

包含2个主要的部分：类似java8 Stream的函数式编程api； 以及 利用Java8新语法实现的Mixin。

### 如何在Android工程中开启Java8支持
[java8-support](https://developer.android.com/studio/write/java8-support)

### Stream like
```
    @Test
    public void test_concat() {
        final List<Object> list = Stream.empty()
                .concat(Stream.range(0, 10))
                .concat(Stream.range(10, 30))
                .concat(Stream.range(30, 90))
                .concat(Stream.of(90, 91, 92))
                .concat(Stream.iterate(93, Operators::inc).limit(7))
                .toList();

        assertThat(list, hasSize(100));
        assertThat(list, allOf(
                hasItem(0),
                hasItem(10),
                hasItem(30),
                hasItem(31),
                hasItem(32),
                hasItem(33),
                hasItem(90),
                hasItem(91),
                hasItem(92),
                hasItem(99),
                not(hasItem(100))
        ));   
    }
```
