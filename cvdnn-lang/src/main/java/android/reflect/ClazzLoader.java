/*
 * ClazzLoader.java
 *
 * Copyright 2011 sillar team, Inc. All rights reserved.
 *
 * SILLAR PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package android.reflect;

import android.assist.Assert;
import android.content.Context;
import android.log.Log;
import android.util.ArrayMap;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Enumeration;

import dalvik.system.DexClassLoader;
import dalvik.system.DexFile;

/**
 * @author sillar team
 * @version 1.0.0
 * @see android.reflect.Clazz
 * @since 1.0.0 Handy 2013-9-2
 * @deprecated
 */
public final class ClazzLoader {
    private static final String TAG = "ClazzLoader";

    /**
     * 获取包下类文件
     *
     * @return
     */
    public static ArrayList<String> scanPackageClazz(Context context, ClazzNameFilter clazzNameFilter) {

        return context != null ? scanPackageClazz(context.getPackageCodePath(), clazzNameFilter) : null;
    }

    /**
     * 获取包下类文件
     */
    public static ArrayList<String> scanPackageClazz(String apkPath, ClazzNameFilter clazzNameFilter) {
        ArrayList<String> clazzList = null;

        if (Assert.notEmpty(apkPath)) {
            clazzList = new ArrayList<>();

            try {
                Enumeration<String> entries = new DexFile(apkPath).entries();
                if (entries != null) {
                    while (entries.hasMoreElements()) {
                        String clazzName = entries.nextElement();

                        if (clazzNameFilter == null || clazzNameFilter.accept(clazzName)) {
                            clazzList.add(clazzName);
                        }
                    }
                }
            } catch (Exception e) {
                Log.d(TAG, e);
            }
        }

        return clazzList;
    }

