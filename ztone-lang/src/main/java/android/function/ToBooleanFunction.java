package android.function;

import androidx.arch.core.util.Function;

public interface ToBooleanFunction<T> extends Function<T, Boolean> {

    @Override
    Boolean apply(T t);
}
