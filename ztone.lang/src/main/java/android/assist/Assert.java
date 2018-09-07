package android.assist;

import android.check.Validator;
import android.collection.ByteArray;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.SparseArray;

import org.json.JSONArray;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

public final class Assert {

    public static boolean as(int v, int make) {

        return make != 0 && (v & make) == make;
    }

    /**
     * 判断对象为空
     *
     * @param object
     */
    public static boolean isNull(Object object) {

        return object == null;
    }

    /**
     * 判断对象不为空
     *
     * @param object
     */
    public static boolean notNull(Object object) {

        return !isNull(object);
    }

    /**
     * 判断字符串包含字符
     *
     * @param text
     */
    public static boolean isEmpty(CharSequence text) {
        return TextUtils.isEmpty(text);
    }

    /**
     * 判断字符串包含字符
     *
     * @param text
     */
    public static boolean notEmpty(CharSequence text) {

        return !isEmpty(text);
    }

    public static boolean isEmpty(JSONArray jsonArray) {

        return jsonArray == null || jsonArray.length() <= 0;
    }

    public static boolean notEmpty(JSONArray jsonArray) {

        return !isEmpty(jsonArray);
    }

    /**
     * 判断字符串包含空格,换行等
     *
     * @param text
     */
    public static boolean hasWhitespace(CharSequence text) {
        boolean result = false;
        if (!isEmpty(text)) {
            for (int i = 0, strLen = text.length(); i < strLen; i++) {
                if (Character.isWhitespace(text.charAt(i))) {
                    result = true;
                    break;
                }
            }
        }

        return result;
    }

    /**
     * 判断是否不包含子字符串
     *
     * @param textToSearch
     * @param substring
     */
    public static boolean doesNotContain(String textToSearch, String substring) {
        return !TextUtils.isEmpty(textToSearch) && !TextUtils.isEmpty(substring)
                && textToSearch.indexOf(substring) != -1;
    }

    /**
     * 判断数组是否为空
     *
     * @param array
     */
    public static boolean isEmpty(int[] array) {

        return array == null || array.length <= 0;
    }

    /**
     * 判断数组是否为空
     *
     * @param array
     */
    public static boolean notEmpty(int[] array) {

        return !isEmpty(array);
    }

    /**
     * 判断数组是否为空
     *
     * @param array
     */
    public static boolean isEmpty(long[] array) {

        return array == null || array.length <= 0;
    }

    /**
     * 判断数组是否为空
     *
     * @param array
     */
    public static boolean notEmpty(long[] array) {

        return !isEmpty(array);
    }

    /**
     * 判断数组是否为空
     *
     * @param array
     */
    public static boolean isEmpty(float[] array) {

        return array == null || array.length <= 0;
    }

    /**
     * 判断数组是否为空
     *
     * @param array
     */
    public static boolean notEmpty(float[] array) {

        return !isEmpty(array);
    }

    /**
     * 判断数组是否为空
     *
     * @param array
     */
    public static boolean isEmpty(double[] array) {

        return array == null || array.length <= 0;
    }

    /**
     * 判断数组是否为空
     *
     * @param array
     */
    public static boolean notEmpty(double[] array) {

        return !isEmpty(array);
    }

    /**
     * 判断数组是否为空
     *
     * @param array
     */
    public static boolean isEmpty(short[] array) {

        return array == null || array.length <= 0;
    }

    /**
     * 判断数组是否为空
     *
     * @param array
     */
    public static boolean notEmpty(short[] array) {

        return !isEmpty(array);
    }

    /**
     * 判断数组是否为空
     *
     * @param array
     */
    public static boolean isEmpty(boolean[] array) {

        return array == null || array.length <= 0;
    }

    /**
     * 判断数组是否为空
     *
     * @param array
     */
    public static boolean notEmpty(boolean[] array) {

        return !isEmpty(array);
    }

    /**
     * 判断数组是否为空
     *
     * @param array
     */
    public static boolean isEmpty(byte[] array) {

        return array == null || array.length <= 0;
    }

    /**
     * 判断数组是否为空
     *
     * @param array
     */
    public static boolean notEmpty(byte[] array) {

        return !isEmpty(array);
    }

    /**
     * 判断数组是否为空
     *
     * @param array
     */
    public static boolean isEmpty(char[] array) {

        return array == null || array.length <= 0;
    }

    /**
     * 判断数组是否为空
     *
     * @param array
     */
    public static boolean notEmpty(char[] array) {

        return !isEmpty(array);
    }

    /**
     * 判断对象数组是否为空
     *
     * @param array
     */
    public static <O> boolean isEmpty(O[] array) {

        return array == null || array.length <= 0;
    }

    /**
     * 判断对象数组是否为空
     *
     * @param array
     */
    public static <O> boolean notEmpty(O[] array) {

        return !isEmpty(array);
    }

