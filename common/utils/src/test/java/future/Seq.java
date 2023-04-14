package future;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringJoiner;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

@FunctionalInterface
public interface Seq<T> {

    // 创建映射关系
    static <T> Seq<T> unit(T t) {
        return c -> c.accept(t);
    }

    static <T> T stop() {
        throw StopException.INSTANCE;
    }

    default void consumeTillStop(Consumer<T> consumer) {
        try {
            consume(consumer);
        } catch (StopException ignore) {

        }
    }

    default Seq<T> take(int n) {
        return c -> {
            int[] i = {n};
            consumeTillStop(t -> {
                if (i[0]-- > 0) {
                    c.accept(t);
                } else {
                    stop();
                }
            });
        };
    }

    default Seq<T> parallel() {
        final ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();
        return c -> map(t -> forkJoinPool.submit(() -> c.accept(t))).cache().consume(ForkJoinTask::join);
    }

    default void asyncConsume(Consumer<T> consumer) {
        final ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();
        map(t -> forkJoinPool.submit(() -> consumer.accept(t))).cache().consume(ForkJoinTask::join);
    }

    default Seq<T> cache() {
        final ArraySeq<T> arraySeq = new ArraySeq<>();
        consume(arraySeq::add);
        return arraySeq;
    }

    /**
     * 丢弃前n个元素 <--> 等价 Stream.skip
     * 不涉及流的中断控制，反而更像是 filter 的变种，一种带有状态的 filter
     */
    default Seq<T> drop(int n) {
        return c -> {
            int[] a = {n - 1};
            consume(t -> {
                if (a[0] < 0) {
                    c.accept(t);
                } else {
                    a[0]--;
                }
            });
        };
    }

    /**
     * 对流的某个元素添加一个操作 consumer,但是不执行流 <----> 对应 Stream.peek
     */
    default Seq<T> onEach(Consumer<T> consumer) {
        return c -> consume(consumer.andThen(c));
    }

    /**
     * 流与一个 Iterable 元素两两聚合，然后转换为一个新的流
     */
    default <E, R> Seq<R> zip(Iterable<E> iterable, BiFunction<T, E, R> function) {
        return c -> {
            final Iterator<E> iterator = iterable.iterator();
            consumeTillStop(t -> {
                if (iterator.hasNext()) {
                    c.accept(function.apply(t, iterator.next()));
                } else {
                    stop();
                }
            });
        };
    }

    /**
     * 将类型转T的流转换为E的流，也就 根据函数 T -> E 得到 Seq<T> -> Seq<E> 的映射
     */
    default <E> Seq<E> map(Function<T, E> function) {
        return c -> consume(t -> c.accept(function.apply(t)));
    }

    default <E> Seq<E> map2(Function<T, E> function) {
        return flatMap(t -> unit(function.apply(t)));
    }

    default <E> Seq<E> map3(Function<T, E> function) {
        return flatMap(t -> c -> c.accept(function.apply(t)));
    }


    /**
     * 将每个元素展开一个流之后在合并
     */
    default <E> Seq<E> flatMap(Function<T, Seq<E>> function) {
        return c -> consume(t -> function.apply(t).consume(c));
    }

    /**
     * 过滤元素
     */
    default Seq<T> filter(Predicate<T> predicate) {
        return c -> consume(t -> {
            if (predicate.test(t)) {
                c.accept(t);
            }
        });
    }

    default String join(String sep) {
        final StringJoiner joiner = new StringJoiner(sep);
        consume(t -> joiner.add(t.toString()));
        return joiner.toString();
    }

    default List<T> toList() {
        final List<T> list = new ArrayList<>();
        consume(list::add);
        return list;
    }

    void consume(Consumer<T> consumer);

}


