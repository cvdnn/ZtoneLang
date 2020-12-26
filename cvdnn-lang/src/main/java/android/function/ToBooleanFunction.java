package android.function;

import java.util.function.Function;

public interface ToBooleanFunction<T> extends Function<T, Boolean> {

    @Override
    Boolean apply(T t);
}
