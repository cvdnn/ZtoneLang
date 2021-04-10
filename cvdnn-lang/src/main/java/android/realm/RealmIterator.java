package android.realm;

import java.util.Collection;
import java.util.Iterator;

import static android.realm.Realms.PAGE_SIZE;

public abstract class RealmIterator<E> implements Iterator {

    private final int size;
    private int pageNum;
    private boolean hasNextPage;

    public RealmIterator() {
        size = size();
        hasNextPage = size > 0;
    }

    protected abstract int size();

    protected abstract Collection<E> find(int pageNum);

    @Override
    public final boolean hasNext() {
        return hasNextPage;
    }

    @Override
    public final Collection<E> next() {
        Collection<E> list = find(pageNum);

        hasNextPage = PAGE_SIZE * pageNum + list.size() < size;

        pageNum++;

        return list;
    }
}
