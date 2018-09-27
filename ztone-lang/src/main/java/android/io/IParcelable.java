package android.io;


import android.assist.Assert;
import android.os.Parcel;
import android.os.Parcelable;

public abstract class IParcelable implements Parcelable {

    public abstract IParcelable readFromParcel(Parcel in);

    @Override
    public abstract void writeToParcel(Parcel dest, int flags);

    @Override
    public int describeContents() {
        return 0;
    }

    public final void writeString(Parcel dest, String val) {
        if (dest != null) {
            dest.writeString(Assert.notEmpty(val) ? val : "");
        }
    }
}
