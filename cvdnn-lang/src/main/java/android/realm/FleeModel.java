package android.realm;

import io.realm.RealmModel;

public interface FleeModel extends RealmModel {
    <R extends RealmModel> R flee();
}
