package top.hendrixshen.magiclib.util;

import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.util.collect.ValueContainer;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

public class ReflectionUtil {
    public static <T> @NotNull ValueContainer<Class<T>> getClass(String className) {
        try {
            @SuppressWarnings("unchecked")
            ValueContainer<Class<T>> container = ValueContainer.of((Class<T>) Class.forName(className));
            return container;
        } catch (ClassNotFoundException e) {
            return ValueContainer.exception(e);
        }
    }

    public static <T> @NotNull ValueContainer<Class<T>> getClass(@NotNull Class<T> clazz) {
        return ValueContainer.of(clazz);
    }

    public static <T> @NotNull ValueContainer<T> newInstance(String className, Class<?>[] type, Object... args) {
        ValueContainer<Class<T>> clazz = ReflectionUtil.getClass(className);
        return ReflectionUtil.newInstance(clazz, type, args);
    }

    public static <T> @NotNull ValueContainer<T> newInstance(@NotNull ValueContainer<Class<T>> classContainer,
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

    public static <R, T> ValueContainer<T> getStaticFieldValue(@NotNull ValueContainer<Class<R>> classContainer,
                                                               String fieldName) {
        return ReflectionUtil.getDeclaredFieldValue(classContainer, fieldName, null);
    }

    public static <T> ValueContainer<T> getStaticFieldValue(@NotNull Class<?> clazz, String fieldName) {
        return ReflectionUtil.getDeclaredFieldValue(clazz, fieldName, null);
    }

    public static <R, T> @NotNull ValueContainer<Boolean> setStaticFieldValue(String className, String fieldName,
                                                                              T value) {
        ValueContainer<Class<R>> clazz = ReflectionUtil.getClass(className);
        return ReflectionUtil.setDeclaredFieldValue(clazz, fieldName, null, value);
    }

    public static <R, T> ValueContainer<Boolean> setStaticFieldValue(@NotNull ValueContainer<Class<R>> classContainer,
                                                                     String fieldName, T value) {
        if (classContainer.isException()) {
            return ValueContainer.exception(classContainer.getException());
        }

        return ReflectionUtil.setDeclaredFieldValue(classContainer.get(), fieldName, null, value);
    }

    public static <T> ValueContainer<Boolean> setStaticFieldValue(@NotNull Class<T> cls, String fieldName, T value) {
        return ReflectionUtil.setDeclaredFieldValue(cls, fieldName, null, value);
    }

    public static <R, S, T> ValueContainer<T> getDeclaredFieldValue(String className, String fieldName, S instance) {
        ValueContainer<Class<R>> clazz = ReflectionUtil.getClass(className);
        return ReflectionUtil.getDeclaredFieldValue(clazz, fieldName, instance);
    }

    public static <R, S, T> ValueContainer<T> getDeclaredFieldValue(@NotNull ValueContainer<Class<R>> classContainer,
                                                                    String fieldName, S instance) {
        if (classContainer.isException()) {
            return ValueContainer.exception(classContainer.getException());
        }

        return ReflectionUtil.getDeclaredFieldValue(classContainer.get(), fieldName, instance);
    }

    public static <R, S, T> ValueContainer<T> getDeclaredFieldValue(@NotNull Class<R> clazz, String fieldName,
                                                                    S instance) {
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

    public static <R, S, T> @NotNull ValueContainer<Boolean> setDeclaredFieldValue(String className, String fieldName,
                                                                                   S instance, T value) {
        ValueContainer<Class<R>> clazz = ReflectionUtil.getClass(className);
        return ReflectionUtil.setDeclaredFieldValue(clazz, fieldName, instance, value);
    }

    public static <R, S, T> @NotNull ValueContainer<Boolean> setDeclaredFieldValue(
            @NotNull ValueContainer<Class<R>> classContainer, String fieldName, S instance, T value) {
        if (classContainer.isException()) {
            return ValueContainer.exception(classContainer.getException());
        }

        return ReflectionUtil.setDeclaredFieldValue(classContainer.get(), fieldName, instance, value);
    }

    public static <R, S, T> ValueContainer<Boolean> setDeclaredFieldValue(@NotNull Class<R> clazz, String fieldName,
                                                                          S instance, T value) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(instance, value);
            return ValueContainer.of(true);
        } catch (Exception e) {
            return ValueContainer.exception(e);
        }
    }

    public static <R, S, T> ValueContainer<T> getFieldValue(String className, String fieldName, S instance) {
        ValueContainer<Class<R>> clazz = ReflectionUtil.getClass(className);
        return ReflectionUtil.getFieldValue(clazz, fieldName, instance);
    }

    public static <R, S, T> ValueContainer<T> getFieldValue(@NotNull ValueContainer<Class<R>> classContainer,
                                                            String fieldName, S instance) {
        if (classContainer.isException()) {
            return ValueContainer.exception(classContainer.getException());
        }

        return ReflectionUtil.getFieldValue(classContainer.get(), fieldName, instance);
    }

    public static <R, S, T> ValueContainer<T> getFieldValue(@NotNull Class<R> cls, String fieldName, S instance) {
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

    public static <R, S, T> @NotNull ValueContainer<Boolean> setFieldValue(String className, String fieldName,
                                                                           S instance, T value) {
        ValueContainer<Class<R>> clazz = ReflectionUtil.getClass(className);
        return ReflectionUtil.setFieldValue(clazz, fieldName, instance, value);
    }

    public static <R, S, T> @NotNull ValueContainer<Boolean> setFieldValue(
            @NotNull ValueContainer<Class<R>> classContainer, String fieldName, S instance, T value) {
        if (classContainer.isException()) {
            return ValueContainer.exception(classContainer.getException());
        }

        return ReflectionUtil.setFieldValue(classContainer.get(), fieldName, instance, value);
    }

    public static <R, S, T> ValueContainer<Boolean> setFieldValue(@NotNull Class<R> clazz, String fieldName,
                                                                  S instance, T value) {
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

    public static <S, T> ValueContainer<T> invokeDeclared(@NotNull Class<?> cls, String methodName, S instance,
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

    public static <S, T> ValueContainer<T> invoke(@NotNull Class<?> cls, String methodName, S instance,
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
