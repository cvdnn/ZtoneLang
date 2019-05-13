package android.math;

public final class Arrayz {
    /**
     * Reverse the object array.
     *
     * @param arr object array
     * @param <T> object type
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
