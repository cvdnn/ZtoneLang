package android.realm;

import io.realm.Realm;
import io.realm.RealmModel;

public interface FleeModel {
    <R extends RealmModel> R flee();
}
