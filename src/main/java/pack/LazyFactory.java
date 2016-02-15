package pack;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

public class LazyFactory {
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
                Supplier<T> localSup = supplier;
                if (localSup != null) {
                    synchronized (this) {
                        localSup = supplier;
                        if (localSup != null) {
                            result = supplier.get();
                            supplier = localSup = null;
                        }
                    }
                }
                return result;
            }
        };
    }

    private static class AtomicLazy<T> implements Lazy<T> {
        private static final Supplier IN_PROCESS = () -> null;
        private AtomicReference<Supplier<T>> supplierRef;
        private T result;

        AtomicLazy(Supplier<T> s) {
            supplierRef = new AtomicReference<>(s);
        }

        public T get() {
            Supplier<T> supplier = supplierRef.get();
            if (supplier == null) {
                return result;
            }
            T newRes = supplier.get();
            supplier = supplierRef.getAndUpdate((s) -> (s == null) ? null : IN_PROCESS);
            if (supplier == null) {
                return result;
            } else if (supplier != IN_PROCESS) {
                result = newRes;
            }
            supplierRef.set(null);
            return result;
        }
    }

    public static <T> Lazy<T> createLazyAtomic(final Supplier<T> s) {
        return new AtomicLazy<T>(s);
    }
}
