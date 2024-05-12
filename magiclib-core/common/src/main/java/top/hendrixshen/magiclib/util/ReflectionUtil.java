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
            return ValueContainer.exception(classContainer.getException());
        }

        return ReflectionUtil.newInstance(classContainer.get(), type, args);
    }

    public static <T> @NotNull ValueContainer<T> newInstance(@NotNull Class<?> clazz, Class<?>[] type, Object... args) {
        try {
            Constructor<?> declaredConstructor = clazz.getDeclaredConstructor(type);
            declaredConstructor.setAccessible(true);
            @SuppressWarnings("unchecked")
            ValueContainer<T> container = ValueContainer.of((T) declaredConstructor.newInstance(args));
            return container;
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

    public static ValueContainer<Boolean> setStaticFieldValue(@NotNull Class<?> clazz, String fieldName, Object value) {
        return ReflectionUtil.setDeclaredFieldValue(clazz, fieldName, null, value);
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

    public static <T> ValueContainer<T> getFieldValue(@NotNull Class<?> clazz, String fieldName, Object instance) {
        try {
            Field field = clazz.getField(fieldName);
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

    public static <T> ValueContainer<T> invokeStatic(@NotNull Class<?> clazz, String methodName,
                                                     Class<?>[] type, Object... args) {
        return ReflectionUtil.invokeDeclared(clazz, methodName, null, type, args);
    }

    public static <T> ValueContainer<T> invokeDeclared(String className, String methodName, Object instance,
                                                       Class<?>[] type, Object... args) {
        ValueContainer<Class<?>> clazz = ReflectionUtil.getClass(className);
        return ReflectionUtil.invokeDeclared(clazz, methodName, instance, type, args);
    }

    public static <T> ValueContainer<T> invokeDeclared(@NotNull ValueContainer<Class<?>> classContainer,
                                                       String methodName, Object instance, Class<?>[] type,
                                                       Object... args) {
        if (classContainer.isException()) {
            return ValueContainer.exception(classContainer.getException());
        }

        return ReflectionUtil.invokeDeclared(classContainer.get(), methodName, instance, type, args);
    }

    public static <T> ValueContainer<T> invokeDeclared(@NotNull Class<?> clazz, String methodName, Object instance,
                                                       Class<?>[] type, Object... args) {
        try {
            Method declaredMethod = clazz.getDeclaredMethod(methodName, type);
            declaredMethod.setAccessible(true);
            @SuppressWarnings("unchecked")
            ValueContainer<T> container = ValueContainer.of((T) declaredMethod.invoke(instance, args));
            return container;
        } catch (Exception e) {
            return ValueContainer.exception(e);
        }
    }

    public static <T> ValueContainer<T> invoke(String className, String methodName, Object instance,
                                               Class<?>[] type, Object... args) {
        ValueContainer<Class<?>> clazz = ReflectionUtil.getClass(className);
        return ReflectionUtil.invoke(clazz, methodName, instance, type, args);
    }

    public static <T> ValueContainer<T> invoke(@NotNull ValueContainer<Class<?>> classContainer, String methodName,
                                               Object instance, Class<?>[] type, Object... args) {
        if (classContainer.isException()) {
            return ValueContainer.exception(classContainer.getException());
        }

        return ReflectionUtil.invoke(classContainer.get(), methodName, instance, type, args);
    }

    public static <T> ValueContainer<T> invoke(@NotNull Class<?> clazz, String methodName, Object instance,
                                               Class<?>[] type, Object... args) {
        try {
            clazz.getDeclaredMethod(methodName, type);
            Method method = clazz.getMethod(methodName, type);
            method.setAccessible(true);
            @SuppressWarnings("unchecked")
            ValueContainer<T> container = ValueContainer.of((T) method.invoke(instance, args));
            return container;
        } catch (Exception e) {
            return ValueContainer.exception(e);
        }
    }
}
