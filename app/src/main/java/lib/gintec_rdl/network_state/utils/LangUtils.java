package lib.gintec_rdl.network_state.utils;

import androidx.annotation.Nullable;

public final class LangUtils {
    private LangUtils() {
    }

    public static class Optional<T> {
        private final T value;

        private Optional(T value) {
            this.value = value;
        }

        public T orElse(T ifNull) {
            return value != null ? value : ifNull;
        }

        public static <T> Optional<T> of(@Nullable T value) {
            return new Optional<>(value);
        }
    }
}
