package android.realm;

import android.Args;
import android.assist.Assert;
import android.io.Stream;
import android.lang.R;
import android.log.Log;
import android.math.MD5;
import android.math.ShortDigest;

import androidx.annotation.AnyThread;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import io.realm.DynamicRealm;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;
import io.realm.RealmModel;
import io.realm.RealmQuery;

import static android.Args.context;

public class Realms implements RealmMigration {
    private static final String TAG = "Realms";

    static {
        Realm.init(context);
        Realm.setDefaultConfiguration(new RealmConfiguration.Builder()
                .name(ShortDigest.encrypt(MD5.encrypt("default_database.rlm")))
                .allowQueriesOnUiThread(true)
                .allowWritesOnUiThread(true)
                .build());
    }

    ///////////////////////////////////
    //

    public final RealmConfiguration mRealmCfg;

    public Realms() {
        mRealmCfg = new RealmConfiguration.Builder()
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

        mRealmCfg = builder.build();
    }

    public Realms(@NotNull File file, long version) {
        mRealmCfg = new RealmConfiguration.Builder()
                .schemaVersion(version)
                .directory(file.getParentFile())
                .name(file.getName())
                .encryptionKey(Stream.read(Args.Env.Res.openRawResource(R.raw.realm_seeds)))
                .migration(this)
                .allowQueriesOnUiThread(true)
                .allowWritesOnUiThread(true)
                .build();
    }

    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {

    }

    public final Realm open() {
        return Realm.getInstance(mRealmCfg);
    }

    public final <O extends RealmModel> int count(Class<O> clazz) {

        return execute(realm -> (int) realm.where(clazz).count());
    }

    public final <O extends RealmModel> int count0(Class<O> clazz) {

        return execute(realm -> {
            RealmQuery<O> query = realm.where(clazz);
            return query != null && query.isValid() ? (int) query.count() : 0;
        });
    }

    public final <O extends RealmModel> List<O> findAll(Class<O> clazz) {

        return execute(realm -> {
            return new ArrayList<>(realm.where(clazz).findAll());
        });
    }

    @AnyThread
    public final <O extends RealmModel> boolean insertOrUpdate(O data) {
        return executeTransaction(realm -> realm.insertOrUpdate(data));
    }

    @AnyThread
    public final boolean insertOrUpdate(Collection<? extends RealmModel> datas) {
        return executeTransaction(realm -> realm.insertOrUpdate(datas));
    }

    public final <O extends RealmModel> boolean copyFrom(Class<O> clazz, Realms from) {
        boolean result = false;

        if (clazz != null && from != null) {
            result = executeTransaction(realm -> from.execute(rlm -> {
                realm.insertOrUpdate(rlm.where(clazz).findAll());
                return true;
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

    public final <R> R execute(Function<Realm, R> fun) {
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
