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

    private static class nullCounter extends Counter {
        @Override
        public Integer get() {
            counter += 1;
            assertEquals(counter, 1);
            return null;
        }
    }

    @Test
    public void LazyTest() {
        nullCounter supplier = new nullCounter();
        Lazy<Integer> lazy = LazyFactory.createLazy(supplier);
        for (int i = 0; i < 5; i++) {
            assertEquals(lazy.get(), null);
        }
    }

    @Test
    public void ConcurrentTest() {
        nullCounter supplier = new nullCounter();
        Lazy<Integer> lazy = LazyFactory.createLazyLock(supplier);
        ArrayList<Thread> threadList = new ArrayList<Thread>();
        for (int i = 0; i < 100; i++) {
            threadList.add(new Thread(() -> assertEquals(lazy.get(), null)));
        }
        for (int i = 0; i < 100; i++) {
            threadList.get(i).run();
        }
        for (int i = 0; i < 100; i++) {
            try {
                threadList.get(i).join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void AtomicTest() {
        Counter supplier = new Counter();
        Lazy<Integer> lazy = LazyFactory.createLazyAtomic(supplier);
        ArrayList<Thread> threadList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            threadList.add(new Thread(() -> {
                assertEquals(lazy.get(), (Integer) 1);
                assertEquals(lazy.get(), (Integer) 1);
            }));
        }
        for (int i = 0; i < 100; i++) {
            threadList.get(i).run();
        }
        for (int i = 0; i < 100; i++) {
            try {
                threadList.get(i).join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}