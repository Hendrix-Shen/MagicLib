package top.hendrixshen.magiclib.util;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.MagicLibReference;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;

/**
 * See {@link ReflectionUtil}
 */
@Deprecated
@ApiStatus.ScheduledForRemoval
public class ReflectUtil {
    public static Optional<Class<?>> getClass(String className) {
        try {
            return Optional.of(Class.forName(className));
        } catch (ClassNotFoundException e) {
            return Optional.empty();
        }
    }

    public static Optional<Class<?>> getInnerClass(@NotNull Class<?> outerClass, String innerClassName) {
        for (Class<?> cls : outerClass.getDeclaredClasses()) {
            if (cls.getName().replace(String.format("%s$", outerClass.getName()), "").equals(innerClassName)) {
                return Optional.of(cls);
            }
        }

        return Optional.empty();
    }

    public static @NotNull Optional<?> newInstance(String className, int index, Object... parameters) {
        Optional<Class<?>> optional = ReflectUtil.getClass(className);

        if (optional.isPresent()) {
            Constructor<?> constructor = optional.get().getDeclaredConstructors()[index];
            constructor.setAccessible(true);

            try {
                return Optional.of(constructor.newInstance(parameters));
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                MagicLibReference.getLogger().throwing(e);
            }
        }

        return Optional.empty();
    }

    public static @NotNull Optional<?> newInstance(String className, Class<?>[] parameterTypes, Object... parameters) {
        Optional<Class<?>> optional = ReflectUtil.getClass(className);

        if (optional.isPresent()) {
            try {
                Constructor<?> constructor = optional.get().getDeclaredConstructor(parameterTypes);
                constructor.setAccessible(true);
                return Optional.of(constructor.newInstance(parameters));
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                MagicLibReference.getLogger().throwing(e);
            }
        }

        return Optional.empty();
    }

    public static boolean setFieldValue(String className, String fieldName, Object instance, Object value) {
        return ReflectUtil.getClass(className).filter(cls -> ReflectUtil.setFieldValue(cls, fieldName, instance, value)).isPresent();
    }

    public static boolean setFieldValue(@NotNull Class<?> cls, String fieldName, Object instance, Object value) {
        try {
            Field field = cls.getField(fieldName);
            field.setAccessible(true);
            field.set(instance, value);
            return true;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            MagicLibReference.getLogger().throwing(e);
        }

        return false;
    }

    public static boolean setDeclaredFieldValue(String className, String fieldName, Object instance, Object value) {
        return ReflectUtil.getClass(className).filter(cls -> ReflectUtil.setDeclaredFieldValue(cls, fieldName, instance, value)).isPresent();
    }

    public static boolean setDeclaredFieldValue(@NotNull Class<?> cls, String fieldName, Object instance, Object value) {
        try {
            Field field = cls.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(instance, value);
            return true;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            MagicLibReference.getLogger().throwing(e);
        }

        return false;
    }

    public static Optional<?> getFieldValue(String className, String fieldName, Object instance) {
        return ReflectUtil.getClass(className).flatMap(cls -> ReflectUtil.getFieldValue(cls, fieldName, instance));
    }

    public static Optional<?> getFieldValue(@NotNull Class<?> cls, String fieldName, Object instance) {
        try {
            Field field = cls.getField(fieldName);
            field.setAccessible(true);
            return Optional.ofNullable(field.get(instance));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            MagicLibReference.getLogger().throwing(e);
        }

        return Optional.empty();
    }

    public static Optional<?> getDeclaredFieldValue(String className, String fieldName, Object instance) {
        return ReflectUtil.getClass(className).flatMap(cls -> ReflectUtil.getDeclaredFieldValue(cls, fieldName, instance));
    }

    public static Optional<?> getDeclaredFieldValue(@NotNull Class<?> cls, String fieldName, Object instance) {
        try {
            Field field = cls.getDeclaredField(fieldName);
            field.setAccessible(true);
            return Optional.ofNullable(field.get(instance));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            MagicLibReference.getLogger().throwing(e);
        }

        return Optional.empty();
    }

    public static Optional<?> invoke(String className, String methodName, Object instance, Object... parameters) {
        return ReflectUtil.getClass(className).flatMap(cls -> ReflectUtil.invoke(cls, methodName, instance, parameters));
    }

    public static Optional<?> invoke(String className, String methodName, Object instance, Class<?>[] type, Object... parameters) {
        return ReflectUtil.getClass(className).flatMap(cls -> ReflectUtil.invoke(cls, methodName, instance, type, parameters));
    }

    public static Optional<?> invoke(@NotNull Class<?> cls, String methodName, Object instance, Object... parameters) {
        return ReflectUtil.invoke(cls, methodName, instance, null, parameters);
    }

    public static Optional<?> invoke(@NotNull Class<?> cls, String methodName, Object instance, Class<?>[] type, Object... parameters) {
        try {
            Method method = cls.getMethod(methodName, type);
            method.setAccessible(true);
            return Optional.ofNullable(method.invoke(instance, parameters));
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            MagicLibReference.getLogger().throwing(e);
        }

        return Optional.empty();
    }

    public static Optional<?> invokeDeclared(String className, String methodName, Object instance, Object... parameters) {
        return ReflectUtil.getClass(className).flatMap(cls -> ReflectUtil.invokeDeclared(cls, methodName, instance, parameters));
    }

    public static Optional<?> invokeDeclared(String className, String methodName, Object instance, Class<?>[] type, Object... parameters) {
        return ReflectUtil.getClass(className).flatMap(cls -> ReflectUtil.invokeDeclared(cls, methodName, instance, type, parameters));
    }

    public static Optional<?> invokeDeclared(@NotNull Class<?> cls, String methodName, Object instance, Object... parameters) {
        return ReflectUtil.invokeDeclared(cls, methodName, instance,null, parameters);
    }

    public static Optional<?> invokeDeclared(@NotNull Class<?> cls, String methodName, Object instance, Class<?>[] type, Object... parameters) {
        try {
            Method method = cls.getDeclaredMethod(methodName, type);
            method.setAccessible(true);
            return Optional.ofNullable(method.invoke(instance, parameters));
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            MagicLibReference.getLogger().throwing(e);
        }

        return Optional.empty();
    }
}
