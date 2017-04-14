package android.assist;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by handy on 17-3-24.
 */

public class ArrayUtils {

    /**
     * <p>Converts the given array into a {@link Map}. Each element of the array
     * must be either a {@link Map.Entry} or an Array, containing at least two
     * elements, where the first element is used as key and the second as
     * value.
     * <p>
     * <p>This method can be used to initialize:
     * <pre>
     * // Create a Map mapping colors.
     * Map colorMap = ArrayUtils.toMap(new String[][] {
     *     {"RED", "#FF0000"},
     *     {"GREEN", "#00FF00"},
     *     {"BLUE", "#0000FF"}});
     * </pre>
     * <p>
     * <p>This method returns {@code null} for a {@code null} input array.
     *
     * @param array an array whose elements are either a {@link Map.Entry} or
     *              an Array containing at least two elements, may be {@code null}
     * @return a {@code Map} that was created from the array
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
}
