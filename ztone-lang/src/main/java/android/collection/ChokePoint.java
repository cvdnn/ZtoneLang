package android.collection;

import java.util.concurrent.LinkedBlockingQueue;

public final class ChokePoint<E> extends LinkedBlockingQueue<E> {

    public ChokePoint() {
        super(1);
    }
}
