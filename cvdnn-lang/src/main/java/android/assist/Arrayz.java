package android.assist;

import android.log.Log;
import android.math.Maths;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public final class Arrayz {

    /**
     * <p>Converts the given array into a {@link Map}. Each element of the array
     * must be either a {@link Map.Entry} or an Array, containing at least two
     * elements, where the first element is used as key and the second as
     * value.
     * </p>
     * <p>This method can be used to initialize:
     * <pre>
     * // Create a Map mapping colors.
     * Map colorMap = ArrayUtils.toMap(new String[][] {
     *     {"RED", "#FF0000"},
     *     {"GREEN", "#00FF00"},
     *     {"BLUE", "#0000FF"}});
     * </pre>
     * </p>
     * <p>This method returns {@code null} for a {@code null} input array.</p>
     *
     * @param array an array whose elements are either a {@link Map.Entry} or
     *              an Array containing at least two elements, may be {@code null}
     *
     * @return a {@code Map} that was created from the array
     *
     * @throws IllegalArgumentException if one element of this Array is
     *                                  itself an Array containing less then two elements
     * @throws IllegalArgumentException if the array contains elements other
     *                                  than {@link Map.Entry} and an Array
     */
    public static <K, V> HashMap<K, V> toMap(final Object[] array) {
        final HashMap<K, V> map = new HashMap<>((int) (array.length * 1.5));

        if (array != null) {
            for (int i = 0; i < array.length; i++) {
                final Object object = array[i];
                if (object instanceof Map.Entry<?, ?>) {
                    final Map.Entry<?, ?> entry = (Map.Entry<?, ?>) object;
                    map.put((K) entry.getKey(), (V) entry.getValue());

                } else if (object instanceof Object[]) {
                    final Object[] entry = (Object[]) object;
                    if (entry.length < 2) {
                        throw new IllegalArgumentException("Array element " + i + ", '" + object + "', has a length less than 2");
                    }

                    map.put((K) entry[0], (V) entry[1]);

                } else {
                    throw new IllegalArgumentException("Array element " + i + ", '" + object + "', is neither of type Map.Entry nor an Array");
                }
            }
        }

        return map;
    }

    public static int indexOf(byte[] original, int from, int to, byte key) {
        int index = -1;

        if (Assert.notEmpty(original) && from >= 0 && to >= from && original.length - from >= to - from) {
            try {
                index = Arrays.binarySearch(original, from, to, key);
            } catch (Exception e) {
                Log.d(e);
            }
        }

        return index;
    }

    public static byte[] copyOf(byte[] original, int from, int len) {
        int newLength = Maths.min(original.length - from, len);

        byte[] copy = new byte[newLength];
        System.arraycopy(original, from, copy, 0, newLength);

        return copy;
    }

    public static int copy(byte[] src, byte[] dst) {
        return copy(src, dst, 0);
    }

    public static int copy(byte[] src, byte[] dst, int dstPos) {
        int len = 0;
        if (Assert.check(src) && Assert.check(dst) && dst.length > dstPos) {
            try {
                int temp = Maths.min(src.length, dst.length - dstPos);
                System.arraycopy(src, 0, dst, dstPos, temp);

                len = temp;
            } catch (Exception e) {
                Log.e(e);
            }
        }

        return len;
    }

    public static int copy(byte[] src, int srcPos, byte[] dst, int dstPos, int length) {
        int len = 0;

        if (Assert.check(src) && Assert.check(dst) && src.length > srcPos && dst.length > dstPos) {
            try {
                int temp = Maths.min(src.length - srcPos, dst.length - dstPos, length);
                System.arraycopy(src, srcPos, dst, dstPos, temp);

                len = temp;
            } catch (Exception e) {
                Log.e(e);
            }
        }

        return len;
    }

    public static byte[] stack(byte[] a, byte[] b) {
        byte[] array = new byte[a.length + b.length];

        System.arraycopy(a, 0, array, 0, a.length);
        System.arraycopy(b, 0, array, a.length, b.length);

        return array;
    }

    /**
     * Reverse the object array.
     *
     * @param arr object array
     * @param <T> object type
     *
     * @return the reversed object array
     */
    public static <T> T[] reverse(T[] arr) {
        validateNotNull(arr);
        int len = arr.length;
        T temp;
        for (int i = 0; i < len / 2; i++) {
            temp = arr[i];
            arr[i] = arr[len - i - 1];
            arr[len - i - 1] = temp;
        }
        return arr;
    }

    /**
     * Reverse the int array.
     *
     * @param arr int array
     *
     * @return the reversed int array
     */
    public static boolean[] reverse(boolean[] arr) {
        validateNotNull(arr);
        int len = arr.length;
        boolean temp;
        for (int i = 0; i < len / 2; i++) {
            temp = arr[i];
            arr[i] = arr[len - i - 1];
            arr[len - i - 1] = temp;
        }
        return arr;
    }

    /**
     * Reverse the byte array.
     *
     * @param arr byte array
     *
     * @return the reversed byte array
     */
    public static byte[] reverse(byte[] arr) {
        validateNotNull(arr);
        int len = arr.length;
        byte temp;
        for (int i = 0; i < len / 2; i++) {
            temp = arr[i];
            arr[i] = arr[len - i - 1];
            arr[len - i - 1] = temp;
        }
        return arr;
    }

    /**
     * Reverse the short array.
     *
     * @param arr short array
     *
     * @return the reversed short array
     */
    public static short[] reverse(short[] arr) {
        validateNotNull(arr);
        int len = arr.length;
        short temp;
        for (int i = 0; i < len / 2; i++) {
            temp = arr[i];
            arr[i] = arr[len - i - 1];
            arr[len - i - 1] = temp;
        }
        return arr;
    }

    /**
     * Reverse the int array.
     *
     * @param arr int array
     *
     * @return the reversed int array
     */
    public static int[] reverse(int[] arr) {
        validateNotNull(arr);
        int len = arr.length;
        int temp;
        for (int i = 0; i < len / 2; i++) {
            temp = arr[i];
            arr[i] = arr[len - i - 1];
            arr[len - i - 1] = temp;
        }
        return arr;
    }

    /**
     * Reverse the long array.
     *
     * @param arr long array
     *
     * @return the reversed long array
     */
    public static long[] reverse(long[] arr) {
        validateNotNull(arr);
        int len = arr.length;
        long temp;
        for (int i = 0; i < len / 2; i++) {
            temp = arr[i];
            arr[i] = arr[len - i - 1];
            arr[len - i - 1] = temp;
        }
        return arr;
    }

    /**
     * Reverse the double array.
     *
     * @param arr double array
     *
     * @return the reversed double array
     */
    public static double[] reverse(double[] arr) {
        validateNotNull(arr);
        int len = arr.length;
        double temp;
        for (int i = 0; i < len / 2; i++) {
            temp = arr[i];
            arr[i] = arr[len - i - 1];
            arr[len - i - 1] = temp;
        }
        return arr;
    }

    /**
     * Reverse the char array.
     *
     * @param arr char array
     *
     * @return the reversed char array
     */
    public static char[] reverse(char[] arr) {
        validateNotNull(arr);

        int len = arr.length;
        char temp;
        for (int i = 0; i < len / 2; i++) {
            temp = arr[i];
            arr[i] = arr[len - i - 1];
            arr[len - i - 1] = temp;
        }
        return arr;
    }

    /**
     * throw an IllegalArgumentException if obj is null (obj is any type of array)
     *
     * @param obj
     */
    private static void validateNotNull(Object obj) {
        if (obj == null)
            throw new IllegalArgumentException("array is null");
    }
}
