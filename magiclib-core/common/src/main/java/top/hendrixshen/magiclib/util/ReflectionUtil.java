package top.hendrixshen.magiclib.util;

import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.util.collect.ValueContainer;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

public class ReflectionUtil {
    public static @NotNull ValueContainer<Class<?>> getClass(String className) {
        try {
            return ValueContainer.of(Class.forName(className));
        } catch (ClassNotFoundException e) {
            return ValueContainer.exception(e);
        }
    }

    public static @NotNull ValueContainer<Class<?>> getClass(@NotNull Class<?> clazz) {
        return ValueContainer.of(clazz);
    }

    public static <T> @NotNull ValueContainer<T> newInstance(String className, Class<?>[] type, Object... args) {
        ValueContainer<Class<?>> clazz = ReflectionUtil.getClass(className);
        return ReflectionUtil.newInstance(clazz, type, args);
    }

    public static <T> @NotNull ValueContainer<T> newInstance(@NotNull ValueContainer<Class<?>> classContainer,
                                                             Class<?>[] type, Object... args) {
        if (classContainer.isException()) {
            return ReflectionUtil.newInstance(classContainer.get(), type, args);
        }

        return ValueContainer.exception(classContainer.getException());
    }

    public static <T> @NotNull ValueContainer<T> newInstance(@NotNull Class<?> clazz, Class<?>[] type, Object... args) {
        try {
            for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
                if (Arrays.equals(constructor.getParameterTypes(), type)) {
                    constructor.setAccessible(true);
                    @SuppressWarnings("unchecked")
                    ValueContainer<T> container = ValueContainer.ofNullable((T) constructor.newInstance(args));
                    return container;
                }
            }

            return ValueContainer.empty();
        } catch (Exception e) {
            return ValueContainer.exception(e);
        }
    }

    public static <T> ValueContainer<T> getStaticFieldValue(String className, String fieldName) {
        return ReflectionUtil.getDeclaredFieldValue(className, fieldName, null);
    }

    public static <T> ValueContainer<T> getStaticFieldValue(@NotNull ValueContainer<Class<?>> classContainer,
                                                            String fieldName) {
        return ReflectionUtil.getDeclaredFieldValue(classContainer, fieldName, null);
    }

    public static <T> ValueContainer<T> getStaticFieldValue(@NotNull Class<?> clazz, String fieldName) {
        return ReflectionUtil.getDeclaredFieldValue(clazz, fieldName, null);
    }

    public static @NotNull ValueContainer<Boolean> setStaticFieldValue(String className, String fieldName,
                                                                       Object value) {
        ValueContainer<Class<?>> clazz = ReflectionUtil.getClass(className);
        return ReflectionUtil.setDeclaredFieldValue(clazz, fieldName, null, value);
    }

    public static ValueContainer<Boolean> setStaticFieldValue(@NotNull ValueContainer<Class<?>> classContainer,
                                                              String fieldName, Object value) {
        if (classContainer.isException()) {
            return ValueContainer.exception(classContainer.getException());
        }

        return ReflectionUtil.setDeclaredFieldValue(classContainer.get(), fieldName, null, value);
    }

    public static ValueContainer<Boolean> setStaticFieldValue(@NotNull Class<?> cls, String fieldName, Object value) {
        return ReflectionUtil.setDeclaredFieldValue(cls, fieldName, null, value);
    }

    public static <T> ValueContainer<T> getDeclaredFieldValue(String className, String fieldName, Object instance) {
        ValueContainer<Class<?>> clazz = ReflectionUtil.getClass(className);
        return ReflectionUtil.getDeclaredFieldValue(clazz, fieldName, instance);
    }

    public static <T> ValueContainer<T> getDeclaredFieldValue(@NotNull ValueContainer<Class<?>> classContainer,
                                                              String fieldName, Object instance) {
        if (classContainer.isException()) {
            return ValueContainer.exception(classContainer.getException());
        }

        return ReflectionUtil.getDeclaredFieldValue(classContainer.get(), fieldName, instance);
    }

    public static <T> ValueContainer<T> getDeclaredFieldValue(@NotNull Class<?> clazz, String fieldName,
                                                              Object instance) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            @SuppressWarnings("unchecked")
            ValueContainer<T> container = ValueContainer.ofNullable((T) field.get(instance));
            return container;
        } catch (Exception e) {
            return ValueContainer.exception(e);
        }
    }

    public static @NotNull ValueContainer<Boolean> setDeclaredFieldValue(String className, String fieldName,
                                                                         Object instance, Object value) {
        ValueContainer<Class<?>> clazz = ReflectionUtil.getClass(className);
        return ReflectionUtil.setDeclaredFieldValue(clazz, fieldName, instance, value);
    }

    public static @NotNull ValueContainer<Boolean> setDeclaredFieldValue(
            @NotNull ValueContainer<Class<?>> classContainer, String fieldName, Object instance, Object value) {
        if (classContainer.isException()) {
            return ValueContainer.exception(classContainer.getException());
        }

        return ReflectionUtil.setDeclaredFieldValue(classContainer.get(), fieldName, instance, value);
    }

    public static ValueContainer<Boolean> setDeclaredFieldValue(@NotNull Class<?> clazz, String fieldName,
                                                                Object instance, Object value) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(instance, value);
            return ValueContainer.of(true);
        } catch (Exception e) {
            return ValueContainer.exception(e);
        }
    }

    public static <T> ValueContainer<T> getFieldValue(String className, String fieldName, Object instance) {
        ValueContainer<Class<?>> clazz = ReflectionUtil.getClass(className);
        return ReflectionUtil.getFieldValue(clazz, fieldName, instance);
    }

    public static <T> ValueContainer<T> getFieldValue(@NotNull ValueContainer<Class<?>> classContainer,
                                                      String fieldName, Object instance) {
        if (classContainer.isException()) {
            return ValueContainer.exception(classContainer.getException());
        }

        return ReflectionUtil.getFieldValue(classContainer.get(), fieldName, instance);
    }

    public static <T> ValueContainer<T> getFieldValue(@NotNull Class<?> cls, String fieldName, Object instance) {
        try {
            Field field = cls.getField(fieldName);
            field.setAccessible(true);
            @SuppressWarnings("unchecked")
            ValueContainer<T> container = ValueContainer.ofNullable((T) field.get(instance));
            return container;
        } catch (Exception e) {
            return ValueContainer.exception(e);
        }
    }

    public static @NotNull ValueContainer<Boolean> setFieldValue(String className, String fieldName,
                                                                 Object instance, Object value) {
        ValueContainer<Class<?>> clazz = ReflectionUtil.getClass(className);
        return ReflectionUtil.setFieldValue(clazz, fieldName, instance, value);
    }

    public static @NotNull ValueContainer<Boolean> setFieldValue(
            @NotNull ValueContainer<Class<?>> classContainer, String fieldName, Object instance, Object value) {
        if (classContainer.isException()) {
            return ValueContainer.exception(classContainer.getException());
        }

        return ReflectionUtil.setFieldValue(classContainer.get(), fieldName, instance, value);
    }

    public static ValueContainer<Boolean> setFieldValue(@NotNull Class<?> clazz, String fieldName,
                                                        Object instance, Object value) {
        try {
            Field field = clazz.getField(fieldName);
            field.setAccessible(true);
            field.set(instance, value);
            return ValueContainer.of(true);
        } catch (Exception e) {
            return ValueContainer.exception(e);
        }
    }

    public static <T> ValueContainer<T> invokeStatic(@NotNull Class<?> cls, String methodName,
                                                     Class<?>[] type, Object... args) {
        return ReflectionUtil.invokeDeclared(cls, methodName, null, type, args);
    }

    public static <T> ValueContainer<T> invokeDeclared(@NotNull Class<?> cls, String methodName, Object instance,
                                                       Class<?>[] type, Object... args) {
        try {
            for (Method method : cls.getDeclaredMethods()) {
                if (methodName.equals(method.getName()) && Arrays.equals(type, method.getParameterTypes())) {
                    method.setAccessible(true);
                    @SuppressWarnings("unchecked")
                    ValueContainer<T> container = ValueContainer.ofNullable((T) method.invoke(instance, args));
                    return container;
                }
            }

            return ValueContainer.empty();
        } catch (Exception e) {
            return ValueContainer.exception(e);
        }
    }

    public static <T> ValueContainer<T> invoke(@NotNull Class<?> cls, String methodName, Object instance,
                                               Class<?>[] type, Object... args) {
        try {
            for (Method method : cls.getMethods()) {
                if (methodName.equals(method.getName()) && Arrays.equals(type, method.getParameterTypes())) {
                    method.setAccessible(true);
                    @SuppressWarnings("unchecked")
                    ValueContainer<T> container = ValueContainer.ofNullable((T) method.invoke(instance, args));
                    return container;
                }
            }

            return ValueContainer.empty();
        } catch (Exception e) {
            return ValueContainer.exception(e);
        }
    }
}
