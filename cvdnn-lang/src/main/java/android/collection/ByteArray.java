package android.collection;

import android.assist.Assert;
import android.math.Maths;
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

    public byte get(int i) {
        return bytes[i];
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

    /**
     * @param original
     * @return
//     * @see android.collection.ByteArray.write();
     */
    @Deprecated
    public ByteArray appendFrom(byte[] original) {
        return write(original, 0, original.length);
    }

    /**
     * @param original
     * @param offset
     * @param len
     * @return
//     * @see android.collection.ByteArray.write(byte[] original, int offset, int len);
     */
    @Deprecated
    public ByteArray appendFrom(byte[] original, int offset, int len) {

        return write(original, offset, len);
    }

    public int read(byte[] original, int offset, int len) {
        int inLen = 0;
        if (bytes.length > offset) {
            inLen = Math.min(Math.min(bytes.length - offset, original.length), len);

            if (inLen > 0) {
                System.arraycopy(bytes, offset, original, 0, inLen);
            }
        }

        return inLen;
    }

    public ByteArray write(byte original) {

        return write(new byte[]{original}, 0, 1);
    }

    public ByteArray write(byte[] original) {
        return write(original, 0, original.length);
    }

    public ByteArray write(byte[] original, int offset, int len) {
        if (Assert.notEmpty(original)) {
            int newLen = Math.min(original.length - offset, len);
            byte[] newBytes = new byte[bytes.length + newLen];

            if (bytes.length > 0) {
                System.arraycopy(bytes, 0, newBytes, 0, bytes.length);
            }

            System.arraycopy(original, offset, newBytes, bytes.length, newLen);

            bytes = newBytes;
        }

        return this;
    }

    public ByteArray lowWrite(byte[] original) {
        return lowWrite(original, 0, original.length);
    }

    public ByteArray lowWrite(byte[] original, int offset, int len) {
        if (Assert.notEmpty(original)) {
            int newLen = Math.min(original.length - offset, len);
            byte[] newBytes = new byte[bytes.length + len];

            if (bytes.length > 0) {
                System.arraycopy(bytes, 0, newBytes, 0, bytes.length);
            }

            for (int i = 0; i < newLen; i++) {
                newBytes[bytes.length + i] = original[offset + newLen - 1 - i];
            }

            bytes = newBytes;
        }

        return this;
    }

    public ByteArray write(short s) {
        return write(Maths.toBytes(s));
    }

    public ByteArray lowWrite(short s) {
        return lowWrite(Maths.toBytes(s));
    }

    public ByteArray write(int i) {
        return write(Maths.toBytes(i));
    }

    public ByteArray lowWrite(int i) {
        return lowWrite(Maths.toBytes(i));
    }

    public ByteArray write(long l) {
        return write(Maths.toBytes(l));
    }

    public ByteArray lowWrite(long l) {
        return lowWrite(Maths.toBytes(l));
    }

    public void clear() {
        bytes = new byte[]{};
    }

    protected ByteArray(Parcel in) {
        readFromParcel(in);
    }

    @Deprecated
    public int getSize() {
        return size();
    }

    public int size() {
        return bytes != null ? bytes.length : 0;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public void readFromParcel(Parcel in) {
        int size = in.readInt();
        if (size > 0) {
            bytes = new byte[size];

            in.readByteArray(bytes);
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        int size = size();
        dest.writeInt(size);
        if (size > 0) {
            dest.writeByteArray(bytes);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<ByteArray> CREATOR = new Parcelable.Creator<ByteArray>() {
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
