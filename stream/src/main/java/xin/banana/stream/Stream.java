package xin.banana.stream;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import xin.banana.base.BiConsumer;
import xin.banana.base.BinaryOperator;
import xin.banana.base.Consumer;
import xin.banana.base.Function;
import xin.banana.base.Optional;
import xin.banana.base.Predicate;
import xin.banana.base.Supplier;
import xin.banana.base.UnaryOperator;

import static xin.banana.base.Objects.checkArgument;
import static xin.banana.base.Objects.requireNonNull;

/**
 * 函数表达式
 * Created by wangwei on 2018/07/28.
 */
@SuppressWarnings("unused")
public interface Stream<T> extends Iterable<T> {

    static <T, R> Stream<R> map(Iterable<? extends T> iterable, Function<? super T, ? extends R> mapper) {
        requireNonNull(iterable);
        requireNonNull(mapper);

        final Iterator<? extends T> sourceIter = iterable.iterator();

        return () -> new Iterator<R>() {
            @Override
            public boolean hasNext() {
                return sourceIter.hasNext();
            }

            @Override
            public R next() {
                return mapper.apply(sourceIter.next());
            }
        };
    }

    default <R> Stream<R> map(Function<? super T, ? extends R> mapper) {
        return map(this, mapper);
    }


    static <T> Stream<T> filter(Iterable<? extends T> iterable, Predicate<? super T> predicate) {
        requireNonNull(iterable);
        requireNonNull(predicate);

        final Iterator<? extends T> sourceIter = iterable.iterator();

        return () -> new Iterator<T>() {

            private T next;

            @Override
            public boolean hasNext() {
                while (sourceIter.hasNext()) {
                    final T sourceNext = sourceIter.next();
                    if (predicate.test(sourceNext)) {
                        next = sourceNext;
                        return true;
                    }
                }
                return false;
            }

            @Override
            public T next() {
                return next;
            }
        };
    }

    default Stream<T> filter(Predicate<? super T> predicate) {
        return filter(this, predicate);
    }

    static <T, R> Stream<R> flatMap(Iterable<? extends T> iterable, Function<? super T, ? extends Iterable<? extends R>> mapper) {
        requireNonNull(iterable);
        requireNonNull(mapper);

        final Iterator<? extends T> outIter = iterable.iterator();

        return () -> new Iterator<R>() {

            private R next;
            private Iterator<? extends R> innerIterator;

            @Override
            public boolean hasNext() {

                if (innerIterator != null && innerIterator.hasNext()) {
                    next = innerIterator.next();
                    return true;
                }

                while (outIter.hasNext()) {
                    final Iterator<? extends R> iterator = mapper.apply(outIter.next()).iterator();
                    if (iterator.hasNext()) {
                        innerIterator = iterator;
                        next = iterator.next();
                        return true;
                    }
                }

                return false;
            }

            @Override
            public R next() {
                return next;
            }
        };
    }

    default <R> Stream<R> flatMap(Function<? super T, ? extends Iterable<? extends R>> mapper) {
        return flatMap(this, mapper);
    }

    static <T> Stream<T> distinct(Iterable<? extends T> iterable) {
        requireNonNull(iterable);

        final Set<T> ds = new HashSet<>();
        final Iterator<? extends T> iter = iterable.iterator();

        return () -> new Iterator<T>() {

            private T next;

            @Override
            public boolean hasNext() {
                while (iter.hasNext()) {
                    final T _next = iter.next();
                    if (ds.add(_next)) {
                        next = _next;
                        return true;
                    }
                }
                return false;
            }

            @Override
            public T next() {
                return next;
            }
        };
    }

    default Stream<T> distinct() {
        return distinct(this);
    }

    static <T> Stream<T> peek(Iterable<? extends T> iterable, Consumer<? super T> action) {
        requireNonNull(iterable);
        requireNonNull(action);

        final Iterator<? extends T> iter = iterable.iterator();

        return () -> new Iterator<T>() {
            @Override
            public boolean hasNext() {
                return iter.hasNext();
            }

            @Override
            public T next() {
                final T next = iter.next();
                action.accept(next);
                return next;
            }
        };
    }

    default Stream<T> peek(Consumer<? super T> action) {
        return peek(this, action);
    }

