package android.realm;

import android.Args;
import android.assist.Assert;
import android.io.Stream;
import android.lang.R;
import android.log.Log;
import android.math.MD5;
import android.math.Maths;
import android.math.ShortDigest;

import androidx.annotation.AnyThread;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.ListIterator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import io.realm.DynamicRealm;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;
import io.realm.RealmModel;
import io.realm.RealmResults;

import static android.Args.context;

public class Realms implements RealmMigration {
    private static final String TAG = "Realms";

    public static final int PAGE_SIZE = 20;

    static {
        Realm.init(context);
        Realm.setDefaultConfiguration(new RealmConfiguration.Builder()
                .name(ShortDigest.encrypt(MD5.encrypt("default_database.rlm")))
                .allowQueriesOnUiThread(true)
                .allowWritesOnUiThread(true)
                .build());
    }

    public static final HashSet<Class<? extends RealmModel>> Mdl = new HashSet<>();

    ///////////////////////////////////
    //

    public final RealmConfiguration Config;

    public Realms() {
        Config = new RealmConfiguration.Builder()
                .inMemory()
                .allowQueriesOnUiThread(true)
                .allowWritesOnUiThread(true)
                .build();
    }

    public Realms(@NotNull File file, boolean delete) {
        RealmConfiguration.Builder builder = new RealmConfiguration.Builder()
                .directory(file.getParentFile())
                .name(file.getName())
                .encryptionKey(Stream.read(Args.Env.Res.openRawResource(R.raw.realm_seeds)))
                .allowQueriesOnUiThread(true)
                .allowWritesOnUiThread(true);

        if (delete) {
            builder.deleteRealmIfMigrationNeeded();
        } else {
            builder.migration(this);
        }

        Config = builder.build();
    }

    public Realms(@NotNull File file, long version) {
        Config = new RealmConfiguration.Builder()
                .schemaVersion(version)
                .directory(file.getParentFile())
                .name(file.getName())
                .encryptionKey(Stream.read(Args.Env.Res.openRawResource(R.raw.realm_seeds)))
                .modules(Realm.getDefaultModule(), Mdl.toArray())
                .migration(this)
                .allowQueriesOnUiThread(true)
                .allowWritesOnUiThread(true)
                .build();
    }

    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
        Log.e(TAG, ">>: %d -> %d", oldVersion, newVersion);

        DefaultMigration.migrate(realm);
    }

    public final Realm open() {
        return Realm.getInstance(Config);
    }

    public final <E extends RealmModel> int count(Class<E> clazz) {

        return Maths.intValue(call(realm -> (int) realm.where(clazz).count()));
    }

    public final <E extends FleeModel> Collection<E> fleeList(Function<Realm, Collection<E>> function) {
        return fleeList(function, null);
    }

    public final <E extends FleeModel, R> Collection<R> fleeList(Function<Realm, Collection<E>> function, Function<E, R> mapper) {
        return call(realm -> {
            final ArrayList<R> metaList = new ArrayList<>();

            Collection<E> list = function.apply(realm);
            if (Assert.notEmpty(list)) {
                if (mapper != null) {
                    metaList.addAll(list.stream().map(mapper).collect(Collectors.toList()));
                } else {
                    list.forEach(item -> {
                        if (item != null) {
                            metaList.add((R) item.flee());
                        }
                    });
                }
            }

            return metaList;
        });
    }

    public final <E extends FleeModel> Collection<E> fleeList(int pageNum, Function<Realm, RealmResults<E>> function) {
        return fleeList(pageNum, function, null);
    }

    public final <E extends FleeModel, R> Collection<R> fleeList(int pageNum, Function<Realm, RealmResults<E>> function, Function<E, R> mapper) {
        return call(realm -> {
            ArrayList<R> metaList = new ArrayList<>();

            if (pageNum >= 0 && function != null) {
                RealmResults<E> result = function.apply(realm);
                if (Assert.notEmpty(result) && result.size() >= PAGE_SIZE * pageNum) {
                    ListIterator<E> it = result.listIterator(PAGE_SIZE * pageNum);
                    for (int i = 0; i < PAGE_SIZE && it.hasNext(); i++) {
                        E e = it.next();
                        if (e != null) {
                            metaList.add(mapper != null ? mapper.apply(e) : e.flee());
                        }

                    }
                }
            }

            return metaList;
        });
    }

    public final <E extends FleeModel> Collection<E> findAll(Class<E> clazz) {
        return fleeList(realm -> realm.where(clazz).findAll());
    }

    public final <E extends FleeModel> Collection<E> findList(Class<E> clazz, int pageNum) {
        return fleeList(pageNum, realm -> realm.where(clazz).findAll());
    }

    @AnyThread
    public final <E extends RealmModel> boolean insertOrUpdate(E data) {
        return executeTransaction(realm -> realm.insertOrUpdate(data));
    }

    @AnyThread
    public final boolean insertOrUpdate(Collection<? extends RealmModel> list) {
        return executeTransaction(realm -> realm.insertOrUpdate(list));
    }

    public final <E extends RealmModel> boolean copyFrom(Realms from, Class<E> clazz) {
        boolean result = false;

        if (clazz != null && from != null) {
            result = executeTransaction(realm -> from.execute(rlm -> {
                realm.insertOrUpdate(rlm.where(clazz).findAll());
            }));
        }

        return result;
    }

    public final boolean execute(Consumer<Realm> fun) {
        boolean result = false;

        if (fun != null) {
            Realm realm = null;
            try {
                realm = open();
                fun.accept(realm);

                result = true;
            } catch (Exception e) {
                Log.e(TAG, e);
            } finally {
                realm = close(realm);
            }
        }

        return result;
    }

    public final <R> R call(Function<Realm, R> fun) {
        R r = null;

        if (fun != null) {
            Realm realm = null;
            try {
                realm = open();
                r = fun.apply(realm);
            } catch (Exception e) {
                Log.e(TAG, e);
            } finally {
                realm = close(realm);
            }
        }

        return r;
    }

    public final boolean executeTransaction(Realm.Transaction tr) {
        boolean result = false;

        if (tr != null) {
            Realm realm = null;
            try {
                realm = open();
                realm.executeTransaction(tr);

                result = true;
            } catch (Exception e) {
                Log.e(TAG, e);
            } finally {
                realm = close(realm);
            }
        }

        return result;
    }

    public static boolean isClosed(Realm... realms) {
        boolean result = false;

        if (Assert.notEmpty(realms)) {
            for (Realm r : realms) {
                result |= r.isClosed();
            }
        }

        return result;
    }

    public static Realm close(Realm... realms) {
        if (Assert.notEmpty(realms)) {
            for (Realm r : realms) {
                if (r != null && !r.isClosed()) {
                    try {
                        r.close();
                    } catch (Exception e) {
                        Log.v(TAG, e);
                    }
                }
            }
        }

        return null;
    }
}
