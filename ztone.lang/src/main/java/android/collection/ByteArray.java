package android.collection;

import android.assist.Assert;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;

public final class ByteArray implements Parcelable {
    private byte[] bytes = {};

    public ByteArray() {

    }

    public byte[] get() {
        return bytes;
    }

    public ByteArray set(byte[] original) {
        bytes = original != null ? original : new byte[]{};

        return this;
    }

    public ByteArray copyFrom(byte[] original) {

        return copyFrom(original, 0, original.length);
    }

    public ByteArray copyFrom(byte[] original, int offset, int len) {
        if (Assert.notEmpty(original)) {
            bytes = Arrays.copyOfRange(original, offset, len);
        }

        return this;
    }

    public ByteArray appendFrom(byte[] original) {
        return appendFrom(original, 0, original.length);
    }

    public ByteArray appendFrom(byte[] original, int offset, int len) {
        if (Assert.notEmpty(original)) {
            byte[] newBytes = new byte[bytes.length + len];

            if (bytes.length > 0) {
                System.arraycopy(bytes, 0, newBytes, 0, bytes.length);
            }

            System.arraycopy(original, offset, newBytes, bytes.length, len);

            bytes = newBytes;
        }

        return this;
    }

    public void clear() {
        bytes = new byte[]{};
    }

    protected ByteArray(Parcel in) {
        int size = in.readInt();
        if (size > 0) {
            bytes = new byte[size];

            in.readByteArray(bytes);
        }
    }

    public int getSize() {
        return bytes != null ? bytes.length : 0;
    }

    public boolean isEmpty() {
        return getSize() == 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        int size = getSize();
        dest.writeInt(size);
        if (size > 0) {
            dest.writeByteArray(bytes);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ByteArray> CREATOR = new Creator<ByteArray>() {
        @Override
        public ByteArray createFromParcel(Parcel in) {
            return new ByteArray(in);
        }

        @Override
        public ByteArray[] newArray(int size) {
            return new ByteArray[size];
        }
    };
}
