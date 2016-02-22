package sp;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import java.util.function.Supplier;

public class LazyFactory {
    protected LazyFactory() {}
    public static <T> Lazy<T> createLazy(final Supplier<T> s) {
        return new Lazy<T>() {
            private T result = null;
            private Supplier<T> supplier = s;
            public T get() {
                if (supplier != null) {
                    result = supplier.get();
                    supplier = null;
                }
                return result;
            }
        };
    }

    public static <T> Lazy<T> createLazyLock(final Supplier<T> s) {
        return new Lazy<T>() {
            private volatile T result = null;
            private volatile Supplier<T> supplier = s;
            public T get() {
                if (supplier != null) {
                    synchronized (this) {
                        if (supplier != null) {
                            result = supplier.get();
                            supplier = null;
                        }
                    }
                }
                return result;
            }
        };
    }

    private static class AtomicLazy<T> implements Lazy<T> {
        private static final Object IN_PROCESS = new Object();
        private static final AtomicReferenceFieldUpdater<AtomicLazy, Object> updater =
                AtomicReferenceFieldUpdater.newUpdater(AtomicLazy.class, Object.class, "result");
        private volatile Object result = IN_PROCESS;
        private Supplier<T> supplier;

        AtomicLazy(Supplier<T> s) {
            supplier = s;
        }

        public T get() {
            if (result == IN_PROCESS) {
                updater.compareAndSet(this, IN_PROCESS, supplier.get());
            }
            return (T) result;
        }
    }

    public static <T> Lazy<T> createLazyAtomic(final Supplier<T> s) {
        return new AtomicLazy<T>(s);
    }
}
