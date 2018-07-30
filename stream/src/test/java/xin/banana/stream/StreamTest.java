package xin.banana.stream;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import xin.banana.base.Function;
import xin.banana.base.Optional;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

/**
 * $end$
 * Created by wangwei on 2018/07/28.
 */
public class StreamTest {

    @Test
    public void test_map() {
        assertThat(Stream.of(1, 2, 3)
                        .map(value -> value * 2)
                        .reduce(0, Operators::add),
                equalTo(12));

        assertThat(Stream.of("1", "2", "3")
                        .map(Integer::parseInt)
                        .reduce(0, Operators::add),
                equalTo(6));

        assertThat(Stream.of("1", "2", "3")
                        .map(Integer::parseInt)
                        .map(v -> v + 1)
                        .reduce(0, Operators::add),
                equalTo(9));

        assertThat(Stream.range(1, 4)
                        .reduce(0, Operators::add),
                equalTo(6));
    }

    @Test
    public void test_filter() {
        assertThat(Stream.range(0, 6)
                        .filter(v -> v % 2 == 0)
                        .toList(),
                equalTo(Arrays.asList(0, 2, 4)));

        final List<Object> list = Stream.of(1, "str", 1.0, new Object())
                .filter(v -> v instanceof String)
                .toList();
        assertThat(list, hasSize(1));
        assertThat(list, contains("str"));
    }

    @Test
    public void test_flatMap() {
        final List<Integer> list = Stream.of(Stream.range(1, 2), Stream.range(2, 3), Stream.range(3, 4))
                .flatMap(v -> v)
                .toList();

        assertThat(list, hasSize(3));
        assertThat(list, contains(1, 2, 3));
        assertThat(list, not(contains(4)));
    }

    @Test
    public void test_distinct() {
        final List<Integer> list = Stream.of(1, 2, 3, 1, 2, 3, 1, 2, 3, 4)
                .distinct()
                .toList();

        assertThat(list, hasSize(4));
        assertThat(list, contains(1, 2, 3, 4));
    }

    @Test
    public void test_peek() {
        final AtomicInteger peekCalled = new AtomicInteger(0);
        Stream.of(1, 2, 3)
                .peek(v -> peekCalled.incrementAndGet())
                .forEach_(System.out::println);

        assertThat(peekCalled.get(), equalTo(3));
    }

    @Test
    public void test_limit() {
        final List<Integer> list = Stream.iterate(1, integer -> integer + 1)
                .limit(10)
                .toList();

        assertThat(list, hasSize(10));
        assertThat(list, allOf(
                hasItem(1),
                hasItem(2),
                hasItem(3),
                hasItem(4),
                hasItem(10),
                not(hasItem(11))
        ));
    }

    @Test
    public void test_skip() {
        final List<Integer> list = Stream.iterate(1, Operators::inc)
                .skip(3)
                .limit(10)
                .toList();

        assertThat(list, hasSize(10));
        assertThat(list, allOf(
                hasItem(4),
                hasItem(5),
                hasItem(6),
                not(hasItem(3)),
                hasItem(13),
                not(hasItem(14))
        ));
    }

    @Test
    public void test_forEach() {
        final AtomicInteger called = new AtomicInteger(0);
        Stream.generate(() -> 1)
                .limit(10)
                .forEach_(called::addAndGet);

        assertThat(called.get(), equalTo(10));
    }

    @Test
    public void test_reduce() {
        assertThat(Stream.range(1, 11)
                        .reduce(0, Operators::add),
                equalTo(55));
    }

    @Test
    public void test_collect() {
        assertThat(Stream.range(0, 10)
                        .map(String::valueOf)
                        .collect(StringBuilder::new, StringBuilder::append)
                        .toString(),
                equalTo("0123456789"));
    }

    @Test
    public void test_toList() {

        final List<Integer> integers = Stream.of(1, 2, 3, 1, 2, 3, 4, 5).toList();
        assertThat(integers, hasSize(8));
        assertThat(integers, contains(1, 2, 3, 1, 2, 3, 4, 5));
    }

    @Test
    public void test_toSet() {
        final Stream<Integer> set = Stream.of(1, 2, 3, 4, 1, 2, 5, 6);
        assertThat(set.toSet(), hasSize(6));
        assertThat(set.toSet(), containsInAnyOrder(1, 2, 3, 4, 5, 6));
    }

    @Test
    public void test_min() {
        assertThat(Stream.range(0, 10)
                        .min(Integer::compare).get(),
                equalTo(0));

        assertThat(Stream.<Integer>empty().min(Integer::compare), equalTo(Optional.empty()));

        assertThat(Stream.of("a", "bb", "ccc", "dddd", "eeeee")
                        .min(Comparators.compareInt(String::length)).get(),
                equalTo("a"));
    }

    @Test
    public void test_max() {
        final Optional<Integer> max = Stream.range(0, 10)
                .max(Integer::compare);

        assertThat(max.get(), equalTo(9));

        assertThat(Stream.of("a", "bb", "ccc", "dddd", "eeeee")
                        .max(Comparators.compareInt(String::length)).get(),
                equalTo("eeeee"));
    }

    @Test
    public void test_count() {
        assertThat(Stream.range(0, 10).count(), equalTo(10L));
        assertThat(Stream.generate(() -> 1).limit(10).count(), equalTo(10L));
    }

    @Test
    public void test_anyMatch() {
        assertThat(Stream.range(0, 10).anyMatch(v -> v == 9), is(true));
    }

    @Test
    public void test_allMatch() {
        assertThat(Stream.range(0, 10).allMatch(v -> v < 10), is(true));
    }

    @Test
    public void test_noneMatch() {
        assertThat(Stream.range(0, 10).noneMatch(v -> v >= 10), is(true));
        assertThat(Stream.range(0, 10).noneMatch(v -> v < 9), is(false));
    }

    @Test
    public void test_findFirst() {
        assertThat(Stream.empty().findFirst(), equalTo(Optional.empty()));
        assertThat(Stream.range(0, 10).findFirst(), equalTo(Optional.of(0)));
        assertThat(Stream.of(null, "hello").filter(Objects::nonNull).findFirst(),
                equalTo(Optional.of("hello")));

        assertThat(Stream.of(1).findFirst(), equalTo(Optional.of(1)));
    }

    @Test
    public void test_empty() {
        final AtomicInteger called = new AtomicInteger(0);
        Stream.empty().forEach_(v -> called.incrementAndGet());

        assertThat(called.get(), equalTo(0));
    }

    @Test
    public void test_iterate() {
        final Long sum10 = Stream.iterate(1L, Operators::inc)
                .limit(10)
                .reduce(0L, Operators::add);

        assertThat(sum10, equalTo(55L));
    }

    @Test
    public void test_generate() {
        final Integer count = Stream.generate(() -> 1)
                .limit(100)
                .reduce(0, Operators::add);

        assertThat(count, equalTo(100));
    }

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

    @Test
    public void test_compose() {
        final Integer result = Stream.of(1, 2, 3)
                .concat(Stream.range(4, 10))
                .concat(Stream.iterate(10, Operators::inc).limit(10))
                .concat(Stream.generate(() -> -1).limit(10).skip(3))
                .filter(v -> v % 2 == 0)
                .map(Operators::inc)
                .reduce(0, Operators::add);

        System.out.println("result = " + result);
    }
}