    static <T> Stream<T> limit(Iterable<? extends T> iterable, long maxSize) {
        requireNonNull(iterable);
        checkArgument(maxSize >= 0);

        final Iterator<? extends T> iter = iterable.iterator();

        return () -> new Iterator<T>() {

            private int index = 0;
            private T next;

            @Override
            public boolean hasNext() {
                return iter.hasNext() && index++ < maxSize;
            }

            @Override
            public T next() {
                checkArgument(index <= maxSize);
                return iter.next();
            }
        };

    }

    default Stream<T> limit(long maxSize) {
        return limit(this, maxSize);
    }

    static <T> Stream<T> skip(Iterable<? extends T> iterable, long n) {
        requireNonNull(iterable);
        checkArgument(n >= 0);

        final Iterator<? extends T> iter = iterable.iterator();

        return () -> new Iterator<T>() {

            private int index = 0;

            @Override
            public boolean hasNext() {
                while (index < n && iter.hasNext()) {
                    iter.next();
                    index++;
                }
                return iter.hasNext();
            }

            @Override
            public T next() {
                return iter.next();
            }
        };

    }

    default Stream<T> skip(long n) {
        return skip(this, n);
    }

    static <T> void forEach_(Iterable<? extends T> iterable, Consumer<? super T> action) {
        requireNonNull(iterable);
        requireNonNull(action);

        for (T value : iterable) {
            action.accept(value);
        }
    }

    default void forEach_(Consumer<? super T> action) {
        forEach_(this, action);
    }

    static <T> T reduce(Iterable<T> iterable, T identity, BinaryOperator<T> accumulator) {
        requireNonNull(iterable);
        requireNonNull(identity);
        requireNonNull(accumulator);

        T result = identity;
        for (T value : iterable) {
            result = accumulator.apply(result, value);
        }

        return result;
    }

    default T reduce(T identity, BinaryOperator<T> accumulator) {
        return reduce(this, identity, accumulator);
    }

    static <T, R> R collect(Iterable<? extends T> iterable,
                            Supplier<R> supplier,
                            BiConsumer<R, ? super T> accumulator) {

        requireNonNull(iterable);
        requireNonNull(supplier);
        requireNonNull(accumulator);

        final R acc = supplier.get();
        for (T value : iterable) {
            accumulator.accept(acc, value);
        }

        return acc;
    }

    default <R> R collect(Supplier<R> supplier, BiConsumer<R, ? super T> accumulator) {

        return collect(this, supplier, accumulator);
    }

    static <T> List<T> toList(Iterable<? extends T> iterable) {
        requireNonNull(iterable);

        if (iterable instanceof List) {
            //noinspection unchecked
            return Collections.unmodifiableList(((List<T>) iterable));
        }

        return Collections.unmodifiableList(
                collect(iterable, ArrayList::new, ArrayList::add)
        );
    }

    default List<T> toList() {
        return toList(this);
    }

    static <T> Set<T> toSet(Iterable<? extends T> iterable) {
        requireNonNull(iterable);

        if (iterable instanceof Set) {
            //noinspection unchecked
            return Collections.unmodifiableSet(((Set<T>) iterable));
        }

        return Collections.unmodifiableSet(
                collect(iterable, HashSet::new, HashSet::add)
        );
    }

    default Set<T> toSet() {
        return toSet(this);
    }

    static <T> Optional<T> min(Iterable<? extends T> iterable, Comparator<? super T> comparator) {
        requireNonNull(iterable);
        requireNonNull(comparator);

        boolean findAny = false;
        T result = null;
        for (T value : iterable) {
            if (!findAny) {
                findAny = true;
                result = value;
            } else {
                final boolean isValueSmaller = comparator.compare(value, result) < 0;
                if (isValueSmaller) {
                    result = value;
                }
            }
        }

        return findAny ? Optional.of(result) : Optional.empty();
    }

    default Optional<T> min(Comparator<? super T> comparator) {
        return min(this, comparator);
    }

    static <T> Optional<T> max(Iterable<? extends T> iterable, Comparator<? super T> comparator) {
        return min(iterable, Collections.reverseOrder(comparator));
    }

    default Optional<T> max(Comparator<? super T> comparator) {
        return max(this, comparator);
    }

    static long count(Iterable<?> iterable) {
        int count = 0;
        for (Object v : iterable) {
            count += 1;
        }
        return count;
    }

