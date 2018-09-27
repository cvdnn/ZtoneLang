package android.reflect;

public interface ClazzFilter {

	boolean accept(String clazzName, Class<?> clazz);
}