    /**
     * 获取包下类文件
     */
    public static ArrayMap<String, Class<?>> scanPackageClazz(Context context, ClazzFilter clazzFilter) {
        ArrayMap<String, Class<?>> clazzMap = null;

        if (context != null) {
            clazzMap = new ArrayMap<String, Class<?>>();

            try {
                DexFile dexFile = new DexFile(context.getPackageCodePath());
                if (dexFile != null) {
                    Enumeration<String> entries = dexFile.entries();
                    if (entries != null) {
                        while (entries.hasMoreElements()) {
                            String clazzName = entries.nextElement();
                            if (Assert.notEmpty(clazzName)) {
                                Class<?> clazz = ClazzLoader.forName(clazzName);
                                if (clazz != null && (clazz == null || clazzFilter.accept(clazzName, clazz))) {
                                    clazzMap.put(clazzName, clazz);
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                Log.d(TAG, e);
            }
        }

        return clazzMap;
    }

    /**
     * 获取参数类型
     */
    public static Class<?>[] getParameterTypes(Object... objs) {
        Class<?>[] parameterTypes = null;
        if (Assert.notEmpty(objs)) {
            parameterTypes = new Class<?>[objs.length];
            for (int i = 0; i < objs.length; i++) {
                Object obj = objs[i];
                if (obj != null) {
                    parameterTypes[i] = obj.getClass();
                }
            }
        }

        return parameterTypes;
    }

    /**
     * 加载类
     */
    public static <Z> Class<Z> forName(String className) {
        Class<Z> clazz = null;

        if (Assert.notEmpty(className)) {
            try {
                Class<?> tempClazz = null;

                ClassLoader classLoader = ClazzLoader.class.getClassLoader();
                if (classLoader != null) {
                    tempClazz = classLoader.loadClass(className);
                } else {
                    tempClazz = Class.forName(className);
                }

                if (tempClazz != null) {
                    clazz = (Class<Z>) tempClazz;
                }
            } catch (Throwable t) {
                Log.e(TAG, t);
            }
        }

        return clazz;
    }

    /**
     * 加载本地.dex中的class
     */
    public static <Z> Class<Z> forName(ClassLoader classLoader, String desPath, String dexPath, String className) {
        Class<Z> clazz = null;

        if (classLoader != null && Assert.notEmpty(desPath) && Assert.notEmpty(dexPath) && Assert.notEmpty(className)) {
            DexClassLoader dexClassLoader = null;
            try {
                dexClassLoader = new DexClassLoader(dexPath, desPath, null, classLoader);
                Class<?> tempClazz = dexClassLoader.loadClass(className);
                if (tempClazz != null) {
                    clazz = (Class<Z>) tempClazz;
                }
            } catch (Throwable t) {
                Log.e(TAG, "class: " + className);
                Log.e(TAG, t);
            }
        }

        return clazz;
    }

    /**
     * 實例化加载本地.dex中的class
     */
    public static <Z> Z newInstance(ClassLoader classLoader, String desPath, String dexPath, String className, Object... objs) {
        Z z = null;

        Class<Z> clazz = forName(classLoader, desPath, dexPath, className);
        if (clazz != null) {
            z = newInstance(clazz, objs);
        }

        return z;
    }

    /**
     * 實例化
     */
    public static <Z> Z newInstance(String className, Object... objs) {
        Z z = null;

        Class<?> clazz = forName(className);
        if (clazz != null) {
            try {
                z = newInstance((Class<Z>) clazz, objs);
            } catch (Throwable t) {
                Log.e(TAG, "class: " + className);
                Log.e(TAG, t);
            }
        }

        return z;
    }

    /**
     * 實例化
     */
    public static <Z> Z newInstance(Class<Z> clazz, Object... objs) {
        Z z = null;

        if (clazz != null) {
            try {
                Constructor<Z> constructor = clazz.getConstructor(getParameterTypes(objs));
                z = constructor != null ? constructor.newInstance(objs) : clazz.newInstance();
            } catch (Throwable t) {
                Log.e(TAG, "class: " + clazz.getName());
                Log.e(TAG, t);
            }
        }

        return z;
    }

    /**
     * 實例化
     */
    public static <O, Z> Z newInstance(O o, Class<Z> clazz, Object... objs) {
        Z z = null;

        if (clazz != null) {
            try {
                boolean isInner = clazz.isAnnotationPresent(Inner.class);
                if (isInner && o != null) {
                    int size = (Assert.notEmpty(objs) ? objs.length : 0) + 1; // 1 内部类首参数是外部类实例

                    Class<?>[] clazzes = new Class<?>[size];
                    clazzes[0] = o.getClass();

                    Object[] values = new Object[size];
                    values[0] = o;

                    if (Assert.notEmpty(objs)) {
                        for (int i = 0; i < objs.length; i++) {
                            Object obj = objs[i];
                            if (obj != null) {

                                clazzes[i + 1] = obj.getClass();
                                values[i + 1] = obj;
                            }
                        }
                    }

                    Constructor<Z> constructor = clazz.getConstructor(clazzes);
                    z = constructor != null ? constructor.newInstance(values) : clazz.newInstance();

                } else {
                    Constructor<Z> constructor = clazz.getConstructor(getParameterTypes(objs));
                    z = constructor != null ? constructor.newInstance(objs) : clazz.newInstance();

                }
            } catch (Throwable t) {
                Log.i(TAG, "class: " + clazz.getName());
                Log.e(TAG, t);
            }
        }

        return z;
    }

    /**
     * 遍历查找Field
     */
    public static <O> Field getField(O o, String fieldName) {

        return getField(null, o, fieldName);
    }

    /**
     * 遍历查找Field
     */
    public static <O> Field getField(Class<?> clazz, O o, String fieldName) {
        Field field = null;

        if (Assert.notEmpty(fieldName)) {
            Class<?> desClazz = clazz != null ? clazz : (o != null ? o.getClass() : null);
            while (desClazz != null && desClazz != Object.class) {
                try {
                    field = desClazz.getDeclaredField(fieldName);
                    if (field != null) {
                        field.setAccessible(true);
                    } else {
                        desClazz = desClazz.getSuperclass();
                    }
                } catch (Throwable t) {
                    Log.e(TAG, "fieldName: " + fieldName);
                    Log.e(TAG, t);
                }
            }
        }

        return field;
    }

    /**
     * 反射获取静态变量
     */
    public static <O, V> V getFieldValue(O o, String fieldName) {

        return getFieldValue(null, o, fieldName);
    }

    /**
     * 反射获取静态变量
     */
    public static <V> V getFieldValue(Class<?> clazz, String fieldName) {

        return getFieldValue(clazz, null, fieldName);
    }

    /**
     * 反射设置变量
     */
    public static <O, V> V getFieldValue(Class<?> clazz, O o, String fieldName) {
        V value = null;

        if (Assert.notEmpty(fieldName)) {
            Class<?> desClazz = clazz != null ? clazz : (o != null ? o.getClass() : null);
            if (desClazz != null) {
                try {
                    value = getFieldValue(o, desClazz.getDeclaredField(fieldName));
                } catch (Throwable t) {
                    Log.e(TAG, "fieldName: " + fieldName);
                    Log.e(TAG, t);
                }
            }
        }

        return value;
    }

    /**
     * 反射设置变量
     */
    public static <O, V> V getFieldValue(Field field) {

        return getFieldValue(null, field);
    }

    /**
     * 反射设置变量
     */
    public static <O, V> V getFieldValue(O o, Field field) {
        V value = null;

        if (field != null) {
            try {
                field.setAccessible(true);

                value = (V) field.get(o);
            } catch (Throwable e) {
                Log.v(TAG, e);
            }
        }

        return value;
    }

    /**
     * 反射设置变量
     */
    public static <O, V> void setFieldValue(O o, String fieldName, V v) {
        setFieldValue(null, o, fieldName, v);
    }

    /**
     * 反射设置静态变量
     */
    public static <V> void setFieldValue(Class<?> clazz, String fieldName, V v) {
        setFieldValue(clazz, null, fieldName, v);
    }

    /**
     * 反射设置变量
     */
    public static <O, V> void setFieldValue(Class<?> clazz, O o, String fieldName, V v) {
        if (Assert.notEmpty(fieldName)) {
            Class<?> desClass = clazz != null ? clazz : (o != null ? o.getClass() : null);
            if (desClass != null) {
                try {
                    setFieldValue(o, desClass.getDeclaredField(fieldName), v);
                } catch (Throwable t) {
                    Log.e(TAG, t);
                }
            }
        }
    }

    /**
     * 反射设置变量
     */
    public static <O, V> void setFieldValue(Field field, V v) {
        setFieldValue(null, field, v);
    }

    /**
     * 反射设置变量
     */
    public static <O, V> void setFieldValue(O o, Field field, V v) {
        if (field != null) {
            try {
                field.setAccessible(true);

                if (v == null) {
                    field.set(o, null);
                } else {
                    Class<?> vType = v.getClass();
                    if (vType == Integer.TYPE) {
                        field.setInt(o, (Integer) v);
                    } else if (vType == Long.TYPE) {
                        field.setLong(o, (Long) v);
                    } else if (vType == Boolean.TYPE) {
                        field.setBoolean(o, (Boolean) v);
                    } else if (vType == Float.TYPE) {
                        field.setFloat(o, (Float) v);
                    } else if (vType == Short.TYPE) {
                        field.setShort(o, (Short) v);
                    } else if (vType == Byte.TYPE) {
                        field.setByte(o, (Byte) v);
                    } else if (vType == Double.TYPE) {
                        field.setDouble(o, (Double) v);
                    } else if (vType == Character.TYPE) {
                        field.setChar(o, (Character) v);
                    } else {
                        field.set(o, v);
                    }
                }
            } catch (Throwable t) {
                Log.e(TAG, t);
            }
        }
    }

    /**
     * 获取一个带注解的方法
     */
    public static Method getSingleMethodByAnnotation(Class<?> clazz, Class<? extends Annotation> annClazz) {
        Method method = null;

        if (clazz != null && annClazz != null) {
            Method[] methods = clazz.getMethods();
            if (Assert.notEmpty(methods)) {
                for (Method m : methods) {
                    if (m != null && m.isAnnotationPresent(annClazz)) {
                        method = m;

                        break;
                    }
                }
            }
        }

        return method;
    }

    /**
     * 反射执行静态方法
     */
    public static <V> V invoke(String className, String methodName, Object... args) {

        return invoke(forName(className), null, methodName, args);
    }

    /**
     * 反射执行静态方法
     */
    public static <V> V invoke(Class<?> clazz, String methodName, Object... args) {

        return invoke(clazz, null, methodName, args);
    }

    /**
     * 反射执行静态方法
     */
    public static <O, V> V invoke(O o, String methodName, Object... args) {

        return invoke(null, o, methodName, args);
    }

    /**
     * 反射执行静态方法
     */
    public static <O, V> V invoke(Class<?> clazz, O o, String methodName, Object... args) {

        return invoke(clazz, o, methodName, getParameterTypes(args), args);
    }

    /**
     * 反射执行静态方法
     */
    public static <O, V> V invoke(Class<?> clazz, O o, String methodName, Class<?>[] parameterTypes, Object[] args) {
        V v = null;

        if (Assert.notEmpty(methodName)) {
            Class<?> desClazz = clazz != null ? clazz : (o != null ? o.getClass() : null);
            if (desClazz != null) {
                try {
                    Method method = desClazz.getDeclaredMethod(methodName, parameterTypes);
                    if (method != null) {
                        method.setAccessible(true);

                        v = (V) method.invoke(o, args);
                    }
                } catch (Throwable t) {
                    Log.e(TAG, "methodName: " + methodName);
                    Log.e(TAG, t);
                }
            }
        }

        return v;
    }
}