    default long count() {
        return count(this);
    }

    static <T> boolean anyMatch(Iterable<? extends T> iterable, Predicate<? super T> predicate) {
        requireNonNull(iterable);
        requireNonNull(predicate);

        for (T value : iterable) {
            if (predicate.test(value)) {
                return true;
            }
        }

        return false;
    }

    default boolean anyMatch(Predicate<? super T> predicate) {
        return anyMatch(this, predicate);
    }

    static <T> boolean allMatcher(Iterable<? extends T> iterable, Predicate<? super T> predicate) {
        requireNonNull(iterable);
        requireNonNull(predicate);

        for (T value : iterable) {
            if (!predicate.test(value)) {
                return false;
            }
        }

        return true;
    }

    default boolean allMatch(Predicate<? super T> predicate) {
        return allMatcher(this, predicate);
    }

    static <T> boolean noneMatch(Iterable<? extends T> iterable, Predicate<? super T> predicate) {
        requireNonNull(iterable);
        requireNonNull(predicate);

        for (T value : iterable) {
            if (predicate.test(value)) {
                return false;
            }
        }

        return true;
    }

    default boolean noneMatch(Predicate<? super T> predicate) {
        return noneMatch(this, predicate);
    }

    static <T> Optional<T> findFirst(Iterable<? extends T> iterable) {

        requireNonNull(iterable);

        final Iterator<? extends T> iter = iterable.iterator();
        if (iter.hasNext()) {
            return Optional.ofNullable(iter.next());
        }

        return Optional.empty();
    }

    default Optional<T> findFirst() {
        return findFirst(this);
    }

    static <T> Stream<T> empty() {
        return () -> new Iterator<T>() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public T next() {
                throw new UnsupportedOperationException("Empty Iterator has no value");
            }
        };
    }

    static <T> Stream<T> of(T t) {
        return of(Collections.singleton(t));
    }

    @SafeVarargs
    static <T> Stream<T> of(T... values) {
        requireNonNull(values);

        return () -> new Iterator<T>() {

            int index = -1;

            @Override
            public boolean hasNext() {
                return ++index < values.length;
            }

            @Override
            public T next() {
                return values[index];
            }
        };
    }

    static <T> Stream<T> of(Iterable<? extends T> iterable) {
        if (iterable instanceof Stream) {
            //noinspection unchecked
            return ((Stream<T>) iterable);
        }

        final Iterator<? extends T> iterator = iterable.iterator();

        return () -> new Iterator<T>() {
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public T next() {
                return iterator.next();
            }
        };
    }

    @SuppressWarnings("UnnecessaryInterfaceModifier")
    static final Object NONE = new Object();

    static <T> Stream<T> iterate(final T seed, final UnaryOperator<T> f) {
        return () -> new Iterator<T>() {

            @SuppressWarnings("unchecked")
            T t = ((T) NONE);

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public T next() {
                return t = (t == NONE) ? seed : f.apply(t);
            }
        };
    }

    static <T> Stream<T> generate(Supplier<T> s) {
        return () -> new Iterator<T>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public T next() {
                return s.get();
            }
        };
    }

    static <T> Stream<T> concat(Iterable<? extends T> a, Iterable<? extends T> b) {
        requireNonNull(a);
        requireNonNull(b);

        //noinspection unchecked
        final Iterator<? extends T>[] iters = new Iterator[]{a.iterator(), b.iterator()};

        return () -> new Iterator<T>() {

            int index = 0;

            @Override
            public boolean hasNext() {
                if (iters[index].hasNext()) {
                    return true;
                }
                while (index + 1 < iters.length) {
                    index += 1;
                    if (iters[index].hasNext()) {
                        return true;
                    }
                }

                return false;
            }

            @Override
            public T next() {
                return iters[index].next();
            }
        };
    }

    default Stream<T> concat(Iterable<? extends T> iterable) {
        return concat(this, iterable);
    }

    static Stream<Integer> range(int fromInclusive, int toExclusive) {
        if (fromInclusive >= toExclusive) {
            return empty();
        }

        return () -> new Iterator<Integer>() {

            int cursor = fromInclusive;

            @Override
            public boolean hasNext() {
                return cursor < toExclusive;
            }

            @Override
            public Integer next() {
                return cursor++;
            }
        };
    }
}
