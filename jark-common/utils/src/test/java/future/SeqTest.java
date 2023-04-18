package future;

import java.util.List;

import org.junit.jupiter.api.Test;

public class SeqTest {


    @Test
    public void seq1() {
        List<Integer> list = List.of(1, 2, 3);
        Seq<Integer> seq = list::forEach;
    }

    @Test
    public void seq2() {
        List<Integer> list = List.of(1, 2, 3);
        Seq<Integer> seq = c -> list.forEach(c);
    }

    @Test
    public void seq3() {
        List<Integer> list = List.of(1, 2, 3);
        Seq<Integer> seq = c -> {
            for (final Integer i : list) {
                c.accept(i);
            }
        };
    }

    @Test
    public void seq4() {

    }

    @Test
    public void testChan() {
        // 生产无限的自然数，放入通道seq，这里流本身就是通道，同步流还是异步流都无所谓
        Seq<Long> seq = c -> {
            long i = 0;
            while (true) {
                c.accept(i++);
            }
        };
        long start = System.currentTimeMillis();
        // 通道seq交给消费者，消费者表示只要偶数，只要5个
        seq.filter(i -> (i & 1) == 0).take(5).asyncConsume(i -> {
            try {
                Thread.sleep(1000);
                System.out.printf("produce %d and consume\n", i);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        System.out.printf("elapsed time: %dms\n", System.currentTimeMillis() - start);
    }
}


