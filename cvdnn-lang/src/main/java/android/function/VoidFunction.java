package android.function;

import androidx.arch.core.util.Function;

public interface VoidFunction<R> extends Function<Void, R> {

    @Override
    R apply(Void v);
}
