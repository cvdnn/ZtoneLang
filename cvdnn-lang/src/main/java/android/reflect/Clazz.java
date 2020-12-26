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
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Enumeration;

import dalvik.system.DexClassLoader;
import dalvik.system.DexFile;

/**
 * @author sillar team
 * @version 1.0.0
 * @since 1.0.0 Handy 2013-9-2
 */
public final class Clazz {
    private static final String TAG = "Clazz";

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
                DexFile dexFile = new DexFile(apkPath);
                Enumeration<String> entries = dexFile.entries();
                if (entries != null) {
                    while (entries.hasMoreElements()) {
                        String clazzName = entries.nextElement();

                        if (clazzNameFilter == null || clazzNameFilter.accept(clazzName)) {
                            clazzList.add(clazzName);
                        }
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        }

        return clazzList;
    }

    /**
     * 获取包下类文件
     */
    public static ArrayMap<String, Class<?>> scanPackageClazz(Context context, ClazzFilter clazzFilter) {
        ArrayMap<String, Class<?>> clazzMap = new ArrayMap<>();

        if (context != null) {
            try {
                DexFile dexFile = new DexFile(context.getPackageCodePath());
                Enumeration<String> entries = dexFile.entries();
                if (entries != null) {
                    while (entries.hasMoreElements()) {
                        String clazzName = entries.nextElement();
                        if (Assert.notEmpty(clazzName)) {
                            Class<?> clazz = Clazz.forName(clazzName);
                            if (clazz != null && (clazzFilter == null || clazzFilter.accept(clazzName, clazz))) {
                                clazzMap.put(clazzName, clazz);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        }

        return clazzMap;
    }

    /**
     * 获取参数类型
     */
    public static Class<?>[] getClassTypes(Object... objs) {
        Class<?>[] pTypes = null;
        if (Assert.notEmpty(objs)) {
            pTypes = new Class<?>[objs.length];
            for (int i = 0; i < objs.length; i++) {
                pTypes[i] = getClassType(objs[i]);
            }
        }

        return pTypes;
    }

    /**
     * 获取参数类型
     */
    public static Class<?> getClassType(Object obj) {
        Class<?> pType = null;

        if (obj != null) {
            Class<?> type = obj.getClass();
            if (type == Integer.class) {
                pType = Integer.TYPE;
            } else if (type == Long.class) {
                pType = Long.TYPE;
            } else if (type == Boolean.class) {
                pType = Boolean.TYPE;
            } else if (type == Float.class) {
                pType = Float.TYPE;
            } else if (type == Short.class) {
                pType = Short.TYPE;
            } else if (type == Byte.class) {
                pType = Byte.TYPE;
            } else if (type == Double.class) {
                pType = Double.TYPE;
            } else if (type == Character.class) {
                pType = Character.TYPE;
            } else {
                pType = type;
            }
        }

        return pType;
    }

    /**
     * 加载类
     */
    public static <Z> Class<Z> forName(String className) {
        Class<Z> clazz = null;

        if (Assert.notEmpty(className)) {
            try {
                Class<?> tempClazz = Class.forName(className);
                if (tempClazz != null) {
                    clazz = (Class<Z>) tempClazz;
                }
            } catch (Throwable t) {
                Log.e(TAG, t.toString());
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
                Log.e(TAG, t.toString());
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

        return newInstance(forName(className), objs);
    }

    /**
     * 實例化
     */
    public static <Z> Z newInstance(Class<Z> clazz, Object... objs) {
        Z z = null;

        if (clazz != null) {
            try {
                Constructor<Z> constructor = clazz.getDeclaredConstructor(getClassTypes(objs));
                z = constructor != null ? constructor.newInstance(objs) : clazz.newInstance();
            } catch (Throwable t) {
                Log.e(TAG, t.toString());
            }
        }

        return z;
    }

    public static <Z> Z newInstance(Class<Z> clazz, Class<?>[] ptypes, Object... objs) {
        Z z = null;
        if (clazz != null) {
            try {
                Constructor<Z> constructor = clazz.getDeclaredConstructor(ptypes);
                z = constructor != null ? constructor.newInstance(objs) : clazz.newInstance();
            } catch (Throwable t) {
                Log.e(TAG, t.toString());
            }
        }

        return z;
    }

    /**
     * 實例化
     */
    public static <O, Z> Z newInnerInstance(O o, Class<Z> clazz, Object... objs) {
        Z z = null;

        if (clazz != null) {
            try {
                // 内部非静态类实例化
                if (clazz.isMemberClass() && (clazz.getModifiers() & Modifier.STATIC) == 0 && o != null) {
                    int size = (Assert.notEmpty(objs) ? objs.length : 0) + 1; // 1 内部类首参数是外部类实例

                    Class<?>[] clazzes = new Class<?>[size];
                    clazzes[0] = o.getClass();

                    Object[] values = new Object[size];
                    values[0] = o;

                    if (Assert.notEmpty(objs)) {
                        for (int i = 0; i < objs.length; i++) {
                            Object obj = objs[i];
                            if (obj != null) {
                                clazzes[i + 1] = getClassType(obj);
                                values[i + 1] = obj;
                            }
                        }
                    }

                    Constructor<Z> constructor = clazz.getDeclaredConstructor(clazzes);
                    z = constructor != null ? constructor.newInstance(values) : clazz.newInstance();

                } else {
                    Constructor<Z> constructor = clazz.getDeclaredConstructor(getClassTypes(objs));
                    z = constructor != null ? constructor.newInstance(objs) : clazz.newInstance();

                }
            } catch (Throwable t) {
                Log.e(TAG, t.toString());
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
                    Log.e(TAG, t.toString());
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
                    Log.e(TAG, t.toString());
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
                Log.e(TAG, e.toString());
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
                    Log.e(TAG, t.toString());
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
                Log.e(TAG, t.toString());
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

        return invoke(clazz, o, methodName, getClassTypes(args), args);
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
                    Log.e(TAG, t.toString());
                }
            }
        }

        return v;
    }
}