package top.hendrixshen.magiclib.util;

import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.util.collect.ValueContainer;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

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

    @SuppressWarnings("unchecked")
    public static @NotNull <T extends Annotation> ValueContainer<Class<T>> getAnnotationClass(String className) {
        return ReflectionUtil.getClass(className)
                .filter(Class::isAnnotation)
                .map(clazz -> (Class<T>) clazz)
                .or(() -> ValueContainer.exception(new RuntimeException("Class" + className + " is not an annotation.")));
    }

    public static <T> @NotNull ValueContainer<T> newInstance(String className, Class<?>[] type, Object... args) {
        ValueContainer<Class<?>> classContainer = ReflectionUtil.getClass(className);
        return ReflectionUtil.newInstance(classContainer, type, args);
    }

    public static <T> @NotNull ValueContainer<T> newInstance(@NotNull ValueContainer<Class<?>> classContainer,
                                                             Class<?>[] type, Object... args) {
        if (classContainer.isException()) {
            return ValueContainer.exception(classContainer.getException());
        }

        return classContainer.flatMap(clazz -> ReflectionUtil.newInstance(clazz, type, args));
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

    public static ValueContainer<Field> getStaticField(String className, String fieldName) {
        return ReflectionUtil.getDeclaredField(className, fieldName);
    }

    public static ValueContainer<Field> getStaticField(@NotNull ValueContainer<Class<?>> classContainer,
                                                       String fieldName) {
        return ReflectionUtil.getDeclaredField(classContainer, fieldName);
    }

    public static @NotNull ValueContainer<Field> getStaticField(@NotNull Class<?> clazz, String fieldName) {
        return ReflectionUtil.getDeclaredField(clazz, fieldName);
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
        ValueContainer<Class<?>> classContainer = ReflectionUtil.getClass(className);
        return ReflectionUtil.setDeclaredFieldValue(classContainer, fieldName, null, value);
    }

    public static ValueContainer<Boolean> setStaticFieldValue(@NotNull ValueContainer<Class<?>> classContainer,
                                                              String fieldName, Object value) {
        if (classContainer.isException()) {
            return ValueContainer.exception(classContainer.getException());
        }

        return classContainer.flatMap(clazz -> ReflectionUtil.setDeclaredFieldValue(clazz, fieldName, null, value));
    }

    public static ValueContainer<Boolean> setStaticFieldValue(@NotNull Class<?> clazz, String fieldName, Object value) {
        return ReflectionUtil.setDeclaredFieldValue(clazz, fieldName, null, value);
    }

    public static ValueContainer<Field> getDeclaredField(String className, String fieldName) {
        ValueContainer<Class<?>> classContainer = ReflectionUtil.getClass(className);
        return ReflectionUtil.getDeclaredField(classContainer, fieldName);
    }

    public static ValueContainer<Field> getDeclaredField(@NotNull ValueContainer<Class<?>> classContainer,
                                                         String fieldName) {
        if (classContainer.isException()) {
            return ValueContainer.exception(classContainer.getException());
        }

        return classContainer.flatMap(clazz -> ReflectionUtil.getDeclaredField(clazz, fieldName));
    }

    public static @NotNull ValueContainer<Field> getDeclaredField(@NotNull Class<?> clazz, String fieldName) {
        try {
            return ValueContainer.ofNullable(clazz.getDeclaredField(fieldName));
        } catch (Exception e) {
            return ValueContainer.exception(e);
        }
    }

    public static <T> ValueContainer<T> getDeclaredFieldValue(String className, String fieldName, Object instance) {
        ValueContainer<Class<?>> classContainer = ReflectionUtil.getClass(className);
        return ReflectionUtil.getDeclaredFieldValue(classContainer, fieldName, instance);
    }

    public static <T> ValueContainer<T> getDeclaredFieldValue(@NotNull ValueContainer<Class<?>> classContainer,
                                                              String fieldName, Object instance) {
        if (classContainer.isException()) {
            return ValueContainer.exception(classContainer.getException());
        }

        return classContainer.flatMap(clazz -> ReflectionUtil.getDeclaredFieldValue(clazz, fieldName, instance));
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
        ValueContainer<Class<?>> classContainer = ReflectionUtil.getClass(className);
        return ReflectionUtil.setDeclaredFieldValue(classContainer, fieldName, instance, value);
    }

    public static @NotNull ValueContainer<Boolean> setDeclaredFieldValue(
            @NotNull ValueContainer<Class<?>> classContainer, String fieldName, Object instance, Object value) {
        if (classContainer.isException()) {
            return ValueContainer.exception(classContainer.getException());
        }

        return classContainer.flatMap(clazz -> ReflectionUtil.setDeclaredFieldValue(clazz, fieldName, instance, value));
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

    public static ValueContainer<Field> getField(String className, String fieldName) {
        ValueContainer<Class<?>> clazz = ReflectionUtil.getClass(className);
        return ReflectionUtil.getField(clazz, fieldName);
    }

    public static ValueContainer<Field> getField(@NotNull ValueContainer<Class<?>> classContainer,
                                                 String fieldName) {
        if (classContainer.isException()) {
            return ValueContainer.exception(classContainer.getException());
        }

        return ReflectionUtil.getField(classContainer.get(), fieldName);
    }

    public static @NotNull ValueContainer<Field> getField(@NotNull Class<?> clazz, String fieldName) {
        try {
            return ValueContainer.ofNullable(clazz.getField(fieldName));
        } catch (Exception e) {
            return ValueContainer.exception(e);
        }
    }

    public static <T> ValueContainer<T> getFieldValue(String className, String fieldName, Object instance) {
        ValueContainer<Class<?>> classContainer = ReflectionUtil.getClass(className);
        return ReflectionUtil.getFieldValue(classContainer, fieldName, instance);
    }

    public static <T> ValueContainer<T> getFieldValue(@NotNull ValueContainer<Class<?>> classContainer,
                                                      String fieldName, Object instance) {
        if (classContainer.isException()) {
            return ValueContainer.exception(classContainer.getException());
        }

        return classContainer.flatMap(clazz -> ReflectionUtil.getFieldValue(clazz, fieldName, instance));
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

        return classContainer.flatMap(clazz -> ReflectionUtil.setFieldValue(clazz, fieldName, instance, value));
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

    public static <T> ValueContainer<T> invokeStatic(String className, String methodName,
                                                     Class<?>[] type, Object... args) {
        ValueContainer<Class<?>> clazz = ReflectionUtil.getClass(className);
        return ReflectionUtil.invokeStatic(clazz, methodName, type, args);
    }

    public static <T> ValueContainer<T> invokeStatic(@NotNull ValueContainer<Class<?>> classContainer,
                                                     String methodName, Class<?>[] type, Object... args) {
        if (classContainer.isException()) {
            return ValueContainer.exception(classContainer.getException());
        }

        return ReflectionUtil.invokeStatic(classContainer.get(), methodName, type, args);
    }

    public static <T> ValueContainer<T> invokeStatic(@NotNull Class<?> clazz, String methodName,
                                                     Class<?>[] type, Object... args) {
        return ReflectionUtil.invokeDeclared(clazz, methodName, null, type, args);
    }

    public static <T> ValueContainer<T> invokeStatic(ValueContainer<Method> methodContainer, Object... args) {
        if (methodContainer.isException()) {
            return ValueContainer.exception(methodContainer.getException());
        }

        return methodContainer.flatMap(method -> ReflectionUtil.invokeStatic(method, args));
    }

    public static <T> ValueContainer<T> invokeStatic(Method method, Object... args) {
        return ReflectionUtil.invoke(method, null, args);
    }

    public static ValueContainer<Method> getDeclaredMethod(String className, String methodName, Class<?>... type) {
        ValueContainer<Class<?>> clazz = ReflectionUtil.getClass(className);
        return ReflectionUtil.getDeclaredMethod(clazz, methodName, type);
    }

    public static ValueContainer<Method> getDeclaredMethod(@NotNull ValueContainer<Class<?>> classContainer,
                                                           String methodName, Class<?>... type) {
        if (classContainer.isException()) {
            return ValueContainer.exception(classContainer.getException());
        }

        return classContainer.flatMap(clazz -> ReflectionUtil.getDeclaredMethod(clazz, methodName, type));
    }

    public static ValueContainer<Method> getDeclaredMethod(@NotNull Class<?> clazz, String methodName,
                                                           Class<?>... type) {
        try {
            Method declaredMethod = clazz.getDeclaredMethod(methodName, type);
            declaredMethod.setAccessible(true);
            return ValueContainer.of(declaredMethod);
        } catch (Exception e) {
            return ValueContainer.exception(e);
        }
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

        return classContainer.flatMap(clazz -> ReflectionUtil.invokeDeclared(clazz, methodName, instance, type, args));
    }

    public static <T> ValueContainer<T> invokeDeclared(@NotNull Class<?> clazz, String methodName, Object instance,
                                                       Class<?>[] type, Object... args) {
        ValueContainer<Method> methodContainer = ReflectionUtil.getDeclaredMethod(clazz, methodName, type);
        return ReflectionUtil.invoke(methodContainer, instance, args);
    }

    public static ValueContainer<Method> getMethod(String className, String methodName, Class<?>... type) {
        ValueContainer<Class<?>> clazz = ReflectionUtil.getClass(className);
        return ReflectionUtil.getMethod(clazz, methodName, type);
    }

    public static ValueContainer<Method> getMethod(@NotNull ValueContainer<Class<?>> classContainer,
                                                   String methodName, Class<?>... type) {
        if (classContainer.isException()) {
            return ValueContainer.exception(classContainer.getException());
        }

        return classContainer.flatMap(clazz -> ReflectionUtil.getMethod(clazz, methodName, type));
    }

    public static ValueContainer<Method> getMethod(@NotNull Class<?> clazz, String methodName, Class<?>... type) {
        try {
            Method method = clazz.getMethod(methodName, type);
            method.setAccessible(true);
            return ValueContainer.of(method);
        } catch (NoSuchMethodException e) {
            return ValueContainer.exception(e);
        }
    }

    public static <T> @NotNull ValueContainer<T> invoke(String className, String methodName, Object instance,
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
        ValueContainer<Method> methodContainer = ReflectionUtil.getMethod(clazz, methodName, type);
        return ReflectionUtil.invoke(methodContainer, instance, args);
    }

    public static <T> ValueContainer<T> invoke(ValueContainer<Method> methodContainer, Object instance, Object... args) {
        if (methodContainer.isException()) {
            return ValueContainer.exception(methodContainer.getException());
        }

        return methodContainer.flatMap(method -> ReflectionUtil.invoke(method, instance, args));
    }

    public static <T> ValueContainer<T> invoke(Method method, Object instance, Object... args) {
        try {
            method.setAccessible(true);
            @SuppressWarnings("unchecked")
            ValueContainer<T> container = ValueContainer.of((T) method.invoke(instance, args));
            return container;
        } catch (Exception e) {
            return ValueContainer.exception(e);
        }
    }

    public static <T extends Annotation> ValueContainer<T> getFieldAnnotation(Field field, String annotationClassName) {
        ValueContainer<Class<T>> clazz = ReflectionUtil.getAnnotationClass(annotationClassName);
        return ReflectionUtil.getFieldAnnotation(field, clazz);
    }

    public static <T extends Annotation> ValueContainer<T> getFieldAnnotation(
            Field field, @NotNull ValueContainer<Class<T>> classContainer) {
        if (classContainer.isException()) {
            return ValueContainer.exception(classContainer.getException());
        }

        return ReflectionUtil.getFieldAnnotation(field, classContainer.get());
    }

    public static <T extends Annotation> ValueContainer<T> getFieldAnnotation(Field field, Class<T> annotationClass) {
        try {
            return ValueContainer.ofNullable(field.getAnnotation(annotationClass));
        } catch (Exception e) {
            return ValueContainer.exception(e);
        }
    }
}
