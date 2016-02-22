package sp;

import org.junit.Test;

import java.util.ArrayList;
import java.util.function.Supplier;

import static org.junit.Assert.assertEquals;

public class Tests {
    private static class Counter implements Supplier<Integer> {
        int counter = 0;

        @Override
        public Integer get() {
            counter += 1;
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return counter;
        }
    }

    private static class NullCounter extends Counter {
        @Override
        public Integer get() {
            counter += 1;
            assertEquals(1, counter);
            return null;
        }
    }

    @Test
    public void lazyTest() {
        NullCounter supplier = new NullCounter();
        Lazy<Integer> lazy = LazyFactory.createLazy(supplier);
        for (int i = 0; i < 5; i++) {
            assertEquals(null, lazy.get());
        }
    }

    @Test
    public void concurrentTest() {
        NullCounter supplier = new NullCounter();
        Lazy<Integer> lazy = LazyFactory.createLazyLock(supplier);
        ArrayList<Thread> threadList = new ArrayList<Thread>();
        Boolean barrier = false;
        for (int i = 0; i < 100; i++) {
            threadList.add(new Thread(() -> {
                if (barrier) {
                    assertEquals(null, lazy.get());
                }
            }));
        }
        for (int i = 0; i < 100; i++) {
            threadList.get(i).start();
        }
        for (int i = 0; i < 100; i++) {
            try {
                threadList.get(i).join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
/*
    @Test
    public void atomicTest() {
        Counter supplier = new Counter();
        Lazy<Integer> lazy = LazyFactory.createLazyAtomic(supplier);
        ArrayList<Thread> threadList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            threadList.add(new Thread(() -> {
                assertEquals((Integer) 1, lazy.get());
                assertEquals((Integer) 1, lazy.get());
            }));
        }
        for (int i = 0; i < 100; i++) {
            threadList.get(i).start();
        }
        for (int i = 0; i < 100; i++) {
            try {
                threadList.get(i).join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }*/
}