    /**
     * 判断对象Collection集合是否为空
     *
     * @param collection
     */
    public static boolean isEmpty(Collection<?> collection) {

        return collection == null || collection.isEmpty();
    }

    /**
     * 判断对象Collection集合是否为空
     *
     * @param collection
     */
    public static boolean notEmpty(Collection<?> collection) {

        return !isEmpty(collection);
    }

    /**
     * 判断对象Map是否为空
     *
     * @param map
     */
    public static boolean isEmpty(Map<?, ?> map) {

        return map == null || map.isEmpty();
    }

    /**
     * 判断对象Map是否为空
     *
     * @param map
     */
    public static boolean notEmpty(Map<?, ?> map) {

        return !isEmpty(map);
    }

    public static boolean isEmpty(SparseArray<?> sparseArray) {

        return sparseArray == null || sparseArray.size() <= 0;
    }

    public static boolean notEmpty(SparseArray<?> sparseArray) {

        return !isEmpty(sparseArray);
    }

    public static boolean isEmpty(Bundle bundle) {

        return bundle == null || bundle.isEmpty();
    }

    public static boolean notEmpty(Bundle bundle) {

        return !isEmpty(bundle);
    }

    public static boolean isEmpty(ByteArray bytes) {
        return bytes == null || bytes.isEmpty();
    }

    public static boolean notEmpty(ByteArray bytes) {
        return !isEmpty(bytes);
    }

    /**
     * 判断某个对象是否属于指定类
     *
     * @param type
     * @param obj
     */
    public static boolean isInstanceOf(Class<?> type, Object obj) {


        return type != null && type.isInstance(obj);
    }

    /**
     * 判定此 Class 对象所表示的类或接口与指定的 Class 参数所表示的类或接口是否相同,或是否是其超类或超接口.
     *
     * @param superType
     * @param subType
     */
    public static boolean isAssignable(Class<?> superType, Class<?> subType) {

        return superType != null && subType != null && superType.isAssignableFrom(subType);
    }

    public static boolean exists(File file) {
        return file != null && file.exists();
    }

    @Deprecated
    public static boolean check(Validator v) {

        return v != null && v.check();
    }

    public static boolean checkIndex(Collection<?> collection, int index) {
        return notEmpty(collection) && index >= 0 && collection.size() > index;
    }

    public static <O> boolean checkIndex(O[] array, int index) {
        return notEmpty(array) && index >= 0 && array.length > index;
    }

    public static boolean containsKey(Map<?, ?> map, Object key) {

        return notEmpty(map) && map.containsKey(key);
    }

    public static boolean containsValue(Map<?, ?> map, Object value) {

        return notEmpty(map) && map.containsValue(value);
    }

    public static boolean containsKey(SparseArray<?> array, int key) {

        return notEmpty(array) && array.indexOfKey(key) >= 0;
    }

    public static boolean containsValue(int[] array, int value) {
        boolean result = false;

        if (notEmpty(array)) {
            result = Arrays.binarySearch(array, 0, array.length, value) >= 0;
        }

        return result;
    }

    /**
     * 判断是否以指定字符结尾
     *
     * @param strBuilder
     * @param suffix
     * @return
     */
    public static boolean endsWith(StringBuilder strBuilder, String suffix) {
        boolean result = false;

        if (strBuilder != null && Assert.notEmpty(suffix)) {
            int endIndex = strBuilder.length() - suffix.length();
            result = endIndex >= 0 && strBuilder.lastIndexOf(suffix) == endIndex;
        }

        return result;
    }

    /**
     * 判断是否以指定字符结尾
     *
     * @param strBuilder
     * @param suffix
     * @return
     */
    public static boolean startWith(StringBuilder strBuilder, String suffix) {
        boolean result = false;

        if (strBuilder != null && Assert.notEmpty(suffix)) {
            result = strBuilder.length() > suffix.length() && strBuilder.indexOf(suffix) == 0;
        }

        return result;
    }

    /**
     * 判断是否以指定字符结尾
     *
     * @param strBuffer
     * @param suffix
     * @return
     */
    public static boolean endsWith(StringBuffer strBuffer, String suffix) {
        boolean result = false;

        if (strBuffer != null && Assert.notEmpty(suffix)) {
            int endIndex = strBuffer.length() - suffix.length();
            result = endIndex >= 0 && strBuffer.lastIndexOf(suffix) == endIndex;
        }

        return result;
    }

    /**
     * 判断是否以指定字符结尾
     *
     * @param strBuffer
     * @param suffix
     * @return
     */
    public static boolean startWith(StringBuffer strBuffer, String suffix) {
        boolean result = false;

        if (strBuffer != null && Assert.notEmpty(suffix)) {
            result = strBuffer.length() > suffix.length() && strBuffer.indexOf(suffix) == 0;
        }

        return result;
    }

    private Assert() {

    }
}
